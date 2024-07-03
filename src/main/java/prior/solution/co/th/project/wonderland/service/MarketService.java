package prior.solution.co.th.project.wonderland.service;

import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import prior.solution.co.th.project.wonderland.model.*;
import prior.solution.co.th.project.wonderland.repository.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MarketService {

    private MarketNativeRepository marketNativeRepository;

    private PlayerNativeRepository playerNativeRepository;

    private ItemNativeRepository itemNativeRepository;

    private InboxNativeRepository inboxNativeRepository;

    private PlayerItemNativeRepository playerItemNativeRepository;

    public MarketService(MarketNativeRepository marketNativeRepository,
                         PlayerNativeRepository playerNativeRepository,
                         ItemNativeRepository itemNativeRepository,
                         PlayerItemNativeRepository playerItemNativeRepository,
                         InboxNativeRepository inboxNativeRepository) {
        this.playerNativeRepository = playerNativeRepository;
        this.itemNativeRepository = itemNativeRepository;
        this.playerItemNativeRepository = playerItemNativeRepository;
        this.marketNativeRepository = marketNativeRepository;
        this.inboxNativeRepository = inboxNativeRepository;
    }

    public ResponseModel<List<MarketModel>> getAllListByNativeSql (){
        ResponseModel<List<MarketModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("find all lists");
        try{
            List<MarketModel> transfromedData = marketNativeRepository.findAllList();
            result.setData(transfromedData);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }


    //ประกาศขายไอเทม
    public ResponseModel<List<MarketModel>> sellItemByNativeSql (MarketModel marketModel){
        ResponseModel<List<MarketModel>> result = new ResponseModel<>();

        result.setStatus(205);
        result.setDescription("item sell success");
        try{
            int sellerId = marketModel.getSellerId();
            int itemId = marketModel.getItemId();
            double itemPrice = itemNativeRepository.getItemPrice(itemId);
            marketModel.setPrice(itemPrice);
            this.marketNativeRepository.insertList(marketModel);

            this.playerItemNativeRepository.deletePlayerItem(sellerId, itemId);

            InboxModel inboxModel = new InboxModel();
            inboxModel.setPId(marketModel.getSellerId());
            inboxModel.setMessage("Successfully consigned items");
            this.inboxNativeRepository.insertInbox(inboxModel);

            List<MarketModel> list = this.marketNativeRepository.findAllList();
            result.setData(list);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    //ซื้อไอเทม
    public ResponseModel<String> buyItemByNativeSql (Map<String, Object> data){
        ResponseModel<String> result = new ResponseModel<>();

        result.setStatus(206);
        result.setDescription("buy item success");
        try{

            int playerId = (Integer) data.get("pId");
            int listId = (Integer) data.get("listId");

            PlayerModel playerModel = this.playerNativeRepository.findPlayer(playerId);
            MarketModel marketModel = this.marketNativeRepository.findList(listId);

            int itemId = marketModel.getItemId();

            ItemModel itemModel = this.itemNativeRepository.findItem(itemId);

            double playerBalance = playerModel.getBalance();
            double itemPrice = itemModel.getItemPrice();
            List<PlayerItemModel> playerItem = new ArrayList<>();

            if(playerBalance >= itemPrice){
                playerBalance -= itemPrice;
                playerModel.setBalance(playerBalance);
                this.playerNativeRepository.updatePlayer(playerModel);

                PlayerModel seller = this.playerNativeRepository.findPlayer(marketModel.getSellerId());
                double sellerBalance = seller.getBalance() + itemPrice;
                seller.setBalance(sellerBalance);
                this.playerNativeRepository.updatePlayer(seller);

                PlayerItemModel pi = new PlayerItemModel();
                pi.setItemId(itemId);
                pi.setPId(playerId);
                pi.setQuantity(1);
                playerItem.add(pi);
                this.playerItemNativeRepository.insertPlayerItem(playerItem);

                InboxModel inboxModel = new InboxModel();
                inboxModel.setPId(marketModel.getSellerId());
                inboxModel.setMessage("Your item was sold");
                this.inboxNativeRepository.insertInbox(inboxModel);

                marketModel.setStatus("sold");
                this.marketNativeRepository.updateList(marketModel);

                result.setData("The purchase is completed");
            }else{
                result.setData("Your money is not enough");
            }
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

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
    public ResponseModel<List<MarketModel>> sellItemByNativeSql (Map<String, Object> data){
        ResponseModel<List<MarketModel>> result = new ResponseModel<>();

        result.setStatus(205);
        result.setDescription("item sell success");
        try{
            int playerItemId = (Integer) data.get("pItemId");
            PlayerItemModel playerItemModel = this.playerItemNativeRepository.findPlayerItem(playerItemId);

            if(playerItemModel.getStatus().equals("selling")){
                result.setData(null);
            }else {
                MarketModel marketModel = new MarketModel();
                int sellerId = playerItemModel.getPId();
                int pItemId = playerItemModel.getPItemId();
                int itemId = playerItemModel.getItemId();
                double itemPrice = this.itemNativeRepository.getItemPrice(itemId);
                marketModel.setPItemId(pItemId);
                marketModel.setPrice(itemPrice);

                this.marketNativeRepository.insertList(marketModel);

                PlayerItemModel play = playerItemModel;
                play.setStatus("selling");
                this.playerItemNativeRepository.updatePlayerItem(play);

                InboxModel inboxModel = new InboxModel();
                inboxModel.setPId(sellerId);
                inboxModel.setMessage("Item" + itemId + " Successfully consigned items");
                this.inboxNativeRepository.insertInbox(inboxModel);

                List<MarketModel> list = this.marketNativeRepository.findAllList();
                result.setData(list);
            }

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

            int pItemId = marketModel.getPItemId();
            PlayerItemModel playerItemModel = this.playerItemNativeRepository.findPlayerItem(pItemId);

            int itemId = playerItemModel.getItemId();
            ItemModel itemModel = this.itemNativeRepository.findItem(itemId);

            double playerBalance = playerModel.getBalance();
            double itemPrice = itemModel.getItemPrice();


            if ("sell".equals(marketModel.getStatus())) {
                if (playerId != playerItemModel.getPId()) {
                    if (playerBalance >= itemPrice) {
                        playerBalance -= itemPrice;
                        playerModel.setBalance(playerBalance);
                        this.playerNativeRepository.updatePlayer(playerModel);

                        PlayerModel seller = this.playerNativeRepository.findPlayer(playerItemModel.getPId());
                        double sellerBalance = seller.getBalance() + itemPrice;
                        seller.setBalance(sellerBalance);
                        this.playerNativeRepository.updatePlayer(seller);

                        InboxModel inboxModel = new InboxModel();
                        inboxModel.setPId(playerItemModel.getPId());
                        inboxModel.setMessage("Your item" + itemModel.getItemName() + " was sold");
                        this.inboxNativeRepository.insertInbox(inboxModel);

                        marketModel.setStatus("sold");
                        this.marketNativeRepository.updateList(marketModel);

                        playerItemModel.setPId(playerId);
                        playerItemModel.setStatus("normal");
                        this.playerItemNativeRepository.updatePlayerItem(playerItemModel);
                        result.setData("The purchase is completed");
                    } else {
                        result.setData("Your money is not enough");
                    }
                } else {
                    result.setData("You can't buy your own item");
                }
            }


        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

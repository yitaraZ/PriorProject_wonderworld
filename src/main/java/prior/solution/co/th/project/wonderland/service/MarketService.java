package prior.solution.co.th.project.wonderland.service;

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
    public ResponseModel<Void> buyItemByNativeSql (Map<String, Object> data){
        ResponseModel<Void> result = new ResponseModel<>();

        result.setStatus(206);
        result.setDescription("buy item success");
        try{
            Map<String, Object> playerMap = (Map<String, Object>) data.get("playerModel");
            Map<String, Object> itemMap = (Map<String, Object>) data.get("itemModel");

            PlayerModel playerModel = new PlayerModel();
            playerModel.setPId((Integer) playerMap.get("pId"));
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

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



    public ResponseModel<MarketModel> sellItemByNativeSql (MarketModel marketModel){
        ResponseModel<MarketModel> result = new ResponseModel<>();

        result.setStatus(205);
        result.setDescription("item sell success");
        try{
            marketNativeRepository.insertList(marketModel);

            int item = marketModel.getItemId();
            int sellerId = marketModel.getSellerId();
            //playerItemNativeRepository.deletePlayerItem();

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}

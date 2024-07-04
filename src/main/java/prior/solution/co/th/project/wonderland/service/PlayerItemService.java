package prior.solution.co.th.project.wonderland.service;

import org.springframework.stereotype.Service;
import prior.solution.co.th.project.wonderland.model.PlayerItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.PlayerItemNativeRepository;

import java.util.List;

@Service
public class PlayerItemService {
    private PlayerItemNativeRepository playerItemNativeRepository;

    public PlayerItemService(PlayerItemNativeRepository playerItemNativeRepository) {
        this.playerItemNativeRepository = playerItemNativeRepository;
    }

    public ResponseModel<List<PlayerItemModel>> getAllPlayerItemByNativeSql (){
        ResponseModel<List<PlayerItemModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("find all player items");
        try{
            List<PlayerItemModel> transfromedData = playerItemNativeRepository.findAllPlayerItem();
            result.setData(transfromedData);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }



    public ResponseModel<PlayerItemModel> getPlayerItemByNativeSql(int playerItemModel){
        ResponseModel<PlayerItemModel> result = new ResponseModel<>();

        result.setStatus(204);
        result.setDescription("find player item succes");
        try{
            PlayerItemModel transfromedData = playerItemNativeRepository.findPlayerItem(playerItemModel);
            result.setData(transfromedData);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

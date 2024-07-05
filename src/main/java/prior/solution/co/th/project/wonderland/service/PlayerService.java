package prior.solution.co.th.project.wonderland.service;

import org.springframework.stereotype.Service;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.ItemNativeRepository;
import prior.solution.co.th.project.wonderland.repository.MonsterNativeRepository;
import prior.solution.co.th.project.wonderland.repository.PlayerItemNativeRepository;
import prior.solution.co.th.project.wonderland.repository.PlayerNativeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {

    private PlayerNativeRepository playerNativeRepository;

    private MonsterNativeRepository monsterNativeRepository;

    private PlayerItemNativeRepository playerItemNativeRepository;

    public PlayerService(PlayerNativeRepository playerNativeRepository, MonsterNativeRepository monsterNativeRepository,
                         PlayerItemNativeRepository playerItemNativeRepository) {
        this.playerNativeRepository = playerNativeRepository;
        this.monsterNativeRepository = monsterNativeRepository;
        this.playerItemNativeRepository = playerItemNativeRepository;
    }

    public ResponseModel<List<PlayerModel>> getAllPlayerByNativeSql(){
        ResponseModel<List<PlayerModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("find all players");
        try{
            List<PlayerModel> transfromedData = playerNativeRepository.findAllPlayer();
            result.setData(transfromedData);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }



    public ResponseModel<Integer> insertPlayerByNativeSql(List<PlayerModel> playerModels){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("insert players success");
        try{
            int insertedRows = this.playerNativeRepository.insertPlayer(playerModels);
            result.setData(insertedRows);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> updatePlayerByNativeSql(PlayerModel playerModel) {
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(202);
        result.setDescription("Update player success");
        try{
            int updateRow = this.playerNativeRepository.updatePlayer(playerModel);
            result.setData(updateRow);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Void> deletePlayerByNativeSql(PlayerModel playerModel) {
        ResponseModel<Void> result = new ResponseModel<>();

        result.setStatus(203);
        result.setDescription("Delete player success");
        try{
            this.playerNativeRepository.deletePlayer(playerModel);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<PlayerModel> getPlayerByNativeSql(int playerId){
        ResponseModel<PlayerModel> result = new ResponseModel<>();

        result.setStatus(204);
        result.setDescription("find player succes");
        try{
            PlayerModel transfromedData = playerNativeRepository.findPlayer(playerId);
            result.setData(transfromedData);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<MonsterModel> attackMonster(Map<String, Object> data){
        ResponseModel<MonsterModel> result = new ResponseModel<>();

        result.setStatus(205);
        result.setDescription("player attack!");
        try{
            int playerId = (Integer) data.get("pId");
            int monsId = (Integer) data.get("mId");

            PlayerModel player = playerNativeRepository.findPlayer(playerId);
            MonsterModel monster = monsterNativeRepository.findMonster(monsId);
            List<PlayerItemModel> playerItem = new ArrayList<>();

            int playerAtk = player.getAtk();
            int monsterHp = monster.getHp();

            if(playerAtk < monsterHp){
                monsterHp -= playerAtk;
                monster.setHp(monsterHp);
                this.monsterNativeRepository.updateMonster(monster);

                result.setData(monster);
            }else{
                monsterHp = 0;
                monster.setHp(monsterHp);
                this.monsterNativeRepository.updateMonster(monster);

                int monsItem = monster.getItemDrop();
                PlayerItemModel pi = new PlayerItemModel();
                pi.setPId(player.getPId());
                pi.setItemId(monsItem);
                pi.setQuantity(1);
                playerItem.add(pi);
                this.playerItemNativeRepository.insertPlayerItem(playerItem);

                double playerBalance = player.getBalance();
                playerBalance += monster.getPrize();
                player.setBalance(playerBalance);
                this.playerNativeRepository.updatePlayer(player);

                result.setData(monster);
                this.monsterNativeRepository.deleteMonster(monster);

            }
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }


}

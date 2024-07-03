package prior.solution.co.th.project.wonderland.service;

import org.springframework.stereotype.Service;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.MonsterNativeRepository;

import java.util.List;

@Service
public class MonsterService {

    private MonsterNativeRepository monsterNativeRepository;

    public MonsterService(MonsterNativeRepository monsterNativeRepository) {
        this.monsterNativeRepository = monsterNativeRepository;
    }

    public ResponseModel<List<MonsterModel>> getAllMonsterByNativeSql(){
        ResponseModel<List<MonsterModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("find all monsters");
        try{
            List<MonsterModel> transfromedData = monsterNativeRepository.findAllMonster();
            result.setData(transfromedData);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertMonsterByNativeSql(List<MonsterModel> monsterModels){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("insert monsters success");
        try{
            int insertRow = monsterNativeRepository.insertMonster(monsterModels);
            result.setData(insertRow);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> updateMonsterByNativeSql(MonsterModel monsterModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(202);
        result.setDescription("Update monster success");
        try{
            int insertRow = monsterNativeRepository.updateMonster(monsterModel);
            result.setData(insertRow);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Void> deleteMonsterBNativeSql(MonsterModel monsterModel){
        ResponseModel<Void> result = new ResponseModel<>();

        result.setStatus(203);
        result.setDescription("Delete monster success");
        try{
            monsterNativeRepository.deleteMonster(monsterModel);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<MonsterModel> getMonsterByNativeSql(int monsId){
        ResponseModel<MonsterModel> result = new ResponseModel<>();

        result.setStatus(204);
        result.setDescription("find monster succes");
        try{
            MonsterModel transfromedData = monsterNativeRepository.findMonster(monsId);
            result.setData(transfromedData);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

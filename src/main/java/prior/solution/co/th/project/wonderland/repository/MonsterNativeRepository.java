package prior.solution.co.th.project.wonderland.repository;


import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;

import java.util.List;


public interface MonsterNativeRepository {

    public List<MonsterModel> findAllMonster();

    public MonsterModel findMonster(int monsId);

    public int insertMonster(List<MonsterModel> monsterModels);

    public int updateMonster(MonsterModel monsterModel);

    public void deleteMonster(MonsterModel monsterModel);
}

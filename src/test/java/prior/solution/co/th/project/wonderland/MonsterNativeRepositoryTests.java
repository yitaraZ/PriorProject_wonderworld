package prior.solution.co.th.project.wonderland;

import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.repository.MonsterNativeRepository;

import java.util.List;

public class MonsterNativeRepositoryTests implements MonsterNativeRepository {
    @Override
    public List<MonsterModel> findAllMonster() {
        return List.of();
    }

    @Override
    public MonsterModel findMonster(int monsId) {
        return null;
    }

    @Override
    public int insertMonster(List<MonsterModel> monsterModels) {
        return monsterModels.size();
    }

    @Override
    public int updateMonster(MonsterModel monsterModel) {
        return 0;
    }

    @Override
    public void deleteMonster(MonsterModel monsterModel) {

    }
}

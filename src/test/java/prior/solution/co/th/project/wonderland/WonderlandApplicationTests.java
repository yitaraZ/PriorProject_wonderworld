package prior.solution.co.th.project.wonderland;

import io.micrometer.common.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.MonsterNativeRepository;
import prior.solution.co.th.project.wonderland.service.MonsterService;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
class WonderlandApplicationTests {
	@Test
	public void test_findMonster_expect_ResponseModel_eq_204() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests(){
			@Override
			public MonsterModel findMonster(int monsId) {
				MonsterModel monsterModel = new MonsterModel();
				if(monsId == 1){
					monsterModel.setMId(1);
					monsterModel.setMName("test");
					monsterModel.setHp(10);
					monsterModel.setItemDrop(1);
					monsterModel.setPrize(10);
				}
				return monsterModel;
			}
		};
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		int monsterId = 1;
		ResponseModel<MonsterModel> result = monsterService.getMonsterByNativeSql(monsterId);
		Assertions.assertTrue(result.getStatus() == 204);
	}

	@Test
	public void test_findMonster_expect_ResponseModel_eq_500() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests(){
			@Override
			public MonsterModel findMonster(int monsId) {
				throw new RuntimeException("find monster error");
			}
		};
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		int monsterId = 1;
		ResponseModel<MonsterModel> result = monsterService.getMonsterByNativeSql(monsterId);
		Assertions.assertTrue(result.getStatus() == 500);
	}


	@Test
	public void test_insertMonster_expect_ResponseModel_eq_201() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests();
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		List<MonsterModel> monsterModelList = new ArrayList<>();
		for(int i=1; i<=10;i++){
			MonsterModel x = new MonsterModel();
			x.setMName("test"+i);
			x.setHp(100+i);
			x.setItemDrop(i);
			x.setPrize(10+i);
			monsterModelList.add(x);
		}
		ResponseModel<Integer> result = monsterService.insertMonsterByNativeSql(monsterModelList);
		Assertions.assertTrue(result.getStatus() == 201);
	}

	@Test
	public void test_insertMonster_expect_ResponseModel_eq_400() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests();
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		List<MonsterModel> monsterModelList = new ArrayList<>();
		for(int i=1; i<=10;i++){
			MonsterModel x = new MonsterModel();
			x.setMName("test"+i);
			//x.setHp(100+i);
			//x.setItemDrop(i);
			x.setPrize(10+i);
			monsterModelList.add(x);
		}
		ResponseModel<Integer> result = monsterService.insertMonsterByNativeSql(monsterModelList);
		Assertions.assertTrue(result.getStatus() == 400);
	}

	@Test
	public void test_insertMonster_expect_ResponseModel_eq_500() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests(){
			@Override
			public int insertMonster(List<MonsterModel> monsterModels) {
				throw new RuntimeException("insert error");
			}
		};
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		List<MonsterModel> monsterModelList = new ArrayList<>();
		for(int i=1; i<=10;i++){
			MonsterModel x = new MonsterModel();
			x.setMName("test"+i);
			x.setHp(100+i);
			x.setItemDrop(i);
			x.setPrize(10+i);
			monsterModelList.add(x);
		}
		ResponseModel<Integer> result = monsterService.insertMonsterByNativeSql(monsterModelList);
		Assertions.assertTrue(result.getStatus() == 500);
	}

	@Test
	public void test_UpdateMonster_expect_ResponseModel_eq_202() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests(){
			@Override
			public int updateMonster(MonsterModel monsterModel) {
				int count = 0;
				if(StringUtils.isNotEmpty(monsterModel.getMName()) || monsterModel.getHp() != 0
						|| monsterModel.getItemDrop() != 0 || monsterModel.getPrize() != 0){
					count++;
				}
				return count;
			}
		};
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		MonsterModel monsterModel = new MonsterModel();
		monsterModel.setMId(1);
		monsterModel.setMName("test");
		monsterModel.setHp(100);

		ResponseModel<Integer> result = monsterService.updateMonsterByNativeSql(monsterModel);
		Assertions.assertTrue(result.getStatus() == 202);
	}

	@Test
	public void test_UpdatetMonster_expect_ResponseModel_eq_400() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests(){
			@Override
			public int updateMonster(MonsterModel monsterModel) {
				int count = 0;
				if(StringUtils.isNotEmpty(monsterModel.getMName()) || monsterModel.getHp() != 0
						|| monsterModel.getItemDrop() != 0 || monsterModel.getPrize() != 0){
					count++;
				}
				return count;
			}
		};
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		MonsterModel monsterModel = new MonsterModel();

		monsterModel.setMName("test");
		monsterModel.setHp(100);

		ResponseModel<Integer> result = monsterService.updateMonsterByNativeSql(monsterModel);
		Assertions.assertTrue(result.getStatus() == 400);
	}

	@Test
	public void test_UpdateMonster_expect_ResponseModel_eq_500() {
		MonsterNativeRepository monsterNativeRepository = new MonsterNativeRepositoryTests(){
			@Override
			public int updateMonster(MonsterModel monsterModel) {
				throw new RuntimeException("update error");
			}
		};
		MonsterService monsterService = new MonsterService(monsterNativeRepository);

		MonsterModel monsterModel = new MonsterModel();
		monsterModel.setMId(1);
		monsterModel.setMName("test");
		monsterModel.setHp(100);

		ResponseModel<Integer> result = monsterService.updateMonsterByNativeSql(monsterModel);
		Assertions.assertTrue(result.getStatus() == 500);
	}

}

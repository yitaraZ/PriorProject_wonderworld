package prior.solution.co.th.project.wonderland.controller;


import org.springframework.web.bind.annotation.*;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.MonsterService;

import java.util.List;

@RestController
@RequestMapping("api")
public class MonsterRestController {

    private MonsterService monsterService;

    public MonsterRestController(MonsterService monsterService) {
        this.monsterService = monsterService;
    }

    @GetMapping("/find/all/monsters")
    public ResponseModel<List<MonsterModel>> getAllMonster(){
        return this.monsterService.getAllMonsterByNativeSql();
    }

    @GetMapping("/find/monster")
    public ResponseModel<MonsterModel> getMonster(@RequestBody MonsterModel monsterModel){
        return this.monsterService.getMonsterByNativeSql(monsterModel);
    }

    @PostMapping("/insert/monster")
    public ResponseModel<Integer> insertMonster(@RequestBody  List<MonsterModel> monsterModels){
        return this.monsterService.insertMonsterByNativeSql(monsterModels);
    }

    @PutMapping("/update/monster")
    public ResponseModel<Integer> updateMonster(@RequestBody MonsterModel monsterModel){
        return this.monsterService.updateMonsterByNativeSql(monsterModel);
    }

    @DeleteMapping("/delete/monster")
    public ResponseModel<Void> deleteMonster(@RequestBody MonsterModel monsterModel){
        return this.monsterService.deleteMonsterBNativeSql(monsterModel);
    }
}

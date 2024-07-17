package prior.solution.co.th.project.wonderland.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.PlayerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class PlayerRestController {

    private PlayerService playerService;

    public PlayerRestController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/find/all/players")
    public ResponseModel<List<PlayerModel>> getAllPlayer(){
        return this.playerService.getAllPlayerByNativeSql();
    }

    @GetMapping("/find/player")
    public ResponseModel<PlayerModel> getPLayer(@RequestBody Map<String, Object> data){
        int playerId = (Integer) data.get("pId");
        return this.playerService.getPlayerByNativeSql(playerId);
    }

    @PostMapping("/insert/player")
    public ResponseModel<Integer> insertPlayer(@RequestBody List<PlayerModel> playerModels){
        return this.playerService.insertPlayerByNativeSql(playerModels);
    }

    @PutMapping("/update/player")
    public ResponseModel<Integer> updatePlayer(@RequestBody PlayerModel playerModel){
        return this.playerService.updatePlayerByNativeSql(playerModel);
    }

    @DeleteMapping("/delete/player")
    public ResponseModel<Void> deletePlayer(@RequestBody PlayerModel playerModel){
        return this.playerService.deletePlayerByNativeSql(playerModel);
    }

    @PutMapping("/attack")
    public ResponseModel<MonsterModel> attackMonster(@RequestBody Map<String, Object> data) {
        return this.playerService.attackMonster(data);
    }

}

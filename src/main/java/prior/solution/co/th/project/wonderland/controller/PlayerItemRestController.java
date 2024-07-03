package prior.solution.co.th.project.wonderland.controller;

import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import prior.solution.co.th.project.wonderland.model.PlayerItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.PlayerItemService;

import java.util.List;

@RestController
@RequestMapping("api")
public class PlayerItemRestController {

    private PlayerItemService playerItemService;

    public PlayerItemRestController(PlayerItemService playerItemService) {
        this.playerItemService = playerItemService;
    }

    @GetMapping("/find/all/player_items")
    public ResponseModel<List<PlayerItemModel>> getAllPlayerItems(){
        return this.playerItemService.getAllPlayerItemByNativeSql();
    }

    @GetMapping("/find/player_item")
    public ResponseModel<PlayerItemModel> getPLayer(@RequestBody PlayerItemModel playerItemModel){
        return this.playerItemService.getPlayerItemByNativeSql(playerItemModel);
    }


}

package prior.solution.co.th.project.wonderland.controller;

import org.springframework.web.bind.annotation.*;
import prior.solution.co.th.project.wonderland.model.ItemModel;
import prior.solution.co.th.project.wonderland.model.MarketModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.MarketService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class MarketRestController {

    private MarketService marketService;

    public MarketRestController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping("/find/all/lists")
    public ResponseModel<List<MarketModel>> getAllItem(){
        return this.marketService.getAllListByNativeSql();
    }

    @PostMapping("/sell/item")
    public ResponseModel<List<MarketModel>> sellItem(@RequestBody MarketModel marketModel){
        return this.marketService.sellItemByNativeSql(marketModel);
    }
}

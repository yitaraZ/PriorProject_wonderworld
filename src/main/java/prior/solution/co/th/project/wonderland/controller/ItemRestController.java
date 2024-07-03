package prior.solution.co.th.project.wonderland.controller;

import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.*;
import prior.solution.co.th.project.wonderland.model.ItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("api")
public class ItemRestController {

    private ItemService itemService;

    public ItemRestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/find/all/items")
    public ResponseModel<List<ItemModel>> getAllItem(){
        return this.itemService.getAllItemByNativeSql();
    }

    @GetMapping("/find/item")
    public ResponseModel<ItemModel> getPLayer(@RequestBody int itemId){
        return this.itemService.getItemByNativeSql(itemId);
    }

    @PostMapping("/insert/item")
    public ResponseModel<Integer> insertItem(@RequestBody List<ItemModel> itemModels){
        return this.itemService.insertItemByNativeSql(itemModels);
    }

    @PutMapping("/update/item")
    public ResponseModel<Integer> updateItem(@RequestBody ItemModel itemModel){
        return this.itemService.updateItemByNativeSql(itemModel);
    }

    @DeleteMapping("/delete/item")
    public ResponseModel<Void> deleteItem(@RequestBody ItemModel itemModel){
        return this.itemService.deleteItemByNativeSql(itemModel);
    }
}

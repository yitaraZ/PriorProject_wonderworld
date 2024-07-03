package prior.solution.co.th.project.wonderland.service;

import org.springframework.stereotype.Service;
import prior.solution.co.th.project.wonderland.model.ItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.ItemNativeRepository;

import java.util.List;

@Service
public class ItemService {

    private ItemNativeRepository itemNativeRepository;

    public ItemService(ItemNativeRepository itemNativeRepository) {
        this.itemNativeRepository = itemNativeRepository;
    }

    public ResponseModel<List<ItemModel>> getAllItemByNativeSql (){
        ResponseModel<List<ItemModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("find all items");
        try{
            List<ItemModel> transfromedData = itemNativeRepository.findAllItem();
            result.setData(transfromedData);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> insertItemByNativeSql (List<ItemModel> itemModels){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(201);
        result.setDescription("insert items success");
        try{
            int insertRow = itemNativeRepository.insertItem(itemModels);
            result.setData(insertRow);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Integer> updateItemByNativeSql (ItemModel itemModel){
        ResponseModel<Integer> result = new ResponseModel<>();

        result.setStatus(202);
        result.setDescription("Update item success");
        try{
            int updateRow = itemNativeRepository.updateItem(itemModel);
            result.setData(updateRow);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }

    public ResponseModel<Void> deleteItemByNativeSql (ItemModel itemModel){
        ResponseModel<Void> result = new ResponseModel<>();

        result.setStatus(203);
        result.setDescription("Delete item success");
        try{
            itemNativeRepository.deleteItem(itemModel);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    public ResponseModel<ItemModel> getItemByNativeSql(ItemModel itemModel){
        ResponseModel<ItemModel> result = new ResponseModel<>();

        result.setStatus(204);
        result.setDescription("find item succes");
        try{
            ItemModel transfromedData = itemNativeRepository.findItem(itemModel);
            result.setData(transfromedData);

        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }
}

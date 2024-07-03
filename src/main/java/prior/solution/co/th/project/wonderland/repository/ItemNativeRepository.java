package prior.solution.co.th.project.wonderland.repository;

import prior.solution.co.th.project.wonderland.model.ItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;

import java.util.List;

public interface ItemNativeRepository {

    public List<ItemModel>  findAllItem();

    public ItemModel findItem(int itemId);

    public int insertItem(List<ItemModel> itemModels);

    public int updateItem(ItemModel itemModel);

    public void deleteItem(ItemModel itemModel);

    public double getItemPrice(int itemId);

}

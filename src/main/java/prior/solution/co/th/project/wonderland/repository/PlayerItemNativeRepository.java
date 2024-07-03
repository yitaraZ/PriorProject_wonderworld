package prior.solution.co.th.project.wonderland.repository;

import prior.solution.co.th.project.wonderland.model.PlayerItemModel;

import java.util.List;;

public interface PlayerItemNativeRepository {

    public List<PlayerItemModel> findAllPlayerItem();

    public PlayerItemModel findPlayerItem(PlayerItemModel playerItemModel);

    public int insertPlayerItem(List<PlayerItemModel> playerItemModels);

    public int updatePlayerItem(PlayerItemModel playerItemModel);

    public void deletePlayerItem(PlayerItemModel playerItemModel);
}

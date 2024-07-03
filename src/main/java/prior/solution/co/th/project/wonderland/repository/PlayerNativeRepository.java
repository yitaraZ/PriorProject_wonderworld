package prior.solution.co.th.project.wonderland.repository;


import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;

import java.util.List;
import java.util.Map;

public interface PlayerNativeRepository {

    public List<PlayerModel> findAllPlayer();

    public PlayerModel findPlayer(int playerId);

    public int insertPlayer(List<PlayerModel> playerModels);

    public int updatePlayer(PlayerModel playerModel);

    public void deletePlayer(PlayerModel playerModel);

}

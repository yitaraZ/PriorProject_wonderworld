package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.PlayerItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.repository.PlayerItemNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class PlayerItemNativeRepositoryImpl implements PlayerItemNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public PlayerItemNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<PlayerItemModel> findAllPlayerItem() {
        String sql = "select p_item_id, p_id, i_id, quantity from player_item";

        List<PlayerItemModel> result = this.jdbcTemplate.query(sql, new RowMapper<PlayerItemModel>() {
            @Override
            public PlayerItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlayerItemModel x = new PlayerItemModel();
                int col = 1;
                x.setPItemId(rs.getInt(col++));
                x.setPId(rs.getInt(col++));
                x.setItemId(rs.getInt(col++));
                x.setQuantity(rs.getInt(col++));
                return x;
            }
        });
        return result;
    }

    @Override
    public PlayerItemModel findPlayerItem(PlayerItemModel playerItemModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = "SELECT p_item_id, p_id, i_id, quantity from player_item WHERE p_id = ?";
        paramList.add(playerItemModel.getPId());

        PlayerItemModel result = this.jdbcTemplate.queryForObject(sql, paramList.toArray(), new RowMapper<PlayerItemModel>() {
            @Override
            public PlayerItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlayerItemModel playerItem = new PlayerItemModel();
                playerItem.setPItemId(rs.getInt("p_item_id"));
                playerItem.setPId(rs.getInt("p_id"));
                playerItem.setItemId(rs.getInt("i_id"));
                playerItem.setQuantity(rs.getInt("quantity"));
                return playerItem;
            }
        });
        return result;
    }

    @Override
    public int insertPlayerItem(List<PlayerItemModel> playerItemModels) {
        List<Object> paramList = new ArrayList<>();

        String sql = "insert into player_item (p_item_id, p_id, i_id, quantity) values ";

        StringJoiner stringJoiner = new StringJoiner(",");
        for(PlayerItemModel i: playerItemModels){
            String value = "((SELECT MAX(p_item_id) + 1 FROM player_item p) ,? ,? ,?)";
            paramList.add(i.getPId());
            paramList.add(i.getItemId());
            paramList.add(i.getQuantity());

            stringJoiner.add(value);
        }

        sql += stringJoiner.toString();

        int insertPlayerItem = this.jdbcTemplate.update(sql, paramList.toArray());
        return insertPlayerItem;
    }

    @Override
    public int updatePlayerItem(PlayerItemModel playerItemModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " update player_item set ";
        StringJoiner stringJoiner = new StringJoiner(",");

        if (playerItemModel.getPId() != 0) {
            stringJoiner.add("p_id = ?");
            paramList.add(playerItemModel.getPId());
        }

        if(playerItemModel.getItemId() != 0){
            stringJoiner.add("i_id = ?");
            paramList.add(playerItemModel.getItemId());
        }

        if(playerItemModel.getQuantity() != 0){
            stringJoiner.add("quantity = ?");
            paramList.add(playerItemModel.getQuantity());
        }

        sql += stringJoiner.toString();
        sql += " where p_item_id = ? ";
        paramList.add(playerItemModel.getPItemId());

        int updateRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return updateRow;
    }

    @Override
    public void deletePlayerItem(int pId, int itemId) {
        String sql = " delete from player_item where p_id = ? and i_id = ?";
        Object[] params = new Object[]{pId, itemId};
        // Execute the update query
        this.jdbcTemplate.update(sql, params);

    }
}

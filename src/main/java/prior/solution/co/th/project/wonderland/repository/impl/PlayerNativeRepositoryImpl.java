package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.repository.PlayerNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Repository
public class PlayerNativeRepositoryImpl implements PlayerNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public PlayerNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PlayerModel> findAllPlayer(){
        String sql = "select p_id, p_name, p_attack, p_balance from player";

        List<PlayerModel> result = this.jdbcTemplate.query(sql, new RowMapper<PlayerModel>() {
            @Override
            public PlayerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlayerModel x = new PlayerModel();
                int col = 1;
                x.setPid(rs.getInt(col++));
                x.setPname(rs.getString(col++));
                x.setAtk(rs.getInt(col++));
                x.setBalance(rs.getInt(col++));

                return x;
            }
        });
        return result;
    }

    @Override
    public PlayerModel findPlayer(int playerId) {
        List<Object> paramList = new ArrayList<>();

        String sql = "SELECT p_id, p_name, p_attack, p_balance FROM player WHERE p_id = ?";
        paramList.add(playerId);

        PlayerModel result = this.jdbcTemplate.queryForObject(sql, paramList.toArray(), new RowMapper<PlayerModel>() {
            @Override
            public PlayerModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                PlayerModel player = new PlayerModel();
                player.setPid(rs.getInt("p_id"));
                player.setPname(rs.getString("p_name"));
                player.setAtk(rs.getInt("p_attack"));
                player.setBalance(rs.getInt("p_balance"));
                return player;
            }
        });
        return result;
    }

    @Override
    public int insertPlayer(List<PlayerModel> playerModels){
         List<Object> paramList = new ArrayList<>();
         String sql = " insert into player (p_id, p_name, p_attack, p_balance) values ";

        StringJoiner stringJoiner = new StringJoiner(",");
        for(PlayerModel p: playerModels){
            String value = "((SELECT MAX(p_id) + 1 FROM player p) ,? ,? ,?)";
            paramList.add(p.getPname());
            paramList.add(p.getAtk());
            paramList.add(p.getBalance());
            stringJoiner.add(value);
        }

        sql += stringJoiner.toString();

        int insertRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return insertRow;
    }

    @Override
    public int updatePlayer(PlayerModel playerModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " update player set ";
        StringJoiner stringJoiner = new StringJoiner(",");

        if (StringUtils.isNotEmpty(playerModel.getPname())) {
            stringJoiner.add("p_name = ?");
            paramList.add(playerModel.getPname());
        }
        if (playerModel.getAtk() != 0) {
            stringJoiner.add("p_attack = ?");
            paramList.add(playerModel.getAtk());
        }
        if (playerModel.getBalance() != 0) {
            stringJoiner.add("p_balance = ?");
            paramList.add(playerModel.getBalance());
        }

        sql += stringJoiner.toString();
        sql += " where p_id = ? ";
        paramList.add(playerModel.getPid());

        int updateRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return updateRow;
    }

    @Override
    public void deletePlayer(PlayerModel playerModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " delete from player where p_id = ? ";
        if (StringUtils.isNotEmpty(String.valueOf(playerModel.getPid()))){
            paramList.add(playerModel.getPid());
            this.jdbcTemplate.update(sql, paramList.toArray());
        }
    }

}

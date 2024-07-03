package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.MonsterModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.repository.MonsterNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class MonsterNativeRepositoryImpl implements MonsterNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public MonsterNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MonsterModel> findAllMonster() {
        String sql = "select m_id, m_name, m_hp, item_drop, prize from monster";

        List<MonsterModel> result = this.jdbcTemplate.query(sql, new RowMapper<MonsterModel>() {
            @Override
            public MonsterModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MonsterModel x = new MonsterModel();
                int col = 1;
                x.setMId(rs.getInt(col++));
                x.setMName(rs.getString(col++));
                x.setHp(rs.getInt(col++));
                x.setItemDrop(rs.getInt(col++));
                x.setPrize(rs.getInt(col++));

                return x;
            }
        });
        return result;
    }

    @Override
    public MonsterModel findMonster(int monsId) {
        List<Object> paramList = new ArrayList<>();

        String sql = "select m_id, m_name, m_hp, item_drop, prize from monster WHERE m_id = ?";
        paramList.add(monsId);

        MonsterModel result = this.jdbcTemplate.queryForObject(sql, paramList.toArray(), new RowMapper<MonsterModel>() {
            @Override
            public MonsterModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MonsterModel monster = new MonsterModel();
                monster.setMId(rs.getInt("m_id"));
                monster.setMName(rs.getString("m_name"));
                monster.setHp(rs.getInt("m_hp"));
                monster.setItemDrop(rs.getInt("item_drop"));
                monster.setPrize(rs.getDouble("prize"));
                return monster;
            }
        });
        return result;
    }

    @Override
    public int insertMonster(List<MonsterModel> monsterModels) {
        List<Object> paramList = new ArrayList<>();
        String sql = " insert into monster (m_id, m_name, m_hp, item_drop, prize) values ";

        StringJoiner stringJoiner = new StringJoiner(",");
        for(MonsterModel m: monsterModels){
            String value = "((SELECT MAX(m_id) + 1 FROM monster m) ,? ,? ,?, ?)";
            paramList.add(m.getMName());
            paramList.add(m.getHp());
            paramList.add(m.getItemDrop());
            paramList.add(m.getPrize());
            stringJoiner.add(value);
        }

        sql += stringJoiner.toString();

        int insertMonster = this.jdbcTemplate.update(sql, paramList.toArray());
        return insertMonster;
    }

    @Override
    public int updateMonster(MonsterModel monsterModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " update monster set ";
        StringJoiner stringJoiner = new StringJoiner(",");

        if (StringUtils.isNotEmpty(monsterModel.getMName())) {
            stringJoiner.add("m_name = ?");
            paramList.add(monsterModel.getMName());
        }
        if (monsterModel.getHp() != 0) {
            stringJoiner.add("m_hp = ?");
            paramList.add(monsterModel.getHp());
        }
        if (monsterModel.getItemDrop() != 0) {
            stringJoiner.add("item_drop = ?");
            paramList.add(monsterModel.getItemDrop());
        }
        if (monsterModel.getPrize() != 0) {
            stringJoiner.add("prize = ?");
            paramList.add(monsterModel.getPrize());
        }

        sql += stringJoiner.toString();
        sql += " where m_id = ? ";
        paramList.add(monsterModel.getMId());

        int updateRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return updateRow;
    }

    @Override
    public void deleteMonster(MonsterModel monsterModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " delete from monster where m_id = ? ";
        if (StringUtils.isNotEmpty(String.valueOf(monsterModel.getMId()))){
            paramList.add(monsterModel.getMId());
            this.jdbcTemplate.update(sql, paramList.toArray());
        }
    }
}

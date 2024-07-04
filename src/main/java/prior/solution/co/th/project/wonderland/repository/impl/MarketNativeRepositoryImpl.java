package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.InboxModel;
import prior.solution.co.th.project.wonderland.model.MarketModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.repository.MarketNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Repository
public class MarketNativeRepositoryImpl implements MarketNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public MarketNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MarketModel> findAllList() {
        String sql = "select list_id, p_item_id, quantity, price, status from market";

        List<MarketModel> result = this.jdbcTemplate.query(sql, new RowMapper<MarketModel>() {
            @Override
            public MarketModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MarketModel x = new MarketModel();
                int col = 1;
                x.setListId(rs.getInt(col++));
                x.setPItemId(rs.getInt(col++));
                x.setQty(rs.getInt(col++));
                x.setPrice(rs.getDouble(col++));
                x.setStatus(rs.getString(col++));
                return x;
            }
        });
        return result;
    }

    @Override
    public int insertList(MarketModel marketModel) {
        List<Object> paramList = new ArrayList<>();

        String getIdSql = "SELECT COALESCE(MAX(list_id), 0) + 1 FROM market";
        int newListId = this.jdbcTemplate.queryForObject(getIdSql, Integer.class);

        String sql = "INSERT INTO market (list_id, p_item_id, quantity, price, status) " +
                "VALUES (?, ?, 1, ?, 'sell')";

        paramList.add(newListId);
        paramList.add(marketModel.getPItemId());
        paramList.add(marketModel.getPrice());

        int insertCount = this.jdbcTemplate.update(sql, paramList.toArray());

        return insertCount;
    }

    @Override
    public int updateList(MarketModel marketModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " update market set ";
        StringJoiner stringJoiner = new StringJoiner(",");

        if (StringUtils.isNotEmpty(marketModel.getStatus())) {
            stringJoiner.add("status = ?");
            paramList.add(marketModel.getStatus());
        }

        sql += stringJoiner.toString();
        sql += " where list_id = ? ";
        paramList.add(marketModel.getListId());

        int updateRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return updateRow;
    }

    @Override
    public void deleteList(MarketModel marketModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " delete from market where list_id = ?";
        if(StringUtils.isNotEmpty(String.valueOf(marketModel.getListId()))){
            paramList.add(marketModel.getListId());
            this.jdbcTemplate.update(sql, paramList.toArray());
        }
    }

    @Override
    public MarketModel findList(int ListId) {
        List<Object> paramList = new ArrayList<>();

        String sql = "select list_id, p_item_id, quantity, price, status from market WHERE list_id = ?";
        paramList.add(ListId);

        MarketModel result = this.jdbcTemplate.queryForObject(sql, paramList.toArray(), new RowMapper<MarketModel>() {
            @Override
            public MarketModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MarketModel market = new MarketModel();
                market.setListId(rs.getInt("list_id"));
                market.setPItemId(rs.getInt("p_item_id"));
                market.setQty(rs.getInt("quantity"));
                market.setPrice(rs.getDouble("price"));
                market.setStatus(rs.getString("status"));
                return market;
            }
        });
        return result;
    }


}

package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.InboxModel;
import prior.solution.co.th.project.wonderland.model.MarketModel;
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
        String sql = "select list_id, seller_id, i_id, quantity, price, status from market";

        List<MarketModel> result = this.jdbcTemplate.query(sql, new RowMapper<MarketModel>() {
            @Override
            public MarketModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                MarketModel x = new MarketModel();
                int col = 1;
                x.setListId(rs.getInt(col++));
                x.setSellerId(rs.getInt(col++));
                x.setItemId(rs.getInt(col++));
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

        String sql = "insert into market (list_id, seller_id, i_id, quantity, price, status) values ";
        String value = "((SELECT COALESCE(MAX(list_id), 0) + 1 FROM inbox), ?, ?, 1, ?, 'sell')";

        paramList.add(marketModel.getSellerId());
        paramList.add(marketModel.getItemId());
        paramList.add(marketModel.getPrice());

        int insertCount = this.jdbcTemplate.update(sql + value, paramList.toArray());

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


}

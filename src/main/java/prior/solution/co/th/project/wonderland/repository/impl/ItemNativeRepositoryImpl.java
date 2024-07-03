package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.ItemModel;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.repository.ItemNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class ItemNativeRepositoryImpl implements ItemNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public ItemNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemModel> findAllItem() {
        String sql = "select i_id, i_name, description, i_price from item ";

        List<ItemModel> result = this.jdbcTemplate.query(sql, new RowMapper<ItemModel>() {
            @Override
            public ItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemModel x = new ItemModel();
                int col = 1;
                x.setItemId(rs.getInt(col++));
                x.setItemName(rs.getString(col++));
                x.setItemDes(rs.getString(col++));
                x.setItemPrice(rs.getInt(col++));

                return x;
            }
        });
        return result;
    }

    @Override
    public ItemModel findItem(ItemModel itemModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = "SELECT i_id, i_name, description, i_price FROM item WHERE i_id = ?";
        paramList.add(itemModel.getItemId());

        ItemModel result = this.jdbcTemplate.queryForObject(sql, paramList.toArray(), new RowMapper<ItemModel>() {
            @Override
            public ItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemModel item = new ItemModel();
                item.setItemId(rs.getInt("i_id"));
                item.setItemName(rs.getString("i_name"));
                item.setItemDes(rs.getString("description"));
                item.setItemPrice(rs.getInt("i_price"));
                return item;
            }
        });
        return result;
    }

    @Override
    public int insertItem(List<ItemModel> itemModels) {
        List<Object> paramList = new ArrayList<>();

        String sql = "insert into item (i_id, i_name, description, i_price) values";

        StringJoiner stringJoiner = new StringJoiner(",");
        for(ItemModel i: itemModels){
            String value = "((SELECT MAX(i_id) + 1 FROM item i) ,? ,? ,?)";
            paramList.add(i.getItemName());
            paramList.add(i.getItemDes());
            paramList.add(i.getItemPrice());

            stringJoiner.add(value);
        }

        sql += stringJoiner.toString();

        int insertItem = this.jdbcTemplate.update(sql, paramList.toArray());
        return insertItem;
    }

    @Override
    public int updateItem(ItemModel itemModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " update item set ";
        StringJoiner stringJoiner = new StringJoiner(",");

        if (StringUtils.isNotEmpty(itemModel.getItemName())) {
            stringJoiner.add("i_name = ?");
            paramList.add(itemModel.getItemName());
        }
        if(StringUtils.isNotEmpty((itemModel.getItemDes()))) {
            stringJoiner.add("description = ?");
            paramList.add(itemModel.getItemDes());
        }
        if(itemModel.getItemPrice() != 0){
            stringJoiner.add("i_price = ?");
            paramList.add(itemModel.getItemPrice());
        }

        sql += stringJoiner.toString();
        sql += " where i_id = ? ";
        paramList.add(itemModel.getItemId());

        int updateRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return updateRow;
    }

    @Override
    public void deleteItem(ItemModel itemModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " delete from item where i_id = ?";
        if(StringUtils.isNotEmpty(String.valueOf(itemModel.getItemId()))){
            paramList.add(itemModel.getItemId());
            this.jdbcTemplate.update(sql, paramList.toArray());
        }
    }

    @Override
    public double getItemPrice(int itemId) {
        String sql = "SELECT i_price FROM item WHERE i_id = ?";
        Object[] params = new Object[]{itemId};
        Double price = this.jdbcTemplate.queryForObject(sql, params, Double.class);
        return price != null ? price : 0.0;
    }

}

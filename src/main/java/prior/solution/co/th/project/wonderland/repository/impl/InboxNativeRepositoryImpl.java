package prior.solution.co.th.project.wonderland.repository.impl;

import io.micrometer.common.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.model.InboxModel;
import prior.solution.co.th.project.wonderland.model.ItemModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.InboxNativeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class InboxNativeRepositoryImpl implements InboxNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public InboxNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<InboxModel> findAllInbox() {
        String sql = "select inbox_id, p_id, message, sent_date from inbox";

        List<InboxModel> result = this.jdbcTemplate.query(sql, new RowMapper<InboxModel>() {
            @Override
            public InboxModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                InboxModel x = new InboxModel();
                int col = 1;
                x.setInboxId(rs.getInt(col++));
                x.setPId(rs.getInt(col++));
                x.setMessage(rs.getString(col++));
                x.setDate(rs.getTimestamp(col++));
                return x;
            }
        });
        return result;
    }

    @Override
    public int insertInbox(InboxModel inboxModel) {
        String getIdSql = "SELECT COALESCE(MAX(inbox_id), 0) + 1 FROM inbox";
        int newInboxId = this.jdbcTemplate.queryForObject(getIdSql, Integer.class);

        String sql = "INSERT INTO inbox (inbox_id, p_id, message, sent_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        Object[] params = new Object[]{newInboxId, inboxModel.getPId(), inboxModel.getMessage()};

        int insertCount = this.jdbcTemplate.update(sql, params);
        return insertCount;
    }

    @Override
    public int updateInbox(InboxModel inboxModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " update inbox set ";
        StringJoiner stringJoiner = new StringJoiner(",");

        if(inboxModel.getPId() != 0) {
            stringJoiner.add("p_id = ?");
            paramList.add(inboxModel.getPId());
        }
        if(StringUtils.isNotEmpty(inboxModel.getMessage())){
            stringJoiner.add("message = ?");
            paramList.add(inboxModel.getMessage());
        }

        sql += stringJoiner.toString();
        sql += " where inbox_id = ? ";
        paramList.add(inboxModel.getInboxId());

        int updateRow = this.jdbcTemplate.update(sql, paramList.toArray());
        return updateRow;
    }

    @Override
    public void deleteInbox(InboxModel inboxModel) {
        List<Object> paramList = new ArrayList<>();

        String sql = " delete from inbox where inbox_id = ?";
        if(StringUtils.isNotEmpty(String.valueOf(inboxModel.getInboxId()))){
            paramList.add(inboxModel.getInboxId());
            this.jdbcTemplate.update(sql, paramList.toArray());
        }
    }
}

package prior.solution.co.th.project.wonderland.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import prior.solution.co.th.project.wonderland.repository.ExportPlayerNativeRepository;

import java.sql.Connection;
import java.sql.SQLException;

@Repository
public class ExportPlayerNativeRepositoryImpl implements ExportPlayerNativeRepository {

    private JdbcTemplate jdbcTemplate;

    public ExportPlayerNativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection =  this.jdbcTemplate.getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
}

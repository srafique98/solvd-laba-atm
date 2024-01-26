package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.Atm;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.AtmRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class AtmDAO implements AtmRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AtmDAO.class);
    @Override
    public void create(Atm atm) {
        Connection conn = CONNECTION_POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            String createQuery =  "INSERT INTO atms (city) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, atm.getCity());
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()){
                    atm.setId(resultSet.getLong(1));
                }
            }
            conn.commit();
            LOGGER.info("Atm created:\n " + atm);
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.error("Rollback failed: " + ex.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Auto-commit failed " + e.getMessage());
            }
            CONNECTION_POOL.releaseConnection(conn);
        }
    }

    @Override
    public Atm findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Atm atm = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT a.id AS atm_id, a.city AS atm_city, " +
                    "u.id AS user_id, u.atm_id AS user_atm_id, " +
                    "u.credential_id AS user_credential_id, u.name AS user_name, " +
                    "ac.id AS account_id, ac.balance AS account_balance, ac.type AS account_type, " +
                    "ac.interest_rate AS account_interest_rate, " +
                    "cr.id AS credential_id, cr.pin AS credential_pin, cr.account_number AS credential_account_number, " +
                    "t.id AS transaction_id, t.date AS transaction_date, t.amount AS transaction_amount, t.type AS transaction_type " +
                    "FROM atms a JOIN users u ON a.id = u.atm_id " +
                    "LEFT JOIN accounts ac ON u.id = ac.user_id " +
                    "LEFT JOIN credentials cr ON u.credential_id = cr.id " +
                    "LEFT JOIN transactions t ON u.id = t.user_id WHERE a.id = ?";

            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setLong(1, id);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    atm = mapRow(resultSet);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.error("Rollback failed: " + ex.getMessage());
            }
            LOGGER.error("Select failed: " + e.getMessage());
        } finally {
            try {
                connection.setReadOnly(false);
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Auto-commit reset failed: " + e.getMessage());
            }
            CONNECTION_POOL.releaseConnection(connection);
        }
        return atm;
    }

    public static Atm mapRow(ResultSet resultSet) throws SQLException {
        Atm atm = new Atm();
        long atmId = resultSet.getLong("atm_id");
        if (atmId != 0) {
            atm.setId(atmId);
            atm.setCity(resultSet.getString("atm_city"));
            atm.setUsers(UserDAO.mapRow(resultSet,atm.getUsers()));
        }
        return atm;
    }

    @Override
    public void updateById(Atm atm) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE atms SET city = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setString(1, atm.getCity());
                ps.executeUpdate();
            }
            connection.commit();
            LOGGER.info("Atm updated: " + atm);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.error("Rollback failed: " + ex.getMessage());
            }
            LOGGER.error("Update failed: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.error("Auto-commit failed: " + e.getMessage());
            }
            CONNECTION_POOL.releaseConnection(connection);
        }
    }
}

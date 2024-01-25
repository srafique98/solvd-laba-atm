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
//        Connection connection = CONNECTION_POOL.getConnection();
//        Atm atm = null;
//        try {
//            connection.setReadOnly(true);
//            connection.setAutoCommit(false);
//            String selectQuery = "SELECT id as account_id, balance as account_balance, type as account_type, interest_rate as account_interest_rate, user_id as account_user_id FROM accounts WHERE id = ?";
//            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
//                ps.setLong(1, id);
//                ResultSet resultSet = ps.executeQuery();
//                if (resultSet.next()) {
//                    atm = mapRow(resultSet);
//                }
//            }
//            connection.commit();
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                LOGGER.error("Rollback failed: " + ex.getMessage());
//            }
//            LOGGER.error("Select failed: " + e.getMessage());
//        } finally {
//            try {
//                connection.setReadOnly(false);
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                LOGGER.error("Auto-commit reset failed: " + e.getMessage());
//            }
//            CONNECTION_POOL.releaseConnection(connection);
//        }
//        return atm;
        return null;
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

package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.User;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements UserRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);
    @Override
    public void create(User user, Long atmID, Long credentialID) {
        Connection conn = CONNECTION_POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            String createQuery =  "INSERT INTO users (name, atm_id, credential_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getName());
                ps.setLong(2, atmID);
                ps.setLong(3, credentialID);
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()){
                    user.setId(resultSet.getLong(1));
                }
            }
            conn.commit();
            LOGGER.info("User created:\n " + user);
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
    public User findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        User user = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT u.id AS user_id, u.atm_id AS user_atm_id, u.credential_id AS user_credential_id, " +
                    "u.name AS user_name, a.id AS account_id, a.balance AS account_balance, a.type AS account_type, " +
                    "a.interest_rate AS account_interest_rate, " +
                    "c.id AS credential_id, c.pin AS credential_pin, c.account_number AS credential_account_number, " +
                    "t.id AS transaction_id, t.date AS transaction_date, " +
                    "t.amount AS transaction_amount, t.type AS transaction_type " +
                    "FROM users u JOIN accounts a ON u.id = a.user_id " +
                    "JOIN credentials c ON u.credential_id = c.id " +
                    "LEFT JOIN transactions t ON u.id = t.user_id " +
                    "WHERE u.id = ?";

            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setLong(1, id);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    user = mapRow(resultSet);
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
        return user;
    }

    public static List<User> mapRow(ResultSet resultSet, List<User> users) throws SQLException{
        if (users == null){
            users = new ArrayList<>();
        }
        users.add(mapRow(resultSet));
        return users;
    }

    public static User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        long userId = resultSet.getLong("user_id");
        if (userId != 0) {
            user.setId(userId);
            user.setName(resultSet.getString("user_name"));
            user.setCredential(CredentialDAO.mapRow(resultSet));
            user.setAccounts(AccountDAO.mapRow(resultSet,user.getAccounts()));
            user.setTransactions(TransactionDAO.mapRow(resultSet, user.getTransactions()));
        }
        return user;
    }

    @Override
    public void updateById(User user) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE users SET name = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setString(1, user.getName());
                ps.setLong(2, user.getId());
                ps.executeUpdate();
            }
            connection.commit();
            LOGGER.info("User updated: " + user);
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

    @Override
    public User findByCredentialID(Long credentialID){
        Connection connection = CONNECTION_POOL.getConnection();
        User user = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT u.id AS user_id, u.atm_id AS user_atm_id, u.credential_id AS user_credential_id, " +
                    "u.name AS user_name, a.id AS account_id, a.balance AS account_balance, a.type AS account_type, " +
                    "a.interest_rate AS account_interest_rate, " +
                    "c.id AS credential_id, c.pin AS credential_pin, c.account_number AS credential_account_number, " +
                    "t.id AS transaction_id, t.date AS transaction_date, " +
                    "t.amount AS transaction_amount, t.type AS transaction_type " +
                    "FROM users u JOIN accounts a ON u.id = a.user_id " +
                    "JOIN credentials c ON u.credential_id = c.id " +
                    "LEFT JOIN transactions t ON u.id = t.user_id " +
                    "WHERE c.id = ?";

            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setLong(1, credentialID);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    user = mapRow(resultSet);
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
        return user;
    }
}

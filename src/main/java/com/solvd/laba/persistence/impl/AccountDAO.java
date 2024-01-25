package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.Account;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class AccountDAO implements AccountRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(AccountDAO.class);
    @Override
    public void create(Account account, Long userID) {
        Connection conn = CONNECTION_POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            String createQuery =  "INSERT INTO accounts (balance, type, interest_rate, user_id) VALUES (?, ?, ?,?)";
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, account.getBalance());
                ps.setString(2, account.getType());
                ps.setDouble(3, account.getInterestRate());
                ps.setLong(4, userID);
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()){
                    account.setId(resultSet.getLong(1));
                }
            }
            conn.commit();
            LOGGER.info("Account created:\n " + account);
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
    public Account findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Account account = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT id as account_id, balance as account_balance, type as account_type, interest_rate as account_interest_rate, user_id as account_user_id FROM accounts WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setLong(1, id);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    account = mapRow(resultSet);
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
        return account;
    }

    public static Account mapRow(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        long accountID = resultSet.getLong("account_id");
        if (accountID != 0) {
            account.setId(accountID);
            account.setBalance(resultSet.getDouble("account_balance"));
            account.setType(resultSet.getString("account_type"));
            account.setInterestRate(resultSet.getDouble("account_interest_rate"));
        }
        return account;
    }

    @Override
    public void updateById(Account account) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE accounts SET balance = ?, type = ?, interest_rate = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setDouble(1, account.getBalance());
                ps.setString(2, account.getType());
                ps.setDouble(3, account.getInterestRate());
                ps.setLong(4, account.getId());
                ps.executeUpdate();
            }
            connection.commit();
            LOGGER.info("Account updated: " + account);
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

package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.Transaction;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements TransactionRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(TransactionDAO.class);
    @Override
    public void create(Transaction transaction, Long userID) {
        Connection conn = CONNECTION_POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            String createQuery =  "INSERT INTO transactions (date, user_id, amount, type) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(transaction.getDate());
                ps.setDate(1, sqlDate);
                ps.setLong(2, userID);
                ps.setDouble(3, transaction.getAmount());
                ps.setString(4, transaction.getType());

                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()){
                    transaction.setId(resultSet.getLong(1));
                }
            }
            conn.commit();
            LOGGER.info("Transaction created:\n " + transaction);
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
    public Transaction findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Transaction transaction = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT id AS transaction_id, date AS transaction_date, " +
                    "user_id AS transaction_user_id, amount AS transaction_amount,  " +
                    "type AS transaction_type FROM transactions WHERE id = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setLong(1, id);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    transaction = mapRow(resultSet);
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
        return transaction;
    }

    public static List<Transaction> mapRow(ResultSet resultSet, List<Transaction> transactions) throws SQLException{
        if (transactions == null){
            transactions = new ArrayList<>();
        }
        transactions.add(mapRow(resultSet));
        return transactions;
    }

    public static Transaction mapRow(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        long transactionID = resultSet.getLong("transaction_id");
        if (transactionID != 0) {
            transaction.setId(transactionID);
            java.sql.Timestamp dateTimestamp = resultSet.getTimestamp("transaction_date");
            transaction.setDate(dateTimestamp == null ? null : dateTimestamp.toLocalDateTime().toLocalDate());
            transaction.setAmount(resultSet.getDouble("transaction_amount"));
            transaction.setType(resultSet.getString("transaction_type"));
        }
        return transaction;
    }

    @Override
    public void updateById(Transaction transaction) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE transactions SET date = ?, amount = ?, type = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(transaction.getDate());
                ps.setDate(1, sqlDate);
                ps.setDouble(2, transaction.getAmount());
                ps.setString(3, transaction.getType());
                ps.setLong(4, transaction.getId());

                ps.executeUpdate();
            }
            connection.commit();
            LOGGER.info("Transaction updated: " + transaction);
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

package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.Transaction;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TransactionDAO implements TransactionRepository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(TransactionDAO.class);
    @Override
    public void create(Transaction transaction, Long userID) {
        Connection conn = CONNECTION_POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            String createQuery =  "INSERT INTO transactions (date, user_id, transaction_detail_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(transaction.getDate());
                ps.setDate(1, sqlDate);
                ps.setLong(2, userID);
                ps.setLong(3, transaction.getTransactionDetail().getId());
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
            String selectQuery =
                    "SELECT transactions.id AS transaction_id, transactions.date AS transaction_date, transactions.user_id AS user_id, " +
                            "transactions.transaction_detail_id AS transaction_detail_id, transaction_details.amount AS transaction_amount, " +
                            "transaction_details.pre_balance AS pre_balance, transaction_details.post_balance AS post_balance, " +
                            "transaction_details.transfer_to AS transfer_to, transaction_details.type AS transaction_type " +
                            "FROM transactions " +
                            "JOIN " +
                            "transaction_details ON transactions.transaction_detail_id = transaction_details.id " +
                            "WHERE " +
                            "transactions.id = ?";
            
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

    private Transaction mapRow(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        long transactionID = resultSet.getLong("transaction_id");
        if (transactionID != 0) {
            transaction.setId(transactionID);
            java.sql.Timestamp dateTimestamp = resultSet.getTimestamp("transaction_date");
            transaction.setDate(dateTimestamp == null ? null : dateTimestamp.toLocalDateTime().toLocalDate());

//            patient.setMedications(MedicationDAOImpl.mapRow(resultSet, patient.getMedications()));

        }
        return transaction;
    }

    @Override
    public void updateById(Transaction transaction) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE transactions SET date = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(transaction.getDate());
                ps.setDate(1, sqlDate);
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

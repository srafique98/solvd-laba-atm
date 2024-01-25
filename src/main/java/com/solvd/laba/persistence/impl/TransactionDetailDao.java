package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.TransactionDetail;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.TransactionDetailRespository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TransactionDetailDao implements TransactionDetailRespository {
    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(TransactionDetailDao.class);
    @Override
    public void create(TransactionDetail transactionDetail) {
        Connection conn = CONNECTION_POOL.getConnection();
        String createQuery;
        boolean transfer = false;
        try {
            conn.setAutoCommit(false);
            if (transactionDetail.getType().equals("Transfer")) {
                createQuery = "INSERT INTO transaction_details (amount, pre_balance, post_balance, transfer_to, type) VALUES (?, ?, ?,?, ?)";
                transfer = true;
            }
            else {
                createQuery = "INSERT INTO transaction_details (amount, pre_balance, post_balance, type) VALUES (?, ?, ?,?)";
            }
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                if (transfer) {
                    ps.setDouble(1, transactionDetail.getAmount());
                    ps.setDouble(2, transactionDetail.getPreBalance());
                    ps.setDouble(3, transactionDetail.getPostBalance());
                    ps.setString(4, transactionDetail.getTransferTo());
                    ps.setString(5, transactionDetail.getType());
                } else {
                    ps.setDouble(1, transactionDetail.getAmount());
                    ps.setDouble(2, transactionDetail.getPreBalance());
                    ps.setDouble(3, transactionDetail.getPostBalance());
                    ps.setString(4, transactionDetail.getType());
                }
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()){
                    transactionDetail.setId(resultSet.getLong(1));
                }
            }
            conn.commit();
            LOGGER.info("TransactionDetail created:\n " + transactionDetail);
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
    public TransactionDetail findById(Long id) {
        return null;
    }

    @Override
    public void updateById(TransactionDetail transactionDetail) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE transaction_details SET amount = ?, pre_balance = ?, post_balance = ?, transfer_to = ?, type = ? WHERE id = ?";

            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setDouble(1, transactionDetail.getAmount());
                ps.setDouble(2, transactionDetail.getPreBalance());
                ps.setDouble(3, transactionDetail.getPostBalance());
                ps.setString(4, transactionDetail.getTransferTo());
                ps.setString(2, transactionDetail.getType());
                ps.setLong(4, transactionDetail.getId());
                ps.executeUpdate();
            }
            connection.commit();
            LOGGER.info("TransactionDetail updated: " + transactionDetail);
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

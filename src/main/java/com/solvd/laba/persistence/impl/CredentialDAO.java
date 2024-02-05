package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.CredentialRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;


public class CredentialDAO implements CredentialRepository {

    private static final ConnectionPool CONNECTION_POOL = ConnectionPool.getInstance();
    private static final Logger LOGGER = LogManager.getLogger(CredentialDAO.class);

    @Override
    public void create(Credential credential) {
        Connection conn = CONNECTION_POOL.getConnection();
        try {
            conn.setAutoCommit(false);
            String createQuery =  "INSERT INTO credentials (pin, account_number) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, credential.getPin());
                ps.setString(2, credential.getAccountNumber());
                ps.executeUpdate();
                ResultSet resultSet = ps.getGeneratedKeys();
                while (resultSet.next()){
                    credential.setId(resultSet.getLong(1));
                }
            }
            conn.commit();
            LOGGER.info("Credential created:\n " + credential);
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
    public Credential findById(Long id) {
        Connection connection = CONNECTION_POOL.getConnection();
        Credential credential = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT id as credential_id, pin as credential_pin, account_number as credential_account_number FROM credentials WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setLong(1, id);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    credential = mapRow(resultSet);
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
        return credential;

    }

    public static Credential mapRow(ResultSet resultSet) throws SQLException {
        Credential credential = new Credential();
        long credentialID = resultSet.getLong("credential_id");
        if (credentialID != 0) {
            credential.setId(credentialID);
            credential.setPin(resultSet.getString("credential_pin"));
            credential.setAccountNumber(resultSet.getString("credential_account_number"));
        }
        return credential;
    }

    @Override
    public void updateById(Credential credential) {
        Connection connection = CONNECTION_POOL.getConnection();
        try {
            connection.setAutoCommit(false);
            String updateQuery = "UPDATE credentials SET pin = ?, account_number = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                ps.setString(1, credential.getPin());
                ps.setString(2, credential.getAccountNumber());
                ps.setLong(3, credential.getId());
                ps.executeUpdate();
            }
            connection.commit();
            LOGGER.info("Credential updated: " + credential);
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
    public Credential findByAccountNumber(String accountNumber){
        Connection connection = CONNECTION_POOL.getConnection();
        Credential credential = null;
        try {
            connection.setReadOnly(true);
            connection.setAutoCommit(false);
            String selectQuery = "SELECT id as credential_id, pin as credential_pin, account_number as credential_account_number FROM credentials WHERE account_number = ?";
            try (PreparedStatement ps = connection.prepareStatement(selectQuery)) {
                ps.setString(1, accountNumber);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    credential = mapRow(resultSet);
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
        return credential;
    }

}

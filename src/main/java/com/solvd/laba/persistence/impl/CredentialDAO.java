package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.CredentialRepository;
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
        return null;
    }

    @Override
    public void updateById(Credential credential) {

    }
}

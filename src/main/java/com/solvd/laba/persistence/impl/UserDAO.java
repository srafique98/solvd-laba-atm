package com.solvd.laba.persistence.impl;

import com.solvd.laba.domain.User;
import com.solvd.laba.persistence.ConnectionPool;
import com.solvd.laba.persistence.interfaces.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

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
        return null;
    }

    @Override
    public void updateById(User user) {

    }
}

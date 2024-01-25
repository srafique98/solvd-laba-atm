package com.solvd.laba.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ConnectionPool {
    private static ConnectionPool instance;
    private final List<Connection> connections;

    private ConnectionPool(){
        try{
            Class.forName(Config.DRIVER.getValue());
        }
        catch (ClassNotFoundException e){
            throw new RuntimeException("No Driver class: ", e);
        }
        int connectionPoolSize = Integer.parseInt(Config.POOL_SIZE.getValue());
        this.connections = new ArrayList<>(connectionPoolSize);
        IntStream.range(0,connectionPoolSize)
                .boxed()
                .forEach(index -> connections.add(createConnection()));
    }


    private Connection createConnection() {
        try {
            Connection connection = DriverManager.getConnection(Config.URL.getValue(), Config.USERNAME.getValue(), Config.PASSWORD.getValue());
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Create connection failed! ", e);
        }
    }

    public Connection getConnection(){
        synchronized (connections){
            while (connections.isEmpty()){
                try {
                    connections.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Waiting for connection was interrupted", e);
                }
            }
            return connections.remove(0);
        }
    }

    public void releaseConnection(Connection connection){
        synchronized (connections){
            if (!connections.contains(connection)) {
                connections.add(connection);
            }
            else{
                throw new RuntimeException("Connection already exist in pool");
            }
        }
    }

    public void closeConnection(){
        synchronized (connections){
            connections.forEach(connection -> {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            connections.clear();
        }
    }

    public static ConnectionPool getInstance(){
        if (instance == null){
            synchronized (ConnectionPool.class){
                if (instance == null){
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }
}

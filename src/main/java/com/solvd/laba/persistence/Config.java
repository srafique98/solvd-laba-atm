package com.solvd.laba.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public enum Config {
    DRIVER("driver"),
    URL("url"),
    USERNAME("username"),
    PASSWORD("password"),
    POOL_SIZE("poolSize");

    private static final Logger LOGGER = LogManager.getLogger(Config.class);
    private static final Properties prop;
    private final String key;

    static {
        prop = new Properties();
        try {
            InputStream input = new FileInputStream("src/main/resources/config.properties");
            prop.load(input);
            LOGGER.info("Successfully loaded config.properties file. Driver: "+ prop.getProperty("driver"));
        } catch (FileNotFoundException e) {
            LOGGER.error("No config.properties file found!");
            throw new RuntimeException(e);
        } catch (Exception e){
            LOGGER.error("Something went wrong");
            e.printStackTrace();
        }
    }

    Config(String configKey) {
        this.key = configKey;
    }

    public String getValue() {
        return prop.getProperty(this.key);
    }
}

package org.how_wow.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCManager {
    private static final JDBCConfig jdbcConfig;

    static {
        jdbcConfig = new JDBCConfig();
    }
    public static Connection getConnection() {
        try {
            Class.forName(jdbcConfig.getDriverClassName());
            return DriverManager.getConnection(jdbcConfig.getUrl(), jdbcConfig.getUsername(), jdbcConfig.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось установить соединение с базой данных", e);
        }
    }

}

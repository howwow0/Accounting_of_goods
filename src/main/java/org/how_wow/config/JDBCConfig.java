package org.how_wow.config;

import lombok.Getter;

import java.util.Properties;

@Getter
public final class JDBCConfig {
    private final String driverClassName;
    private final String url;
    private final String username;
    private final String password;

    private final String liquibaseChangelog;

    {
        try {
            Properties prop = new Properties();
            prop.load(JDBCConfig.class.getClassLoader().getResourceAsStream("JDBCConfig.properties"));
            driverClassName = prop.getProperty("database.driverClassName");
            url = prop.getProperty("database.url");
            username = prop.getProperty("database.username");
            password = prop.getProperty("database.password");
            liquibaseChangelog = prop.getProperty("liquibase.changeLogFile");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке JDBC конфигурации", e);
        }
    }
}

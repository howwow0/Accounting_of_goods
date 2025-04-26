package org.how_wow.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class LiquibaseRunner {
    public void migrate() {
        try {
            JDBCConfig jdbcConfig = new JDBCConfig();
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(JDBCManager.getConnection()));

            Liquibase liquibase = new Liquibase(
                    jdbcConfig.getLiquibaseChangelog(),
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(new Contexts(), new LabelExpression());
            System.out.println("Миграции успешно выполнены");

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении миграций:" + e.getMessage());
        }
    }
}
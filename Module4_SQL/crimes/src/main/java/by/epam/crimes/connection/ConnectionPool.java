package by.epam.crimes.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;

public class ConnectionPool {

    //Setting up HikariCP
    private static final HikariConfig config = new HikariConfig("/datasource.properties");
    private static final HikariDataSource dataSource = new HikariDataSource(config);
    //Setting up FluentJdbc
    private static final FluentJdbc fluentJdbc = new FluentJdbcBuilder().connectionProvider(dataSource).build();

    public static FluentJdbc getFluentJdbc() {
        return fluentJdbc;
    }

    private ConnectionPool() {
    }

}
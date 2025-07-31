package config;


import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

public class DatabaseConfig {
	private static PGSimpleDataSource dataSource;
	
	public static DataSource getDataSource(){
		if(dataSource != null) return (DataSource) dataSource;
		
		dataSource = new PGSimpleDataSource();
		dataSource.setServerNames(new String[]{"postgres"});
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setDatabaseName("postgres");
        dataSource.setCurrentSchema("postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        
        return (DataSource) dataSource;
	}
}

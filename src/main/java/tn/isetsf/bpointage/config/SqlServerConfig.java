package tn.isetsf.bpointage.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "ServerEntityManagerFactory", transactionManagerRef = "ServerTransactionManager", basePackages = {
		"tn.isetsf.bpointage.repository.SqlServer" })
public class SqlServerConfig {

	@Bean(name = "ServerDataSource")
	@ConfigurationProperties(prefix = "spring.book.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "ServerEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean ServerEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("ServerDataSource") DataSource dataSource) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "none");
		properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
		return builder.dataSource(dataSource).properties(properties)
				.packages("tn.isetsf.bpointage.model.SqlServer").persistenceUnit("Server").build();
	}

	@Bean(name = "ServerTransactionManager")
	public PlatformTransactionManager ServerTransactionManager(
			@Qualifier("ServerEntityManagerFactory") EntityManagerFactory ServerEntityManagerFactory) {
		return new JpaTransactionManager(ServerEntityManagerFactory);
	}
}

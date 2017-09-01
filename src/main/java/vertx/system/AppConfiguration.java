package vertx.system;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * A configuration bean.
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.dao"})
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan("com.service")
public class AppConfiguration {

  @Autowired
  Environment environment;

  /**
   * 设置网络访问端口
   * @return
   */
  int httpPort() {
    return environment.getProperty("http.port", Integer.class, 8080);
  }

  /**
   * 设置数据库访问连接
   * @param populator
   * @return
   */
  @Bean
  @Autowired
  public DataSource dataSource(DatabasePopulator populator) {
    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
    dataSource.setUrl(environment.getProperty("jdbc.url"));
    dataSource.setUsername(environment.getProperty("jdbc.username"));
    dataSource.setPassword(environment.getProperty("jdbc.password"));
    DatabasePopulatorUtils.execute(populator, dataSource);
    return dataSource;
  }
  
  @Bean
  @Autowired
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource) {
    final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(dataSource);
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(Boolean.TRUE); vendorAdapter.setShowSql(Boolean.TRUE);
    factory.setDataSource(dataSource);
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.bean");
    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
    jpaProperties.put("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
    factory.setJpaProperties(jpaProperties);
    return factory;
  }

  @Bean
  @Autowired
  public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory.getObject());
  }

  @Bean
  @Autowired
  public DatabasePopulator databasePopulator() {
    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.setContinueOnError(false);
    populator.addScript(new ClassPathResource("products.sql"));
    return populator;
  }
}

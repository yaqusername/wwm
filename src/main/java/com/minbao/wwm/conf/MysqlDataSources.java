/**
 * caicongyang.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.minbao.wwm.conf;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * mysql数据源
 *
 * @author admin
 * @version $Id: DataSources.java, v 0.1 admin Exp $
 */
@Configuration
@MapperScan(basePackages = "com.minbao.wwm.dao.mapper", sqlSessionFactoryRef = "mysqlSessionFactory")
public class MysqlDataSources {

	@Autowired
	private MySqlProperties properties;

	/**
	 * mysql库数据源
	 *
	 * @return
	 */
	@Bean(name = "mysqlDataSource")
	public DataSource getMysqlDataSource() {
		final HikariDataSource ds = new HikariDataSource();
		ds.setDriverClassName(properties.getDriverClassName());
		ds.setConnectionTestQuery(properties.getConnectionTestQuery());
		ds.setConnectionTimeout(properties.getConnectionTimeoutMs());
		ds.setIdleTimeout(properties.getIdleTimeoutMs());
		ds.setMaxLifetime(properties.getMaxLifetimeMs());
		ds.setMaximumPoolSize(properties.getMaxPoolSize());
		ds.setMinimumIdle(properties.getMinIdle());
		ds.setJdbcUrl(properties.getJdbcUrl());
		ds.setUsername(properties.getUsername());
		ds.setPassword(properties.getPassword());
		return ds;
	}

	/**
	 * 数据源事务管理器
	 */
	@Bean(name = "mysqlDataSourceTransactionManager")
	public DataSourceTransactionManager getTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * 编程式事务
	 */
	@Bean(name = "mysqlTransactionTemplate")
	public TransactionTemplate getTransactionTemplate(@Qualifier("mysqlDataSourceTransactionManager") DataSourceTransactionManager manager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate();
		transactionTemplate.setTimeout(properties.getTransactionTimeoutS());
		transactionTemplate.setTransactionManager(manager);
		return transactionTemplate;
	}

	/**
	 * mybatis 的sessionFactory
	 */
	@Bean(name = "mysqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		// 扫描mapper.xml
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mysql/*.xml"));

		return sqlSessionFactoryBean.getObject();
	}

}

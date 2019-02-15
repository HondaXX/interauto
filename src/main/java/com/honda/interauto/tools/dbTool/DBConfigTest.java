package com.honda.interauto.tools.dbTool;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.honda.interauto.dao.user", sqlSessionFactoryRef  = "sqlSessionFactoryDBtest")  //sqlSessionTemplateRef = "sqlSessionTemplateDBauto"
public class DBConfigTest {
    @Bean(name = "dataSourceDBtest")  //initMethod = "init", destroyMethod = "close"
    @ConfigurationProperties(prefix = "spring.datasource.dbtest")
    public DataSource dataSourceDBtest(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactoryDBtest")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSourceDBtest") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/user/*.xml"));
        return sessionFactoryBean.getObject();
    }
}

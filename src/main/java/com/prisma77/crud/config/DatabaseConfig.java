package com.prisma77.crud.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final SqlSessionFactory sqlSessionFactory;

    static {
        try {
            // 1. 데이터베이스 설정 프로퍼티 파일 로드
            Properties props = new Properties();
            try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db/dev.properties")) {
                props.load(input);
            }

            // 2. HikariCP 커넥션 풀 설정 및 생성
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("jdbc.driverClassName"));
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.username"));
            config.setPassword(props.getProperty("jdbc.password"));

            DataSource dataSource = new HikariDataSource(config);

            // 3. MyBatis 설정 (보다 안정적인 방식으로 변경)
            try (InputStream mybatisConfigStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("config/mybatis-config.xml")) {
                // XML 설정 파일을 먼저 분석합니다.
                XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(mybatisConfigStream, null, null);
                Configuration configuration = xmlConfigBuilder.parse();

                // DB 커넥션 풀(DataSource) 정보를 담은 Environment를 생성합니다.
                Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);

                // 분석된 설정 정보에 우리가 만든 Environment를 명시적으로 설정합니다.
                configuration.setEnvironment(environment);

                // 최종적으로 완성된 설정을 바탕으로 SqlSessionFactory를 빌드합니다.
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            }

        } catch (IOException e) {
            throw new RuntimeException("데이터베이스 설정 초기화에 실패했습니다.", e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    private DatabaseConfig() {}
}
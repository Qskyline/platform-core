package com.skyline.platform.core.configure;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.db.DBAppender;
import ch.qos.logback.core.db.DataSourceConnectionSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class ApplicationConfigure {
    @Autowired
    DBAppender dbAppender;

    private String logDbMarkerString = "savedb";

    @Bean
    public DBAppender dbAppender(DataSource dataSource){
        DBAppender dbAppender = new DBAppender();
        MyDBNameResolver myDBNameResolver = new MyDBNameResolver();
        dbAppender.setDbNameResolver(myDBNameResolver);
        DataSourceConnectionSource connectionSource = new DataSourceConnectionSource();
        connectionSource.setDataSource(dataSource);
        connectionSource.start();
        dbAppender.setConnectionSource(connectionSource);
        LogSave2DbFilter logSave2DbFilter = new LogSave2DbFilter();
        logSave2DbFilter.setMarker(logDbMarkerString);
        logSave2DbFilter.start();
        dbAppender.addFilter(logSave2DbFilter);
        dbAppender.start();
        return dbAppender;
    }

    @Bean
    public AsyncAppender asyncAppender() {
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.addAppender(dbAppender);
        asyncAppender.start();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("ROOT").addAppender(asyncAppender);
        return asyncAppender;
    }
}

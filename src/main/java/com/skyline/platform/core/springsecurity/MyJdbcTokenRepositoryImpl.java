package com.skyline.platform.core.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
public class MyJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {
    public static final String DEF_REMOVE_SERIES_TOKENS_SQL = "delete from persistent_logins where series = ?";

    private String removeSeriesTokensSql = DEF_REMOVE_SERIES_TOKENS_SQL;

    @Autowired
    DataSource dataSource;

    public void removeSeriesTokens(String series) {
        getJdbcTemplate().update(removeSeriesTokensSql, series);
    }

    @PostConstruct
    public void init() {
        this.setDataSource(dataSource);
    }
}

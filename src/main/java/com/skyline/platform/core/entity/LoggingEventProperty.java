package com.skyline.platform.core.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_logging_event_property")
public class LoggingEventProperty {
    private LoggingEventPropertyKey loggingEventPropertyKey;
    private String mapped_value;

    @Id
    public LoggingEventPropertyKey getLoggingEventPropertyKey() {
        return loggingEventPropertyKey;
    }
    public void setLoggingEventPropertyKey(LoggingEventPropertyKey loggingEventPropertyKey) {
        this.loggingEventPropertyKey = loggingEventPropertyKey;
    }

    @Column
    public String getMapped_value() {
        return mapped_value;
    }
    public void setMapped_value(String mapped_value) {
        this.mapped_value = mapped_value == null ? null : mapped_value.trim();
    }
}
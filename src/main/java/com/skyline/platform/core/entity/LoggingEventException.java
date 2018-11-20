package com.skyline.platform.core.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sys_logging_event_exception")
public class LoggingEventException  {
    private LoggingEventExceptionKey loggingEventExceptionKey;
    private String trace_line;

    @Id
    public LoggingEventExceptionKey getLoggingEventExceptionKey() {
        return loggingEventExceptionKey;
    }
    public void setLoggingEventExceptionKey(LoggingEventExceptionKey loggingEventExceptionKey) {
        this.loggingEventExceptionKey = loggingEventExceptionKey;
    }

    public String getTrace_line() {
        return trace_line;
    }
    public void setTrace_line(String trace_line) {
        this.trace_line = trace_line == null ? null : trace_line.trim();
    }
}
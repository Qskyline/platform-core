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
@Table(name = "sys_logging_event")
public class LoggingEvent {
    private Long event_id;
    private Long timestmp;
    private String logger_name;
    private String level_string;
    private String thread_name;
    private Short reference_flag;
    private String arg0;
    private String arg1;
    private String arg2;
    private String arg3;
    private String caller_filename;
    private String caller_class;
    private String caller_method;
    private String caller_line;
    private String formatted_message;

    @Id
    public Long getEvent_id() {
        return event_id;
    }
    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    @Column
    public Long getTimestmp() {
        return timestmp;
    }
    public void setTimestmp(Long timestmp) {
        this.timestmp = timestmp;
    }

    @Column
    public String getLogger_name() {
        return logger_name;
    }
    public void setLogger_name(String logger_name) {
        this.logger_name = logger_name == null ? null : logger_name.trim();
    }

    @Column
    public String getLevel_string() {
        return level_string;
    }
    public void setLevel_string(String level_string) {
        this.level_string = level_string == null ? null : level_string.trim();
    }

    @Column
    public String getThread_name() {
        return thread_name;
    }
    public void setThread_name(String thread_name) {
        this.thread_name = thread_name == null ? null : thread_name.trim();
    }

    @Column
    public Short getReference_flag() {
        return reference_flag;
    }
    public void setReference_flag(Short reference_flag) {
        this.reference_flag = reference_flag;
    }

    @Column
    public String getArg0() {
        return arg0;
    }
    public void setArg0(String arg0) {
        this.arg0 = arg0 == null ? null : arg0.trim();
    }

    @Column
    public String getArg1() {
        return arg1;
    }
    public void setArg1(String arg1) {
        this.arg1 = arg1 == null ? null : arg1.trim();
    }

    @Column
    public String getArg2() {
        return arg2;
    }
    public void setArg2(String arg2) {
        this.arg2 = arg2 == null ? null : arg2.trim();
    }

    @Column
    public String getArg3() {
        return arg3;
    }
    public void setArg3(String arg3) {
        this.arg3 = arg3 == null ? null : arg3.trim();
    }

    @Column
    public String getCaller_filename() {
        return caller_filename;
    }
    public void setCaller_filename(String caller_filename) {
        this.caller_filename = caller_filename == null ? null : caller_filename.trim();
    }

    @Column
    public String getCaller_class() {
        return caller_class;
    }
    public void setCaller_class(String caller_class) {
        this.caller_class = caller_class == null ? null : caller_class.trim();
    }

    @Column
    public String getCaller_method() {
        return caller_method;
    }
    public void setCaller_method(String caller_method) {
        this.caller_method = caller_method == null ? null : caller_method.trim();
    }

    @Column
    public String getCaller_line() {
        return caller_line;
    }
    public void setCaller_line(String caller_line) {
        this.caller_line = caller_line == null ? null : caller_line.trim();
    }

    @Column
    public String getFormatted_message() {
        return formatted_message;
    }
    public void setFormatted_message(String formatted_message) {
        this.formatted_message = formatted_message == null ? null : formatted_message.trim();
    }
}
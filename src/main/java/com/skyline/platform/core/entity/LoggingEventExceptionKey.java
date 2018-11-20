package com.skyline.platform.core.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LoggingEventExceptionKey implements Serializable {
    private Long event_id;
    private Short i;

    private static final long serialVersionUID = 1L;

    public LoggingEventExceptionKey(Long event_id, Short i) {
        this.event_id = event_id;
        this.i = i;
    }

    public LoggingEventExceptionKey() {}

    public Long getEvent_id() {
        return event_id;
    }
    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    public Short getI() {
        return i;
    }
    public void setI(Short i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (!(other instanceof LoggingEventExceptionKey)) return false;

        LoggingEventExceptionKey t = (LoggingEventExceptionKey) other;

        return (this.event_id == t.getEvent_id() || (this.event_id != null && this.event_id.equals(t.getEvent_id())))&&
                (this.i == t.getI() || (this.i != null && this.i.equals(t.getI())));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (event_id == null ? 0 : event_id.hashCode());
        result = 37 * result + (i == null ? 0 : i.hashCode());
        return result;
    }
}
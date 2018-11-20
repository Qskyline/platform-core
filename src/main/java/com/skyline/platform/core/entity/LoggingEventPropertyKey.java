package com.skyline.platform.core.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LoggingEventPropertyKey implements Serializable {
    private Long event_id;
    private String mapped_key;

    private static final long serialVersionUID = 1L;

    public Long getEvent_id() {
        return event_id;
    }
    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    public String getMapped_key() {
        return mapped_key;
    }
    public void setMapped_key(String mapped_key) {
        this.mapped_key = mapped_key == null ? null : mapped_key.trim();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (!(other instanceof LoggingEventExceptionKey)) return false;

        LoggingEventPropertyKey t = (LoggingEventPropertyKey) other;

        return (this.event_id == t.getEvent_id() || (this.event_id != null && this.event_id.equals(t.getEvent_id())))&&
                (this.mapped_key == t.getMapped_key() || (this.mapped_key != null && this.mapped_key.equals(t.getMapped_key())));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (event_id == null ? 0 : event_id.hashCode());
        result = 37 * result + (mapped_key == null ? 0 : mapped_key.hashCode());
        return result;
    }
}
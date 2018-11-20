package com.skyline.platform.core.configure;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogSave2DbFilter extends AbstractMatcherFilter<ILoggingEvent> {
    private Marker markerToMatch = null;

    @Override
    public void start() {
        if (null != this.markerToMatch) {
            super.start();
        } else {
            addError(" no marker yet !");
        }
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        Marker marker = event.getMarker();
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (null == marker) {
            return FilterReply.DENY;
        }
        if (markerToMatch.contains(marker) && StringUtils.isNotBlank(event.getFormattedMessage())) {
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }

    public void setMarker(String markerStr) {
        if(null != markerStr) {
            markerToMatch = MarkerFactory.getMarker(markerStr);
        }
    }
}

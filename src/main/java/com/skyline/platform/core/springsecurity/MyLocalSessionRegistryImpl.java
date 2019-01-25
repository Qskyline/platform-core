package com.skyline.platform.core.springsecurity;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.util.Assert;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName MyLocalSessionRegistryImpl
 * @Description TODO
 * @Author skyline
 * @Date 2019/1/25 15:41
 * Version 1.0
 **/
public class MyLocalSessionRegistryImpl extends SessionRegistryImpl {

    private final ConcurrentMap<Object, Set<String>> principals;
    private final Map<String, SessionInformation> sessionIds;

    public MyLocalSessionRegistryImpl() {
        this(new ConcurrentHashMap(), new ConcurrentHashMap());
    }

    public MyLocalSessionRegistryImpl(ConcurrentMap<Object, Set<String>> principals, Map<String, SessionInformation> sessionIds) {
        super(principals, sessionIds);
        this.principals = principals;
        this.sessionIds = sessionIds;
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Registering session " + sessionId + ", for principal " + principal);
        }

        if (this.getSessionInformation(sessionId) != null) {
            this.removeSessionInformation(sessionId);
        }

        this.sessionIds.put(sessionId, new MySessionInformation(principal, sessionId, new Date()));
        Set<String> sessionsUsedByPrincipal = this.principals.get(principal);
        if (sessionsUsedByPrincipal == null) {
            sessionsUsedByPrincipal = new CopyOnWriteArraySet();
            Set<String> prevSessionsUsedByPrincipal = this.principals.putIfAbsent(principal, sessionsUsedByPrincipal);
            if (prevSessionsUsedByPrincipal != null) {
                sessionsUsedByPrincipal = prevSessionsUsedByPrincipal;
            }
        }

        sessionsUsedByPrincipal.add(sessionId);
        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Sessions used by '" + principal + "' : " + sessionsUsedByPrincipal);
        }

    }
}

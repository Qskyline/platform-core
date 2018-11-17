package com.skyline.platform.core.springsecurity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class MySessionRegistryImpl implements SessionRegistry, ApplicationListener<SessionDestroyedEvent> {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	protected final Log logger = LogFactory.getLog(MySessionRegistryImpl.class);
	private String principals = "principals";
	private String sessionIds = "sessionIds";
	
	private void putPrincipal(Object principal, Set<String> sessionIds) {
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("key", principal);
		temp.put("value", sessionIds);
		redisTemplate.opsForHash().put(principals, String.valueOf(principal.hashCode()), temp);
	}
	private void removePrincipal(Object principal) {
		redisTemplate.opsForHash().delete(principals, String.valueOf(principal.hashCode()));
	}
	
	private void putSession(SessionInformation sessionInformation) {
		redisTemplate.opsForHash().put(sessionIds, sessionInformation.getSessionId(), sessionInformation);
	}
	private void removeSession(SessionInformation sessionInformation) {
		redisTemplate.opsForHash().delete(sessionIds, sessionInformation.getSessionId());
	}

	private CopyOnWriteArraySet<String> getSessionsUsedByPrincipal(Object principal) {
		Map<Object, Object> temp = (Map<Object, Object>)redisTemplate.opsForHash().get(principals, String.valueOf(principal.hashCode()));
		if(temp == null) return null;
		return (CopyOnWriteArraySet<String>) temp.get("value");
	}	

	public List<Object> getAllPrincipals() {
		ArrayList<Object> result = new ArrayList<>();
		Map<Object, Object> temp = redisTemplate.opsForHash().entries(principals);
		
		for (Iterator<Entry<Object, Object>> iterator = temp.entrySet().iterator(); iterator.hasNext();) {
			Entry<Object, Object> entry = iterator.next();
			HashMap<Object, Object> value = (HashMap<Object, Object>) entry.getValue();
			result.add(value.get("key"));
		}
		
		return result;
	}

	public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
		Set<String> sessionsUsedByPrincipal = getSessionsUsedByPrincipal(principal);
		if (sessionsUsedByPrincipal == null) {
			return Collections.emptyList();
		} else {
			ArrayList<SessionInformation> list = new ArrayList<SessionInformation>(sessionsUsedByPrincipal.size());
			Iterator<String> arg4 = sessionsUsedByPrincipal.iterator();
			while (true) {
				SessionInformation sessionInformation;
				do {
					do {
						if (!arg4.hasNext()) {
							return list;
						}
						String sessionId = (String) arg4.next();
						sessionInformation = this.getSessionInformation(sessionId);
					} while (sessionInformation == null);
				} while (!includeExpiredSessions && sessionInformation.isExpired());
				list.add(sessionInformation);
			}
		}
	}

	public SessionInformation getSessionInformation(String sessionId) {
		Assert.hasText(sessionId, "SessionId required as per interface contract");
		return (SessionInformation) redisTemplate.opsForHash().get(sessionIds, sessionId);
	}

	public void onApplicationEvent(SessionDestroyedEvent event) {
		String sessionId = event.getId();
		this.removeSessionInformation(sessionId);
	}

	public void refreshLastRequest(String sessionId) {
		Assert.hasText(sessionId, "SessionId required as per interface contract");
		SessionInformation info = this.getSessionInformation(sessionId);
		if (info != null) {
			info.refreshLastRequest();
		}
		putSession(info);
	}

	public void registerNewSession(String sessionId, Object principal) {
		Assert.hasText(sessionId, "SessionId required as per interface contract");
		Assert.notNull(principal, "Principal required as per interface contract");
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Registering session " + sessionId + ", for principal " + principal);
		}
		if (this.getSessionInformation(sessionId) != null) {
			this.removeSessionInformation(sessionId);
		}
		putSession(new MySessionInformation(principal, sessionId, new Date()));
		
		CopyOnWriteArraySet<String> sessionsUsedByPrincipal = getSessionsUsedByPrincipal(principal);
		if (sessionsUsedByPrincipal == null) {
			sessionsUsedByPrincipal = new CopyOnWriteArraySet<String>();
		}
		sessionsUsedByPrincipal.add(sessionId);
		putPrincipal(principal, sessionsUsedByPrincipal);
		
		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Sessions used by \'" + principal + "\' : " + sessionsUsedByPrincipal);
		}
	}

	public void removeSessionInformation(String sessionId) {
		Assert.hasText(sessionId, "SessionId required as per interface contract");
		SessionInformation info = this.getSessionInformation(sessionId);
		if (info != null) {
			if (this.logger.isTraceEnabled()) {
				this.logger.debug("Removing session " + sessionId + " from set of registered sessions");
			}
			removeSession(info);
			
			Set<String> sessionsUsedByPrincipal = getSessionsUsedByPrincipal(info.getPrincipal());
			if (sessionsUsedByPrincipal != null) {
				if (this.logger.isDebugEnabled()) {
					this.logger.debug("Removing session " + sessionId + " from principal\'s set of registered sessions");
				}
				sessionsUsedByPrincipal.remove(sessionId);
				if (sessionsUsedByPrincipal.isEmpty()) {
					if (this.logger.isDebugEnabled()) {
						this.logger.debug("Removing principal " + info.getPrincipal() + " from registry");
					}
					removePrincipal(info.getPrincipal());
				} else {
					putPrincipal(info.getPrincipal(), sessionsUsedByPrincipal);
				}
				if (this.logger.isTraceEnabled()) {
					this.logger.trace("Sessions used by \'" + info.getPrincipal() + "\' : " + sessionsUsedByPrincipal);
				}
			}
		}
	}
	
	public void updateSessionInformation(SessionInformation session) {
		if(getSessionInformation(session.getSessionId()) != null) {
			putSession(session);
		}
	}
}
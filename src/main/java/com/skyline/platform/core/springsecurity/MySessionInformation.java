package com.skyline.platform.core.springsecurity;

import org.springframework.security.core.session.SessionInformation;
import java.io.Serializable;
import java.util.Date;

public class MySessionInformation extends SessionInformation implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean kicked = false;

	public MySessionInformation(Object principal, String sessionId, Date lastRequest) {
		super(principal, sessionId, lastRequest);
	}
	
	public boolean isKicked() {
		return kicked;
	}	
	public void setKicked() {
		kicked = true;
	}

}

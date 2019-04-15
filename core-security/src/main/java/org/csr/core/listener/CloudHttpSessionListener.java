package org.csr.core.listener;

import javax.servlet.http.HttpSessionListener;

import org.csr.core.cloudsession.CloudSessionManager;

public abstract class CloudHttpSessionListener implements HttpSessionListener {

	public CloudHttpSessionListener() {
		CloudSessionManager.getInstance().addApplicationEventListener(this);
	}

}

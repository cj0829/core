package org.csr.core.userdetails;

import java.io.Serializable;

public interface SecurityUser extends Serializable {

	public Long getId();
	
	public String getLoginName();

	public String getPassword();

	/**
	 * 域
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public Long getRoot();
	
	/**
	 * 主域
	 * @author caijin
	 * @return
	 * @since JDK 1.7
	 */
	public Long getPrimaryOrgId();
}

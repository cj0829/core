package org.csr.core.persistence.business.domain;

/**
 * ClassName:UserStatus.java <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface UserStatus {
	/**新建*/
	Integer NEW = 1;
	/**正常的*/
	Integer NORMAL = 2;
	/**停止*/
	Integer STOP = 3;
	/**4.待批*/
	public Integer PENDING=4;
	/**5.通过*/
	public Integer PASS = 5;
	/**6.驳回*/
	public Integer REFUSAL=6;
}

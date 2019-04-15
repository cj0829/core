package org.csr.core.context;

import java.io.Serializable;

import org.csr.core.Authentication;

/**
 * ClassName:SecurityContext.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-28上午9:20:10 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public interface SecurityContext extends Serializable {

    Authentication getAuthentication();

    void setAuthentication(Authentication authentication);

}

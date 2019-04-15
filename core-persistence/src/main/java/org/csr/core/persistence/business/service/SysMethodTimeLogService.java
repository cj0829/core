package org.csr.core.persistence.business.service;

import org.csr.core.persistence.business.domain.SysMethodTimeLog;
import org.csr.core.persistence.service.BasisService;
import org.csr.core.queue.MessageService;

/**
 * @(#)Sss.java
 * 
 * 				ClassName:SysMethodTimeLog.java <br/>
 *              Date: Thu Mar 15 12:13:51 CST 2018 <br/>
 * @author caijin-ThinkPad-X260 <br/>
 * @version 1.0 <br/>
 * @since JDK 1.7 系统方法日志 service接口
 */
public interface SysMethodTimeLogService extends BasisService<SysMethodTimeLog, Long>, MessageService<SysMethodTimeLog> {

}

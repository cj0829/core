package org.csr.core.persistence.listener;

import org.csr.core.Authentication;
import org.csr.core.Constants;
import org.csr.core.SecurityResource;
import org.csr.core.UserSession;
import org.csr.core.constant.OperationLogType;
import org.csr.core.context.SecurityContextHolder;
import org.csr.core.context.anonymous.AnonymousToken;
import org.csr.core.persistence.message.LogMsg;
import org.csr.core.persistence.message.OperLogMessage;
import org.csr.core.persistence.message.PointsLogMessage;
import org.csr.core.queue.QueueService;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ObjUtil;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

/**
 * ClassName:OperationLogListener.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午4:17:40 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class OperationLogListener extends DefaultLoadEventListener implements PostUpdateEventListener, PostDeleteEventListener
, PostInsertEventListener{
	
	private static final long serialVersionUID = 8362594087867818933L;

	public void onPostUpdate(final PostUpdateEvent event) {
		log(new EventSession(){
//			@Override
//			public void asJson(JSONObject oldjson, JSONObject newjson) {
//				if(event.getEntity()!=null){
//					newjson.put("class", event.getEntity().getClass().getName());
//					oldjson.put("class", event.getEntity().getClass().getName());
//				}
//				 for (int i = 0; i < event.getState().length; i++) {  
//			        // 更新前的值  
//			        Object oldValue = event.getOldState()[i];  
//			        // 更新后的新值  
//			        Object newValue = event.getState()[i];  
//			        //更新的属性名  
//			        String propertyName = event.getPersister().getPropertyNames()[i];  
//			        if (newValue != null) {
//						if (isWrapClass(newValue.getClass())) {
//							newjson.put(propertyName, newValue);
//						} else if (newValue.getClass().equals(String.class)) {
//							if(newValue.toString().length() >= 6000){
//								newValue=newValue.toString().substring(0, 6000)+"... ...";
//							}
//							newjson.put(propertyName, newValue);
//						}
//					}else{
//						newjson.put(propertyName, "Null");
//					}
//			        if (oldValue != null) {
//						if (isWrapClass(oldValue.getClass())) {
//							oldjson.put(propertyName, oldValue);
//						} else if (oldValue.getClass().equals(String.class)) {
//							if(oldValue.toString().length() >= 6000){
//								oldValue=oldValue.toString().substring(0, 6000)+"... ...";
//							}
//							oldjson.put(propertyName, oldValue);
//						}
//					}else{
//						oldjson.put(propertyName, "Null");
//					}
//			    } 
//			}
			@Override
			public Byte getLogType() {
				return OperationLogType.UPDATE;
			}

			@Override
			public Object getEntity() {
				return event.getEntity();
			}
		});
	}

	public void onPostDelete(final PostDeleteEvent event) {
		log(new EventSession(){
//			@Override
//			public void asJson(JSONObject oldjson, JSONObject newjson) {
//				if(event.getEntity()!=null){
//					newjson.put("class", event.getEntity().getClass().getName());
//				}
//			}
			@Override
			public Byte getLogType() {
				return OperationLogType.DELETE;
			}

			@Override
			public Object getEntity() {
				return event.getEntity();
			}
		});
	}

	public void onPostInsert(final PostInsertEvent event) {
	
		log(new EventSession(){
//			@Override
//			public void asJson(JSONObject oldjson, JSONObject newjson) {
//				if(event.getEntity()!=null){
//					newjson.put("class", event.getEntity().getClass().getName());
//				}
//				 for (int i = 0; i < event.getState().length; i++) {
//		            // 更新后的新值  
//		            Object newValue = event.getState()[i];  
//		            //更新的属性名  
//		            String propertyName = event.getPersister().getPropertyNames()[i];  
//					if (newValue != null) {
//						if (isWrapClass(newValue.getClass())) {
//							newjson.put(propertyName, newValue);
//						} else if (newValue.getClass().equals(String.class)) {
//							if(newValue.toString().length() >= 6000){
//								newValue=newValue.toString().substring(0, 6000)+"... ...";
//							}
//							newjson.put(propertyName, newValue);
//						}
//					}else{
//						newjson.put(propertyName, "Null");
//					}
//		        } 
//			}
			@Override
			public Byte getLogType() {
				return OperationLogType.ADD;
			}

			@Override
			public Object getEntity() {
				return event.getEntity();
			}
		});
	}
	
	private interface EventSession {

//		void asJson(JSONObject oldjson, JSONObject newjson);

//		Session openSession();
		Object 	getEntity();
		Byte getLogType();

	}
	
	private void log(EventSession event){
		Object entity = event.getEntity();
		if(entity instanceof LogMsg ){
			return;
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//当前如果没有，安全认证，不记日志。或者，当前为匿名访问，也不记录日志
		if(ObjUtil.isEmpty(authentication) || authentication instanceof AnonymousToken){
			return;
		}
		if(Constants.USER_SUPER.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
			return;
		}
		UserSession us=authentication.getUserSession();
		//记录积分日志
		if(ObjUtil.isNotEmpty(us)){
			//TODO 判断是否用户的记录日志的次数
			pointsLog(us);
		}
		SecurityResource security=us.getSecurityResource();
		//记录操作日志
		if (ObjUtil.isNotEmpty(us) && ObjUtil.isNotEmpty(security) && security.getOperationLogLevel()>= 2) {
			operLog(event,security,us);
		}
	}
	
	//记录操作日志
	private void operLog(EventSession event,SecurityResource security,UserSession us){
		QueueService queueService = (QueueService) ClassBeanFactory.getBean("queueService");
		if(ObjUtil.isNotEmpty(queueService)){
			OperLogMessage log=new OperLogMessage(us);
			log.setLogType(event.getLogType());
			queueService.sendMessage(log);
		}
	}

	//记录积分日志
	private void pointsLog(UserSession us){
		QueueService queueService = (QueueService) ClassBeanFactory.getBean("queueService");
		if(ObjUtil.isNotEmpty(queueService)){
			PointsLogMessage pointsLog=new PointsLogMessage(us);
			queueService.sendMessage(pointsLog);
		}
	
	}
	

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		return false;
	}   
}

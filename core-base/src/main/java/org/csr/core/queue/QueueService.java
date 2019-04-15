package org.csr.core.queue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ObjUtil;


/**
 * ClassName:QueueService.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-20下午2:17:55 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class QueueService {

	protected boolean await = false;
	protected ConcurrentLinkedQueue<Message> queue=new ConcurrentLinkedQueue<Message>();
	protected long Interval = 1;
	protected long size = 100;
	
	protected Map<String,String> mapProcessingService=new LinkedHashMap<String,String>();
	
	public QueueService() {
		init();
	}

	public void init(){
		start();
	}
	
	public int sendMessage(Message message){
		if(queue==null){
			queue=new ConcurrentLinkedQueue<Message>();
		}
		queue.offer(message);
		return queue.size();
	}
	/**
	 * 启动服务
	 */
	public void start() {

		try {
			SearchTask enumerator = new SearchTask();     
	        new Thread(enumerator).start();   
		} catch (Throwable t) {
		}
		if (await) {
			stop();
		}
	}

	public void stop() {
		await=true;
	}
	
	class SearchTask implements Runnable {
		long t1 = System.nanoTime();

		public void run() {
			while (!await) {
				try {
					Thread.sleep(5);
					long t2 = System.nanoTime();
					if (queue.size() >= size || ((t2 - t1) <= (Interval * 1000))) {
						t1 = System.nanoTime();
						Message log = null;
						while (!queue.isEmpty()) {
							log = queue.poll();
							if (ObjUtil.isNotEmpty(log)) {
								MessageService messageService = registration(log.getClass().getName());
								if (messageService != null) {
									messageService.processMessages(log);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	public MessageService registration(String messageClass) {
		try {
			String clazzName = mapProcessingService.get(messageClass);
			if(ObjUtil.isNotEmpty(clazzName)){
				return (MessageService) ClassBeanFactory.getBean(mapProcessingService.get(messageClass));
			}
		} catch (Exception e) {
			System.out.println("无法获取到 消息服务：["+messageClass+"],请把服务注册到系统中");
		}
		return null;
	}


	public void setMapProcessingService(Map<String, String> mapProcessingService) {
		if(ObjUtil.isNotEmpty(mapProcessingService)){
			this.mapProcessingService.putAll(mapProcessingService);
		}
	}

	public long getInterval() {
		return Interval;
	}

	public void setInterval(long interval) {
		Interval = interval;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}

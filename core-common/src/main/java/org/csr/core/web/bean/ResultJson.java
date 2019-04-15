package org.csr.core.web.bean;

/**
 * 返回数据类型
 * @author caijin
 *
 */
public class ResultJson extends VOBase<Long> {
	
	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = -6904727705328272312L;
	private Object data;
	private String message;
	
	public ResultJson() {

	}

	public ResultJson(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public ResultJson(Object data, int status, String message) {
		this.data = data;
		this.status = status;
		this.message = message;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

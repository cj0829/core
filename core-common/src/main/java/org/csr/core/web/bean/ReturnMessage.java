package org.csr.core.web.bean;


@SuppressWarnings("rawtypes")
public class ReturnMessage  extends VOBase {
	
	private static final long serialVersionUID = -1448694113427363123L;
	public static final String  USERNAME_PASSWORD_ERROR="Username_Password_Error";
	String message;
	/**需要跳转的的url*/
	String forwardUrl;
	
	Object data;
	
	public ReturnMessage(){
		
	}
	public ReturnMessage(int status){
		this(status,"",null);
	}
	
	public ReturnMessage(int status,String message){
		this(status,message,null);
	}
	
	public ReturnMessage(int status,String message,Object data){
		this.message=message;
		this.status=status;
		this.data=data;
	}
	
	public ReturnMessage(int status,String code,String message,Object data){
		this.message=message;
		this.status=status;
		this.data=data;
		setCode(code);
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getForwardUrl() {
		return forwardUrl;
	}
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}

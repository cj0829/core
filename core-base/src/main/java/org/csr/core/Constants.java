package org.csr.core;
 
/**
 * ClassName:Constants.java <br/>
 * System Name：    博海云领 <br/>
 * Date:     2014-2-18下午3:00:24 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public abstract class Constants{
	
	/**全局的工程名称（web工程名）*/
	public  static String CXT = "/";
	/**
	 * html静态文件根目录
	 */
	public static final String HtmlRootPath="html";
	public final static String UTF8_ENCODING = "UTF-8";
	
	/**
	 * 系统级别的范围
	 */
	public static final String SYSTEM_TARGET_ID = "0";
	/**
	 * 顶级机构
	 */
	public static final Long ROOT=null;
	/**
	 * 超级管理员
	 */
	public static final String USER_SUPER="super";
	/**
	 * 用户初始密码
	 */
	public static final String USER_DEFAULT_PASSWORD="111111";
	/**
	 * 用户session Key
	 */
	public static final String SECURITY_CONTEXT_KEY = "SECURITY_CONTEXT";
	/**
	 * 用户session Name
	 */
	public static final String SECURITY_CONTEXT_NAME = "userSession";
	
	/**
	 * 原系统管理员菜单
	 */
	public static final Long MENU1 = 1L;
	/**
	 * 个人菜单
	 */
	public static final Long MENU2 = 500l;
	/**
	 * 团队菜单
	 */
	public static final Long MENU3 = 1000l;
	/**
	 * 新管理员菜单
	 */
	public static final Long MENU4 = 1500l;
	/**
	 * 考试系统菜单
	 */
	public static final Long MENU5 = 10000l;
	
	/**
	 * 全部菜单
	 */ 
	public static final String ALL_MENU = "ALL_MENU";
	/**
	 * 用户管理系统全部菜单
	 */ 
	public static final String ROLE_FUNCTION_USER = "ROLE_FUNCTION_USER";
	/**
	 * 当前登录用的，全部功能点
	 */ 
	public static final String CURRENT_LOGIN_MENU = "CURRENT_LOGIN_MENU";
	
	/**
	 * 管理员菜单
	 */ 
	public static final String ROLE_FUNCTION_ADMIN = "ROLE_FUNCTION_ADMIN";
	/**
	 * 部门经理
	 */ 
	public static final String ROLE_FUNCTION_DEPTMANAGER = "ROLE_FUNCTION_DEPTMANAGER";
	/**
	 * 个人菜单
	 */ 
	public static final String ROLE_FUNCTION_OWN = "ROLE_FUNCTION_OWN";
	
	/**
	 * 主菜单
	 */ 
	public static final String ROLE_FUNCTION_MAIN = "ROLE_FUNCTION_MAIN";

	/**
	 * 考试系统菜单
	 */ 
	public static final String ROLE_FUNCTION_EXAM = "ROLE_FUNCTION_EXAM";
	/**
	 * 列表中每页默认显示条数
	 */
	public static final int PAGE_SIZE = 10;   
	/**
	 *  点
	 */
	public static final String POINT = ".";
	
	/**
	 *  左斜杠
	 */
	public static final String LEFT_SLASH = "/";
	/**
	 * 批量操作分割符,分割每组数据的标识
	 */
	public static final String OPERATE_COLON = ":";
	
	/**
	 * 批量操作分割符，分割属性
	 */
	public static final String OPERATE_AT = "@";
	/**
	 *  多选题多个答案之间的分割符
	 */
	public static final String SPLITCHAR2 = ",";
	/**
	 *  分隔符
	 */
	public static final String BAR_DOLLAR = "|$"; 
	
	/**
	 *  上传文件存放目录
	 */
	public static final String  ROOT_DIRECTOTY = "upload";   
	
	/**
	 * 文档大小上传限制 2M
	 */
	public static final long UPLOAD_FILE_SIZE = 2*1024*1024;  
	 
	/**
	 * 服务器地址
	 */
	 public static final String SERVICEURL = "";    
	/**
	 * 邮箱验证码
	 */
	public static final String MAILVALIDATECODE = "mailValidateCode";   
	
	/**
	 * 缓存服务名称
	 */
	public static final String  WEB_CACHE = "webCache";
	
	/**
	 * 验证码
	 */
	public static final String VALIDATE_CODE_KEY="VALIDATE_CODE_KEY";
	
	/**
	 * 登录id
	 */
	public static final String LOGIN_APPLIED="LOGIN_APPLIED";
	
	/**
	 * 文件根目录
	 */
	public static final String FileRootPath="";
	/**
	 * 文件根目录
	 */
	public static final String twitterWordFilePath="";
	/**
	 * 存储在线同个用户在线信息Map缓存的key
	 */
	public static final String PRINCIPALS_CACHE_HEAD_KEY = "principals";
	/**
	 * 存储在线用户信息map缓存的key
	 */
	public static final String SESSIONS_CACHE_HEAD_KEY = "sessionIds";
	/**
	 * 用户cookie
	 */
	public static final String USER_COOKIE_KEY = "user_skin";
	 
}

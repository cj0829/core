package org.csr.core.persistence.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.csr.core.persistence.business.service.DictionaryService;
import org.csr.core.util.ClassBeanFactory;
 

/**
 * ClassName:SelectTaglib.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-26下午12:22:23 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class SelectTaglib extends TagSupport implements DynamicAttributes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 从页面的自定义标签mjf:select获取的属性值,得到该值后，以dictType为key值，从静态map中获取数据即可
	 */
	private String dictType="";
	/**
	 * 下拉菜单的id
	 */
	private String id="";
	/**
	 * 下拉菜单的name
	 */
	private String name="";
	/**
	 * 下拉菜单的初始值
	 */
	private String value="";
	/**
	 * 关联查询
	 */
	private String connect="";
	
	private String dataOptions="";
	
	/**
	 * select标签验证属性
	 */
	private String selct="";
	/**
	 * select标签验证属性 
	 */
	private String check="";
	/**
	 * select标签验证属性 
	 */
	private String msg="";
	/**
	 * select标签验证属性 
	 */
	private String msgId="";
	/**
	 * 查询的时候需要的属性
	 */
	private String dataType;
	/**
	 * 列表样式
	 */
	private String className;
	
	private String style = "";
	/**
	 * 下拉菜单的头信息，从页面传值为null则不添加头信息
	 */
	private  String headerInfo="true";
	
	/**
	 * 附加属性
	 */
	private String identity;
	
	/**
	 * 过滤列表中的key值，需要过滤的用该变量声明，用“,”隔开
	 */
	private String filterKey ;
	
	/**
	 * 次级菜单属性，对应于主级菜单的初始值
	 */
	private String parentValue;
	
	public static final String headInfo="<option value=\"\">-请选择-</option>";
	
	private Map<String, String> dynAttributes = new HashMap<String, String>(); 
	
	
	// 实现处理动态属性的setDynamicAttribute方法 
	public void setDynamicAttribute(String uri, String localName, Object value)throws JspException 
	{ 
		// 将所有的动态属性名称和属性值保存在Map对象中 
		dynAttributes.put(localName, value.toString()); 
	} 

	
	// 覆盖TagSupport类的doStartTag方法
	// 当遇到标签（也就是<mjf:select>）的开始标记时调用该方法
	@Override 
	public int doStartTag() throws JspException 
	{ 
		try 
		{ 
			//从spring上下文获取service实例
			DictionaryService dictionaryService=(DictionaryService)ClassBeanFactory.getBean("dictionaryService");
			Map<String,String> map=dictionaryService.findDictMap(dictType);
			StringBuffer htmlStr=new StringBuffer();
			//判断是否有数据
			if(map==null || map.size()<=0){
				htmlStr.append(addSelectInfo());
				htmlStr.append(headInfo).append("</select>");
				pageContext.getOut().write(htmlStr.toString()); 
			}else{
				Iterator<Entry<String, String>> it=map.entrySet().iterator();
				htmlStr.append(addSelectInfo());
				if("true".equals(headerInfo)){
					htmlStr.append(headInfo);
				}
				
				while(it.hasNext()){
					Map.Entry<String, String> entry = it.next();
					String key =  entry.getKey();
					String val = (String) entry.getValue();
					if(filterKey !=null && !"".equals(filterKey) && (","+filterKey+",").indexOf(","+key.toString()+",")!=-1){
					}else{
						//获取下拉列表中的初始值对应的属性
						String initValue="-1";
						if(StringUtils.isNotEmpty(value)){
							initValue=value;
						}
						if(initValue!=null && initValue.equals(key)){
							htmlStr.append("<option selected=\"selected\" value=\"").append(key).append("\">").append(val).append("</option>");
						}else{
							htmlStr.append("<option  value=\"").append(key).append("\">").append(val).append("</option>");
						}
					}
					
				}
				htmlStr.append("</select>");
				// 将生成数据传到客户端
				pageContext.getOut().write(htmlStr.toString()); 
			}
			
		} 
		catch (IOException e) 
		{ 
		} 
		// TagSupport类的doStartTag方法默认返回SKIP_BODY，表示忽略自定义标签体
		return super.doStartTag(); 
	} 
	
	public String addSelectInfo(){
		StringBuffer htmlStr=new StringBuffer();
		htmlStr.append("<select");
		
		// 获得动态属性名称集合 
		Set<String> keys = dynAttributes.keySet(); 

		// 根据动态属性名称查找动态属性值，并根据每一个动态属性生成<option>元素 
		for (String key : keys) 
		{ 
			String value = dynAttributes.get(key); 
			htmlStr.append(" "+ key + "=\"" + value + "\""); 
		} 

		
		if(StringUtils.isNotEmpty(id)){
			htmlStr.append(" id=\"").append(id).append("\"");
		}
		
		if(StringUtils.isNotEmpty(name)){
			htmlStr.append(" name=\"").append(name).append("\"");
		}
		if(StringUtils.isNotEmpty(connect)){
			htmlStr.append(" connect=\"").append(connect).append("\"");
		}
		if(StringUtils.isNotEmpty(selct)){
			htmlStr.append(" selct=\"").append(selct).append("\"");
		}
		if(StringUtils.isNotEmpty(check)){
			htmlStr.append(" check=\"").append(check).append("\"");
		}
		if(StringUtils.isNotEmpty(msg)){
			htmlStr.append(" msg=\"").append(msg).append("\"");
		}
		if(StringUtils.isNotEmpty(msgId)){
			htmlStr.append(" msgId=\"").append(msgId).append("\"");
		}
		if(StringUtils.isNotEmpty(dataType)){
			htmlStr.append(" dataType=\"").append(dataType).append("\"");
		}
		if(StringUtils.isNotEmpty(dictType)){
			htmlStr.append(" dictType=\"").append(dictType).append("\"");
		}
		if(StringUtils.isNotEmpty(dataOptions)){
			htmlStr.append(" data-options=\"").append(dataOptions).append("\"");
		}
		
		if(StringUtils.isNotEmpty(className)){
			htmlStr.append(" class=\"").append(className).append("\"");
		}
		if(StringUtils.isNotEmpty(style)){
			htmlStr.append(" style=\"").append(style).append("\"");
		}
		if(StringUtils.isNotEmpty(identity)){
			htmlStr.append(" identity=\"").append(identity).append("\"");
		}
		htmlStr.append(">");
		return htmlStr.toString();
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}
	
	public String getSelct() {
		return selct;
	}

	public void setSelct(String selct) {
		this.selct = selct;
	}

	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getHeaderInfo() {
		return headerInfo;
	}

	public void setHeaderInfo(String headerInfo) {
		this.headerInfo = headerInfo;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public static String getHeadInfo() {
		return headInfo;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getFilterKey() {
		return filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDataOptions() {
		return dataOptions;
	}

	public void setDataOptions(String dataOptions) {
		this.dataOptions = dataOptions;
	}
 
	
}

package org.csr.core.persistence.taglib;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.csr.core.persistence.business.service.DictionaryService;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ClassUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.StrUtil;

/**
 * ClassName:RadioTaglib.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-26下午12:22:07 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class RadioTaglib extends TagSupport {
	
	/**
	 */
	private static final long serialVersionUID = -2208807106060824383L;

	/**
	 * 从页面的自定义标签获取的属性值,得到该值后，以dictType为key值，从静态map中获取数据即可
	 */
	private String dictType="";
	
	/**
	 * 下拉菜单的初始值
	 */
	private String value="";
	
	private String name="";
	
	private String id="";
	
	/**
	 * 样式
	 */
	private String className;
	/**
	 * 不需要的值
	 */
	private String exclude="";
	/**
	 * 样式
	 */
	private String dataType;
	/**
	 * 该属性表示radio选项的展示方式
	 */
	private String radioClass;
	
	@Override
	public int doStartTag() throws JspException {
		try 
		{ 
			//获取下拉列表中的初始值对应的属性
			if(StringUtils.isNotEmpty(value)){
				if(!ObjUtil.stringIsNumber(value)){
					String[] obj = value.split("\\.");
					if(ObjUtil.isNotEmpty(obj) && obj.length>=2){
						try {
							Object object = this.pageContext.getRequest().getAttribute(obj[0]);
							if(ObjUtil.isNotEmpty(object)){
								String fieldName=obj[1];
								Field f=ClassUtil.findField(object.getClass(), "get"+StrUtil.toUpperCaseFirstOne(fieldName));
								if(ObjUtil.isNotEmpty(f)){
									ClassUtil.get(f, object);
								}
							}
						} catch (Exception e) {e.printStackTrace();}
					}
					
				}
			}
			
			//从spring上下文获取service实例
			DictionaryService dictionaryService = (DictionaryService)ClassBeanFactory.getBean("dictionaryService");
			//获取数据
			Map<String,String> map = dictionaryService.findDictMap(dictType);
			
			StringBuffer htmlStr=new StringBuffer();
			//如果没有初值，则需要选择第一个map中的值为初值
			boolean flag = true;
			//判断是否有数据
			if(map != null && map.size()>0){
				htmlStr.append("<div><ul><li>");
				Iterator<Entry<String, String>> it=map.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry<String, String> entry =  it.next();
					String key = (String) entry.getKey();
					//如果存在不要的属性可用过滤
					if(!"".equals(exclude) && exclude.indexOf(key.toString()) != -1)
						continue;
					String val = (String) entry.getValue();
					
					htmlStr.append("<label><input type='radio' name='").append(name).append("' value='").append(key).append("' ");
					if(value.matches("\\d") && key.equals(value)){
						htmlStr.append("checked='checked'");
					}else if(!value.matches("\\d") && flag) {
						htmlStr.append("checked='checked'");
						flag = false;
					}
					if(StringUtils.isNotEmpty(className)){
						htmlStr.append(" class='").append(className).append("'");
					}
					if(StringUtils.isNotEmpty(radioClass)){
						htmlStr.append(" radioClass='").append(radioClass).append("'");
					}
					if(StringUtils.isNotEmpty(id)){
						htmlStr.append(" id='").append(id).append("'");
					}
					if(StringUtils.isNotEmpty(dataType)){
						htmlStr.append(" dataType='").append(dataType).append("'");
					}
					htmlStr.append("/>"+val.trim()+"</label>");
//					htmlStr.append("<br/>");
				}
				htmlStr.append("</li></ul></div>");
				// 将生成数据传到客户端
				pageContext.getOut().write(htmlStr.toString()); 
			}
		}catch (IOException e) {
			e.printStackTrace();
		} 
		// TagSupport类的doStartTag方法默认返回SKIP_BODY，表示忽略自定义标签体
		return super.doStartTag(); 
	}


	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDictType() {
		return dictType;
	}

	public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getExclude() {
		return exclude;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	
	public String getRadioClass() {
		return radioClass;
	}

	public void setRadioClass(String radioClass) {
		this.radioClass = radioClass;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
}

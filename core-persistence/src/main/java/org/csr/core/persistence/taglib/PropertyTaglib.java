package org.csr.core.persistence.taglib;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.csr.core.persistence.business.service.DictionaryService;
import org.csr.core.util.ClassBeanFactory;
import org.csr.core.util.ClassUtil;
import org.csr.core.util.ObjUtil;
import org.csr.core.util.StrUtil;

/**
 * ClassName:PropertyTaglib.java <br/>
 * System Name： 核心框架<br/>
 * Date:     2014-2-26下午12:21:19 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class PropertyTaglib extends TagSupport {
	/**
	 */
	private static final long serialVersionUID = -1066390516563771093L;
	/**
	 * 对应数据字典类型
	 */
	private String dictType="";
	/**
	 * 对应数据字典类型的key
	 */
	private String value="";
	
	// 覆盖TagSupport类的doStartTag方法
	// 当遇到标签（也就是<bluedot:property>）的开始标记时调用该方法

	@Override 
	public int doStartTag() throws JspException 
	{ 
		try{
			//从spring上下文获取service实例
			DictionaryService dictionaryService=(DictionaryService)ClassBeanFactory.getBean("dictionaryService");
			Map<String,String> map = null;
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
									Object value =ClassUtil.get(f, object);
								}
							}
						} catch (Exception e) {}
					}
				}
			}
			//表示是关联查询，而且有初始值
			map=dictionaryService.findDictMap(dictType);
			if(map==null || map.size()<=0){
				pageContext.getOut().write("");
			}else{
				String vString=map.get(value);
				pageContext.getOut().write(ObjUtil.isEmpty(vString)?"":vString);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// TagSupport类的doStartTag方法默认返回SKIP_BODY，表示忽略自定义标签体
		return super.doStartTag(); 
		
	} 
	
	
	public String getDictType() {
		return dictType;
	}
	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}

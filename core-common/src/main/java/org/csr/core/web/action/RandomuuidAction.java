package org.csr.core.web.action;

import java.util.ArrayList;
import java.util.UUID;

import org.csr.core.util.ObjUtil;
import org.csr.core.web.bean.UUIDBean;
import org.csr.core.web.controller.BasisAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * ClassName:Question.java <br/>
 * Date:     Thu Jun 18 16:39:20 CST 2015
 * @author   n-caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 * 试题库 action
 */
 
@Controller
@Scope("prototype")
@RequestMapping(value="/randomuuid")
public class RandomuuidAction extends BasisAction{
	
	@RequestMapping(value = "ajax/uuid", method = RequestMethod.POST)
    public ModelAndView randomuuid(Integer number) {
		if (ObjUtil.isEmpty(number)) {
			return resultExcludeJson(new UUIDBean());
		}else{
			ArrayList<String> uuid=new ArrayList<String>();
			for (int i = 0; i < number; i++) {
				uuid.add(UUID.randomUUID().toString());
			}
			return resultExcludeJson(new UUIDBean(uuid.toArray(new String[uuid.size()])));
		}
    }
}

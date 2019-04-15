package org.csr.core.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.web.controller.BasisAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * ClassName:DictionaryAction <br/>
 * System Name：    用户管理系统 <br/>
 * Date:     2014-1-27 上午9:31:56 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/fileDialogs")
public class FileDialogsAction extends BasisAction {
	
	private final String imageWin="/js/nicEdit/dialogs/image/imageWin.jsp";
	private final String videoWin="/js/nicEdit/dialogs/video/videoWin.jsp";
	/**
	 * @description:进入img编辑上传页面
	 * @param: 
	 * @return: String 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value = "imageWin",method = RequestMethod.GET)
	public void imageWin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher(imageWin).forward(request,response);
	}
	/**
	 * @description:进入视频传输页面
	 * @param: 
	 * @return: String 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value = "videoWin",method = RequestMethod.GET)
	public void videoWin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher(videoWin).forward(request,response);
	}
	
}

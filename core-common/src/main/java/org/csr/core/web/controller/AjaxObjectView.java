package org.csr.core.web.controller;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csr.core.util.ObjUtil;
import org.springframework.web.servlet.view.AbstractView;

public class AjaxObjectView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try{
			response.setContentType("text/json; charset=UTF-8");
		}catch(Exception e){
			System.out.println("设置ContentType异常");
			try{
				response.setContentType("text/json; charset=UTF-8");
			}
			catch(Exception e2){
				System.out.println("再次设置ContentType异常");
			}
		}
//		// Set standard HTTP/1.1 no-cache headers.
//		response.setHeader("Cache-Control","no-store, max-age=0, no-cache, must-revalidate");
//		// Set IE extended HTTP/1.1 no-cache headers.
//		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
//		// Set standard HTTP/1.0 no-cache header.
//		response.setHeader("Pragma", "no-cache");

		// 获取原始数据
		String jsonData=map.get("jsonData").toString();
		Boolean isNotGzip=(Boolean) map.get("is_Not_Gzip");
		
		if(ObjUtil.isNotBlank(jsonData)){
			if(ObjUtil.isNotEmpty(isNotGzip) && isNotGzip){
				System.out.println("is not isNotGzip");
				 response.getWriter().write(jsonData);
//				 response.getWriter().close();
			}else{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] b = jsonData.getBytes("UTF-8");
//				System.out.println("原始数据的大小: " + b.length);
				if(b.length>128){
					// 这里需要的是一个底层流
					GZIPOutputStream gzipout = new GZIPOutputStream(baos);
					// 告诉浏览器数据是经过gzip压缩的
					response.setHeader("content-encoding", "gzip");
					// 对数据进行压缩,并存在baos这个底层流中
					gzipout.write(b);
					gzipout.close(); // 确保数据被刷到底层流baos中
					b = baos.toByteArray();
				}
//				System.out.println("压缩后数据的大小: " + b.length);
				// 告诉浏览器数据的大小
				// 将数据返回给浏览器
				response.getOutputStream().write(b);
			}
		}
	}
}

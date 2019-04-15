//package org.csr.core.license;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.csr.core.business.domain.Parameters;
//import org.csr.core.business.service.ParameterService;
//import org.csr.core.exception.Exceptions;
//import org.csr.core.util.ClassBeanFactory;
//import org.csr.core.util.ObjUtil;
//
//public class LicenseUtil{
//	final static ParameterService parameterService=(ParameterService) ClassBeanFactory.getBean("parameterService");;
//	/**
//	 * 激活
//	 * @param name
//	 */
//	public static Parameters activate(String name,String realPath){
//		String val="";
//		try {
//			Class<?> valAction = Class.forName("org.csr.exam.action.ValAction");
//			val=((String)valAction.getField("VALUE").get("VALUE"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if(ObjUtil.isBlank(val)){
//			if(Arg.NAME.equals(LicenseTool.encode(name))){
//				SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
//				String times=sdf.format(new Date());
//				String enName=LicenseTool.encode(name+times);
//				String value=LicenseTool.encode(LicenseTool.toChar(times));
//				Parameters parameters=parameterService.findByName(Arg.NAME);
//				if(ObjUtil.isEmpty(parameters)){
//					parameters=new Parameters();
//					parameters.setName(enName);
//					parameters.setParameterValue(value);//使用日期。
//					parameterService.save(parameters);
//				}
//				LicenseTool.reVal(realPath, value);
//				return parameters;
//			}
//		}
//		return null;
//	}
//	/**
//	 * 运行
//	 * @param name
//	 * @param parameterValue
//	 * @return
//	 */
//	public static int execute(String name,String parameterValue){
//		if(ObjUtil.isBlank(name)){
//			return 410;
//		}
//		String former=LicenseTool.decode(name);
//		String times=former.substring(former.length()-8);//从name中提取的时间。
//		String last=LicenseTool.toInt(LicenseTool.decode(parameterValue));
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
//		Date date1=null;
//		Date date2=null;
//		try{
//			date1=sdf.parse(times);
//			date2=sdf.parse(last);
//		}catch(ParseException e){
//			e.printStackTrace();
//		}
//		Date now=new Date();
//		if(now.before(date2)){
//			return 409;
//		}else{
//			long days=(now.getTime()-date1.getTime())/(3600*24*1000);
//			if(days<=30){
//				return 200;
//			}else{
//				return 409;//过期
//			}
//		}
//	}
//	/**
//	 * 注册
//	 * @param name
//	 * @param remark
//	 */
//	public static void reg(String name,String remark){
//		Parameters parameters=parameterService.findByName(LicenseTool.encode(name));
//		if(ObjUtil.isEmpty(parameters)){
//			Exceptions.service("", "产品出错！");
//		}
//		parameters.setRemark(remark);
//		parameterService.update(parameters);
//	}
//}
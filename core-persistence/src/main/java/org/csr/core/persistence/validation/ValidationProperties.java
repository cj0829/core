package org.csr.core.persistence.validation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.csr.core.Comment;
import org.csr.core.persistence.tool.jpa.hbm5ddl.NewMetadataSources;
import org.csr.core.util.DecodeUnicodeUtil;
import org.csr.core.util.JsonUtil;
import org.csr.core.util.ObjUtil;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XMethod;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.AnnotationBinder;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

public class ValidationProperties{
	
	private static final Map<String,String> endMap = new LinkedHashMap<String,String>();
	
	
	/**
	 * 
	 * 
	 * 	<sysproperty key="filePath" value="${basedir}\src\main\resources" />
	 *		<sysproperty key="fileName" value="validation.properties" />
	**		<sysproperty key="dataMap" value="{c:'org.csr.core.business',u:'org.csr.core.domain',e:'org.csr.exam.domain',f:'org.csr.common.flow.domain'}" />
	*		<sysproperty key="backFilePath" value="${basedir}\doc" />
	*		<sysproperty key="backFileName" value="backValidation.properties" />
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
//		String filePath = "c:/";
//		String fileName = "validation.properties";
//		String dataMap = "{c:'org.csr.core.business',u:'org.csr.core.domain',e:'org.csr.exam.domain',f:'org.csr.common.flow.domain'}";
		String filePath = System.getProperty("filePath");
		String fileName = System.getProperty("fileName");
		String dataMap = System.getProperty("dataMap");
		System.out.println(dataMap);
		Map<String,String> correlativeMap=JsonUtil.jsonToMap(dataMap);
		finish(correlativeMap);
		//将map输出到指定的文件中
//		System.out.println("endMap:"+endMap.keySet());
		String buffer = "";
//		FileOutputStream writer = new FileOutputStream(filePath+"\\"+fileName);
		FileWriter writer = new FileWriter(filePath+"\\"+fileName);
		
		//读取特殊字段的说明
//		String backFilePath = "";
//		String backFileName = "";
		String backFilePath = System.getProperty("backFilePath");
		String backFileName = System.getProperty("backFileName");
		 HashMap<Object,Object> map=new HashMap<Object,Object>();
		try {
			InputStream in = new BufferedInputStream (new FileInputStream(backFilePath+"\\"+backFileName));
			Properties prop = new Properties();
	        prop.load(in);     ///加载属性列表
	        map.putAll(prop);
		} catch (Exception e) {
			System.out.println("没有找到补充文件");
		}
		for(Map.Entry<String, String> entry:endMap.entrySet()){
	        String key = (String) entry.getKey();
	        String value = (String) entry.getValue();
	        System.out.println("key:"+key);
	        
	        String t=ObjUtil.toString(map.get(key));
	        if(ObjUtil.isNotBlank(t)){
	        	value=t;
	        }
	        //将value值转换为unicode码存到文件中
	        buffer=key+"="+DecodeUnicodeUtil.chDecodeUnicode(value);
	        writer.write(buffer+"\r");
	    }
	    writer.close();
		
//		String projectLocation = System.getProperty("user.dir");
//		String proLoc = projectLocation.replace("\\","\\\\");
//		System.out.println(proLoc);
//		
//		Map<String,String> correlativeMap=new HashMap<String,String>();
//		correlativeMap.put("c", "org.csr.core.business");
//		correlativeMap.put("u", "org.csr.core.domain");
//		correlativeMap.put("e", "org.csr.exam.domain");
//		correlativeMap.put("f", "org.csr.common.flow.domain");
//		finish(correlativeMap);
//		//将map输出到指定的文件中
//		System.out.println("endMap:"+endMap.keySet());
//		String buffer = "";
//		FileOutputStream writer = new FileOutputStream(proLoc+"\\src\\main\\resources\\validation.properties");
//	    for(Map.Entry entry:endMap.entrySet()){
//	        String key = (String) entry.getKey();
//	        String value = (String) entry.getValue();
//	        buffer=key+"="+value+"\r";
//	        writer.write(buffer.getBytes());
//	    }
//	    writer.close();

		
//		List<Class<?>> lists = getClasses("org.csr.core.business");
//		String name = lists.get(0).getName();
//		System.out.println(name);
//		
//		Configuration cf = new Configuration();
//		ReflectionManager reflectionManager = cf.getReflectionManager();
//		XClass persistentXClass=null;
//		persistentXClass=reflectionManager.classForName("org.csr.core.business.Parameters", AnnotationBinder.class );
//		List<XProperty> list=persistentXClass.getDeclaredProperties(XClass.ACCESS_FIELD);
//		for(int i=0;i<list.size();i++){
//			XProperty property=list.get(i);
//			Comment comment =property.getAnnotation(Comment.class);
//			if(ObjUtil.isNotEmpty(comment)){
//				property.getAnnotation(Comment.class).ch();
//			}
//			javax.persistence.Column colm=property.getAnnotation(javax.persistence.Column.class);
//			if(ObjUtil.isNotEmpty(colm)){
//				property.getAnnotation(javax.persistence.Column.class).name();
//			}
//		}
//		List<XMethod> methods=persistentXClass.getDeclaredMethods();
//		Table table = new Table();
//		table.setAbstract(false);
//		table.setName("c_Parameters");
//		table.setSchema(null);
//		table.setCatalog(null);
//		table.setSubselect(null);
//		bindMethodsColumnComment(methods,table);
		
	}
	
	private static void finish(Map<String,String> map) throws ClassNotFoundException{
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
		builder.applySetting("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
		StandardServiceRegistry serviceRegistry = builder.build();
		
		MetadataImplementor build = (MetadataImplementor) new NewMetadataSources().getMetadataBuilder(serviceRegistry).build();
		
		//循环map
		for (Entry<String, String> entry : map.entrySet()) {
		    String key = entry.getKey().toString();
		    String value = entry.getValue().toString();
		    List<Class<?>> classList = getClasses(value);
		    //循环classList
		    for (Class<?> cla : classList) {
				ReflectionManager reflectionManager = build.getMetadataBuildingOptions().getReflectionManager();
				String className = cla.getName();
				XClass persistentXClass=reflectionManager.classForName(className, AnnotationBinder.class );
				List<XProperty> list=persistentXClass.getDeclaredProperties(XClass.ACCESS_FIELD);
				//递归查询所有本类和父类的所有属性
				List<XProperty> allPerClassList = superPersistentXClass(persistentXClass,list);
				
				for(int i=0;i<allPerClassList.size();i++){
					XProperty property=allPerClassList.get(i);
					Comment comment =property.getAnnotation(Comment.class);
					if(ObjUtil.isNotEmpty(comment)){
						property.getAnnotation(Comment.class).ch();
					}
					javax.persistence.Column colm=property.getAnnotation(javax.persistence.Column.class);
					if(ObjUtil.isNotEmpty(colm)){
						property.getAnnotation(javax.persistence.Column.class).name();
					}
				}
				List<XMethod> methods=persistentXClass.getDeclaredMethods();
				//递归查询所有本类和父类的所有methods
				List<XMethod> allMethodsList = superPersistentXMethods(persistentXClass,methods);
				
				Table table = new Table();
				table.setAbstract(false);
				String name = className.substring(className.lastIndexOf(".")+1,className.length());
				table.setName(key+"_"+name);
				table.setSchema(null);
				table.setCatalog(null);
				table.setSubselect(null);
				//表名和字段封装成map
				bindMethodsColumnCommentMap(allMethodsList,className);
			}
		}
	}
	
	/**
	 * 组装成需要的map
	 * @param methods
	 * @param className
	 */
	private static void bindMethodsColumnCommentMap(List<XMethod> methods,String className){
		for(int i=0;i<methods.size();i++){
			XMethod property=methods.get(i);
			Comment comment=property.getAnnotation(Comment.class);
			if(ObjUtil.isNotEmpty(comment)){
				String ch = property.getAnnotation(Comment.class).ch();
				String en= property.getAnnotation(Comment.class).en();
				if(ObjUtil.isNotBlank(ch) && ObjUtil.isNotBlank(en)){
					endMap.put(className+"."+en, ch);
				}
			}
		}
	}
	
	/**
	 * 递归查询所有的本类和父类的所有的methods
	 * @param x
	 * @param list
	 * @return
	 */
	public static List<XMethod> superPersistentXMethods(XClass x,List<XMethod> list){
		XClass xc = x.getSuperclass();
		String superName = "";
		if(ObjUtil.isNotEmpty(xc)){
			superName = xc.getName();
		}
		if(ObjUtil.isNotEmpty(xc) && !superName.endsWith("SimpleDomain") && !superName.endsWith("RootDomain") && !superName.endsWith("Persistable")){
			list.addAll(xc.getDeclaredMethods());
			superPersistentXMethods(xc,list);
		}else{
			return list;
		}
		return list;
	}
	
	
	/**
	 * 递归查询所有的本类和父类的所有属性
	 * @param x
	 * @param list
	 * @return
	 */
	public static List<XProperty> superPersistentXClass(XClass x,List<XProperty> list){
		XClass xc = x.getSuperclass();
		String superName = "";
		if(ObjUtil.isNotEmpty(xc)){
			superName = xc.getName();
		}
		if(ObjUtil.isNotEmpty(xc) && !superName.endsWith("SimpleDomain") && !superName.endsWith("RootDomain") && !superName.endsWith("Persistable")){
			list.addAll(xc.getDeclaredProperties(XClass.ACCESS_FIELD));
			superPersistentXClass(xc,list);
		}else{
			return list;
		}
		return list;
	}
	
	
	/**
	 * hibernate 生成sql语句时对comment值的设置
	 * @param methods
	 * @param table
	 */
	private static void bindMethodsColumnComment(List<XMethod> methods,Table table){
		for(int i=0;i<methods.size();i++){
			XMethod property=methods.get(i);
			Comment comment=property.getAnnotation(Comment.class);
			if(ObjUtil.isNotEmpty(comment)){
				property.getAnnotation(Comment.class).ch();
				javax.persistence.Column colm=property.getAnnotation(javax.persistence.Column.class);
				if(ObjUtil.isNotEmpty(colm)){
					String columnName=property.getAnnotation(javax.persistence.Column.class).name();
					@SuppressWarnings("unchecked")
					Iterator<Column> columns=table.getColumnIterator();
					while(columns.hasNext()){
						Column column=columns.next();
						if(columnName.equals(column.getName())){
							String comment1=property.getAnnotation(Comment.class).ch();
							column.setComment(comment1);
						}
					}
				}
			}
		}
	}
	
	 /** 
     * 从包package中获取所有的Class 
     * @param pack 
     * @return 
     */  
	public static List<Class<?>> getClasses(String packageName){  
        //第一个class类的集合  
        List<Class<?>> classes = new ArrayList<Class<?>>();  
        //是否循环迭代  
        boolean recursive = true;  
        //获取包的名字 并进行替换  
        String packageDirName = packageName.replace('.', '/');  
        //定义一个枚举的集合 并进行循环来处理这个目录下的things  
        Enumeration<URL> dirs;  
        try {  
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);  
            //循环迭代下去  
            while (dirs.hasMoreElements()){  
                //获取下一个元素  
                URL url = dirs.nextElement();  
                //得到协议的名称  
                String protocol = url.getProtocol();  
                //如果是以文件的形式保存在服务器上  
                if ("file".equals(protocol)) {  
                    //获取包的物理路径  
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
                    //以文件的方式扫描整个包下的文件 并添加到集合中  
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);  
                } else if ("jar".equals(protocol)){  
                    //如果是jar包文件   
                    //定义一个JarFile  
                    JarFile jar;  
                    try {  
                        //获取jar  
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();  
                        //从此jar包 得到一个枚举类  
                        Enumeration<JarEntry> entries = jar.entries();  
                        //同样的进行循环迭代  
                        while (entries.hasMoreElements()) {  
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件  
                            JarEntry entry = entries.nextElement();  
                            String name = entry.getName();  
                            //如果是以/开头的  
                            if (name.charAt(0) == '/') {  
                                //获取后面的字符串  
                                name = name.substring(1);  
                            }  
                            //如果前半部分和定义的包名相同  
                            if (name.startsWith(packageDirName)) {  
                                int idx = name.lastIndexOf('/');  
                                //如果以"/"结尾 是一个包  
                                if (idx != -1) {  
                                    //获取包名 把"/"替换成"."  
                                    packageName = name.substring(0, idx).replace('/', '.');  
                                }  
                                //如果可以迭代下去 并且是一个包  
                                if ((idx != -1) || recursive){  
                                    //如果是一个.class文件 而且不是目录  
                                    if (name.endsWith(".class") && !entry.isDirectory()) {  
                                        //去掉后面的".class" 获取真正的类名  
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);  
                                        try {  
                                            //添加到classes  
                                            classes.add(Class.forName(packageName + '.' + className));  
                                        } catch (ClassNotFoundException e) {  
                                            e.printStackTrace();  
                                        }  
                                      }  
                                }  
                            }  
                        }  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    }   
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return classes;  
    }
	
	 /** 
     * 以文件的形式来获取包下的所有Class 
     * @param packageName 
     * @param packagePath 
     * @param recursive 
     * @param classes 
     */  
	 public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes){  
        //获取此包的目录 建立一个File  
        File dir = new File(packagePath);  
        //如果不存在或者 也不是目录就直接返回  
        if (!dir.exists() || !dir.isDirectory()) {  
            return;  
        }  
        //如果存在 就获取包下的所有文件 包括目录  
        File[] dirfiles = dir.listFiles(new FileFilter() {  
        //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)  
              public boolean accept(File file) {  
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));  
              }  
            });  
        //循环所有文件  
        for (File file : dirfiles) {  
            //如果是目录 则继续扫描  
            if (file.isDirectory()) {  
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),  
                                      file.getAbsolutePath(),  
                                      recursive,  
                                      classes);  
            }  
            else {  
                //如果是java类文件 去掉后面的.class 只留下类名  
                String className = file.getName().substring(0, file.getName().length() - 6);  
                try {  
                    //添加到集合中去  
                    classes.add(Class.forName(packageName + '.' + className));  
                } catch (ClassNotFoundException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
	
	//通过内容获取文件属性
	public static Map parserContentFields(String content) throws IOException{
		List<String> comments = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
		String line=null;
			while((line=bfr.readLine())!=null){
				line = line.trim();
				if(line.startsWith("@Comment(")&&line.endsWith(")")){
					line = line.substring(9, line.length()-1);
					comments.add(line);
				}
			}
			bfr.close();
		for(int i=0;i<comments.size();i++){
			String fullName = comments.get(i);
			String vtype = "";              //用于后面的拼接
			List<String> list = new ArrayList<String>();
			String[] spiltName= fullName.split(",");
			for(int j = 0;j<spiltName.length;j++){
				String equalName = spiltName[j].trim();
				//接下来判断是否还有len，ch，vtype,search
				if(equalName.startsWith("ch")){
					list.add(equalName);
				}else if(equalName.startsWith("en")){
					list.add(equalName);
				}
			}
			if(!vtype.equals("")){
				list.add(vtype);
			}
			String key="";
			String val="";
			try{
				for(int k=0;k<list.size();k++){
					String[] names = list.get(k).split("=");
					if(names.length>1){
						String name = names[0].trim();
						String value =  names[1].trim();
						if("ch".equals(name)){
							val=value.substring(1,value.length()-1);
						}else if("en".equals(name)){
							key=value.substring(1,value.length()-1);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			if(ObjUtil.isNotBlank(key) && ObjUtil.isNotBlank(val)){
				map.put(key,val);
			}
		}
		return map;
	}
	
	
	//通过路径获取文件属性
	public static Map getFields(String url) throws IOException{
		List<String> comments = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(url)));
		String line=null;
			while((line=bfr.readLine())!=null){
				line = line.trim();
				if(line.startsWith("@Comment(")&&line.endsWith(")")){
					line = line.substring(9, line.length()-1);
					comments.add(line);
				}
			}
			bfr.close();
		for(int i=0;i<comments.size();i++){
			String fullName = comments.get(i);
			String[] spiltName= fullName.split("\",");
			for(int j = 0;j<spiltName.length;j++){
				String key="";
				String val="";
				String equalName = spiltName[j];
				String[] names = equalName.split("=");
				if(names.length>=1){
					String name=names[0];
					String value=names[1];
					if("ch".equals(name)){
						val = value.substring(1,value.length());
					}else if("en".equals(name)){
						key = value.substring(1,value.length());
					}
				}
				if(ObjUtil.isNotBlank(key) && ObjUtil.isNotBlank(val)){
					map.put(key,val);
				}
			}
		}
		return map;
	}
}

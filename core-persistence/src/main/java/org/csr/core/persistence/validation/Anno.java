package org.csr.core.persistence.validation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Anno {
	
	//通过路径获取文件属性
	public static List getFields(String url) throws IOException{
		List<String> comments = new ArrayList<String>();
		List<Annotate> anno = new ArrayList<Annotate>();
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
			Annotate annotate = new Annotate();
			String fullName = comments.get(i);
			String[] spiltName= fullName.split("\",");
			for(int j = 0;j<spiltName.length;j++){
				String equalName = spiltName[j];
				String[] names = equalName.split("=");
				if(names.length>=1){
					String name=names[0];
					String value=names[1];
					if("ch".equals(name)){
						annotate.setCh(value.substring(1,value.length()));
					}else if("en".equals(name)){
						annotate.setEn(value.substring(1,value.length()));
					}else if("vtype".equals(name)){
						annotate.setVtype(value.substring(1,value.length()));
					}else if("len".equals(name)){
						annotate.setLen((Integer.parseInt(value)));
					}else if("search".equals(name)){
						if("true".equals(value)){
							annotate.setSearch(true);
						}
					}
				}
				
			}
			anno.add(annotate);
		}
		return anno;
	}
	//通过内容获取文件属性
	public static List parserContentFields(String content) throws IOException{
		List<String> comments = new ArrayList<String>();
		List<Annotate> anno = new ArrayList<Annotate>();
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
			Annotate annotate = new Annotate();
			String fullName = comments.get(i);
			String vtype = "";              //用于后面的拼接
			List<String> list = new ArrayList<String>();
			String[] spiltName= fullName.split(",");
			for(int j = 0;j<spiltName.length;j++){
				String equalName = spiltName[j].trim();
				//接下来判断是否还有len，ch，vtype,search
				if(equalName.startsWith("len")){
					list.add(equalName);
				}else if(equalName.startsWith("ch")){
					list.add(equalName);
				}else if(equalName.startsWith("en")){
					list.add(equalName);
				}else if(equalName.startsWith("search")){
					list.add(equalName);
				}else{
					if(vtype.equals("")){
						vtype = equalName;
					}else{
						vtype =vtype+","+equalName;
						}
					}
			}
				if(!vtype.equals("")){
					list.add(vtype);
				}
				
				try{
			for(int k=0;k<list.size();k++){
				String[] names = list.get(k).split("=");
				if(names.length>1){
					String name = names[0].trim();
					String value =  names[1].trim();
					if("ch".equals(name)){
						annotate.setCh(value.substring(1,value.length()-1));
					}else if("en".equals(name)){
						annotate.setEn(value.substring(1,value.length()-1));
					}else if("vtype".equals(name)){
						annotate.setVtype(value.substring(1,value.length()-1));
					}else if("len".equals(name)){
						annotate.setLen((Integer.parseInt(value)));
					}else if("search".equals(name)){
						if("true".equals(value)){
							annotate.setSearch(true);
						}
					}
				}
			}
				}catch(Exception e){
					e.printStackTrace();
				}
			anno.add(annotate);
		}
		return anno;
	}
	
	
	//通过内容获取文件属性
	public static List parserContentFields1(String content) throws IOException{
		List<String> comments = new ArrayList<String>();
		List<Annotate> anno = new ArrayList<Annotate>();
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
			Annotate annotate = new Annotate();
			String fullName = comments.get(i);
			parserStringField(annotate,"ch",fullName);
			parserStringField(annotate,"en",fullName);
			parserStringField(annotate,"vtype",fullName);
			parserNotStringField(annotate,"len",fullName);
			parserNotStringField(annotate,"search",fullName);
			anno.add(annotate);
		}
		return anno;
	}

	private static void parserStringField(Annotate annotate, String filedName,String comment){
		if(StringUtils.isNotBlank(comment) && StringUtils.isNotBlank(filedName)){
			String vtype = "";              //用于后面的拼接
			List<String> list = new ArrayList<String>();
			String[] spiltName= comment.split(",");
			for(int j = 0;j<spiltName.length;j++){
				String equalName = spiltName[j];
				//接下来判断是否还有len，ch，vtype,search
				if(equalName.startsWith("len")){
					list.add(equalName);
				}else if(equalName.startsWith("ch")){
					list.add(equalName);
				}else if(equalName.startsWith("en")){
					list.add(equalName);
				}else if(equalName.startsWith("search")){
					list.add(equalName);
				}else{
					if(vtype.equals("")){
						vtype = equalName;
					}else{
						vtype =vtype+","+equalName;
						}
					}
			}
			
		}
	}
	private static void parserNotStringField(Annotate annotate, String filedName, String comment) {
		
	}
}

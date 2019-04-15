/**
 * Project Name:core
 * File Name:NewMetadataSources.java
 * Package Name:org.csr.core.persistence.tool.hbm5ddl
 * Date:2016年7月4日上午11:25:59
 * Copyright (c) 2016, 版权所有 ,All rights reserved 
*/

package org.csr.core.persistence.tool.jpa.hbm5ddl;

import java.io.IOException;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.MappingException;
import org.hibernate.boot.MetadataSources;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

/**
 * ClassName: NewMetadataSources.java <br/>
 * System Name：    在线学习系统<br/>
 * Date:     2016年7月4日上午11:25:59 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 *
 */
public class NewMetadataSources extends MetadataSources{
	/**
	 * serialVersionUID:(用一句话描述这个变量表示什么).
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;

	private static final String RESOURCE_PATTERN="/**/*.class";

	private static final String PACKAGE_INFO_SUFFIX=".package-info";

	private static final TypeFilter[] ENTITY_TYPE_FILTERS=new TypeFilter[]{new AnnotationTypeFilter(Entity.class,false),new AnnotationTypeFilter(Embeddable.class,false),new AnnotationTypeFilter(MappedSuperclass.class,false)};
	
	private final ResourcePatternResolver resourcePatternResolver=ResourcePatternUtils.getResourcePatternResolver(new PathMatchingResourcePatternResolver());;

	
	public NewMetadataSources scanPackage(String key,String packagesToScan){
		try{
			String pattern=ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+ClassUtils.convertClassNameToResourcePath(packagesToScan)+RESOURCE_PATTERN;
			Resource[] resources=this.resourcePatternResolver.getResources(pattern);
			MetadataReaderFactory readerFactory=new CachingMetadataReaderFactory(this.resourcePatternResolver);
			for(Resource resource:resources){
				if(resource.isReadable()){
					MetadataReader reader=readerFactory.getMetadataReader(resource);
					String className=reader.getClassMetadata().getClassName();
					if(matchesEntityTypeFilter(reader,readerFactory)){
						addAnnotatedClass(this.resourcePatternResolver.getClassLoader().loadClass(className));
					}else if(className.endsWith(PACKAGE_INFO_SUFFIX)){
						addPackage(className.substring(0,className.length()-PACKAGE_INFO_SUFFIX.length()));
					}
				}
			}
			return this;
		}catch(Exception ex){
			throw new MappingException("Failed to scan classpath for unlisted classes",ex);
		}
	}
	
	private boolean matchesEntityTypeFilter(MetadataReader reader,MetadataReaderFactory readerFactory) throws IOException{
		for(TypeFilter filter:ENTITY_TYPE_FILTERS){
			if(filter.match(reader,readerFactory)){
				return true;
			}
		}
		return false;
	}
}


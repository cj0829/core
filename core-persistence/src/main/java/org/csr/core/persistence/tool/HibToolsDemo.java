package org.csr.core.persistence.tool;

import org.csr.core.persistence.tool.jpa.hbm5ddl.NewMetadataSources;
import org.csr.core.persistence.tool.jpa.hbm5ddl.NewSchemaExport;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;

/**
 * ClassName:HibTools.java <br/>
 * System Name：    在线学习系统 <br/>
 * Date:     2016年7月4日上午11:25:21 <br/>
 * @author   caijin <br/>
 * @version  1.0 <br/>
 * @since    JDK 1.7
 *
 * 功能描述：  <br/>
 * 公用方法描述：  <br/>
 */
public class HibToolsDemo{
	public static void main(String[] args){
		try{
			NewMetadataSources metadata=new NewMetadataSources();
			metadata.scanPackage("c","org.csr.core.domain");
			
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
			builder.applySetting("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
			StandardServiceRegistry serviceRegistry = builder.build();
			
			MetadataBuilder metBuilder = metadata.getMetadataBuilder(serviceRegistry);
			
			/**
			MetadataImplementor mt=(MetadataImplementor) metBuilder.build();
			mt.getMetadataBuildingOptions().getReflectionManager();*/
			 
			NewSchemaExport export = new NewSchemaExport((MetadataImplementor) metBuilder.build());
			export.setOutputFile("create_table.sql");
			export.setFormat(true);
			export.setDelimiter(";");
			export.create(false, false);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}

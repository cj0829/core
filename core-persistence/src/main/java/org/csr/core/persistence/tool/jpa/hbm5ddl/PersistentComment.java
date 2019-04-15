package org.csr.core.persistence.tool.jpa.hbm5ddl;

import java.util.Iterator;
import java.util.List;

import org.csr.core.Comment;
import org.csr.core.util.ObjUtil;
import org.hibernate.HibernateException;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.annotations.common.reflection.XMethod;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.cfg.AnnotationBinder;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;

public class PersistentComment {
	final MetadataImplementor metadata;
	final ServiceRegistry serviceRegistry;
	
	public PersistentComment(MetadataImplementor metadata, ServiceRegistry serviceRegistry) {
		this.metadata=metadata;
		this.serviceRegistry=serviceRegistry;
		
	}

	public ReflectionManager generateDefaultReflectionManager() {
		 return metadata.getMetadataBuildingOptions().getReflectionManager();
	}
	
	public void setTableColumnCommentScript() throws HibernateException {
		for ( PersistentClass pclazz : metadata.getEntityBindings() ) {
			// tables
			try{
				XClass persistentXClass=generateDefaultReflectionManager().classForName( pclazz.getClassName(), AnnotationBinder.class );
				bindTableComment(persistentXClass,pclazz);
				List<XProperty> list=persistentXClass.getDeclaredProperties(XClass.ACCESS_FIELD);
				for(int i=0;i<list.size();i++){
					XProperty property=list.get(i);
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
				bindMethodsColumnComment(methods,pclazz);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	private void bindTableComment(XClass clazzToProcess,PersistentClass persistentClass){
		if(clazzToProcess.isAnnotationPresent(Comment.class)){
			String tableComment=clazzToProcess.getAnnotation(Comment.class).ch();
			persistentClass.getTable().setComment(tableComment);
		}
	}

	private void bindMethodsColumnComment(List<XMethod> methods,PersistentClass persistentClass){
		for(int i=0;i<methods.size();i++){
			XMethod property=methods.get(i);
			Comment comment=property.getAnnotation(Comment.class);
			if(ObjUtil.isNotEmpty(comment)){
				property.getAnnotation(Comment.class).ch();
				javax.persistence.Column colm=property.getAnnotation(javax.persistence.Column.class);
				if(ObjUtil.isNotEmpty(colm)){
					String columnName=property.getAnnotation(javax.persistence.Column.class).name();
					@SuppressWarnings("unchecked")
					Iterator<Column> columns=persistentClass.getTable().getColumnIterator();
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
}
  
  
  


package org.csr.core.license;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class GeneralClassAdapter extends ClassAdapter{
	private String val;
	public GeneralClassAdapter(ClassVisitor cv,String val) {  
	        super(cv); 
	        this.val=val;
    }  
    @Override
    public FieldVisitor visitField(int access, String name, String desc,  
            String signature, Object value){
    	if("VALUE".equals(name)){
    		return cv.visitField(access, name, desc,signature,val); 
    	}
        return super.visitField(access, name, desc, signature, value);
    }
}
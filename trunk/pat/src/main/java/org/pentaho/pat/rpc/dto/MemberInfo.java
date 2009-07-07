package org.pentaho.pat.rpc.dto;


import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MemberInfo extends BaseCell implements Serializable, IsSerializable{
    private static final long serialVersionUID = 1L;
 
    public MemberInfo(){
	
    }
    
    public MemberInfo(boolean b, boolean c) {
	    this.right=b;
	    this.sameAsPrev=c;
	}

}

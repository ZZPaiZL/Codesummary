package com.mini.program.enums;

public enum BgmOperStatus {
	
	ADD("1", "添加bgm"),				
	DELETE("2", "删除bgm");		
	
	public final String type;
	public final String value;
	
	BgmOperStatus(String type, String value){
		this.type = type;
		this.value = value;
	}
	
	public String getUserType() {
		return type;
	}  
	
	public String getValue() {
		return value;
	} 
	
	public static String getValueByKey(String key) {
		for (BgmOperStatus type : BgmOperStatus.values()) {
			if (type.getUserType().equals(key)) {
				return type.value;
			}
		}
		return null;
	}
}

package com.mini.program.enums;

public enum VideoStautsEnum {
		SUCCESS(1),//发布成功
		FORBID(2);//禁止播放
	
	public  int values;

	VideoStautsEnum(int values) {
		
		this.values = values;
	}

	public int getValues() {
		return values;
	}
	
	
	
}

package com.twsbrian.MobDrop2.DataBase;

public class MessageSet {
	private Long time;
	private Object object;
	
	public MessageSet(Object object,Long time){
		this.object = object;
		this.time = time;
	}
	
	public Long getTime(){
		return time;
	}
	
	public Object getobject(){
		return object;
	}
	
}

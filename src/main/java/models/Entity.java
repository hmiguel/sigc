package models;

public class Entity {
	
	private String entity;
	private String type;
	
	private int ocurrence;
	
	public void setEntity(String e) {
		this.entity = e;
	}

	public String getEntity() {
		return entity;
	}	
	
	public String getType(){
		return type;
	
	}
	
	public void setType(String t){
		this.type = t;
	}
	
	
	public void setOcurrence(int n) {
		this.ocurrence = n;
	}

	public int getOcurrence() {
		return ocurrence;
	}	

}

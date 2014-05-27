package mallet;

import cc.mallet.types.Label;

public class Ranking {
	
	private double value;
	
	private int rank;
	
	private Label category;
	
	public Label getCategory(){
		
		return category;
	}

	public int getRank(){
		
		return rank;
	}
	
	public void setRank(int r){
		
		this.rank = r;
	}
	
	public double getValue(){
		
		return value;
	}
	
	public void setValue(double d){
		
		this.value = d;
	}
	
	public void setCategory(Label label){
		
		this.category = label;
	}
	
}

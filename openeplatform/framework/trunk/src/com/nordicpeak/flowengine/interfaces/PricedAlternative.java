package com.nordicpeak.flowengine.interfaces;


public interface PricedAlternative extends MutableAlternative {
	
	public void setPrice(Integer price);
	
	public Integer getPrice();
}

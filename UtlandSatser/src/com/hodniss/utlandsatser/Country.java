package com.hodniss.utlandsatser;

public class Country {
	private String name;
	private int hotelRate;
	private int dietRate;
	
	public Country(String name, int hotelRate, int dietRate) {
		this.name = name;
		this.hotelRate = hotelRate;
		this.dietRate = dietRate;
	}
	
	public String getName() {
		return name;
	}
	
	public int getHotelRate() {
		return hotelRate;
	}
	
	public int getDietRate() {
		return dietRate;
	}
}

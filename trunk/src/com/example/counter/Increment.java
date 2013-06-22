package com.example.counter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Increment {

	private long id;
	private int id_counter;
	private String date_time; 
	
	public Increment()
	{}

	
	public Increment(int id_counter)
	{
		this.id_counter = id_counter;
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
		this.date_time = s.format(new Date());
		
	}
	public Increment(long id,int id_counter)
	{
		this.id = id;
		this.id_counter = id_counter;
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
		this.date_time = s.format(new Date());
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getId_counter() {
		return id_counter;
	}

	public void setId_counter(int id_counter) {
		this.id_counter = id_counter;
	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}
	
	
	
	
}

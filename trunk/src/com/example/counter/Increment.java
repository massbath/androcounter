package com.example.counter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Increment {

	private long id;
	private long id_counter;
	private long date_time; 
	
	public Increment()
	{}

	
	public Increment(long id_counter)
	{
		this.id_counter = id_counter;
		
		this.date_time = System.currentTimeMillis()/1000;
		
	}
	public Increment(long id,long id_counter)
	{
		this.id = id;
		this.id_counter = id_counter;
		this.date_time = System.currentTimeMillis()/1000;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId_counter() {
		return id_counter;
	}

	public void setId_counter(long id_counter) {
		this.id_counter = id_counter;
	}

	public long getDate_time() {
		return date_time;
	}

	public void setDate_time(long date_time) {
		this.date_time = date_time;
	}
	
	
	
	
}

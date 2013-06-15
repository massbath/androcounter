package com.example.counter;

public class Increment {

	private long id;
	private int id_counter;
	private long date_time; 
	
	public Increment()
	{}
	
	public Increment(int id_counter,long date_time)
	{
		this.id_counter = id_counter;
		this.date_time = 1111; //récupérer la date actuelle
	}
	public Increment(long id,int id_counter,long date_time)
	{
		this.id = id;
		this.id_counter = id_counter;
		this.date_time = 1111; //récupérer la date actuelle
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

	public long getDate_time() {
		return date_time;
	}

	public void setDate_time(long date_time) {
		this.date_time = date_time;
	}
	
	
	
	
}

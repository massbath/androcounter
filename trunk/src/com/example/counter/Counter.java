package com.example.counter;

public class Counter {
	
	private long id;
	private String title;
	private String description;
	private int count;
	
	public Counter()
	{}
	
	public Counter(String title,String description)
	{
		this.setTitle(title);
		this.setDescription(description);
	}
	
	public Counter(String title,String description,int count)
		{
			this.setTitle(title);
			this.setDescription(description);
			this.setCount(count);
		
		}
	 public Counter(Long id,String title,String description,int count)
	 {
		 	this.setId(id);
		    this.setTitle(title);
			this.setDescription(description);
			this.setCount(count); 
	 }
	 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void incrementCount()
	{
		
		this.count++;
	}
	
	public void decrementCount()
	{
		this.count--;
	}
	
	public String toString()
	{
		
		return "ID : "+this.id+"\nTitle : "+this.title+"\nDesciption : "+this.description+"\nCounted : "+String.valueOf(this.count);
		
	}
	
	
	
	
}

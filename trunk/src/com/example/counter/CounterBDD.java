package com.example.counter;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class CounterBDD extends DBBaseAdapter{
	
IncrementBDD table_increment;
	
	public CounterBDD(Activity a)
	{
		super(a);
		Log.d("Counter CounterBDD","CounterBDD intancied");
		table_increment = new IncrementBDD(a);
	}
	
	
	
	
	
	
	public long insertCounter(Counter newCounter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, newCounter.getTitle());
		values.put(COL_DESCRIPTION, newCounter.getDescription());
		values.put(COL_COUNT, newCounter.getCount());
		
		return ourDatabase.insert(TABLE_COUNTER, null, values);
		
	}
	
	public int updateCounter(long id, Counter updateCounter)
	{
		//we don't want have 2 counter with the same title in the DB
		Cursor c = ourDatabase.query(TABLE_COUNTER,new String[]{COL_ID},COL_TITLE+" LIKE \""+updateCounter.getTitle()+"\" AND "+COL_ID+" !="+id,null,null,null,null);
		//if there is already a counter with this name in database
		if(c.getCount()==1)
			return -1;
		
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, updateCounter.getTitle());
		values.put(COL_DESCRIPTION, updateCounter.getDescription());
		//values.put(COL_COUNT, updateCounter.getCount());
		return ourDatabase.update(TABLE_COUNTER, values, COL_ID+" = "+id, null);
	}
	
	public int updateCountofCounter(Counter counter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_COUNT, counter.getCount());
		Log.d("Counter CounterBDD updateCountOfCounter",counter.toString());
		return ourDatabase.update(TABLE_COUNTER, values, COL_ID+" = "+counter.getId(), null);
		
		
	}
	
	//increment the counter and add a nex increment date in the table increment
	public void countPlusCounter(Counter counter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_COUNT, counter.getCount());
		Log.d("Counter CounterBDD countPlusCounter",counter.toString());
		ourDatabase.update(TABLE_COUNTER, values, COL_ID+" = "+counter.getId(), null);
	   
		Increment newIncrement = new Increment(counter.getId());
		table_increment.addIncrement(newIncrement);
		
	}
	
	//decrement the counter TODO remove the last increment date in the table increment
	public void countMinusCounter(Counter counter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_COUNT, counter.getCount());
		Log.d("Counter CounterBDD countPlusCounter",counter.toString());
		ourDatabase.update(TABLE_COUNTER, values, COL_ID+" = "+counter.getId(), null);	
	   
		table_increment.deleteLastIncrementOfCounter(counter.getId());
	}
	
	public int removeCounterWithId(int id)
	{
		int retour =  ourDatabase.delete(TABLE_COUNTER, COL_ID +" = "+id, null);
		table_increment.deleteIncrementOfCounter(id);
		return retour;
		
	}
	
	// todo remove all the increment date in the table increment with id of this counter
	public int removeCounter(Counter counter)
	{
		int retour =  ourDatabase.delete(TABLE_COUNTER, COL_ID+" = "+counter.getId(), null);
		table_increment.deleteIncrementOfCounter(counter.getId());
		return retour;
		
	}
	public Counter getCounterWithTitle(String title)
	{
		Cursor c = ourDatabase.query(TABLE_COUNTER, new String[]{COL_ID,COL_TITLE,COL_DESCRIPTION,COL_COUNT}, COL_TITLE+" LIKE \""+title+"\"", null, null, null, null);
		return cursorToCounter(c);
	}
	
	public Counter getCounterWithId(Long id)
	{
		Cursor c = ourDatabase.query(TABLE_COUNTER, new String[]{COL_ID,COL_TITLE,COL_DESCRIPTION,COL_COUNT}, COL_ID+" = "+id, null, null, null, null);
		return cursorToCounter(c);
	}
	
	public boolean isFreeTitle(long id,String title)
	{
		Cursor c = ourDatabase.query(TABLE_COUNTER,new String[]{COL_ID},COL_TITLE+" LIKE \""+title+"\" AND "+COL_ID+" !="+id,null,null,null,null);
		//if there is already a counter with this name in database
		if(c.getCount()>=1)
			return false;
		else return true;
	}
	
	public boolean isFreeTitle(String title)
	{
		Cursor c = ourDatabase.query(TABLE_COUNTER,new String[]{COL_ID},COL_TITLE+" LIKE \""+title+"\"",null,null,null,null);
		//if there is already a counter with this name in database
		if(c.getCount()>=1)
			return false;
		else return true;
	}
	
	
	public Counter cursorToCounter(Cursor c)
	{
		if(c.getCount() ==0)
			return null;
		
		c.moveToFirst();
		Counter counter = new Counter();
		counter.setCount(c.getInt(NUM_COL_COUNT));
		counter.setDescription(c.getString(NUM_COL_DESCRIPTION));
		counter.setId(c.getLong(NUM_COL_ID));
		counter.setTitle(c.getString(NUM_COL_TITLE));
		c.close();
		
		return counter;
		
	}
	
	public ArrayList<Counter> getAllCounter()
	{
		Cursor c = ourDatabase.query(TABLE_COUNTER, new String[]{COL_ID,COL_TITLE,COL_DESCRIPTION,COL_COUNT}, null, null, null, null, null);
		
		ArrayList<Counter> listCounter = new ArrayList<Counter>();
		
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) 
		{			
			listCounter.add(new Counter(c.getLong(NUM_COL_ID),c.getString(NUM_COL_TITLE),c.getString(NUM_COL_DESCRIPTION),c.getInt(NUM_COL_COUNT)));
		}
		
		c.close();
		return listCounter;
	}
	
	public ArrayList<Integer>getAllDateIncrementOfCounter(long id)
	{
		ArrayList<Integer> dateIncrement = new ArrayList<Integer>();
		
		Cursor res = ourDatabase.query(TABLE_INCREMENT, new String[]{COL_ID,COL_ID_COUNTER,COL_DATE_INCREMENT}, COL_ID_COUNTER+" = "+id, null, null, null, null);
		Log.d("Counter IncrementBDD", "getAllDateIncrementOfCounter cursor size : "+String.valueOf(res.getCount()));
		
		for(res.moveToFirst();!res.isAfterLast();res.moveToNext())
			{
				dateIncrement.add(res.getInt(NUM_COL_DATE_INCREMENT));
			}
		res.close();
		Log.d("Counter Counter", "getAllDateIncrementOfCounter "+String.valueOf(dateIncrement.size()));
		return dateIncrement;
	}
}

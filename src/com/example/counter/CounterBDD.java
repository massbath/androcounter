package com.example.counter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CounterBDD {
	
	private static final int VERSION_BDD =1;
	private static final String NOM_BDD = "counter.bd";
	private static final String TABLE_COUNTER = "table_counter";
	private static final String COL_ID = "ID";
	private static final int NUM_COL_ID = 0;
	private static final String COL_TITLE ="Title";
	private static final int NUM_COL_TITLE = 1;
	private static final String COL_DESCRIPTION = "Description";
	private static final int NUM_COL_DESCRIPTION = 2;
	private static final String COL_COUNT = "Count";
	private static final int NUM_COL_COUNT = 3;
	
	private SQLiteDatabase bdd;
	private MaBaseSQLite maBaseSQLite;
	
	public CounterBDD(Context context)
	{
		maBaseSQLite = new MaBaseSQLite(context,NOM_BDD,null,VERSION_BDD);
	}
	
	public void open()
	{
		bdd = maBaseSQLite.getWritableDatabase();
	}
	
	public void close()
	{
		bdd.close();
		
	}
	public SQLiteDatabase getBDD()
	{
		return bdd;
	}
	
	public long insertCounter(Counter newCounter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, newCounter.getTitle());
		values.put(COL_DESCRIPTION, newCounter.getDescription());
		values.put(COL_COUNT, newCounter.getCount());
		
		return bdd.insert(TABLE_COUNTER, null, values);
		
	}
	
	public int updateCounter(long id, Counter updateCounter)
	{
		//we don't want have 2 counter with the same title in the DB
		Cursor c = bdd.query(TABLE_COUNTER,new String[]{COL_ID},COL_TITLE+" LIKE \""+updateCounter.getTitle()+"\" AND "+COL_ID+" !="+id,null,null,null,null);
		//if there is already a counter with this name in database
		if(c.getCount()==1)
			return -1;
		
		ContentValues values = new ContentValues();
		values.put(COL_TITLE, updateCounter.getTitle());
		values.put(COL_DESCRIPTION, updateCounter.getDescription());
		//values.put(COL_COUNT, updateCounter.getCount());
		return bdd.update(TABLE_COUNTER, values, COL_ID+" = "+id, null);
	}
	
	public int updateCountofCounter(Counter counter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_COUNT, counter.getCount());
		Log.d("Counter CounterBDD updateCountOfCounter",counter.toString());
		return bdd.update(TABLE_COUNTER, values, COL_ID+" = "+counter.getId(), null);
		
		
	}
	
	//increment the counter and add a nex increment date in the table increment
	public void countPlusCounter(Counter counter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_COUNT, counter.getCount());
		Log.d("Counter CounterBDD countPlusCounter",counter.toString());
	    bdd.update(TABLE_COUNTER, values, COL_ID+" = "+counter.getId(), null);
	    /*
	     ContentValues values = new ContentValues();
	     values.put(COL_ID_COUNTER,counter.getId());
	     values.put(COL_DATE,date);
	     bdd.insert(TABLE_COUNT,null,values); 
	     */
	}
	
	//decrement the counter TODO remove the last increment date in the table increment
	public void countMinusCounter(Counter counter)
	{
		ContentValues values = new ContentValues();
		values.put(COL_COUNT, counter.getCount());
		Log.d("Counter CounterBDD countPlusCounter",counter.toString());
	    bdd.update(TABLE_COUNTER, values, COL_ID+" = "+counter.getId(), null);	
	    /*
	    
	    supprimer le dernier count lié au counter
	     */
	  
	}
	
	public int removeCounterWithId(int id)
	{
		return bdd.delete(TABLE_COUNTER, COL_ID +" = "+id, null);
		
	}
	
	// todo remove all the increment date in the table increment with id of this counter
	public int removeCounter(Counter counter)
	{
		return bdd.delete(TABLE_COUNTER, COL_ID+" = "+counter.getId(), null);
		
	}
	public Counter getCounterWithTitle(String title)
	{
		Cursor c = bdd.query(TABLE_COUNTER, new String[]{COL_ID,COL_TITLE,COL_DESCRIPTION,COL_COUNT}, COL_TITLE+" LIKE \""+title+"\"", null, null, null, null);
		return cursorToCounter(c);
	}
	
	public boolean isFreeTitle(long id,String title)
	{
		Cursor c = bdd.query(TABLE_COUNTER,new String[]{COL_ID},COL_TITLE+" LIKE \""+title+"\" AND "+COL_ID+" !="+id,null,null,null,null);
		//if there is already a counter with this name in database
		if(c.getCount()>=1)
			return false;
		else return true;
	}
	
	public boolean isFreeTitle(String title)
	{
		Cursor c = bdd.query(TABLE_COUNTER,new String[]{COL_ID},COL_TITLE+" LIKE \""+title+"\"",null,null,null,null);
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
		Cursor c = bdd.query(TABLE_COUNTER, new String[]{COL_ID,COL_TITLE,COL_DESCRIPTION,COL_COUNT}, null, null, null, null, null);
		
		ArrayList<Counter> listCounter = new ArrayList<Counter>();
		
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) 
		{			
			listCounter.add(new Counter(c.getLong(NUM_COL_ID),c.getString(NUM_COL_TITLE),c.getString(NUM_COL_DESCRIPTION),c.getInt(NUM_COL_COUNT)));
		}
		
		c.close();
		return listCounter;
	}
}

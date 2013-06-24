package com.example.counter;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class IncrementBDD extends DBBaseAdapter {

	public IncrementBDD(Activity a) {
		super(a);
		Log.d("Counter IncrementBDD", "IncrementBDD instancied");
	}
	
	public long addIncrement(Increment newIncrement)
	{
		ContentValues values = new ContentValues();
		values.put(COL_ID_COUNTER, newIncrement.getId_counter());
		values.put(COL_DATE_INCREMENT, newIncrement.getDate_time());
		
		Log.d("Counter IncrementBDD", "addIncrement "+newIncrement.getDate_time());
		
		return ourDatabase.insert(TABLE_INCREMENT, null, values);
		
	}
	
	
	
	
	public int deleteIncrementOfCounter(Increment incrementToDel)
	{
		Log.d("Counter IncrementBDD", "deleteIncrementOfCounter "+incrementToDel.toString());
		return ourDatabase.delete(TABLE_INCREMENT, COL_ID_COUNTER+" = "+incrementToDel.getId_counter(), null);
	}
	
	public int deleteIncrementOfCounter(long id_counter)
	{
		Log.d("Counter IncrementBDD", "deleteIncrementOfCounter "+String.valueOf(id_counter));
		return ourDatabase.delete(TABLE_INCREMENT, COL_ID_COUNTER+" = "+id_counter, null);
	}
	
	public int deleteLastIncrementOfCounter(Increment lastIncrementToDel)
	{
		Log.d("Counter IncrementBDD", "deleteLastIncrementOfCounter");
		Cursor res = ourDatabase.query(TABLE_INCREMENT, new String[]{COL_ID}, COL_ID_COUNTER+" = "+lastIncrementToDel.getId_counter(), null, null, null, null);
		res.moveToLast();
		int retour =  ourDatabase.delete(TABLE_INCREMENT, COL_ID+" = "+res.getInt(NUM_COL_ID), null);
		res.close();
		return retour;
	}
	
	public int deleteLastIncrementOfCounter(long idcounter)
	{
		
		Cursor res = ourDatabase.query(TABLE_INCREMENT, new String[]{COL_ID,COL_ID_COUNTER,COL_DATE_INCREMENT}, COL_ID_COUNTER+" = "+idcounter, null, null, null, null);
		res.moveToLast();
		Log.d("Counter IncrementBDD", "deleteLastIncrementOfCounter "+res.getString(NUM_COL_DATE_INCREMENT));
		int retour =  ourDatabase.delete(TABLE_INCREMENT, COL_ID+" = "+res.getInt(NUM_COL_ID), null);
		res.close();
		return retour;
	}
	
	
	
}

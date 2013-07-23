package com.example.counter;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBBaseAdapter {
		
	
		public static DbHelper ourHelper;
		public static Context ourContext;
		public static SQLiteDatabase ourDatabase;

		boolean ourConstructorBool = false;
		boolean ourDB = false;
		
    	
    	protected static final int VERSION_BDD =1;
    	protected static final String NOM_BDD = "counter.db";
		//table counter
    	protected static final String TABLE_COUNTER = "table_counter";
    	protected static final String COL_ID = "ID";
    	protected static final int NUM_COL_ID = 0;
    	protected static final String COL_TITLE ="Title";
    	protected static final int NUM_COL_TITLE = 1;
    	protected static final String COL_DESCRIPTION = "Description";
    	protected static final int NUM_COL_DESCRIPTION = 2;
    	protected static final String COL_COUNT = "Count";
    	protected static final int NUM_COL_COUNT = 3;
		
		
		//table increment
    	protected static final String TABLE_INCREMENT = "table_increment";
    	protected static final String COL_ID_COUNTER = "ID_Counter";
    	protected static final int NUM_COL_ID_COUNTER = 1;
    	protected static final String COL_DATE_INCREMENT = "Date_Increment";
    	protected static final int NUM_COL_DATE_INCREMENT = 2;
		
		
		//request to create counter table
		private static final String CREATE_TABLE_COUNTER = "CREATE TABLE "+TABLE_COUNTER+" ("+COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+COL_TITLE+" TEXT NOT NULL UNIQUE,"
				+COL_DESCRIPTION+" TEXT NOT NULL,"
				+COL_COUNT+" INTEGER);";
		
		//request to create increment table
		private static final String CREATE_TABLE_INCREMENT = "CREATE TABLE "+TABLE_INCREMENT+" "
				+"("+COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+COL_ID_COUNTER+ " INTEGER,"
				+COL_DATE_INCREMENT+" TEXT);";
				
		private static final String DROP_TABLE_COUNTER = "DROP TABLE IF EXISTS " + TABLE_COUNTER + ";";
		private static final String DROP_TABLE_INCREMENT = "DROP TABLE IF EXISTS " + TABLE_INCREMENT + ";";

		static class DbHelper extends SQLiteOpenHelper{
	        public DbHelper(Context context) {
	            super(context, NOM_BDD, null, VERSION_BDD);
	        }
	        @Override
	        public void onCreate(SQLiteDatabase db) {
	            db.execSQL(CREATE_TABLE_COUNTER);
	            db.execSQL(CREATE_TABLE_INCREMENT);
	        }

	        @Override
	        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	            db.execSQL(DROP_TABLE_COUNTER);
	            db.execSQL(DROP_TABLE_INCREMENT);
	            onCreate(db);
	        }
	    }

	    public DBBaseAdapter(Activity a){   
	        if(!ourConstructorBool == true){
	            ourContext = a;
	           
	            ourConstructorBool = true;
	        }
	    }

	    public DBBaseAdapter open() throws SQLException{
	        if(!ourDB == true){
	            ourHelper = new DbHelper(ourContext);
	            ourDB = true;
	        }
	        ourDatabase = ourHelper.getWritableDatabase();
	        return this;
	    }

	    public void close(){
	        if(ourDatabase.isOpen())
	            ourHelper.close();
	    }
		
		
}
		


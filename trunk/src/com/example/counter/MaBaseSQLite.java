package com.example.counter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLite extends SQLiteOpenHelper {
	
	//table counter
	private static final String TABLE_COUNTER = "table_counter";
	private static final String COL_ID = "ID";
	private static final String COL_TITLE ="Title";
	private static final String COL_DESCRIPTION = "Description";
	private static final String COL_COUNT = "Count";
	
	//table increment
	private static final String TABLE_INCREMENT = "table_increment";
	private static final String COL_ID_COUNTER = "ID_Counter";
	private static final String COL_DATE_INCREMENT = "Date_Increment";
	
	//request to create counter table
	private static final String CREATE_TABLE_COUNTER = "CREATE TABLE "+TABLE_COUNTER+" ("+COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+COL_TITLE+" TEXT NOT NULL UNIQUE,"
			+COL_DESCRIPTION+" TEXT NOT NULL,"
			+COL_COUNT+" INTEGER);";
	
	//request to create increment table
	private static final String CREATE_TABLE_INCREMENT = "CREATE TABLE"+TABLE_INCREMENT+" "
			+"("+COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+COL_ID_COUNTER+ " INTEGER,"
			+COL_DATE_INCREMENT+" INTEGER);";
			
	private static final String DROP_TABLE_COUNTER = "DROP TABLE IF EXISTS " + TABLE_COUNTER + ";";
	private static final String DROP_TABLE_INCREMENT = "DROP TABLE IF EXISTS " + TABLE_INCREMENT + ";";
	
	public MaBaseSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_TABLE_COUNTER);
		db.execSQL(CREATE_TABLE_INCREMENT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_COUNTER);
		db.execSQL(DROP_TABLE_INCREMENT);
		onCreate(db);

	}

}

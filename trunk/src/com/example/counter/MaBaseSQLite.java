package com.example.counter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLite extends SQLiteOpenHelper {

	private static final String TABLE_COUNTER = "table_counter";
	private static final String COL_ID = "ID";
	private static final String COL_TITLE ="Title";
	private static final String COL_DESCRIPTION = "Description";
	private static final String COL_COUNT = "Count";
	
	private static final String CREATE_BDD = "CREATE TABLE "+TABLE_COUNTER+" ("+COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+COL_TITLE+" TEXT NOT NULL UNIQUE,"
			+COL_DESCRIPTION+" TEXT NOT NULL,"
			+COL_COUNT+" INTEGER);";
	
	private static final String DROP_BDD = "DROP TABLE IF EXISTS " + TABLE_COUNTER + ";";
	
	public MaBaseSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_BDD);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_BDD);
		onCreate(db);

	}

}

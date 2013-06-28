package com.example.counterGraph;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class counterGraphActivity extends Activity {
	
	long id_counter;
	getAllDateIncrementAsyncTask getAllDateIncrementAsyncTask;
	ArrayList<Integer> listDateIncrement;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle objetbunble  = this.getIntent().getExtras();
		
		id_counter = this.getIntent().getLongExtra("id_counter",-1);
		Log.d("Counter counterGraphActivity", String.valueOf(id_counter));
		
		
		ArrayList<Integer> listDateIncrement = new ArrayList<Integer>();
		getAllDateIncrementAsyncTask = new getAllDateIncrementAsyncTask(this);
		getAllDateIncrementAsyncTask.execute(id_counter);
		
		
		
		try {
			listDateIncrement = getAllDateIncrementAsyncTask.get();
			Log.d("Counter counterGraphActivity", listDateIncrement.toString());
			
			for(int i = 0;i<listDateIncrement.size();i++)
				{
					Date date = new Date((long)listDateIncrement.get(i)*1000);
					Log.d("Counter counterGraphActivity",date.toString());
					SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
					Log.d("Counter counterGraphActivity", "X = "+shortDateFormat.format(date)+" Y = "+String.valueOf(i+1));
				}
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	

}

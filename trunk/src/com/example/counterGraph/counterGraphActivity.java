package com.example.counterGraph;


import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.counter.Counter;
import com.example.counter.CounterBDD;
import com.example.counter.MainActivity;
import com.example.counter.R;

public class counterGraphActivity extends Activity {
	
	long id_counter;
	getAllDateIncrementAsyncTask getAllDateIncrementAsyncTask;
	ArrayList<Integer> listDateIncrement;
	TextView tvTitle;
	TextView tvDescription;
	TableLayout tableHistorique;
	ProgressDialog progressDialog;
	String title;
	String description;
	TableRow tableRow;
	
	CounterBDD counterBDD;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.historique);
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		tvDescription = (TextView)findViewById(R.id.tvDescription);
		
		tableHistorique = (TableLayout)findViewById(R.id.tableHistorique);
		tableHistorique.setStretchAllColumns(true);  
		tableHistorique.setShrinkAllColumns(true);  
	   
		counterBDD = new CounterBDD(this);
		try {
			counterBDD.open();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Bundle objetbunble  = this.getIntent().getExtras();
		progressDialog = new ProgressDialog(this);
		
		
		id_counter = this.getIntent().getLongExtra("id_counter",-1);
		Log.d("Counter counterGraphActivity", String.valueOf(id_counter));
		
		
		
		new buildHistoTask().execute();
		
		
	}

	private class buildHistoTask extends AsyncTask<Long,Integer,ArrayList<String>>
	{

		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			
			Log.d("Counter buildHistoTask", result.toString());
			tvTitle.setText(title);
			tvDescription.setText(description);
			
			//build table
			 // create a new TableRow
	       
	        for(int i = 0;i<result.size();i++)
			{
	        	TableRow row = new TableRow(getBaseContext());
	        	//column increment
	 	        TextView increment = new TextView(getBaseContext(),null,R.style.frag1TableRow);
	 	        increment.setTextColor(Color.BLACK);
	 	        increment.setText(String.valueOf(i+1));
	 	        increment.setGravity(Gravity.CENTER_HORIZONTAL);
	 	        increment.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.table_border));
	 	        //column date
	 	        TextView date = new TextView(getBaseContext(),null,R.style.frag1TableRow);
	 	        date.setTextColor(Color.BLACK);
	 	        date.setText(result.get(i));
	 	        date.setGravity(Gravity.CENTER_HORIZONTAL);
	 	        date.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.table_border));
				
	 	        //add to row
	 	        
	 	        row.addView(increment);
	 	        row.addView(date);
	 	       // row.setBackground(R.drawable.table_border);
	 	       // add the TableRow to the TableLayout
	 	        tableHistorique.addView(row,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
			}
	        
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setTitle(R.string.load);
			progressDialog.setMessage(getResources().getString(R.string.wait_please));
			progressDialog.show();
		}

		@Override
		protected ArrayList<String> doInBackground(Long... params) {
			
			Counter counter = new Counter();
			counter = counterBDD.getCounterWithId(id_counter);
			title = counter.getTitle();
			description = counter.getDescription();
			
			ArrayList<Integer> listIncrement = new ArrayList<Integer>();
			ArrayList<String> listDateIncrement  = new ArrayList<String>();
		    listIncrement = counterBDD.getAllDateIncrementOfCounter(id_counter);
		    for(int i = 0;i<listIncrement.size();i++)
			{
				Date date = new Date((long)listIncrement.get(i)*1000);
				SimpleDateFormat shortDateFormat;
				if(DateFormat.is24HourFormat(getApplicationContext()))
					 shortDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");				
				else
					 shortDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss aa");
				listDateIncrement.add(shortDateFormat.format(date));
				
			}
		    
		    return listDateIncrement;
		}
		
		
	}
	

}

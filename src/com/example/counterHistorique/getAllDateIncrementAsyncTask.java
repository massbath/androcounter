package com.example.counterHistorique;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.example.counter.Counter;
import com.example.counter.CounterBDD;
import com.example.counter.IncrementBDD;
import com.example.counter.R;

public class getAllDateIncrementAsyncTask extends AsyncTask<Long , Integer, ArrayList<Integer>> {

	ProgressDialog progressDialog;
	Context context;
	Activity activity;
	IncrementBDD incrementBDD;
	CounterBDD counterBDD;
	String title;
	String description;
	Resources res ;
	
	public interface histoInterface
	{
		void setTitleCounter(String newTitle);
		void setDescriptionCounter(String newDescription);
	}
	
	public getAllDateIncrementAsyncTask(Activity activity)
		{
		  this.activity = activity ;   
		}
	@Override
	protected void onPostExecute(ArrayList<Integer> result) {
		// TODO Auto-generated method stub
		Log.d("counter getAllDateIncrementAsyncTask", "Title :"+title+" Description:"+description);
		
		
		progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		 progressDialog = new ProgressDialog(this.activity);
	     progressDialog.setTitle(R.string.load);
	     /*Resources res = getApplicationContext().getResources();
	     Activity.getApplicationContext().getResources().getString(R.string.wait_please);*/
	     //progressDialog.setMessage(res.getString(R.string.wait_please));
	     progressDialog.show();
	     incrementBDD = new IncrementBDD(this.activity);
	     counterBDD = new CounterBDD(this.activity);
	     
		
	}

	@Override
	protected ArrayList<Integer> doInBackground(Long... arg0) {
		
		try {
			incrementBDD.open();
			counterBDD.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Counter counter = new Counter();
		counter = counterBDD.getCounterWithId(arg0[0]);
		title = counter.getTitle();
		description = counter.getDescription();
		
		ArrayList<Integer> listDateIncrement = new ArrayList<Integer>();
	    listDateIncrement = incrementBDD.getAllDateIncrementOfCounter(arg0[0]);
	    for(int i = 0;i<listDateIncrement.size();i++)
		{
			Date date = new Date((long)listDateIncrement.get(i)*1000);
			Log.d("Counter counterGraphActivity",date.toString());
			SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
			Log.d("Counter counterGraphActivity", "X = "+shortDateFormat.format(date)+" Y = "+String.valueOf(i+1));
		}
	    
	    //Log.d("Counter getAllDateIncrementAsyncTask", listDateIncrement.toString());
	    return listDateIncrement;
	}

}

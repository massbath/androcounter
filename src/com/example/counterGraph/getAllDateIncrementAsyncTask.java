package com.example.counterGraph;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.counter.IncrementBDD;
import com.example.counter.R;

public class getAllDateIncrementAsyncTask extends AsyncTask<Long , Integer, ArrayList<Integer>> {

	ProgressDialog progressDialog;
	Context context;
	Activity activity;
	IncrementBDD incrementBDD;
	
	public getAllDateIncrementAsyncTask(Activity activity)
		{
		  this.activity = activity ;   
		}
	@Override
	protected void onPostExecute(ArrayList<Integer> result) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		 progressDialog = new ProgressDialog(this.activity);
	     progressDialog.setTitle(R.string.load);
	     progressDialog.show();
		
	}

	@Override
	protected ArrayList<Integer> doInBackground(Long... arg0) {
		incrementBDD = new IncrementBDD(this.activity);
		try {
			incrementBDD.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> listDateIncrement = new ArrayList<Integer>();
	    listDateIncrement = incrementBDD.getAllDateIncrementOfCounter(arg0[0]);
	    Log.d("Counter getAllDateIncrementAsyncTask", listDateIncrement.toString());
	    return listDateIncrement;
	}

}

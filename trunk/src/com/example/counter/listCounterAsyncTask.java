package com.example.counter;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class listCounterAsyncTask extends AsyncTask<CounterBDD, Integer, ArrayList<Counter>> {
	private Context context;
	private ProgressDialog dialog;
	
	public listCounterAsyncTask(Context context)
	{
		this.context = context;
	}
	
	@Override
	protected void onPostExecute(ArrayList<Counter> result) {
		// TODO Auto-generated method stub
		Log.d("Counter listCounterAsyncTask", "onPostExecute");
		dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		Log.d("Counter listCounterAsyncTask", "onPreExecute");
		dialog = new ProgressDialog(this.context);
		dialog.setTitle(R.string.load);
		dialog.show();
	}

	@Override
	protected ArrayList<Counter> doInBackground(CounterBDD... counterBDD) {
		// TODO Auto-generated method stub
		Log.d("Counter listCounterAsyncTask", "doInBackground");
		ArrayList<Counter>listCounter = new ArrayList<Counter>();
		listCounter = counterBDD[0].getAllCounter();
		return listCounter;
	}

}

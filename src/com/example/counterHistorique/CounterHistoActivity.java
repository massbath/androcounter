package com.example.counterHistorique;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

import com.example.counter.Counter;
import com.example.counter.CounterBDD;
import com.example.counter.R;

public class CounterHistoActivity extends Activity {
	
	long id_counter;


	getAllDateIncrementAsyncTask getAllDateIncrementAsyncTask;
	ArrayList<String> listDateIncrement;
	TextView tvTitle;
	TextView tvDescription;
	TableLayout tableHistorique;
	ProgressDialog progressDialog;
	String title;
	String description;
	TableRow tableRow;
	String path;
	String final_path;
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.histo, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
			{
				case(R.id.action_export_csv):
				
					Log.d("Counter", "Export csv");
					FileChooserDialog dialog = new FileChooserDialog(this);
					dialog.setFolderMode(true);
					dialog.addListener(new OnFileSelectedListener(){

						@Override
						public void onFileSelected(Dialog source, File file) {
							// TODO Auto-generated method stub
							 source.hide();
				            
				             path = file.getAbsolutePath();
				             new createCsvAsync().execute(listDateIncrement);
				             source.cancel();
						}

						@Override
						public void onFileSelected(Dialog source, File folder,
								String name) {
							// TODO Auto-generated method stub
							 source.hide();
				             
				             source.cancel();
							
						}});
					dialog.show();
					break;
				
			}
		
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	//private async tasks
	
	
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
		    listDateIncrement  = new ArrayList<String>();
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
	
	private class createCsvAsync extends AsyncTask<ArrayList<String>,Integer,Integer>
	{
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			Log.d("Counter counterHistoActivity", "onPostExecute build csv");
			progressDialog.dismiss();
			Toast.makeText(getBaseContext(), getResources().getString(R.string.create_file,final_path), Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreExecute() {
			Log.d("Counter counterHistoActivity", "onPreExecute build csv");
			progressDialog.setTitle(R.string.wait_please);
			progressDialog.setMessage(getResources().getString(R.string.create_file,title+".csv"));
			progressDialog.show();
		}

		@Override
		protected Integer doInBackground(ArrayList<String>... arg0) {
			Log.d("Counter counterHistoActivity", "doInBackGround build csv");
			 FileOutputStream out= null;
			 OutputStreamWriter out_writer = null;
			 try {
				//create file 
				 final_path = path+"/"+title+".csv";
				out = new FileOutputStream(final_path);
				out_writer = new OutputStreamWriter(out);
				//write title
				String ligne = "Titre ;"+title+";\n";
				out_writer.write(ligne);
				
				//write description
				ligne = "Description ;"+description+";\n";
				out_writer.write(ligne);
				
				ligne="\n\n";
				out_writer.write(ligne);
				
				ligne=getResources().getString(R.string.Counter)+";"+getResources().getString(R.string.Date)+";\n";
				out_writer.write(ligne);
				for(int i =0;i<listDateIncrement.size();i++)
					{
						ligne = String.valueOf(i+1)+";"+listDateIncrement.get(i)+";\n";
						out_writer.write(ligne);
					}
				
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				//close the FileOutputStream
				try {
					out_writer.flush();
					if(out!= null) out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			 return 1;
		}
	}
}

package com.example.counter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import ar.com.daidalos.afiledialog.FileChooserDialog;
import ar.com.daidalos.afiledialog.FileChooserDialog.OnFileSelectedListener;

import com.counterDroid.exportImportXML.ExportDBCounter;
import com.counterDroid.exportImportXML.ExportDBCounterAsync;
import com.counterDroid.exportImportXML.ImportDBCounterAsync;
import com.counterDroid.exportImportXML.ImportDBCounterAsync.ImportDBCounterAsyncInterface;
import com.example.counter.AddCounterDialog.AddCounterDialogOkListener;
import com.example.counter.ChangeCounterDialog.ChangeCounterDialogListenerInterface;
import com.example.counterHistorique.CounterHistoActivity;

public class MainActivity extends FragmentActivity implements ImportDBCounterAsyncInterface,AddCounterDialogOkListener,ChangeCounterDialogListenerInterface, OnItemClickListener{
	
	private final static int MENU_ADD = 1;
	

	ArrayList<Counter> listCounter;
	ListView listviewCounter;
	CounterBDD counterBDD;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listviewCounter = (ListView)findViewById(R.id.lvCounter);
		listviewCounter.setOnItemClickListener(this);
		 counterBDD = new CounterBDD(this);
		
		try {
			counterBDD.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		updateListViewCounter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Activity activity =  this;
		switch(item.getItemId())
			{
				case(R.id.add_counter):
				
					showAddCounterDialog();
					break;
					
				case(R.id.export_xml):
					Log.d("Counter", "MainActivity export xml clicked");
					
					
				
					FileChooserDialog dialog = new FileChooserDialog(this);
					dialog.setFolderMode(true);
					dialog.addListener(new OnFileSelectedListener(){
					
						@Override
						public void onFileSelected(Dialog source,  File file) {
							// TODO Auto-generated method stub
							 source.hide();
				             final String path = file.getAbsolutePath();
				             new ExportDBCounterAsync(activity,activity,path).execute(ExportDBCounterAsync.XML);
				             source.cancel();
						}
	
						

						@Override
						public void onFileSelected(Dialog source, File folder,
								String name) {
							// TODO Auto-generated method stub
							
						}});
					dialog.show();
					break;
				case(R.id.import_xml):
					Log.d("Counter", "MainActivity import xml clicked");
					
					FileChooserDialog dialogFile = new FileChooserDialog(this);
					//set to select files
					dialogFile.setFolderMode(false);
					//filter for .xml files
					dialogFile.setFilter(".*xml");
					//the user can create file
					dialogFile.setCanCreateFiles(false);
					dialogFile.addListener(new OnFileSelectedListener(){

						@Override
						public void onFileSelected(Dialog source, File file) {
							// TODO Auto-generated method stub
							 source.hide();				           
				             //file.getAbsolutePath();
				             final String path = file.getAbsolutePath();
				             new ImportDBCounterAsync(activity,activity,path).execute();
				             source.cancel();				             
				             
						}

						@Override
						public void onFileSelected(Dialog source, File folder,
								String name) {
							
						}
						
					});
					
					dialogFile.show();
					break;
					
			}
		
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	
	
	
	//method to show the custom dialogBox to add a new counter
	private void showAddCounterDialog()
	{
		FragmentManager fm = getSupportFragmentManager();
	    AddCounterDialog addCounterDialog = new AddCounterDialog();
	    addCounterDialog.show(fm, "fragment_addCounterDialog");
	}
	
	//method to show the dialogBox which allow you to increment/decrement,change title or description of a counter
	private void showChangeCounterDialog(Counter counter)
	{
		FragmentManager fm = getSupportFragmentManager();
	    ChangeCounterDialog changeCounterDialog = ChangeCounterDialog.newInstance(counter);
	    changeCounterDialog.show(fm, "fragment_changeCounterDialog");
		
	}
	
	

	public void updateListViewCounter()
	{
		
	    listCounterAsyncTask ListCounterAsyncTask = new listCounterAsyncTask(this);
	    ListCounterAsyncTask.execute(counterBDD);
	    
	    try {
			listCounter = ListCounterAsyncTask.get();
			CounterAdapter counterAdapter = new CounterAdapter(this,listCounter);
			listviewCounter.setAdapter(counterAdapter);
			Log.d("Counter MainActivity updateListViewCounter","End update list of counter");
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
						
		
	}

	@Override
	public void onItemClick(AdapterView<?> a, View view, int position, long id) {
		// TODO Auto-generated method stub
		 Object  data = new Object();
		 data = listviewCounter.getItemAtPosition(position);
		 Counter counter = new Counter();
		 counter = (Counter) data;
		 Log.d("[Counter MainActivity onItemClick]",String.valueOf(id));
		 Log.d("[Counter MainActivity onItemClick]",data.toString());
		 showChangeCounterDialog(counter);
		 updateListViewCounter();
	}

	
//on réfinit la méthode de l'inteface AddCounterDialogOkListener de la dialogue
	public void onFinishAddCounterDialog(String Title,String Description)
	{
		Counter newCounter = new Counter(Title,Description,0);
		//counterBDD.open();
		long res = counterBDD.insertCounter(newCounter);
		//counterBDD.close();
		if (res<0)
			{
				Toast.makeText(this, R.string.add_cancel_double, Toast.LENGTH_SHORT).show();
				Log.d("[Counter MainActivity onFinishAddCounterDialog ]","Ajout à la base non fait titre déja utilisé");
				return;
			}
		
		
		updateListViewCounter();
		
		
	}
	
	
	@Override
	public void onFinishChangeCounterDialog(Counter counter) {
		// TODO Auto-generated method stub
		//counterBDD.open();
		counterBDD.updateCounter(counter.getId(), counter);
		//counterBDD.close();
		updateListViewCounter();
		
		
	}

	@Override
	public boolean  onChangeCounterTitle(long id, String title) {
		// TODO Auto-generated method stub
		//counterBDD.open();
		boolean res = counterBDD.isFreeTitle(id, title);
		//counterBDD.close();
		return res;
	}

	@Override
	public void countPlus(final Counter counter) {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//counterBDD.open();
				counterBDD.countPlusCounter(counter);
				//counterBDD.close();
			}
			
		}).start();
	}

	@Override
	public void countMinus(final Counter counter) {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//counterBDD.open();
				counterBDD.countMinusCounter(counter);
				//counterBDD.close();
			}
			
		}).start();
	}

	@Override
	public void removeCounter(final Counter counter) {
		//counterBDD.open();
		counterBDD.removeCounter(counter);
		//counterBDD.close();
		updateListViewCounter();
	}

	@Override
	public boolean isTitleFree(String title) {
		//counterBDD.open();
		boolean res = counterBDD.isFreeTitle(title);
		
		return res;
	}

	@Override
	public void onCancelChangeCounterDialog() {
		// TODO Auto-generated method stub
		updateListViewCounter();
	}

	 protected void onDestroy()
     {
             if (counterBDD != null)
             {
                     counterBDD.close();
             }
             super.onDestroy();
     }

	@Override
	public void showGraphCounter(long id_counter) {
		// TODO Auto-generated method stub
		
		Bundle objetbunble = new Bundle();		
		objetbunble.putLong("id_counter", id_counter);

		Intent intent  = new Intent(MainActivity.this,CounterHistoActivity.class);

		//On affecte à l'Intent le Bundle que l'on a créé
		intent.putExtras(objetbunble);

		//On démarre l'autre Activity
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateListViewCounter();
	}

	//when the import from xml is finished
	@Override
	public void updateListAfterImport() {
		// TODO Auto-generated method stub
		updateListViewCounter();
	}
	
}

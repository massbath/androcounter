package com.counterDroid.exportImportXML;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.example.counter.Counter;
import com.example.counter.CounterBDD;
import com.example.counter.Increment;
import com.example.counter.IncrementBDD;
import com.example.counter.R;

public class ImportDBCounterAsync extends AsyncTask<Void,Void,Void>{
	CounterBDD counterBDD;
	IncrementBDD incrementBDD;
	String FilePath;
	Activity mainActivity;
	Context context;
	ProgressDialog progressDialog;
	BaliseXML baliseXml;
	Resources resources;
	//interface to update listView at the end of computation
	public interface ImportDBCounterAsyncInterface{
		void updateListAfterImport();
	}
	
	 public ImportDBCounterAsync(Activity mainActivity,Context context, String FilePath)
	 {
		 this.counterBDD   = new CounterBDD(mainActivity);
		 this.incrementBDD = new IncrementBDD(mainActivity); 
			
			try {
				counterBDD.open();
				incrementBDD.open();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 this.mainActivity = mainActivity;
		 this.context = context;
		 this.FilePath = FilePath; 
		 this.baliseXml = new BaliseXML();
		 this.resources = context.getResources();
	 }

	private String readFile()
	{
		String xml = null;
		StringBuffer lu = new StringBuffer();		
		FileInputStream in;
		try {
			in = new FileInputStream(this.FilePath);
			int octet;
			
			while((octet = in.read()) != -1)
			{
				lu.append((char)octet);
			}
			xml = lu.toString();
			
			if(in != null) in.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return xml;
	}
	 
	
	
	@Override
	protected void onPreExecute() {
		Log.d("Counter", "ImportDBCounterAsync onPreExecute");
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(R.string.wait_please);
		progressDialog.setMessage(resources.getString(R.string.importXMLInProgress));
		progressDialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
	
		//get xml 
		String xml = readFile();
		Log.d("Counter ImportDBCounterAsync", " file : "+xml);
		
		//set the parser
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(new StringReader(xml));
			
			int eventType = parser.getEventType();
			Counter currentCounter = null;
			long id_counter = -1;
			boolean done = false;
			String name = null;
			while(eventType != XmlPullParser.END_DOCUMENT && !done)
				{
				
					
					switch(eventType)
						{
							case(XmlPullParser.START_DOCUMENT):
								break;
							
							case(XmlPullParser.START_TAG):
								name = parser.getName();
							    Log.d("Counter ImportDBCounterAsync", "doInBackground balise lu :"+name);
								//if it's a counter tag we will try to insert this counter in the db
							    if(name.equalsIgnoreCase( baliseXml.COUNTER))
										{
										currentCounter = new Counter(parser.getAttributeValue("", baliseXml.TITLE),parser.getAttributeValue("", baliseXml.DESCRIPTION),Integer.valueOf(parser.getAttributeValue("",baliseXml.COUNT)));
										Log.d("Counter ImportDBCounterAsync", "doInBackground currentCounter "+currentCounter.toString());
										//if the title is not already used in the database
										if(counterBDD.isFreeTitle(parser.getAttributeValue("", baliseXml.TITLE)))
											{
												//insert the counter in the db, this return -1 if the title is already used
												id_counter = counterBDD.insertCounter(currentCounter);
												
											}
										else id_counter= -1;
										
										String message = (id_counter != -1)?"compteur ajouté":"compteur non ajouté";
										Log.d("Counter ImportDBCounterAsync","doInBackground"+message);
										}
							    else if(name.equalsIgnoreCase(baliseXml.INCREMENT))
							    	{
							    		//if the counter was created previously
							    		if(id_counter != -1)
							    			{
							    				Increment new_increment = new Increment(id_counter,parser.getAttributeValue("", baliseXml.DATE));
							    				incrementBDD.addIncrement(new_increment);
							    				Log.d("Counter ImportDBCounterAsync", "doInBackground add increment "+new_increment.toString());
							    			}
							    	}
								break;
							
							case(XmlPullParser.END_TAG):
								break;
								
								
						}
					eventType = parser.next();
				}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}



	@Override
	protected void onPostExecute(Void result) {
		Log.d("Counter ImportDBCounterAsync", "onPostExecute");
		progressDialog.dismiss();
		ImportDBCounterAsyncInterface activity = (ImportDBCounterAsyncInterface)mainActivity;
		activity.updateListAfterImport();
	}
	
}

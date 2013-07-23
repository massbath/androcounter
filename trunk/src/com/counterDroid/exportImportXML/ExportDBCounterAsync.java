package com.counterDroid.exportImportXML;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;


import com.example.counter.Counter;
import com.example.counter.CounterBDD;
import com.example.counter.IncrementBDD;
import com.example.counter.MainActivity;
import com.example.counter.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

public class ExportDBCounterAsync extends AsyncTask <Integer,Void,Void>{

	CounterBDD counterBDD;
	IncrementBDD incrementBDD;
	String FilePath;
	Activity mainActivity;
	ProgressDialog progressDialog;
	Context context;
	BaliseXML baliseXml;
	String final_filepath;
	Resources resources;
	
	
	public ExportDBCounterAsync(Activity mainActivity,Context context,String FilePath)
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
	this.context = context;	
	  this.FilePath = FilePath;
	  this.resources = context.getResources();
	  baliseXml = new BaliseXML();
	}
	
	public static final int XML =1;
	
	//to write in xml format
	public String intoXML() throws IllegalArgumentException, IllegalStateException, IOException
		{
			
			XmlSerializer xmlSerializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			
			xmlSerializer.setOutput(writer);
			
			//start DOCUMENT
			xmlSerializer.startDocument("UTF-8", true);
			//open tag : counters
			xmlSerializer.startTag("",baliseXml.COUNTERS);
			
			//get all the counters
			ArrayList<Counter> listCounter = new ArrayList<Counter>();
			listCounter = this.counterBDD.getAllCounter();
			
			//set attribute number of counters
			xmlSerializer.attribute("",baliseXml.NUMBER,String.valueOf(listCounter.size()));
			
			for(int i =0;i<listCounter.size();i++)
				{
					//add tag counter
					xmlSerializer.startTag("",baliseXml.COUNTER);
					//set attribute Id with the id of the counter
					xmlSerializer.attribute("",baliseXml.ID,String.valueOf(listCounter.get(i).getId()));				
					//add tag TITLE
					xmlSerializer.attribute("", baliseXml.TITLE, listCounter.get(i).getTitle());
					
					//add tag DESCRIPTION
					xmlSerializer.attribute("", baliseXml.DESCRIPTION, listCounter.get(i).getDescription());
					
					//add tag COUNT
					xmlSerializer.attribute("", baliseXml.COUNT, String.valueOf(listCounter.get(i).getCount()));
					
					//if there are increments
					if(listCounter.get(i).getCount()>0)
					{	
						//add all the increment of this counter
						xmlSerializer.startTag("", baliseXml.INCREMENTS);
						ArrayList<Integer> listIncrement = new ArrayList<Integer>();
						listIncrement = incrementBDD.getAllDateIncrementOfCounter(listCounter.get(i).getId());
						//set attribute number of increments
						xmlSerializer.attribute("", baliseXml.NUMBER, String.valueOf(listIncrement.size()));
						
						for(int j = 0;j<listIncrement.size();j++)
							{
								//add tag increment
								xmlSerializer.startTag("",baliseXml.INCREMENT);
								//set attribute date
								xmlSerializer.attribute("", baliseXml.DATE, listIncrement.get(j).toString());
								//close tag increment
								xmlSerializer.endTag("", baliseXml.INCREMENT);
							}
						//close tag increments
						xmlSerializer.endTag("", baliseXml.INCREMENTS);					
					}
					
					
					//close tag counter
					xmlSerializer.endTag("",baliseXml.COUNTER);
				}
			
			//close tag : counters
			xmlSerializer.endTag("",baliseXml.COUNTERS);
			
			// end DOCUMENT
			xmlSerializer.endDocument();
			
			return writer.toString();
			
		}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.d("Counter", "ExportDBCounterAsync onPostExecute");
		progressDialog.dismiss();
		
		Toast.makeText(context, String.format(resources.getString(R.string.create_file),final_filepath), Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	protected void onPreExecute() {
		Log.d("Counter", "ExportDBCounterAsync onPreExecute");
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle(R.string.wait_please);
	
		progressDialog.setMessage(resources.getString(R.string.exportXMLInProgress));
		progressDialog.show();
	}
	
	protected Void doInBackground(Integer... params) {
		Log.d("Counter", "ExportDBCounterAsync doInBackground");
		FileOutputStream out= null;
		OutputStreamWriter out_writer = null;
		Time time = new Time();
		time.setToNow();
		
		try {
			final_filepath = FilePath+"/ExportCounterDroid_"+time.format("%Y%d%m _%H%M%S")+".xml";
			out = new FileOutputStream(final_filepath);
			out_writer = new OutputStreamWriter(out);
			
			
			switch (params[0])
			{
			  case XML:
				  out_writer.write(intoXML());
			}
			
			out_writer.flush();
			if(out!= null) out.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
	}

	
}

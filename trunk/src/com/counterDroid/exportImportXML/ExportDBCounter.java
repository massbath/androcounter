package com.counterDroid.exportImportXML;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.text.format.Time;
import android.util.Xml;

import com.example.counter.Counter;
import com.example.counter.CounterBDD;
import com.example.counter.IncrementBDD;

public class ExportDBCounter {

	CounterBDD counterBDD;
	IncrementBDD incrementBDD;
	String FilePath;
	Activity mainActivity;
	
	
	//constant
	private class baliseXml{
		private static final String ID="Id";
		private static final String COUNTERS="Counters";
		private static final String COUNTER ="Counter";
		private static final String TITLE="Title";
		private static final String DESCRIPTION="Description";
		private static final String COUNT="Count";
		private static final String INCREMENTS ="Increments";
		private static final String INCREMENT ="Increment";
		private static final String DATE = "Date";
	}
	public static final int XML =1;
	
	public ExportDBCounter(Activity mainActivity,String FilePath)
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
		
	  this.FilePath = FilePath;
	}
	
	//to write just choose a format for the output file
	public void write(int mode) throws IllegalArgumentException, IllegalStateException, IOException
	{
		FileOutputStream out= null;
		OutputStreamWriter out_writer = null;
		Time time = new Time();
		time.setToNow();
		out = new FileOutputStream(FilePath+"/ExportCounterDroid_"+time.format("%Y%d%m _%H%M%S")+".xml");
		out_writer = new OutputStreamWriter(out);
		
		
		switch (mode)
		{
		  case XML:
			  out_writer.write(this.intoXML());
		}
		
		out_writer.flush();
		if(out!= null) out.close();
	}
	
	
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
		
		for(int i =0;i<listCounter.size();i++)
			{
				//add tag counter
				xmlSerializer.startTag("",baliseXml.COUNTER);
				//set attribute Id with the id of the counter
				xmlSerializer.attribute("",baliseXml.ID,String.valueOf(listCounter.get(i).getId()));				
				//add tag TITLE
				xmlSerializer.startTag("", baliseXml.TITLE);
				xmlSerializer.text(listCounter.get(i).getTitle());
				xmlSerializer.endTag("", baliseXml.TITLE);
				
				//add tag DESCRIPTION
				xmlSerializer.startTag("", baliseXml.DESCRIPTION);
				xmlSerializer.text(listCounter.get(i).getDescription());
				xmlSerializer.endTag("", baliseXml.DESCRIPTION);
				
				//add tag COUNT
				xmlSerializer.startTag("", baliseXml.COUNT);
				xmlSerializer.text(String.valueOf(listCounter.get(i).getCount()));
				xmlSerializer.endTag("", baliseXml.COUNT);
				
				//add all the increment of this counter
				xmlSerializer.startTag("", baliseXml.INCREMENTS);
				ArrayList<Integer> listIncrement = new ArrayList<Integer>();
				listIncrement = incrementBDD.getAllDateIncrementOfCounter(listCounter.get(i).getId());
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
				//close tag counter
				xmlSerializer.endTag("",baliseXml.COUNTER);
			}
		
		//close tag : counters
		xmlSerializer.endTag("",baliseXml.COUNTERS);
		
		// end DOCUMENT
		xmlSerializer.endDocument();
		
		return writer.toString();
		
	}
	
}

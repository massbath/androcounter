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
		private static final String INCREMENT ="Increment";
		private static final String DATE = "Date";
	}
	private static final int XML =1;
	
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
		out = new FileOutputStream(FilePath+"ExportCounterDroid_"+time.format("%Y:%m:%d %H:%M:%S")+".xml");
		out_writer = new OutputStreamWriter(out);
		
		
		switch (mode)
		{
		  case XML:
			  out_writer.write(this.intoXML());
		}
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
				
				//close tag counter
				xmlSerializer.endTag("",baliseXml.COUNTERS);
			}
		
		//close tag : counters
		xmlSerializer.endTag("","Counters");
		
		// end DOCUMENT
		xmlSerializer.endDocument();
		
		return writer.toString();
		
	}
	
}

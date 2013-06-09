package com.example.counter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CounterAdapter extends ArrayAdapter<Counter> {

	private final ArrayList<Counter> lstCounter = new ArrayList<Counter>();
	private Context context;
	
	public CounterAdapter(final Context context, final ArrayList<Counter> lstCounter)
	{
		super(context,android.R.id.text1,lstCounter);
		this.context=context;
		this.lstCounter.clear();
		this.lstCounter.addAll(lstCounter);
	}
	
	public View getView(final int position,final View convertView,final ViewGroup parent)
	{
		View view = convertView;
		if(view == null)
			{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.custom_counter_layout, null);
			}
		final Counter counter = lstCounter.get(position);
		if(counter != null)
			{
				final TextView txtTitle = (TextView)view.findViewById(R.id.tvTitle);
				final TextView txtDescription = (TextView)view.findViewById(R.id.tvDescription);
				final TextView txtCount =(TextView) view.findViewById(R.id.tvCount);
				
				txtTitle.setText(counter.getTitle());
				txtDescription.setText(counter.getDescription());
				txtCount.setText(String.valueOf(counter.getCount()));
			}
		
		return view;
	}
}

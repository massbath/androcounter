package com.example.counter;

import com.example.counter.AddCounterDialog.AddCounterDialogOkListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeCounterDialog extends DialogFragment implements OnClickListener, android.view.View.OnClickListener   {

	private EditText title;
	private EditText description;
	private Counter counter = new Counter();
	private Button plus;
	private TextView count;
	private Button moins;
	private NumberPicker numpicker;
	
	//interface à implémenter dans l'activité ouvrant la dialogue, ce qui permet de tranfsférer les données
	public interface ChangeCounterDialogOkListener{
		void onFinishChangeCounterDialog(Counter counter);
	}
	
	public interface ChangeCounterDialogOnchangeCountListener{
		void onChangeCountChangeCounterDialog(Counter counter);
	}
	
	public static ChangeCounterDialog newInstance(Counter counter)
	{
			Log.d("[Counter ChangeCounterDialog newInstance]",counter.toString());
			ChangeCounterDialog fragment = new ChangeCounterDialog();
			Bundle args = new Bundle();
			args.putString("TITLE", counter.getTitle());
			args.putString("DESCRIPTION", counter.getDescription());
			args.putInt("COUNT", counter.getCount());
			args.putLong("ID", counter.getId());
			fragment.setArguments(args);
		return fragment;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	    View view = layoutInflater.inflate(R.layout.change_counter_dialog, null);
	    Bundle args = getArguments();
	    Log.d("[Counter ChangeCounterDialog onCreateDialog]",args.toString());
	    title = (EditText)view.findViewById(R.id.etChangeTitle);
	    title.setHint(args.getString("TITLE"));
		description = (EditText) view.findViewById(R.id.etChangeDescription);
		description.setHint(getArguments().getString("DESCRIPTION"));
		plus = (Button)view.findViewById(R.id.buttonPlus);
		plus.setOnClickListener(this);
		
		moins = (Button)view.findViewById(R.id.buttonMoins);
		moins.setOnClickListener(this);
		
		count = (TextView)view.findViewById(R.id.tvCount);
		count.setText(String.valueOf(args.getInt("COUNT")));
		
		return new AlertDialog.Builder(getActivity())
	    .setView(view)
	    .setTitle(R.string.change_counter)
	    .setPositiveButton(R.string.add, this) 
	    .setNegativeButton(R.string.cancel, this)
	    .create();
	    
	}

	@Override
	public void onClick(DialogInterface arg0, int which) {
		// TODO Auto-generated method stub
		switch(which)
			{
				case DialogInterface.BUTTON_POSITIVE:
						this.dismiss();
						
						break;
				case DialogInterface.BUTTON_NEGATIVE:
					    ChangeCounterDialogOkListener mainActivity = (ChangeCounterDialogOkListener)getActivity();
						mainActivity.onFinishChangeCounterDialog(counter);
					    this.dismiss();
						break;
			}
	}

	@Override
	public void onClick(View v) {
		Bundle args = getArguments();
		
		// TODO Auto-generated method stub
		int valueCount = Integer.valueOf(count.getText().toString());
		switch(v.getId())
		{
			case R.id.buttonMoins:
				
				valueCount -- ;
				if(valueCount >= 0)
					{
						count.setText(String.valueOf(valueCount));
						Counter counter = new  Counter( args.getLong("ID"),args.getString("TITLE"),args.getString("DESCRIPTION"),valueCount);
						ChangeCounterDialogOnchangeCountListener mainActivity = (ChangeCounterDialogOnchangeCountListener) getActivity();
					    mainActivity.onChangeCountChangeCounterDialog(counter);
					    
					}
				else Toast.makeText(getActivity(), R.string.count_negatif, Toast.LENGTH_SHORT).show();
				break;
			case R.id.buttonPlus:
				valueCount++;
				Counter counter = new  Counter( args.getLong("ID"),args.getString("TITLE"),args.getString("DESCRIPTION"),valueCount);
				count.setText(String.valueOf(valueCount));
				ChangeCounterDialogOnchangeCountListener mainActivity = (ChangeCounterDialogOnchangeCountListener) getActivity();
			    mainActivity.onChangeCountChangeCounterDialog(counter);
				break;
			default:
				break;
		}
	}

	
		
	
	
}

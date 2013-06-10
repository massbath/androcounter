package com.example.counter;

import com.example.counter.ChangeCounterDialog.ChangeCounterDialogListenerInterface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCounterDialog extends DialogFragment implements android.content.DialogInterface.OnClickListener, OnFocusChangeListener {

	private EditText title;
	private EditText description;
	
	//interface à implémenter dans l'activité ouvrant la dialogue, ce qui permet de tranfsférer les données
	public interface AddCounterDialogOkListener{
		void onFinishAddCounterDialog(String title,String description);
		boolean isTitleFree(String title);
	}
	
	public AddCounterDialog(){
		
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	    View view = layoutInflater.inflate(R.layout.add_counter_dialog, null);
	    
	    title = (EditText)view.findViewById(R.id.etTitle);
	   // title.setText(R.string.title);
	    title.setOnFocusChangeListener(this);
		description = (EditText) view.findViewById(R.id.etDescription);
		//description.setText(R.string.description);
		return new AlertDialog.Builder(getActivity())
	    .setView(view)
	    .setTitle(R.string.add_counter)
	    .setPositiveButton(R.string.add, this) 
	    .setNegativeButton(R.string.cancel, this)
	    .create();
	    
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch(which)
		{
		case(DialogInterface.BUTTON_POSITIVE):
			{
				Log.d("Counter AddCounterDialog onClick ", "Title : "+title.getText().toString()
						+" Description : "+description.getText().toString());
				
				if(!title.getText().toString().equals("") && !description.getText().toString().equals(""))
				{
					Log.d("Counter AddCounterDialog onClick ","Titre renseigné et description aussi");
					//return title and description to the main activity
				    AddCounterDialogOkListener mainActivity = (AddCounterDialogOkListener) getActivity();
				    mainActivity.onFinishAddCounterDialog(title.getText().toString(), description.getText().toString());
				    this.dismiss();
				}
				else {
					    Log.d("Counter AddCounterDialog onClick ","Titre  ou description mal renseigné");
						Toast.makeText(getActivity(),R.string.not_all_inputs,Toast.LENGTH_SHORT ).show();
						break;
				}
			
			}
		case(DialogInterface.BUTTON_NEGATIVE):
			{
				this.dismiss();			
			}
		default: break;
		}
		
	}

	@Override
	public void onFocusChange(View arg0, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(!hasFocus)
			{
			AddCounterDialogOkListener mainActivity = (AddCounterDialogOkListener) getActivity();
			boolean res = mainActivity.isTitleFree(title.getText().toString());
			Log.d("Counter AddCounterDialog onFocusChange","res = "+res);
			//if there is other counter with the title
			if(!res)
				{
					Toast.makeText(getActivity(), R.string.title_already_used, Toast.LENGTH_SHORT).show();
					title.setText("");
					
				}
			}
	}



	
	
}

package com.example.counter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeCounterDialog extends DialogFragment implements OnClickListener, android.view.View.OnClickListener   {

	private EditText title;
	private boolean titleChecked;
	private boolean descriptionChecked;
	
	private EditText description;
	private Counter counter = new Counter();
	private Button plus;
	private TextView count;
	private Button moins;
	private ImageButton delete;
	
	//interface � impl�menter dans l'activit� ouvrant la dialogue, ce qui permet de tranfsf�rer les donn�es
	public interface ChangeCounterDialogListenerInterface{
		void onFinishChangeCounterDialog(Counter counter);
		boolean onChangeCounterTitle(long id,String title);
		void onCancelChangeCounterDialog();
		void countPlus(Counter counter);
		void countMinus(Counter counter);
		void removeCounter(Counter counter);
	}
	
	
	
	public static ChangeCounterDialog newInstance(Counter counter)
	{
			Log.d("Counter ChangeCounterDialog newInstance",counter.toString());
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
		
		titleChecked = false;
		descriptionChecked = false;
		
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
	    View view = layoutInflater.inflate(R.layout.change_counter_dialog, null);
	    final Bundle args = getArguments();
	    Log.d("[Counter ChangeCounterDialog onCreateDialog]",args.toString());
	    title = (EditText)view.findViewById(R.id.etChangeTitle);
	    title.setHint(args.getString("TITLE"));
	    title.setOnFocusChangeListener(new OnFocusChangeListener()
	    	{

				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(!hasFocus) checkTitle();
				}
	    	
	    	
	    	});
	    
		description = (EditText) view.findViewById(R.id.etChangeDescription);
		description.setHint(getArguments().getString("DESCRIPTION"));
		description.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus) checkDescription();
					
			}
			
		});
		plus = (Button)view.findViewById(R.id.buttonPlus);
		plus.setOnClickListener(this);
		
		moins = (Button)view.findViewById(R.id.buttonMoins);
		moins.setOnClickListener(this);
		
		count = (TextView)view.findViewById(R.id.tvCount);
		count.setText(String.valueOf(args.getInt("COUNT")));
		
		delete = (ImageButton)view.findViewById(R.id.deleteCounter);
		delete.setClickable(true);
		delete.setOnClickListener(this);
		
		return new AlertDialog.Builder(getActivity())
	    .setView(view)
	    .setTitle(R.string.change_counter)
	    .setPositiveButton(R.string.close, this) 
	    
	    .create();
	    
	}

	@Override
	public void onClick(DialogInterface arg0, int which) {
		// TODO Auto-generated method stub
		
		switch(which)
			{
				case DialogInterface.BUTTON_POSITIVE:
						{
							
							Bundle args = getArguments();
							//if one of title or description is different
							if(!title.getText().toString().equals( "") || !description.getText().toString().equals( ""))
								{
									String newTitle = (title.getText().toString().equals(""))?args.getString("TITLE"):title.getText().toString();
									ChangeCounterDialogListenerInterface mainActivity = (ChangeCounterDialogListenerInterface) getActivity();
									boolean res = mainActivity.onChangeCounterTitle(args.getLong("ID"),newTitle);
									Log.d("Counter ChangeCounterDialog onFocusChange","res = "+res);
									//if there is other counter with the title
									if(!res)
										{
											Toast.makeText(getActivity(), R.string.title_already_used, Toast.LENGTH_SHORT).show();
											title.setText("");
											title.setHint(args.getString("TITLE"));
											newTitle = args.getString("TITLE");
										}
									
									String newDescription = (description.getText().toString().equals(""))?args.getString("DESCRIPTION"):description.getText().toString();
									Log.d("Counter ChangeCounterDialog onClick positif",newTitle+" "+newDescription);
									Toast.makeText(getActivity(), newTitle+" "+newDescription, Toast.LENGTH_SHORT).show();
									Counter counter = new Counter(args.getLong("ID"),newTitle,newDescription,Integer.valueOf(count.getText().toString()));     
									
									Log.d("Counter ChangeCounterDialog onClick"," BUTTON_POSITIVE modif ok "+counter.toString());
									mainActivity.onFinishChangeCounterDialog(counter);
								}
							//this.dismiss();
							break;
						}
						
			}
	}

	@Override
	public void onClick(View v) {
		Bundle args = getArguments();
		
		// TODO Auto-generated method stub
		int valueCount = Integer.valueOf(count.getText().toString());
		checkTitle();
		checkDescription();
		switch(v.getId())
		{
			case R.id.buttonMoins:
				{
					plus.requestFocus();
					valueCount -- ;
					if(valueCount >= 0)
						{
							count.setText(String.valueOf(valueCount));
							Counter counter = new  Counter( args.getLong("ID"),args.getString("TITLE"),args.getString("DESCRIPTION"),valueCount);
							ChangeCounterDialogListenerInterface mainActivity = (ChangeCounterDialogListenerInterface) getActivity();
						    mainActivity.countMinus(counter);
						    
						}
					else Toast.makeText(getActivity(), R.string.count_negatif, Toast.LENGTH_SHORT).show();
					break;
				}	
			case R.id.buttonPlus:
				{
					valueCount++;
					Counter counter = new  Counter( args.getLong("ID"),args.getString("TITLE"),args.getString("DESCRIPTION"),valueCount);
					count.setText(String.valueOf(valueCount));
					ChangeCounterDialogListenerInterface mainActivity = (ChangeCounterDialogListenerInterface) getActivity();
				    mainActivity.countPlus(counter);
					break;
				}
			case R.id.deleteCounter:
				{
					Counter counter = new  Counter( args.getLong("ID"),args.getString("TITLE"),args.getString("DESCRIPTION"),valueCount);
					Log.d("Counter ChangeCounterDialog onClick","delete"+args.toString());
					//Toast.makeText(getActivity(),"delete",Toast.LENGTH_SHORT).show();
					ChangeCounterDialogListenerInterface mainActivity  =(ChangeCounterDialogListenerInterface)getActivity();
					mainActivity.removeCounter(counter);
					this.dismiss();
					break;
				}
			default:
				break;
		}
	}

 private void checkTitle()
 {
	 final Bundle args = getArguments();
	 Log.d("Counter counterChangeDialog setOnFocusChangeListener", title.getText().toString());
		if( !title.getText().toString().equals("") && !titleChecked)
		{
		
			AlertDialog.Builder box;
			box = new AlertDialog.Builder(getActivity());
			box.setTitle(R.string.modification);
			Resources res = getResources();
			box.setMessage(res.getString(R.string.accept_change,res.getString( R.string.of_title)));
			box.setPositiveButton(R.string.change,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Bundle args = getArguments();
					ChangeCounterDialogListenerInterface mainActivity = (ChangeCounterDialogListenerInterface) getActivity();
					boolean res = mainActivity.onChangeCounterTitle(args.getLong("ID"),title.getText().toString());
					Log.d("Counter ChangeCounterDialog onFocusChange","res = "+res);
					//if there is other counter with the title
					if(!res)
						{
							Toast.makeText(getActivity(), R.string.title_already_used, Toast.LENGTH_SHORT).show();
							title.setText("");
							title.setHint(args.getString("TITLE"));
							
						}
					else titleChecked=true;
					
				}
			});
			box.setNegativeButton("Annuler", new DialogInterface.OnClickListener(){
	
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
						title.setText("");
						title.setHint(args.getString("TITLE"));
				}});
			box.show();
				
			}
	}
 

	
	private void checkDescription()
	{
		 final Bundle args = getArguments();
		if( !description.getText().toString().equals("") && !descriptionChecked)
		{
			AlertDialog.Builder box;
			box = new AlertDialog.Builder(getActivity());
			box.setTitle(R.string.modification);
			Resources res = getResources();
			box.setMessage(res.getString(R.string.accept_change,res.getString( R.string.of_description)));
			box.setPositiveButton(R.string.change,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					descriptionChecked=true;
										}
			});
			box.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
	
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
						description.setText("");
						description.setHint(args.getString("DESCRIPTION"));
				}});
			box.show();
		}
	}
	
	
}

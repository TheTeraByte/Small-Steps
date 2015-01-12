package in.tusharbhargava.habitapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * This class is a fragment that the app uses to display data
 * about each habit in a list.
 * @author Tushar Bhargava
 *
 */

public class HabitDataDialogFragment extends DialogFragment
{
	// Global variables
	AlertDialog.Builder _builder;
	AlertDialog _dialog;
	String data_to_display;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
				
		if(getActivity()==null)
		{
			System.out.println("'getActivity()' returning null in HabitDataDialog!");
		}

		else
		{
			_builder=new AlertDialog.Builder(getActivity());
			_builder.setMessage(" ");
		}
		
		// This might be null (as getActivity() returns null if onAttach not called)
		if(_builder!=null)
		{
			_builder.setMessage(getArguments().getString(DisplayHabitsByCategory.KEY_HABIT_DATA)).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int id) {
               dismiss();
            }
        });
		
			// Create the AlertDialog object and return it
			_dialog=_builder.create();
		}// end if statement
		
		else
		{
			System.out.println("AlertBuilder is null.");
		}
		
		return _dialog;
		
	}// end function
	
	
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        _builder=new AlertDialog.Builder(activity);
	    }
	
}// end class

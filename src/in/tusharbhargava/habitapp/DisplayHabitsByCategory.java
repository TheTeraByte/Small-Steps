package in.tusharbhargava.habitapp;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class reads the habit details from the internal storage and displays them
 * in list view. 
 * 
 * @author Tushar Bhargava
 */

public class DisplayHabitsByCategory extends FragmentListActivity {

	// Global variables
	private ExtStorageWriterAndReader _reader;
	// The search key for retrieving habit data being passed between activities.
	public static final String KEY_HABIT_DATA="habit_data";
	private String category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		// Getting intent from previous activity
		Intent intent=this.getIntent();
		
		// Initializing file reader
		_reader=new ExtStorageWriterAndReader();
		
		// Getting the category from the previous activity
		category=intent.getExtras().getString("category");
		
		// Choose the appropriate layout 
		setContentView(R.layout.activity_display_habit_by_category);
		
		// The content
		ArrayList<String> values_to_display=new ArrayList<String>();
		
		// Getting all the habit names under this category
		String habits_under_category_text=_reader.getFileContent("/"+category+"/habits_under_category.txt");
		// If there is no habit under category
		if(habits_under_category_text==null)
		{
			Toast.makeText(getApplicationContext(),"No habits under this category.", Toast.LENGTH_SHORT).show();
			Intent return_to_main=new Intent(DisplayHabitsByCategory.this,MainActivity.class);
			finish();
			startActivity(return_to_main);
		}// end if statement
	
		
		else
		{
		String[] habits=habits_under_category_text.split("\r\n");
		
		for(String s:habits)
		{
			// Adding all the habit names to the display
			values_to_display.add(s);
		}
		
		// Gaining control of the ListView
		ListView display_list=this.getListView();
		ArrayAdapter<String> habits_lists=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,values_to_display);
		
		// Linking the adapter with the ListView
		display_list.setAdapter(habits_lists);
		}
	}// end onCreate function
	
	/**
	 * This method defines the actions to be performed when a list item i.e. habit
	 * is clicked.
	 * 
	 * Mainly used to display all the info related to the habit.
	 * @param list_view
	 * @param v
	 * @param position
	 * @param id
	 */
	@Override
	protected void onListItemClick(ListView list_view,View v,int position,long id)
	{
		String selected_habit_name=list_view.getItemAtPosition(position).toString();
		// We use the habit name to extract all the relevant data
		String data_to_display=" Habit name: "+selected_habit_name+"\n Habit description: "+_reader.getFileContent("\\"+category+"\\"+selected_habit_name+"_description.txt")+"\n Total Days Performed: "+_reader.getFileContent("\\"+category+"\\"+selected_habit_name+"_totalDays.txt")+"\n Current streak: "+_reader.getFileContent("\\"+category+"\\"+selected_habit_name+"_streak.txt")+"\n Habit Current Duration: "+_reader.getFileContent("\\"+category+"\\"+selected_habit_name+"_duration.txt")+" minutes";

		// Storing data in the bundle which we'll pass to the HabitDataDialogFragment
		// class
		Bundle bundle_for_dialog=new Bundle();
		bundle_for_dialog.putString(DisplayHabitsByCategory.KEY_HABIT_DATA, data_to_display);
		
		// Making the DialogFragment that will display this data.
		HabitDataDialogFragment display_dialog=new HabitDataDialogFragment();
		display_dialog.setArguments(bundle_for_dialog);
	    FragmentManager fm=this.getSupportFragmentManager();
	    display_dialog.show(fm,"dialog");
	    
		
	}// end onListItemClick method
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}// end onCreateOptions function
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.create_new_reward_menu:
			Intent create_reward=new Intent(DisplayHabitsByCategory.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(DisplayHabitsByCategory.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(DisplayHabitsByCategory.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected
	
}// end class

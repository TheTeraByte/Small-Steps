package in.tusharbhargava.habitapp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class displays the daily check-list and stores ticks for a day.
 * It assigns scores, rewards and level ups. 
 * 
 * Note for version 2.0: Generalize categories (possibly through an interface) to
 * allow easy addition of categories (and thus save repeated code).
 * 
 * @author Tushar Bhargava
 *
 */

public class DailyChecklistActivity extends ListActivity
{
	
	// instance variables
	private ExtStorageWriterAndReader _writerReader;
	// list of encouragements that app draws on
	private final String[] immediate_praise={"Go get 'em tiger!","Killing it.","Growing strong, one step at a time!","You're awesome!"};
	// variable to store list of random rewards (for completing all items on list)
	private ArrayList<String> daily_rewards=new ArrayList<String>();
	// These string arrays store all the habit names sorted by category
	private String[] habits_academic;
	private String[] habits_cs;
	private String[] habits_financial;
	private String[] habits_health;
	private String[] habits_relationships;
	private String[] habits_writing;
	// Stores total no. of active habits
	private int total_no_of_habits=0;
	// This variable stores the total no. of checked items. If this number equals
	// the total no. of active habits (i.e. user has completely checked the list)
	// the user gets a random but level dependent reward
	private int total_no_of_checked_items=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setting the layout (we just need a blank template as this nothing
		// more than a ListView)
		this.setContentView(R.layout.activity_daily_habit_checklist);
		// Getting control of the ListView that is central to this page
		ListView checklist_view=this.getListView();
		
		// Initializing File IO
		_writerReader=new ExtStorageWriterAndReader();
		
		// Displaying all 'active' habits (from all categories)
		ArrayList<String> active_habits=new ArrayList<String>();
		
		// 1.) Getting list of habits under 'Academic' 
		String habits_under_academic_category=_writerReader.getFileContent("Academic"+"/habits_under_category");
		// Getting individual habits from list
		if(habits_under_academic_category!=null)
		{
		habits_academic=habits_under_academic_category.split("\r\n");
			// Adding the Academic habits to the main list of active habits
			for (String s:habits_academic)
			{
				active_habits.add(s);
				// Incrementing no. of active habits by one
				total_no_of_habits++;
			}// end for loop
		}// end if statement
		
		// 2. Getting habits under Computer Science (if there are any) 
		String habits_under_cs_category=_writerReader.getFileContent("Computer Science"+"/habits_under_category");
		
		if(habits_under_cs_category!=null)
		{
			habits_cs=habits_under_cs_category.split("\r\n");
			for(String s:habits_cs)
			{
				active_habits.add(s);
				total_no_of_habits++;
			}// end for loop
		}// end if statement
		
		// 3.) Getting habits under 'Financial' (if any)
		String habits_under_financial_s=_writerReader.getFileContent("Financial"+"/habits_under_category");
		
		if(habits_under_financial_s!=null)
		{
			habits_financial=habits_under_financial_s.split("\r\n");
			for(String s:habits_financial)
			{
				active_habits.add(s);
				total_no_of_habits++;
			}// end for loop
		}// end if statement
		
		// 4.) Getting habits under 'Health' (if any)
		String habits_under_health_s=_writerReader.getFileContent("Health"+"/habits_under_category");
		
		if(habits_under_health_s!=null)
		{
			habits_health=habits_under_health_s.split("\r\n");
			for(String s:habits_health)
			{
				active_habits.add(s);
				total_no_of_habits++;
			}// end for loop
		}// end if statement
		
		// 5.) Getting habits under 'Relationships' (if any)
		String habits_under_relationships_s=_writerReader.getFileContent("Relationships"+"/habits_under_category");
		
		if(habits_under_relationships_s!=null)
		{
			habits_relationships=habits_under_relationships_s.split("\r\n");
			for(String s:habits_relationships)
			{
				active_habits.add(s);
				total_no_of_habits++;
			}// end for loop
		}// end if statement
		
		// 6.) Getting habits under 'Writing' (if any)
		String habits_under_writing_s=_writerReader.getFileContent("Writing"+"/habits_under_category");
		
		if(habits_under_writing_s!=null)
		{
			habits_writing=habits_under_writing_s.split("\r\n");
			for(String s:habits_writing)
			{
				active_habits.add(s);
				total_no_of_habits++;
			}// end for loop
		}// end if statement
		
		// Checking to make sure there are habits to display
		if(active_habits.size()<=0)
		{
			Toast.makeText(getApplicationContext(), "No active habits! Add some.", Toast.LENGTH_SHORT).show();
			Intent returnToMain=new Intent(DailyChecklistActivity.this,MainActivity.class);
			startActivity(returnToMain);
			finish();
		}// end if statement
		
		// Setting up an adapter to show the habits
		ArrayAdapter<String> habits_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,active_habits);
		// Linking the adapter with the ListView
		checklist_view.setAdapter(habits_adapter);
		
		// This makes the ListView represent a check-list
		checklist_view.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
			
		// Code to read persisted ticks here
		// We read the checked_items file and break it into an array of strings
		// using the new line character
		
		String ticks_pos_string_whole=_writerReader.getFileContent("Misc/checked_items");
		String[] ticks_pos_string;
		
		// Making sure the persistence file exists
		if(ticks_pos_string_whole!=null)
		{
		// Converting string position array into int position
		ticks_pos_string=ticks_pos_string_whole.split("\n");
		int[] ticks_pos=new int[ticks_pos_string.length];
		
		for (int i=0;i<ticks_pos.length;i++)
		{
			// Making sure we don't write a null position
			if(ticks_pos_string[i].trim()!=null)
					{
			ticks_pos[i]=Integer.parseInt(ticks_pos_string[i].trim());
					}// end if statement
		}// end for loop
		
		// Retrieving current time and date from the device
		Calendar cal=Calendar.getInstance();
		int curr_date=cal.get(Calendar.DATE);
		
		// If the checklist needs to be updated to a new day
		String last_date;
		String checked_items_date_untrimmed=_writerReader.getFileContent("Misc/checked_items_date");
		// Trimming the string for comparison (if its not null that is)
		if(checked_items_date_untrimmed!=null)
		{
			last_date=checked_items_date_untrimmed.trim();
		}// end trim if statement
		
		// If it is null than we make it into a string with a single space
		else
		{
			last_date=" ";
		}
		
		if(last_date.compareTo(String.valueOf(curr_date-1))==0)
		{
			// Setting a new timestamp
			_writerReader.writeToFile("Misc/checked_items_date",String.valueOf(curr_date),false,getApplicationContext());
			
			// Call to function that updates all the habit data based on the checks
			// of the previous day
			updateHabitsData(ticks_pos,checklist_view);
			// Delete checklist position file (i.e. remove all previous ticks)
			File file=new File(Environment.getExternalStorageDirectory()+"/SmallStepsData/Misc/checked_items"+".txt");
			file.delete();
			
		}// end if statement
		
		// In case the last_date file is missing
		else if(last_date==" ")
		{
			// Setting a new timestamp
			_writerReader.writeToFile("Misc/checked_items_date",String.valueOf(curr_date),false,getApplicationContext());
			updateHabitsData(ticks_pos,checklist_view);
		}
		
		// If we're still on the old day
		else
		{
			// Checking all the items that were checked earlier (provided time-stamp still
			// reads current date)
			for(int pos:ticks_pos)
			{
				total_no_of_checked_items++;
				checklist_view.setItemChecked(pos, true);
			}// end for loop
			}// end outer if statement
				
		}// end else statement
		
	}// end method
	
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
			Intent create_reward=new Intent(DailyChecklistActivity.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(DailyChecklistActivity.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(DailyChecklistActivity.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected
	
	@Override
	public void onListItemClick(ListView parent,View V,int position,long id)
    {
		// If after the click item becomes checked then the following actions
		// take place.
			
		if(parent.isItemChecked(position))
		{
			// 1.) As item is checked, write its position to file (to be exact: append position)
			_writerReader.writeToFile("Misc/checked_items",Integer.toString(position)+"\n",true,getApplicationContext());
			
			// 2.) Also increment checked items count
			total_no_of_checked_items++;
						
			// 3.) Display congratulatory text (drawn from a randomized list of praises)
			Toast.makeText(getApplicationContext(), this.randomPraiseGenerator(), Toast.LENGTH_SHORT).show();
			
			// Checking for 'Occulus Tauri' condition i.e. if all items are checked
			if(total_no_of_checked_items==total_no_of_habits)
			{
				// Launch random daily reward activity! (also provide it with the
				// random reward to display.
				Intent reward_activity=new Intent(DailyChecklistActivity.this,DailyRewardActivity.class);
				// Default reward
				daily_rewards.add("Spend 30 minutes reading a good book.");
				// Reading other rewards from user created list
				this.updateDailyRewards();
				// We pass the random reward which we have taken from the file of rewards
				reward_activity.putExtra("reward_description", this.randomDailyRewardGenerator());
				startActivity(reward_activity);
			}
			
		}// end if statement
		
		// If user un-checks items
		else
		{
			// Decrementing no. of checked items position
			total_no_of_checked_items--;
			
			// Remove item's position from checked_items file
			
			// Reading all the positions from checked_items file
			String curr_checked_pos_whole=_writerReader.getFileContent("Misc/checked_items");
			String[] curr_checked_pos=curr_checked_pos_whole.split("\n");
			// This string array will store the new set of positions (i.e. positions
			// sans the current unchecked one)
			String[] updated_checked_items_pos=new String[curr_checked_pos.length-1];
			int index=0;
			for(String s:curr_checked_pos)
			{
				// As long as the position isn't the one that just got unchecked 
				// we can store it in the updated array
				if(Integer.parseInt(s.trim())!=position)
				{
					updated_checked_items_pos[index]=s;
					index++;
				}// end if statement
				
			}// end for-each loop
			
			StringBuilder final_checked_items_list=new StringBuilder();
			
			// Storing the updated items in a single string (with new line char)
			for(String s:updated_checked_items_pos)
			{
				final_checked_items_list.append(s+"\n");
			}// end for loop
			
			// We now overwrite the checked items file 
			_writerReader.writeToFile("Misc/checked_items",final_checked_items_list.toString(),false,getApplicationContext());
			
		}// end else statement
		
    }// end class
		
	
	/*---------------------------------Standard Functions----------------------*/
	
	// Fetches the latest daily reward list from ext. storage.
	public void updateDailyRewards()
	{
		String rewards_whole=_writerReader.getFileContent("Misc/daily_rewards");
		if(!rewards_whole.equals(null))
		{
			String[] rewards=rewards_whole.split("\n");
			for(String s:rewards)
			{
				daily_rewards.add(s);
			}// end inner for loop
		}// end if statement
	}// end file 
	
	// Writes the latest habit completion info to file.
	public void updateHabitsData(int[] positions,ListView parent)
	{
		if(positions!=null)
		{
			// Updating all of the items at position (counting them as 'checked')
			for(int pos:positions)
			{
				updateHabitDataHelperMethod(pos,parent);
			}// end for loop
		}// end outer if statement
	}// end method
	
	/**
	 * This function updates the data of the parameter -- the 'checked' habit. 
	 * @param position
	 * @param parent
	 */
	public void updateHabitDataHelperMethod(int position, ListView parent)
	{
		// 1.) Increasing the habits days and streak count (which later affects
		// the category score and hence overall avatar level.  
					
		// (But first we have to find the category the habit belongs to) 
		String checked_habit_name=parent.getItemAtPosition(position).toString();
		String category;
					
		// Checking which category habit is in (using an else-if ladder) [To-do]
		if(hasString(habits_academic,checked_habit_name))
		{
			category="Academic";
		}// end if statement
		
		// Note: Stange error in Eclipse spoiled indentation of following code.
		
					else if(hasString(habits_cs,checked_habit_name))
					{
						category="Computer Science";
					}// end if statement
					
					else if(hasString(habits_financial,checked_habit_name))
					{
						category="Financial";
					}// end else-if statement
					
					else if(hasString(habits_health,checked_habit_name))
					{
						category="Health";
					}// end else if statement
					
					else if(hasString(habits_relationships,checked_habit_name))
					{
						category="Relationships";
					}// end else if statement
					
					else if(hasString(habits_writing,checked_habit_name))
					{
						category="Writing";
					}// end else if statement
					
					// Default case that should never be triggered
					else
					{
						System.out.println("Error: The habit name did not match any category which should not happen!");
						category=" ";
					}// end else statement
					
					// Having found the correct habit category we can now update the relevant data
					String current_total_no_of_days_s=_writerReader.getFileContent(category+"/"+checked_habit_name+"_totalDays").trim();
					// Incrementing no. of days habit performed by one.	
					int updated_total_no_of_days=Integer.parseInt(current_total_no_of_days_s)+1;		
					// Re-writing existing days file with new day count
					_writerReader.writeToFile(category+"/"+checked_habit_name+"_totalDays",Integer.toString(updated_total_no_of_days),false,getApplicationContext());
					// Retrieving date of last time the habit was performed
					int last_habit_date=Integer.parseInt(_writerReader.getFileContent(category+"/"+checked_habit_name+"_streakDate").trim());
					Calendar c=Calendar.getInstance();
					int curr_date=c.get(Calendar.DATE);
					
					// We update the habit duration if it has been performed for
					// a certain no. of days
					int curr_duration=Integer.parseInt(_writerReader.getFileContent(category+"/"+checked_habit_name+"_duration").trim());
					int final_duration=Integer.parseInt(_writerReader.getFileContent(category+"/"+checked_habit_name+"_finalDuration").trim());
					
					// Automatically increasing current duration if its not equal to final duration
					if(final_duration>curr_duration)
					{
						
					// If you've performed the habit for 4 months you can jump to final duration	
					if(updated_total_no_of_days>=60)
					{
						curr_duration=final_duration;
					}
					
					else if(updated_total_no_of_days==42)
					{
						curr_duration+=5;
					}
					
					else if(updated_total_no_of_days==28)
					{
						curr_duration+=5;
					}// end else if statement
					
					else if(updated_total_no_of_days==14)
					{
						curr_duration+=5;
					}// end else if statement
					
					_writerReader.writeToFile(category+"/"+checked_habit_name+"_duration",Integer.toString(curr_duration),false,getApplicationContext());
					
					}// end outer if statement
					
					// Variable to store streak
					int updated_streak=0;
					
					// If last day habit was performed was day before (remember the curr. date is 1+
					// the date these habits performed)
					if((curr_date-2)==last_habit_date)
					{
					// Incrementing habit streak
					updated_streak+=Integer.parseInt(_writerReader.getFileContent(category+"/"+checked_habit_name+"_streak").trim())+1;
					// Writing updated habit streak to file (over-writing)
					_writerReader.writeToFile(category+"/"+checked_habit_name+"_streak",Integer.toString(updated_streak),false,getApplicationContext());
					// Writing new streak date to file
					_writerReader.writeToFile(category+"/"+checked_habit_name+"_streakDate",Integer.toString(curr_date-1),false,getApplicationContext());
					}// end if statement 
					
					else
					{
						// Resetting streak
						_writerReader.writeToFile(category+"/"+checked_habit_name+"_streak",Integer.toString(0),false,getApplicationContext());
						// Writing new date to streak habit date
						_writerReader.writeToFile(category+"/"+checked_habit_name+"_streakDate",Integer.toString(curr_date-1),false,getApplicationContext());
					}// end else statement
					
					// Updating habit category score (if it exists)
					String habit_category_curr_score_string=_writerReader.getFileContent(category+"/"+"category_score");
					int curr_score=0;
					if(habit_category_curr_score_string!=null)
					{
						curr_score+=Integer.parseInt(habit_category_curr_score_string.trim());
					}// end if statement
					
					// Adding 20 points for completing a habit
					curr_score+=20;	
					// Multiplying streak by 20 points to add to score
					curr_score+=(updated_streak*20);
					
					// Writing updated habit score to file
					_writerReader.writeToFile(category+"/"+"category_score",Integer.toString(curr_score),false,getApplicationContext());
					
	}// end function
	
	
	/**
	 * This function helps us search an entire category of habits to see whether 
	 * the habit exists in the category or not.
	 * @param array_to_search
	 * @param key
	 * @return True if habit is in this category. False otherwise
	 */
	public boolean hasString(String[] array_to_search,String key)
	{
		// We're searching every single habit in a given category to find
		// the key habit.
		// Runtime: O(n) but no. of habits of user will probably not be of a large 
		// magnitude.
		
		if((array_to_search==null) ||(key==null))
		{
			return false;
		}//end if statement
		
		for(String s:array_to_search)
		{
			if(s.equals(key))
			{
				// Our habit exists in this category of habits
				return true;
			}
		}// end for loop
		
		// Our habit does not exist in this category
		return false; 
	}// end function
	
	/**
	 * This function generates a random index (within range) 
	 * to return a new encouragement each time.
	 * @return String with immediate encouragement
	 */
	public String randomPraiseGenerator()
	{
		int no_of_praises=immediate_praise.length;
		// Generating a random index within range of array
		int randIndex=(int)(Math.random()*no_of_praises);
		return immediate_praise[randIndex];
	}// end function
	
	/**
	 * This function chooses a random reward from
	 * the user's list.
	 * @return String with description of the reward.
	 */
	public String randomDailyRewardGenerator()
	{
		int no_of_rewards=daily_rewards.size();
		int randIndex=(int)(Math.random()*no_of_rewards);
		return daily_rewards.get(randIndex);
	}// end function
		
}// end class

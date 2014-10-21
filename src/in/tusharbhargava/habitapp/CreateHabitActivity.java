package in.tusharbhargava.habitapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This activity provides a simple form to help the user create a new habit.
 * This is limited to one new habit per month.
 * To do: Destroy activity once data is collected.
 * @author tusharb1995
 *
 */

public class CreateHabitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_habit);
		
		// Making the 'Create Habit' button collect all data and store it (and return
		// to previous activity)
		final Button createHabitSubmit=(Button)findViewById(R.id.submit_habit_button);
		createHabitSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Saving all the data the user has provided us
				// Use category as the directory; habit name as file name
				EditText habitNameWidget=(EditText)findViewById(R.id.habit_name_text_field);
				EditText habitDescriptionWidget=(EditText)findViewById(R.id.habit_description_text_field);
				EditText habitTotalDays=(EditText)findViewById(R.id.total_days_habit_create);
				// Stores the final time the user wishes to commit to the habit
				// in the beginning each habit starts at 5 minutes. 5 minutes are added
				// for every 2 weeks till desired duration is met.
				EditText habitDesiredDuration=(EditText)findViewById(R.id.habit_desired_duration_textfield);
				Spinner habitCategoryWidget=(Spinner)findViewById(R.id.habit_category_picker);
				
				
				// Extracting data from the widgets and converting to string data type
				String habitName=habitNameWidget.getText().toString();
				String habitDescription=habitDescriptionWidget.getText().toString();
				String habitCategory=habitCategoryWidget.getSelectedItem().toString();
				int habit_total_days_int=Integer.parseInt(habitTotalDays.getText().toString().trim());
				int habit_final_duration_int=Integer.parseInt(habitDesiredDuration.getText().toString().trim());
				
				// Writing name of habit to 'habits' file stored in category
				// This file stores all habits in a given category, allowing us to
				// display them by category and calculate habit levels.
				writeToFile("/"+habitCategory+"/habits_under_category",habitName+"\r\n",true);
				
				// Writing habit description to file
				writeToFile("/"+habitCategory+"/"+habitName+"_description", habitDescription,false);
				
				int habit_initial_duration;
				
				// Writing habit beginning duration
				if(habit_final_duration_int<=5)
				{
					habit_initial_duration=habit_final_duration_int;
				}// end if statement
				
				else
				{
					// Each habit duration in the beginning is just 5 minutes (keeping
					// with the kaizen principles)
					habit_initial_duration=5;
				}// end if statement
				
				// Writing habit current duration and final duration to file
				writeToFile("/"+habitCategory+"/"+habitName+"_duration",Integer.toString(habit_initial_duration),false);
				writeToFile("/"+habitCategory+"/"+habitName+"_finalDuration",Integer.toString(habit_final_duration_int),false);
				
				// Writing no. of total days habit is performed 
				writeToFile("/"+habitCategory+"/"+habitName+"_totalDays",Integer.toString(habit_total_days_int),false);
				
				// Updating category score
				String curr_category_score_string=getFileContent("/"+habitCategory+"/category_score");
				int category_score=0;
				if(curr_category_score_string!=null)
				{
					category_score+=Integer.parseInt(curr_category_score_string.trim());
				}
				
				// 20 points for each previous day (not counting them as a streak)
				category_score+=(habit_total_days_int*20);
				
				// Writing the new score to file
				writeToFile("/"+habitCategory+"/category_score",Integer.toString(category_score),false);
				
				// Writing no. of days habit performed in row (i.e. streak)
				// Default value is 0
				writeToFile("/"+habitCategory+"/"+habitName+"_streak",Integer.toString(0),false);
				// Also writing the date the habit was performed (if its not equal to previous
				// day than streak gets broken) 
				Calendar c=Calendar.getInstance();
				int curr_date=c.get(Calendar.DATE);
				
				writeToFile("/"+habitCategory+"/"+habitName+"_streakDate",String.valueOf(curr_date),false);
						
				// Notifying user that the habit has been created and re-directing to main
				// activity
				Toast.makeText(getApplicationContext(), "Habit created. Go get 'em tiger!",Toast.LENGTH_SHORT).show();
				Intent back_to_main_page=new Intent(CreateHabitActivity.this,MainActivity.class);
				startActivity(back_to_main_page);
				finish();
			}
		});
	}// end onCreate function
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.create_new_reward_menu:
			Intent create_reward=new Intent(CreateHabitActivity.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(CreateHabitActivity.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(CreateHabitActivity.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected


	
	
	/*-------------------------------Standard Methods--------------------------- */
	
	/**
	 * Function that writes data to (external) Android storage.
	 * @param fileName
	 * @param content
	 */
	public void writeToFile(String fileName,String content,boolean appendable)
	{
		// Output stream to write content to storage
		FileOutputStream outputStream;
		
		if(isExternalStorageWritable())
		{
		try
		{
			File toWrite=new File(Environment.getExternalStorageDirectory()+"/SmallStepsData/"+fileName+".txt");
			// Creating the directories if they didn't already exist
			toWrite.getParentFile().mkdirs();
			// Making outputStream, setting append to true (mainly for habit name 
			// but this is inconvenient for all other files as it might lead to two
			// sets of data). Fix later. (To do)
			outputStream=new FileOutputStream(toWrite,appendable);
			outputStream.write(content.getBytes());
			outputStream.close();
		}// end try block
		
		catch (Exception e)
		{
			e.printStackTrace();
		}//end catch block
		}// end if statement
		
		// If external storage is not available
		else
		{
			// Informing the user of the problem
			Toast.makeText(getApplicationContext(), "Check external storage availability and try again", Toast.LENGTH_SHORT).show();
		}// end else statement 
		
	}// end writeToFile function
	

	/* Checks if external storage is available for read and write 
	 * Adapted from Android docs.
	 * */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	} // end isExternal StorageWritable
	
	
	/**
	 * This function returns file content as a string from external storage.
	 * @param The name of the file (which is same as the habit name with a post-fix)
	 * must be provided. 
	 * @return content of file
	 */
	public String getFileContent(String fileName) 
	{
		String temp;
		StringBuilder build_string=null;
		FileInputStream input;
		InputStreamReader input_stream_reader;
		BufferedReader buffered_reader;
		
		try
		{
		input=new FileInputStream(new File(Environment.getExternalStorageDirectory()+"/SmallStepsData/"+fileName+".txt"));
		input_stream_reader=new InputStreamReader(input);
		buffered_reader=new BufferedReader(input_stream_reader,8192);
		build_string=new StringBuilder();
		while((temp=buffered_reader.readLine())!=null)
		{
			build_string.append(temp+"\r\n");
		}// end while loop
				
		// Closing the streams
		buffered_reader.close();
		
		}// end try block
		
		catch (FileNotFoundException f)
		{	
			f.printStackTrace();
			// If file does not exist than we return null to calling method.
			return null;
		}	
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// Printing the content
		//System.out.println("Content of file (according to my reader): "+build_string.toString());
		
		return build_string.toString();
	}// end method

	
	/**
	 * This function helps us retrieve the Date from the DatePicker widget. 
	 * Adapted from StackOverflow: http://stackoverflow.com/questions/8409043/getdate-from-datepicker-android 
	 * @param datePicker
	 * @return
	 */
	public static Date getDateFromDatePicker(DatePicker datePicker){
	    int day = datePicker.getDayOfMonth();
	    int month = datePicker.getMonth();
	    int year =  datePicker.getYear();

	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month, day);

	    return calendar.getTime();
	}// end getDateFromDatePicker function 
	
	/**
	 * Gets the date part in the Date object (i.e. doesn't care about hours, minutes etc.
	 * Adapted from SO: http://stackoverflow.com/a/227049/398715
	 * @param date
	 * @return
	 */
	public static Calendar getDatePart(Date date){
	    Calendar cal = Calendar.getInstance();       // get calendar instance
	    cal.setTime(date);      
	    cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
	    cal.set(Calendar.MINUTE, 0);                 // set minute in hour
	    cal.set(Calendar.SECOND, 0);                 // set second in minute
	    cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

	    return cal;                                  // return the date part
	}//end getDatePart function
	
	/**
	 * Function to calculate (approximately) no. of days between two days.
	 * Adapted from SO: http://stackoverflow.com/questions/3838527/android-java-date-difference-in-days
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long daysBetween(Date startDate, Date endDate) {
	  Calendar sDate = getDatePart(startDate);
	  Calendar eDate = getDatePart(endDate);

	  long daysBetween = 0;
	  while (sDate.before(eDate)) {
	      sDate.add(Calendar.DAY_OF_MONTH, 1);
	      daysBetween++;
	  }
	  return daysBetween;
	}// end daysBetween function 
	
	
}// end Activity

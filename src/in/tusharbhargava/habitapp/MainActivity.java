package in.tusharbhargava.habitapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * To Do list:
 * 1.) Make a file the 'category' folder to store the names of all habits listed under
 * it
 * 2.) Only allow new habit activity to start if current_habit_count is more than 30.
 * Reset current_habit_count each time new habit starts, i.e. ensure user takes only
 * one step at a time (optional)
 * 3.) In 'Create Habit' activity once button is pressed, show toast for confirmation
 * and start intent to go to main menu once again!
 * 
 * Current: Make check-list and link it with score. Make reward text file (interface+
 * display activity)
 * 
 * Debug: For some reason checklist is going out of memory/not working the second time
 * around.
 * Debug: When months are changing the streak functionality is going to go for a 
 * toss (as the I'm subtracting 1 from an integer it will go to 0 not 31 urggghh)
 * 
 * @author tusharb1995
 *
 */

public class MainActivity extends Activity {
	
	// global variables
	private ViewPager _mPager;
	private CategoryImageAdapter _mImageAdapter;
	// Resource links to habit icon images
	private String extStorageDirectory = Environment.getExternalStorageDirectory().toString()+"/SmallStepsData/Images/";
	private String[] _resources={extStorageDirectory+"tux_graduate.png",extStorageDirectory+"computer_purple.png",extStorageDirectory+"pig_bank_green.png",extStorageDirectory+"apple.png",extStorageDirectory+"abstract_people.png",extStorageDirectory+"paper_pen.png"};
	// Names of the categories to pass to the adapter to display with the appropriate
	// image
	private String[] _categories={"Academic","Computer Science","Financial","Health","Relationships","Writing"};
	// Level upgrade score (i.e. no. of XP points required per level)
	public static final int xp_to_next_level=3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Writing the habit images to the SD card so we can access them from there
		// if they don't already exist
		if(getFileContent("Images/images_loaded")==null)
		{
			System.out.println("Running memory intensive one-time (hopefully) image writer!");
			// Writing a file to the Images directory that serves as a boolean to let
			// us know the next time that we don't need to write the files
			// again 
			writeToFile("Images/images_loaded","true",false);
			
			// Loading the images as bitmaps 
			Bitmap tux=BitmapFactory.decodeResource(getResources(), R.drawable.tux_graduate);
			Bitmap computer=BitmapFactory.decodeResource(getResources(), R.drawable.computer_purple);
			Bitmap pig_bank=BitmapFactory.decodeResource(getResources(), R.drawable.pig_bank_green);
			Bitmap apple=BitmapFactory.decodeResource(getResources(), R.drawable.apple);
			Bitmap abstract_people=BitmapFactory.decodeResource(getResources(), R.drawable.abstract_people);
			Bitmap paper_pen=BitmapFactory.decodeResource(getResources(), R.drawable.paper_pen);
			
			
			File tux_file=new File(extStorageDirectory,"tux_graduate.PNG");
			File computer_file=new File(extStorageDirectory,"computer_purple.PNG");
			File pig_bank_file=new File(extStorageDirectory,"pig_bank_green.PNG");
			File apple_file=new File(extStorageDirectory,"apple.PNG");
			File abstract_people_file=new File(extStorageDirectory,"abstract_people.PNG");
			File paper_pen_file=new File(extStorageDirectory,"paper_pen.PNG");
			FileOutputStream outstream;
			try
			{
				outstream=new FileOutputStream(tux_file);
				tux.compress(Bitmap.CompressFormat.PNG, 50, outstream);
				outstream.flush();
				outstream.close();
				outstream=new FileOutputStream(computer_file);
				computer.compress(Bitmap.CompressFormat.PNG,50, outstream);
				outstream.flush();
				outstream.close();
				outstream=new FileOutputStream(pig_bank_file);
				pig_bank.compress(Bitmap.CompressFormat.PNG, 50, outstream);
				outstream.flush();
				outstream.close();
				outstream=new FileOutputStream(apple_file);
				apple.compress(Bitmap.CompressFormat.PNG, 50, outstream);
				outstream.flush();
				outstream.close();
				outstream=new FileOutputStream(abstract_people_file);
				abstract_people.compress(Bitmap.CompressFormat.PNG, 50, outstream);
				outstream.flush();
				outstream.close();
				outstream=new FileOutputStream(paper_pen_file);
				paper_pen.compress(Bitmap.CompressFormat.PNG, 50, outstream);
				outstream.flush();
				outstream.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}// end catch block 
			
		}// end if statement
		
		
		// Setting the image adapter for habit icons
		_mPager=(ViewPager)findViewById(R.id.pager);
		// Passing the activity and image paths to 
		_mImageAdapter=new CategoryImageAdapter(this,_resources,_categories);
		_mPager.setAdapter(_mImageAdapter);
		// Defining the actions of the buttons
		
		// 1.) New habit button should open 'Create new habit' activity
		final Button button=(Button)findViewById(R.id.new_habit_main_button);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Calls the 'create new habit' activity
				Intent createNewHabit=new Intent(MainActivity.this,CreateHabitActivity.class);
				startActivity(createNewHabit);
			}
		});
		
		// 2.) See habits in category button
		final Button button2=(Button)findViewById(R.id.see_habits_in_category_button);
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// This calls the 'DisplayHabitsByCategory' activity
				Intent displayHabitByCategory=new Intent();
				displayHabitByCategory.setClass(MainActivity.this,DisplayHabitsByCategory.class);
				int current_index=_mPager.getCurrentItem();
				String category=_categories[current_index];
				// Passing the current 'category' to the DisplayHabitsByCategory function
				displayHabitByCategory.putExtra("category", category);
				startActivity(displayHabitByCategory);
			}
		});
		
		// 3.) Daily Checklist button
		final Button button3=(Button)findViewById(R.id.daily_checklist_main_button);
		button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Calls the DailyChecklistActivity 
				Intent dailyHabitChecklist=new Intent(MainActivity.this,DailyChecklistActivity.class);
				startActivity(dailyHabitChecklist);
			}
		});
		
	}// end onCreate function

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}// end onCreateOptionsMenu
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.create_new_reward_menu:
			Intent create_reward=new Intent(MainActivity.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(MainActivity.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(MainActivity.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected

	/*-------------------------Standard Methods-------------------------------- */
	
	
	public void updateProgressBar()
	{
		// Finding current habit category
		String category=_categories[_mPager.getCurrentItem()];
		// Gaining control of the Progress Bar (and setting max. value)
		ProgressBar progress_bar=(ProgressBar)findViewById(R.id.habit_category_level_main_bar);
		progress_bar.setMax(MainActivity.xp_to_next_level);
		// Gaining control of labels (TextViews)
		TextView current_score_label=(TextView)findViewById(R.id.current_progress_label_textview);
		TextView current_level_label=(TextView)findViewById(R.id.current_level_label_textview);
		// Getting score of given habit category (if any) 
		String habit_score_string=getFileContent(category+"/category_score");
		
		int habit_score=0;
		
		if(habit_score_string!=null)
		{
			habit_score+=Integer.parseInt(habit_score_string.trim());
		}// end if statement
		
		// Updating the progress bar with that level no. 
		progress_bar.setProgress(habit_score%MainActivity.xp_to_next_level);
		// Setting current XP label for progress_bar
		current_score_label.setText(Integer.toString(habit_score%MainActivity.xp_to_next_level)+" XP");
		// Setting current level label for progress bar
		current_level_label.setText("Lvl "+Integer.toString(habit_score/MainActivity.xp_to_next_level));
		
	}// end method
	
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
			// User can choose whether to append to file or not
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
	

	
}

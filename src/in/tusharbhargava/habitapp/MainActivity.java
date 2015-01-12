package in.tusharbhargava.habitapp;

import java.io.File;
import java.io.FileOutputStream;

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

/**
 * This is the main class of the app that has code to display the home page (activity).
 * The home-page consists of several images and a linked progress bar, and is one of the
 * more complicated parts of the program, hence this class has significant no. of lines
 * of code.
 * 
 * @author Tushar Bhargava
 */

public class MainActivity extends Activity {
	
	// instance variables
	private ExtStorageWriterAndReader _writerReader;
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
		
		// Initializing File IO
		_writerReader=new ExtStorageWriterAndReader();
		
		// Writing the habit images to the SD card so we can access them from there,
		// if they don't already exist.
		if(_writerReader.getFileContent("Images/images_loaded")==null)
		{
			System.out.println("Running memory intensive one-time (hopefully) image writer!");
			// Writing a file to the Images directory that serves as a boolean to let
			// us know the next time that we don't need to write the files
			// again 
			_writerReader.writeToFile("Images/images_loaded","true",false,getApplicationContext());
			
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

	// Provides the in-app menu.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}// end onCreateOptionsMenu
	
	// Defines the actions performed by choosing items from the in-app menu.
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
	
	/**
	 * Displays stored progress data visually, in the form of XP points and level.
	 */
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
		String habit_score_string=_writerReader.getFileContent(category+"/category_score");
		
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
}

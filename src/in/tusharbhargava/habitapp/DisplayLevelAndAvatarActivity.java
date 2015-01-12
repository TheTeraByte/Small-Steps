package in.tusharbhargava.habitapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Displays the user's current avatar and level. These avatars
 * are representative of the user's progress and are dinosaur themed.
 * 
 * @author Tushar Bhargava
 */

public class DisplayLevelAndAvatarActivity extends Activity
{
	// instance variables
	private ExtStorageWriterAndReader _reader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_and_avatar_display_activity);
		
		// Initializing file reader
		_reader=new ExtStorageWriterAndReader();
		
		// Getting control of the TextView and the ImageView
		TextView text_view=(TextView)findViewById(R.id.level_display_text);
		ImageView image_view=(ImageView)findViewById(R.id.avatar_display_image_view);
		
		// Getting all the habits levels (or 0 if they don't exist)
		int academic_habit_level=0;
		int computer_science_habit_level=0;
		int financial_habit_level=0;
		int health_habit_level=0;
		int relationships_habit_level=0;
		int writing_habit_level=0;
		
		String academic_habit_level_s=_reader.getFileContent("Academic/category_score");
		if(academic_habit_level_s!=null)
		{
			academic_habit_level_s=academic_habit_level_s.trim();
			academic_habit_level=Integer.parseInt(academic_habit_level_s);
		}// end if statement
		
		String computer_science_habit_level_s=_reader.getFileContent("Computer Science/category_score");
		if(computer_science_habit_level_s!=null)
		{
			computer_science_habit_level=Integer.parseInt(computer_science_habit_level_s.trim());
		}// end if statement
		
		String financial_habit_level_s=_reader.getFileContent("Financial/category_score");
		if(financial_habit_level_s!=null)
		{
			financial_habit_level=Integer.parseInt(financial_habit_level_s.trim());
		}// end if statement
		
		String health_habit_level_s=_reader.getFileContent("Health/category_score");
		if(health_habit_level_s!=null)
		{
			health_habit_level=Integer.parseInt(health_habit_level_s.trim());
		}// end if statement
		
		String relationships_habit_level_s=_reader.getFileContent("Relationships/category_score");
		if(relationships_habit_level_s!=null)
		{
			relationships_habit_level=Integer.parseInt(relationships_habit_level_s.trim());
		}// end if statement
		
		String writing_habit_level_s=_reader.getFileContent("Writing/category_score");
		if(writing_habit_level_s!=null)
		{
			writing_habit_level=Integer.parseInt(writing_habit_level_s.trim());
		}// end if statement
		
		// Computing overall character level 
		int overall_habit_level=(academic_habit_level+computer_science_habit_level+financial_habit_level+health_habit_level+relationships_habit_level+writing_habit_level)/MainActivity.xp_to_next_level;
		String habit_level_name=" ";
		// Using overall level to determine avatar (and associated level name)
		
		if(overall_habit_level<=6)
		{
			image_view.setImageResource(R.drawable.eggs_level0to6);
			habit_level_name="Just an egg";
		}// end if statement
		
		else if(overall_habit_level<=12)
		{
			image_view.setImageResource(R.drawable.hatchling_level6to12);
			habit_level_name="Horrible Hatchling";
		}// end else if statement
		
		else if(overall_habit_level<=30)
		{
			image_view.setImageResource(R.drawable.teenage_rexlevel12to30);
			habit_level_name="Teenage Rex";
		}// end else if statement
		
		else if(overall_habit_level<=54)
		{
			image_view.setImageResource(R.drawable.propellor_petro_level30to54);
			habit_level_name="Propellor Pedro";
		}// end else if statement
		
		else
		{
			image_view.setImageResource(R.drawable.triumphant_trex_highestlevel);
			habit_level_name="Triumphant T-Rex";
		}// end else statement
		
		// Setting textview to overall level
		text_view.setText(habit_level_name+" [Lvl "+Integer.toString(overall_habit_level)+"]");
	}// end onCreate method
	
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
			Intent create_reward=new Intent(DisplayLevelAndAvatarActivity.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(DisplayLevelAndAvatarActivity.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(DisplayLevelAndAvatarActivity.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected
}// end class

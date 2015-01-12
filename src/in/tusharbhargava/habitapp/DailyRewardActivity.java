package in.tusharbhargava.habitapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * A simple activity that displays the user's reward for habit completion.
 * 
 * @author Tushar Bhargava
 */

public class DailyRewardActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_reward);
		
		// Getting the intent that started this activity
		Intent starter=getIntent();
		
		// Setting the reward description to the one passed by the previous activity
		String reward_description=starter.getStringExtra("reward_description");
		// Gaining control of appropriate textview
		TextView reward_des_textview=(TextView)findViewById(R.id.daily_reward_description_text);
		reward_des_textview.setText("Reward details: "+reward_description);
		
	}// end onCreate method
	
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
			Intent create_reward=new Intent(DailyRewardActivity.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(DailyRewardActivity.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(DailyRewardActivity.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected

}// end class

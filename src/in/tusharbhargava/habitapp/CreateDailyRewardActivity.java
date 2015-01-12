package in.tusharbhargava.habitapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This activity allows users to add an item to their list of rewards.
 * @author Tushar Bhargava
 *
 */

public class CreateDailyRewardActivity extends Activity
{
	// instance variables
	private ExtStorageWriterAndReader _writer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_daily_reward);
		
		// Initializing the file writer
		_writer=new ExtStorageWriterAndReader();
		
		// Gaining control of the widgets in the layout
		Button add_to_list=(Button)findViewById(R.id.add_reward_to_list_button);
		final EditText reward_description_widget=(EditText)findViewById(R.id.enter_reward_details_textfield);
		
		// Setting action of button
		add_to_list.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String reward_description_text=reward_description_widget.getText().toString()+"\n";
				// Write reward description to appropriate file
				_writer.writeToFile("Misc/daily_rewards",reward_description_text,true,getApplicationContext());
				Toast.makeText(getApplicationContext(), "The reward was added to the list.",Toast.LENGTH_SHORT).show();
				// Returning to main activity
				Intent main_activity=new Intent(CreateDailyRewardActivity.this,MainActivity.class);
				startActivity(main_activity);
				finish();
			}
		});
		
	}// end onCreate method
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}// end onCreateOptions method

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.create_new_reward_menu:
			Intent create_reward=new Intent(CreateDailyRewardActivity.this,CreateDailyRewardActivity.class);
			startActivity(create_reward);
			break;
			
		
		case R.id.see_level_and_avatar_menu:
			Intent see_avatar=new Intent(CreateDailyRewardActivity.this,DisplayLevelAndAvatarActivity.class);
			startActivity(see_avatar);
			break;
			
		case R.id.about_the_app_menu:
			Intent about_the_app_intent=new Intent(CreateDailyRewardActivity.this,AboutTheAppActivity.class);
			startActivity(about_the_app_intent);
			break;
			
		// default case that does nothing	
		default:
	        return super.onOptionsItemSelected(item);
	        
		}// end switch statement
		return true;
	}// end onOptionsItemSelected

}// end class

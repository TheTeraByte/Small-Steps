package in.tusharbhargava.habitapp;

import java.util.Calendar;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/**
 * This service helps remind the user to check items off his checklist before the 
 * day ends.
 * @author tusharb1995
 *
 */

public class DailyChecklistReminderService extends IntentService
{

	// Constructor that defines name of service
	public DailyChecklistReminderService()
	{
		super("Daily checklist reminder service");
	}// end constructor
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		Calendar calendar=Calendar.getInstance();
		// This current hour is returned in 24 hour clock format
		int current_hour=calendar.get(Calendar.HOUR_OF_DAY);
		
		// A loop that lasts till the time is 9 PM
		while(current_hour!=21)
		{
			calendar=Calendar.getInstance();
			current_hour=calendar.get(Calendar.HOUR_OF_DAY);
		}// end while loop
		
		// Reminder to check items off list (displayed via the status bar)
		// Code adapted from: http://developer.android.com/guide/topics/ui/notifiers/notifications.html
		
		NotificationCompat.Builder mBuilder=new NotificationCompat.Builder(this).setSmallIcon(R.drawable.paper_notes).setContentTitle("Daily checklist reminder").setContentText("Click to tick items in checklist.");
		// Intent to start when notification clicked
		Intent resultIntent=new Intent(this,DailyChecklistActivity.class);
		// Creating a back stack that allows user to press back to go to homescreen
		TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
		stackBuilder.addParentStack(DailyChecklistActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		// Notification ID no: 1
		mNotificationManager.notify(1, mBuilder.build());
		
		// Service stops itself
		this.stopSelf();
		
	}// end onHandleIntent method
	
}// end class

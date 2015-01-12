package in.tusharbhargava.habitapp;

import java.io.File;
import java.io.FileInputStream;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This class helps load images into the swipe based selector
 * on the home screen of the app. In order to prevent the memory
 * from running it out, it decodes images at a slightly lower quality.
 * @author Tushar Bhargava
 *
 */

public class CategoryImageAdapter extends PagerAdapter
{
	private Activity _activity;
	private String[] _resources;
	private String[] _categoryNames;
	private LayoutInflater _inflater;
	private final int IMAGE_MAX_SIZE=300; 
	
	// constructor
	public CategoryImageAdapter(Activity activity, String[] resources, String[] categoryNames)
	{
		this._activity=activity;
		this._resources=resources;
		this._categoryNames=categoryNames;
	}// end constructor
	
	 @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((RelativeLayout) object);
	    }
	
	@Override 
	public int getCount()
	{
		return this._resources.length;
	}// end getCount method
	
	/**
	 * This method helps update the progress bar each time the user swipes
	 * to a new category.
	 */
	@Override
	public 	void startUpdate(ViewGroup container)
	{
		super.startUpdate(container);
		MainActivity obj=(MainActivity)_activity; 
		obj.updateProgressBar();
		
	}// end method
	
	/**
	 * This method helps instantiate each image in the list.
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		ImageView imgDisplay;
		TextView textDisplay;
		// Inflater that takes care of layout
		_inflater=(LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view_layout=_inflater.inflate(R.layout.habit_image_display_layout, container,false);
		imgDisplay=(ImageView)view_layout.findViewById(R.id.habit_icon_main_display);
		// Setting the text label to the correct category
		textDisplay=(TextView)view_layout.findViewById(R.id.habit_category_main_display);
		textDisplay.setText(_categoryNames[position]);
		    
		// Setting the new image to the ImageView
		Bitmap bitmap;
		try{
		bitmap=decodeFile(new File(_resources[position]));
		}
		catch(Exception e)
		{
			bitmap=null;
			e.printStackTrace();
		}
		if(bitmap!=null)
		{
        imgDisplay.setImageBitmap(bitmap);
		}
        // Adding the new view with the correct image to the container
        ((ViewPager) container).addView(view_layout);
        return view_layout;
	}// end method
	
	 @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        ((ViewPager) container).removeView((RelativeLayout) object);
	  
	    }
	
	 /*--------------------------------Standard methods-----------------------------*/
	 
	 /** 
	  * This is a method adapted from SO to avoid the bitmaps from causing an 
	  * outOfMemory exception. 
	  * @param file to decode
	  * @return decoded bitmap file
	  * @throws Exception
	  */
	 private Bitmap decodeFile(File f) throws Exception{
		    Bitmap b = null;

		        //Decode image size
		    BitmapFactory.Options o = new BitmapFactory.Options();
		    o.inJustDecodeBounds = true;

		    FileInputStream fis = new FileInputStream(f);
		    BitmapFactory.decodeStream(fis, null, o);
		    fis.close();

		    int scale = 1;
		    if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
		        scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE / 
		           (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
		    }

		    //Decode with inSampleSize
		    BitmapFactory.Options o2 = new BitmapFactory.Options();
		    o2.inSampleSize = scale;
		    fis = new FileInputStream(f);
		    b = BitmapFactory.decodeStream(fis, null, o2);
		    fis.close();

		    return b;
		}
	 
}// end class

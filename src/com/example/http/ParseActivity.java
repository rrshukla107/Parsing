package com.example.http;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ParseActivity extends Activity {
	ImageView poster;
	TextView name,description,year;
	JSONObject obj;
	
	//------------------------------------------------------------------------------
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	   /* public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }*/

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        poster.setImageBitmap(result);
	    }
	}
	//------------------------------------------------------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.parseactivity);
		poster = (ImageView)findViewById(R.id.imageViewPoster);
		name = (TextView)findViewById(R.id.textviewtitle);
		description = (TextView)findViewById(R.id.textViewInfo);
		year =(TextView)findViewById(R.id.textViewYear);
		
		
		Intent intent = getIntent();
		String rawData = intent.getStringExtra("data"); 
		super.onCreate(savedInstanceState);
		
		try{
			obj = new JSONObject(rawData);
		}catch(JSONException e){
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		
		name.setText(obj.optString("Title"));
		year.setText(obj.optString("Year"));
		description.setText(obj.optString("Plot"));
		
		
		//################################################################################
		//Dealing with image in json
		//################################################################################
		String imgurl = obj.optString("Poster").toString(); 
		
		new DownloadImageTask().execute(imgurl);
		
	}

}

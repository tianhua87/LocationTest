package com.example.locationtest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RequestThread extends Thread{
	
	private Location location;
	private Handler handler;
	
	RequestThread(Location location,Handler handler)
	{
		this.location=location;
		this.handler=handler;
	}
	
	public void run()
	{
		try {
		
			double latitude=location.getLatitude();
			double longtitude=location.getLongitude();
			Log.d("RequestThread", "dsda");
			StringBuilder url=new StringBuilder();
			Log.d("RequestThread", latitude+" "+longtitude);
			url.append("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
			url.append(latitude+",");
			url.append(longtitude);
			url.append("&sensor=false");
			
			HttpClient httpclient=new DefaultHttpClient();
			HttpGet httpget=new HttpGet(url.toString());

			httpget.addHeader("Accept-Language","zh-CN");
			Log.d("RequestThread", "exeBefore");
			HttpResponse response=httpclient.execute(httpget);
			Log.d("RequestThread", "exeAfter");
			if(response.getStatusLine().getStatusCode()==200)
			{
				HttpEntity httpentity=response.getEntity();
				Log.d("RequestThread", "200");
				String res=EntityUtils.toString(httpentity, "utf-8");
				JSONObject jsonobject=new JSONObject(res);
				
				JSONArray jsonarr=jsonobject.getJSONArray("results");
				if(jsonarr.length()>0)
				{
					JSONObject subObject=jsonarr.getJSONObject(0);
					
					String address=subObject.getString("formatted_address");
					
					Message message=new Message();
					message.what=MainActivity.SHOW_LOCATION;
					message.obj=address;
					handler.sendMessage(message);
					Log.d("RequestThread", address);
				}
			}
			else
				Log.d("RequestThread", response.getStatusLine().getStatusCode()+"");
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
	}

}

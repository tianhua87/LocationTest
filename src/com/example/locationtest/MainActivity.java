package com.example.locationtest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int SHOW_LOCATION=1; 
	
	private TextView position;
	private LocationManager lm=null;
	private  String provider=null;
	private Handler handler=new Handler(){

		public void handleMessage(Message msg) {
			switch(msg.what)
			{
			case SHOW_LOCATION:
				showLocation((String) msg.obj);
				break;
			}
		}
		
	};
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        position=(TextView)findViewById(R.id.positionView);
        
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
       
        
        
        List<String> providerlist=lm.getProviders(true);
        if(providerlist.contains(LocationManager.NETWORK_PROVIDER))
        	provider=LocationManager.NETWORK_PROVIDER;
        else if(providerlist.contains(LocationManager.GPS_PROVIDER))
        	provider=LocationManager.GPS_PROVIDER;
        else
        {
        	Toast.makeText(this, "没有位置提供器", Toast.LENGTH_SHORT).show();
        	return ;
        }
     //   lm.setTestProviderEnabled("gps",true);
        Location location=lm.getLastKnownLocation(provider);
        
        if(location!=null)
        {
        	new RequestThread(location, handler).start();
        }
        else
        	Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();
        
        lm.requestLocationUpdates(provider, 5000, 10, locationlistner);
        
    }
    
    

    @Override
	protected void onDestroy() {
		super.onDestroy();
		//关闭程序时将监听器移除
		if(lm!=null)
			lm.removeUpdates(locationlistner);
	//	lm.setTestProviderEnabled(provider, false);
	}



	LocationListener locationlistner=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
			
			
		}
		
		@Override
		public void onProviderEnabled(String arg0) {
			
			
		}
		
		@Override
		public void onProviderDisabled(String arg0) {
			
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			
			new RequestThread(location, handler).start();
			
		}
	};
   
    private void showLocation(String address)
    {
 	
    	position.setText(address);
    }
	
}

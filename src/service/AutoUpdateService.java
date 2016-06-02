package service;

import receiver.AutoUpdateReceiver;
import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utils;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
				
			}
		}).start();
		AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
		int anHour=8*60*60*1000;
		long triggerAtTime=SystemClock.elapsedRealtime()+anHour;
		Intent intent2 =new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, intent2, 0);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	
	}
	
	
	/**
	 * 更新天气信息s
	 */
	private void updateWeather(){
		SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
		String countyCode=preferences.getString("countyCode", "");
		String address="http://weather.123.duba.net/static/weather_info/"+countyCode+".html";
		
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(response)){
					//处理从服务器返回的数据
					Utils.HandleWeatherResponse(AutoUpdateService.this, response);
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
		
		
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}

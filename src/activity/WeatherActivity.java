package activity;




import com.example.qzjweather.R;

import service.AutoUpdateService;
import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class WeatherActivity extends Activity implements OnClickListener {
	
	private TextView cityNameText;
	private TextView currentDateText;
	private TextView weatherDespText;
	private TextView windDespText;
	private TextView tempText;

	private TextView pmText;
	private TextView publishTimeText;
	
	private Button button1;
	private Button button2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		
		cityNameText=(TextView)findViewById(R.id.city_name);
		currentDateText=(TextView)findViewById(R.id.current_date);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		windDespText=(TextView)findViewById(R.id.wind_desp);
		tempText=(TextView)findViewById(R.id.temp);
		
		pmText=(TextView)findViewById(R.id.pm2_5);
		publishTimeText=(TextView)findViewById(R.id.publish_time);
		
		button1=(Button)findViewById(R.id.select_city);
		button2=(Button)findViewById(R.id.update_weather);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		
		
		
		
		
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//有intent从choosearea活动传来，查询county天气
			publishTimeText.setText("正在更新。。。");
			
			queryWeatherCode(countyCode);
			
		}else{
			//没有就直接显示本地天气
			showWeather();
		}
		
		
	}
	
	
	/**查询县级代号的天气 
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode){
		String address="http://weather.123.duba.net/static/weather_info/"+countyCode+".html";
		queryFromServer(address);
		
	}
	
	/**根据地址去向服务器查询天气信息 从子线程
	 * @param address
	 */
	private void queryFromServer(final String address){
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
					if(!TextUtils.isEmpty(response)){
						//处理从服务器返回的数据
						Utils.HandleWeatherResponse(WeatherActivity.this, response);
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								showWeather();
							}
						});
					}
					
					
				
				
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishTimeText.setText("更新失败");
					}
				});
			}
		});
	}
	
	
	
	
	/**
	 * 
	 */
	private void showWeather(){
		SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("countyName", ""));
		currentDateText.setText(prefs.getString("date", ""));
		weatherDespText.setText(prefs.getString("weatherDesp", ""));
		windDespText.setText(prefs.getString("windDesp", ""));
		tempText.setText(prefs.getString("temp", ""));
		pmText.setText(prefs.getString("pm", ""));
		publishTimeText.setText(prefs.getString("update_time", ""));
		
		
		
		
		
		
		
		
		
		

		/*一旦更新某个天气后 开启后台更新 ss
		 * */
		Intent intent=new Intent(WeatherActivity.this, AutoUpdateService.class);
		startService(intent);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.select_city:
			Intent intent=new Intent(WeatherActivity.this, ChooseAreaActiviity.class);
			intent.putExtra("from_wather_activity", true);
			startActivity(intent);
			finish();
			break;

		case R.id.update_weather:
			String countyCode=getIntent().getStringExtra("county_code");
			queryWeatherCode(countyCode);
			break;
		}
	}




}

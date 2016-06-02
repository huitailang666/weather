package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import activity.WeatherActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utils {
	
	
	
	
	
	/**解析服务器返回的json数据并存入本地shareprefences
	 * @param context
	 * @param response
	 */
	public static void HandleWeatherResponse(Context context,String response){
		if(response.contains("weather_callback(")){
			response=response.substring(response.indexOf("(")+1, response.indexOf(")"));
			try {
				JSONObject jsonObject=new JSONObject(response);
				String update_time=jsonObject.getString("update_time");
				JSONObject weatherinfo=jsonObject.getJSONObject("weatherinfo");
				String countyName=weatherinfo.getString("city");
				String countyCode=weatherinfo.getString("cityid");
				String weatherDesp=weatherinfo.getString("weather1");
				String windDesp=weatherinfo.getString("wind1");
				String temp=weatherinfo.getString("temp1");
				String date=weatherinfo.getString("date_y");
				String pm=weatherinfo.getString("pm")+"~"+weatherinfo.getString("pm-level")+"~"+weatherinfo.getString("pm-pubtime");
				String currenttemp=weatherinfo.getString("temp");
				//第2天
				String weatherDesp2=weatherinfo.getString("weather2");
				String windDesp2=weatherinfo.getString("wind2");
				String temp2=weatherinfo.getString("temp2");
				//第3天
				String weatherDesp3=weatherinfo.getString("weather3");
				String windDesp3=weatherinfo.getString("wind3");
				String temp3=weatherinfo.getString("temp3");
				//第4天
				String weatherDesp4=weatherinfo.getString("weather4");
				String windDesp4=weatherinfo.getString("wind4");
				String temp4=weatherinfo.getString("temp4");
				//第5天
				String weatherDesp5=weatherinfo.getString("weather5");
				String windDesp5=weatherinfo.getString("wind5");
				String temp5=weatherinfo.getString("temp5");
				//第6天
				String weatherDesp6=weatherinfo.getString("weather6");
				String windDesp6=weatherinfo.getString("wind6");
				String temp6=weatherinfo.getString("temp6");
				
				
				
				saveWeatherInfo(context, update_time, countyName, countyCode,
						weatherDesp, windDesp, date, pm, temp, weatherDesp2,
						windDesp2, temp2, weatherDesp3, windDesp3, temp3,
						weatherDesp4, windDesp4, temp4, weatherDesp5,
						windDesp5, temp5, weatherDesp6, windDesp6, temp6);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void saveWeatherInfo(Context context, String update_time,
			String countyName, String countyCode, String weatherDesp,
			String windDesp, String date, String pm,String temp,String weatherDesp2,
			String windDesp2,String temp2,String weatherDesp3,
			String windDesp3,String temp3,String weatherDesp4,
			String windDesp4,String temp4,String weatherDesp5,
			String windDesp5,String temp5,String weatherDesp6,
			String windDesp6,String temp6) {
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("county_selected", true);
		editor.putString("update_time", update_time);
		editor.putString("countyName", countyName);
		editor.putString("countyCode", countyCode);
		editor.putString("weatherDesp", weatherDesp);
		editor.putString("windDesp", windDesp);
		editor.putString("date", date);
		editor.putString("pm", pm);
		editor.putString("temp", temp);
		
		editor.putString("weatherDesp2", weatherDesp2);
		editor.putString("windDesp2", windDesp2);
		editor.putString("temp2", temp2);
		
		editor.putString("weatherDesp3", weatherDesp3);
		editor.putString("windDesp3", windDesp3);
		editor.putString("temp3", temp3);
		
		editor.putString("weatherDesp4", weatherDesp4);
		editor.putString("windDesp4", windDesp4);
		editor.putString("temp4", temp4);
		
		editor.putString("weatherDesp5", weatherDesp5);
		editor.putString("windDesp5", windDesp5);
		editor.putString("temp5", temp5);
		
		editor.putString("weatherDesp6", weatherDesp6);
		editor.putString("windDesp6", windDesp6);
		editor.putString("temp6", temp6);
		
		
		
		
		editor.commit();
		

	}
	


	

}

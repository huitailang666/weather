package activity;

import com.example.qzjweather.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class RecentDaysActivity extends Activity {
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.recentdays);
		textView = (TextView) findViewById(R.id.recentdaysinfo);
		showWeather();
		
		
	}

	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String str=prefs.getString("weatherDesp2", "")+"	"
				+ prefs.getString("windDesp2", "")+"	"
				+ prefs.getString("temp2", "")+'\n'+
				prefs.getString("weatherDesp3", "")+"	"
				+ prefs.getString("windDesp3", "")+"	"
				+ prefs.getString("temp3", "")+'\n'+
				prefs.getString("weatherDesp4", "")+"	"
				+ prefs.getString("windDesp4", "")+"	"
				+ prefs.getString("temp4", "")+'\n'+
				prefs.getString("weatherDesp5", "")+"	"
				+ prefs.getString("windDesp5", "")+"	"
				+ prefs.getString("temp5", "")+'\n'+
				prefs.getString("weatherDesp6", "")+"	"
				+ prefs.getString("windDesp6", "")+"	"
				+ prefs.getString("temp6", "")+'\n';
		
		
		
		textView.setText(str);

	}

}

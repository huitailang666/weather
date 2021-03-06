package db;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



/*这个类配合AssetsDatebaseManager使用,把常用的数据库操作封装起来
 * @author huitailang
 *
 */
public class WeatherDb {
	
	
	private SQLiteDatabase db;

	
	public WeatherDb(SQLiteDatabase database){
		this.db=database;
	}

	
	
	/**将Province实例存储到数据库
	 * @param province
	 */
	public void saveProvince(Province province){
		ContentValues contentValues=new ContentValues();
		contentValues.put("province_name", province.getProvinceName());
		contentValues.put("province_code", province.getProvinceCode());
		db.insert("Province", null, contentValues);
	}
	
	/**从数据库读取所有省份的信息
	 * @return
	 */
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
				Log.d("QZJWeatherDb", ""+province.toString());
			} while (cursor.moveToNext());
		}

		return list;
	}
	
	/**将city实例存储到数据库
	 * @param city
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues contentValues=new ContentValues();
			contentValues.put("city_name", city.getCityName());
			contentValues.put("city_code", city.getCityCode());
			contentValues.put("province_id", city.getProvinceId());
			db.insert("City", null, contentValues);
		}
	}
	
	/**从数据库读某省份下的所有城市信息
	 * @param provinceId
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list =new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if(cursor.moveToFirst()){
			do {
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
				
				
			} while (cursor.moveToNext());
		}
		
		
		return list;
	}
	
	
	/**将county实例存储到数据库
	 * @param county
	 */
	public void saveCounty(County county){
		if(county!=null){
			ContentValues contentValues=new ContentValues();
			contentValues.put("county_name", county.getCountyName());
			contentValues.put("county_code", county.getCountyCode());
			contentValues.put("city_id", county.getCityId());
			db.insert("County", null, contentValues);
		}
	}
	
	/**从数据库读某城市下的所有县d信息
	 * @param cityId
	 * @return
	 */
	public List<County> loadCounties(int cityId){
		List<County> list =new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if(cursor.moveToFirst()){
			do {
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
				
				
			} while (cursor.moveToNext());
		}
		
		
		return list;
	}
	
}

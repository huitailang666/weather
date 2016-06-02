package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.provider.OpenableColumns;
import android.util.Log;

/**
 * @author huitailang 用来从assets目录下读取db文件到
 *         /data/data/"application package"/database目录下，智能检测，安装 apk后只需加载一次 高效
 *         实现方法 采用单例模式，全局只有1个此类 构建map（assets的db文件---db对象）存储db对象 （用于使用APP期间多次读取db的快捷方式）
 *         使用sharedprefences永久保存标志位来检测要不要 在 重新开启APP的时候快捷读取db，因为map不是永久的）
 *         如果存在db，直接用openDataBase静态方法打开db 用ｉｏ流操作文件，１０２４字节缓冲区
 * 
 *         该封装只是在第一次使用数据库文件的时候把该文件夹拷贝到手机的/data/data/应用程序报名/database文件夹下，
 *         之后就直接从这个地方使用了
 *         。并且它允许你直接通过assets文件夹下的数据库名称来获取SQLiteDatabase对象，这样就极大的方便了你对数据库的使用
 */
public class AssetsDatabaseManager {	
	
	private static String databasepath="/data/data/%s/database";//%s is a package name
	private static String TAG="AssetsDatabaseManager";
	
	
	
	private Map<String, SQLiteDatabase> databases=new HashMap<String, SQLiteDatabase>();
	
	
	
	//全局唯一实例 ，单例模式
	private static AssetsDatabaseManager mInstance=null;
	
	private Context context=null;
	
	
	public static void initManager(Context context){
		if(mInstance==null){
			synchronized (AssetsDatabaseManager.class) {
				mInstance=new AssetsDatabaseManager(context);
			}
			
		}
	}
	
	public static AssetsDatabaseManager getManager(){
		return mInstance;
	}
	
	private AssetsDatabaseManager(Context context){
		this.context=context;
	}
	
	/*
	 * 将属性私有化，对外封装，先initManager，再getInstance  无论执行多少次，只对应全局唯一实例mInstance
	 * */
	
	
	
	
	
	/**
	 * @param assets_file     assets目录下的db文件    "xxx.db"
	 * @return      SQLiteDatabse对象
	 */
	public SQLiteDatabase getDatabase(String assets_file){
		/*首先判断map里有没有db
		 * app运行时可以多次获取database
		 * */
		if(databases.get(assets_file)!=null){
			Log.d(TAG, String.format("return a database copy of %s",assets_file));
			return (SQLiteDatabase)databases.get(assets_file);
		}
		
		if(context==null){
			Log.d(TAG, "please initManager first!");
			return null;
		}
		
		
		
		String spath=getdatabasefilepath();
		String sfile=getdatabasefile(assets_file);
		File file=new File(sfile);
		/*
		 * 判断sharedpreferences里是否为true（代表着曾经某次使用APP已经把db复制到database目录下）通常为刚启动APP时判断
		 * */
		SharedPreferences pf= context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
		boolean flag=pf.getBoolean(assets_file, false);
		if(!flag||!file.exists()){
			file=new File(spath);
			if(!file.exists()&&!file.mkdirs()){
				Log.d(TAG,"Create \""+spath+"\" fail!"); 
				return null;
			}
			if(!copyAssetToDatabase(assets_file,sfile)){
				Log.d(TAG, String.format("copy %s to %s fail!", assets_file,sfile));
				return null;
			}
			Log.d(TAG, String.format("copy %s to %s success", assets_file,sfile));
			pf.edit().putBoolean(assets_file, true).commit();
			
		}
		
		/*直接打开database下的db文件 
		 * */
		SQLiteDatabase database=SQLiteDatabase.openDatabase(sfile, null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		if(database!=null){
			databases.put(assets_file, database);
		}
		return database;
		
	

	}
	
	
	/**
	 *  
	 * @return    /data/data/packageName/database/
	 */
	private String getdatabasefilepath(){
		return String.format(databasepath,context.getApplicationInfo().packageName);
	}
	
	/**
	 * @param path    xxx.db
	 * @return		完整路径： /data/data/packageName/database/xxx.db
	 */
	private String getdatabasefile(String path){
		String str=getdatabasefilepath()+'/'+path;
		return str;
	}
	
	
	
	/**
	 * @param path      xxx.db(in assets:)
	 * @param file		new SQLitedatabase  file in   xxxx/databse/
	 * @return     true if success
	 */
	private boolean copyAssetToDatabase(String path,String file){
		AssetManager assetManager=context.getAssets();
		InputStream inputStream=null;
		OutputStream outputStream=null;
		try {
			inputStream=assetManager.open(path);
			outputStream=new FileOutputStream(file);
			byte []buffer=new byte[1024];
			int count=0;
			while((count=inputStream.read(buffer))>0){
				outputStream.write(buffer, 0, count);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		return true;
		
		
		
	}

}

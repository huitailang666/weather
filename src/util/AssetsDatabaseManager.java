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
 * @author huitailang ������assetsĿ¼�¶�ȡdb�ļ���
 *         /data/data/"application package"/databaseĿ¼�£����ܼ�⣬��װ apk��ֻ�����һ�� ��Ч
 *         ʵ�ַ��� ���õ���ģʽ��ȫ��ֻ��1������ ����map��assets��db�ļ�---db���󣩴洢db���� ������ʹ��APP�ڼ��ζ�ȡdb�Ŀ�ݷ�ʽ��
 *         ʹ��sharedprefences���ñ����־λ�����Ҫ��Ҫ �� ���¿���APP��ʱ���ݶ�ȡdb����Ϊmap�������õģ�
 *         �������db��ֱ����openDataBase��̬������db �ã���������ļ������������ֽڻ�����
 * 
 *         �÷�װֻ���ڵ�һ��ʹ�����ݿ��ļ���ʱ��Ѹ��ļ��п������ֻ���/data/data/Ӧ�ó�����/database�ļ����£�
 *         ֮���ֱ�Ӵ�����ط�ʹ����
 *         ��������������ֱ��ͨ��assets�ļ����µ����ݿ���������ȡSQLiteDatabase���������ͼ���ķ�����������ݿ��ʹ��
 */
public class AssetsDatabaseManager {	
	
	private static String databasepath="/data/data/%s/database";//%s is a package name
	private static String TAG="AssetsDatabaseManager";
	
	
	
	private Map<String, SQLiteDatabase> databases=new HashMap<String, SQLiteDatabase>();
	
	
	
	//ȫ��Ψһʵ�� ������ģʽ
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
	 * ������˽�л��������װ����initManager����getInstance  ����ִ�ж��ٴΣ�ֻ��Ӧȫ��Ψһʵ��mInstance
	 * */
	
	
	
	
	
	/**
	 * @param assets_file     assetsĿ¼�µ�db�ļ�    "xxx.db"
	 * @return      SQLiteDatabse����
	 */
	public SQLiteDatabase getDatabase(String assets_file){
		/*�����ж�map����û��db
		 * app����ʱ���Զ�λ�ȡdatabase
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
		 * �ж�sharedpreferences���Ƿ�Ϊtrue������������ĳ��ʹ��APP�Ѿ���db���Ƶ�databaseĿ¼�£�ͨ��Ϊ������APPʱ�ж�
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
		
		/*ֱ�Ӵ�database�µ�db�ļ� 
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
	 * @return		����·���� /data/data/packageName/database/xxx.db
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

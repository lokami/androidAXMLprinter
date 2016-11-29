package com.slash.androidxmlprinter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.slash.androidxmlprinter.main.AXMLDecoder;

public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";
	static String apkPath = Environment.getExternalStorageDirectory()+"/WiFiwannengyaochi_3089.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        syncGetPermissions(this,new IOnPermissionsDone() {
			
			@Override
			public void onPermissionsDone(List<String> permissions) {
				if(permissions == null){
					return ;
				}
				
				Log.e(TAG, "Permission get !  "+permissions.size());
				for (String p : permissions) {
					Log.e(TAG, "permission = "+p);
				}
			}
		});
    }

	private void syncGetPermissions(final Context context,final IOnPermissionsDone callback) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<String> permissions = AXMLDecoder.getPermissions(context,apkPath);
				
				if(callback!=null){
					callback.onPermissionsDone(permissions);
				}
			}
		}).start();
	}
	
	public interface IOnPermissionsDone{
		void onPermissionsDone(List<String> permissions);
	}
}

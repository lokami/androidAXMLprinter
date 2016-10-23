package com.slash.androidxmlprinter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.slash.androidxmlprinter.axml.XMLparser;
import com.slash.androidxmlprinter.xmlprinter.AXMLDecoder;
import com.slash.androidxmlprinter.zip.Apk2Zip;
import com.slash.androidxmlprinter.zip.ZipReader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
/**
 * 1.将.apk转为.zip包
 * 2.拿到zip包中的manifest文件
 * 3.解析manifest.xml，获取其中的permission权限
 * 4.保存权限数据
 * 5.删除.zip和manifest.xml
 * 
 * 1.重命名apk为zip
 * 2.取出manifest
 * 3.解码manifest并读取permission属性值
 * 4.删除manifest文件
 * 4.恢复apk文件名
 * @author created by W.H.S
 *
 * @date 2016-10-22
 */
public class MainActivity extends Activity {

	protected static final String TAG = "MainActivity";
	static String apkPath = Environment.getExternalStorageDirectory()+"/anzhimarket.apk";
//	static String outZipPath = Environment.getExternalStorageDirectory()+"/anzhimarket.zip";
	static String outXMLpath = Environment.getExternalStorageDirectory()+"/manifest.xml";
//	static String decodedXMLpath = Environment.getExternalStorageDirectory()+"/manifest_decode.xml";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        syncGetPermissions(new IOnPermissionsDone() {
			
			@Override
			public void onPermissionsDone(List<String> permissions) {
				Log.e(TAG, "Permission get !"+permissions.size());
				for (String p : permissions) {
					Log.e(TAG, "permission = "+p);
				}
			}
		});
    }

	private void syncGetPermissions(final IOnPermissionsDone callback) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
//				// Q：不考贝文件，使用重命名是否可行；
//				// Q：在decode过程中获取Permission属性值
//				//1.转.apk为.zip
////				boolean converted = Apk2Zip.copyAPK2ZIP(apkPath, outZipPath);
//				boolean converted = Apk2Zip.renameApk2Zip(apkPath,outZipPath);
//				if(!converted){
//					Log.e(TAG, "转换zip失败");
//					return ;
//				}
//				//2.拿manifest
//				boolean read = ZipReader.readManifestFromZip(outZipPath,outXMLpath);
//				if(!read){
//					Log.e(TAG, "读取manifest.xml失败");
//					return;
//				} 
////				else {
////					new File(outZipPath).delete();
////				}
//				//3.解析manifest.xml
//				List<String> permissions = new ArrayList<>();
//				AXMLPrinter.decodeManifest(permissions,new String[]{outXMLpath,decodedXMLpath});
//				//4.获取权限列表
////				List<String> permissions = XMLparser.parseXMLForPermissions(decodedXMLpath);
////				new File(outXMLpath).delete();
////				new File(decodedXMLpath).delete();
//				Apk2Zip.resetApk(apkPath);
				List<String> permissions = AXMLDecoder.getPermissions(apkPath, outXMLpath);
				
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

package com.slash.androidxmlprinter.android;

import java.lang.ref.WeakReference;

import com.slash.androidxmlprinter.R;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PermissionInfo;
import android.util.Log;

/**
 * 将Permission属性值转换成中文字符串
 * @author created by W.H.S
 *
 * @date 2016-10-22
 */
public class AttrValueConverter {
	
	private static Context mCtx;
	
	private static Context getWeakContext(Context ctx){
		if(null==mCtx){
			mCtx = new WeakReference<>(ctx).get();
			Log.d("AttrValueConverter", "context weakReference is initialized");
		}
		return mCtx;
	}
	
	private static String getString(Context context,int strID){
		if(context!=null){
			return context.getResources().getString(strID);
		}
		return null;
	}
	
	public static String convert(Context ctx,String attrValue){
		Context context = getWeakContext(ctx);
		String str = null;
		int strID = -1;
		switch (attrValue) {
		case "android.permission.ACCESS_COARSE_LOCATION":
		case "android.permission.ACCESS_FINE_LOCATION":
		case "android.permission.ACCESS_MOCK_LOCATION":
		case "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS":
//			str = "获取用户的经纬度信息";
			strID = R.string.LOCATION;
			break;
		case "android.permission.ACCESS_NETWORK_STATE":
		case "android.permission.ACCESS_WIFI_STATE":
		case "android.permission.CHANGE_NETWORK_STATE":
		case "android.permission.CHANGE_WIFI_MULTICAST_STATE":
		case "android.permission.CHANGE_WIFI_STATE":
//			str = "获取用户的网络信息状态或网络变化信息";
			strID = R.string.NETWORK;
			break;
		case "android.permission.ACCOUNT_MANAGER":
		case "android.permission.AUTHENTICATE_ACCOUNTS":
//			str = "获取获取账户验证信息";
			strID = R.string.ACCOUNT;
			break;
		case "android.permission.BLUETOOTH":
		case "android.permission.BLUETOOTH_ADMIN":
		case "android.permission.ACCESS_BLUETOOTH_SHARE":
//			str = "允许程序发现和配对蓝牙设备或通过蓝牙分享";
			strID = R.string.BLUETOOTH;
			break;
		case "android.permission.CAMERA":
//			str = "请求访问使用照相设备";
			strID = R.string.CAMERA;
			break;
		case "android.permission.INTERNET":
//			str = "访问网络连接，可能产生GPRS流量";
			strID = R.string.INTERNET;
			break;
		case "android.permission.VIBRATE":
//			str = "允许震动";
			strID = R.string.VIBRATE;
			break;
		case "android.permission.WRITE_SETTINGS":
//			str = "允许程序读取或写入系统设置";
			strID = R.string.WRITE_SETTINGS;
			break;
		case "android.permission.SYSTEM_ALERT_WINDOW":
//			str = "允许显示系统窗口";
			strID = R.string.SYSTEM_ALERT_WINDOW;
			break;
		case "android.permission.WAKE_LOCK":
//			str = "允许程序在手机屏幕关闭后后台进程仍然运行";
			strID = R.string.WAKE_LOCK;
			break;
		case "android.permission.READ_PHONE_STATE":
//			str = "允许访问电话状态";
			strID = R.string.READ_PHONE_STATE;
			break;
		case "android.permission.WRITE_EXTERNAL_STORAGE":
//			str = "允许程序写入外部存储";
			strID = R.string.WRITE_EXTERNAL_STORAGE;
			break;
		case "android.permission.MODIFY_AUDIO_SETTINGS":
		case "android.permission.RECORD_AUDIO":
//			str = "修改声音设置,允许录音";
			strID = R.string.AUDIO;
			break;
		case "android.permission.MOUNT_UNMOUNT_FILESYSTEMS":
//			str = "挂载、反挂载外部文件系统";
			strID = R.string.FILE_SYSTEMS;
			break;
		case "android.permission.READ_CONTACTS":
//			str = "访问联系人通讯录信息";
			strID = R.string.READ_CONTACTS;
			break;
		default:
//			str="其他未知权限";
			strID = R.string.OTHER;
			break;
		}
		str = getString(context, strID);
		return str;
	}
}

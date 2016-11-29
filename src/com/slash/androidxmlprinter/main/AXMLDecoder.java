package com.slash.androidxmlprinter.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.slash.androidxmlprinter.android.AttrValueConverter;
import com.slash.androidxmlprinter.axml.AXmlResourceParser;
import com.slash.androidxmlprinter.zip.Apk2Zip;

public class AXMLDecoder {
		
		public static final String TAG = "AXMLDecoder";
	
	    private static final float[] RADIX_MULTS = new float[]{0.00390625F, 3.051758E-5F, 1.192093E-7F, 4.656613E-10F};
	    private static final String[] DIMENSION_UNITS = new String[]{"px", "dip", "sp", "pt", "in", "mm", "", ""};
	    private static final String[] FRACTION_UNITS = new String[]{"%", "%p", "", "", "", "", "", ""};
		private static final Object PERMISSION = "uses-permission";
		private static final String MANIFEST_NAME = "AndroidManifest.xml";

	    public AXMLDecoder() {
	    }

	    public static List<String> getPermissions(Context context,String apkPath){
	    	String outZipPath = apkPath.substring(0,apkPath.lastIndexOf("."))+".zip";
	    	
//	    	boolean renamed = Apk2Zip.renameFile(apkPath,outZipPath);
	    	boolean copyed = Apk2Zip.copyAPK2ZIP(apkPath, outZipPath);
			if(!copyed){
				Log.d(TAG, "failed to copy file");
				return null;
			}
			List<String> permissions = readManifestFromZip(context,outZipPath);
			if(permissions == null){
				Log.d(TAG, "read manifest failed");
				return null;
			} 
			
//			Apk2Zip.renameFile(outZipPath, apkPath);
			File outZipFile = new File(outZipPath);
			
			if(outZipFile.exists()){
				boolean delete = outZipFile.delete();
				Log.d(TAG, "删除zip文件?"+delete);
			}
			
			return permissions;
	    }
	    
	    private static List<String> readManifestFromZip(Context context,String zipPath){
			
			List<String> permissions = new ArrayList<>();
			if(TextUtils.isEmpty(zipPath)||!new File(zipPath).exists()){
				Log.d(TAG, "file path is invalid");
				return null;
			}
			ZipInputStream zipReader = null;
			try {
				zipReader = new ZipInputStream(new FileInputStream(zipPath));
				ZipEntry zipEntry;
				while((zipEntry = zipReader.getNextEntry())!=null){
					String name = zipEntry.getName();
					if(name.equals(MANIFEST_NAME)){
						decodeManifest(context,permissions, zipReader);
						break;
					} 
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally{
				if(zipReader!=null){
					try {
						zipReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return permissions;
		}
	    
	    private static void decodeManifest(Context context,List<String> permissions ,InputStream xmlStream){
	            try {
	                AXmlResourceParser e = new AXmlResourceParser();
	                e.open(xmlStream);

	                while(true) {
	                    while(true) {
	                        int type = e.next();
	                        if(type == 1) {
	                            return;
	                        }
	                        if(type == 2){
	                        	String name = e.getName();
	                        	if(name.equals(PERMISSION)){
	                        		Log.d(TAG, "find permission");
	                        		for(int i=0 ;i != e.getAttributeCount(); ++i){
	                        			String attrValue = getAttributeValue(e,i);
	                        			String permission = AttrValueConverter.convert(context,attrValue);
	                        			if(!permissions.contains(permission)){
	                        				permissions.add(permission);
	                        			}
	                        		}
	                        	}
	                            break;
	                        }
	                    }
	                }
	            } catch (Exception var8) {
	                var8.printStackTrace();
	            }
	    }

	    private static String getAttributeValue(AXmlResourceParser parser, int index) {
	        int type = parser.getAttributeValueType(index);
	        int data = parser.getAttributeValueData(index);
	        return type == 3?parser.getAttributeValue(index):(type == 2?String.format("?%s%08X", new Object[]{getPackage(data), Integer.valueOf(data)}):(type == 1?String.format("@%s%08X", new Object[]{getPackage(data), Integer.valueOf(data)}):(type == 4?String.valueOf(Float.intBitsToFloat(data)):(type == 17?String.format("0x%08X", new Object[]{Integer.valueOf(data)}):(type == 18?(data != 0?"true":"false"):(type == 5?Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & 15]:(type == 6?Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & 15]:(type >= 28 && type <= 31?String.format("#%08X", new Object[]{Integer.valueOf(data)}):(type >= 16 && type <= 31?String.valueOf(data):String.format("<0x%X, type 0x%02X>", new Object[]{Integer.valueOf(data), Integer.valueOf(type)}))))))))));
	    }

	    private static String getPackage(int id) {
	        return id >>> 24 == 1?"android:":"";
	    }

	    public static float complexToFloat(int complex) {
	        return (float)(complex & -256) * RADIX_MULTS[complex >> 4 & 3];
	    }
	}


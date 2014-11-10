package com.jason.copyapkfiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
//import android.os.storage.StorageVolume;

import com.jason.copyfiles.R;
public class CopyFilesActivity extends Activity {
	static final String TAG = "CopyFilesActivity";
	//static final String NEED_COPY_FILES_PATH = "/storage/sdcard0/jason";
	//static final String COPY_FILES_PATH = "/storage/sdcard0/apkfiles";
	/*PATH*/
	static final String from_files = "/mnt/sda/sda1/jason";
	@SuppressLint("SdCardPath") 
	static final String to_files = "/mnt/sdcard/apkfiles";
	static private final int COPY_START = 100;
	static private final int COPY_COMPLETE = 101;
    static private final boolean isDebug = false;
    private Myhandler H = new Myhandler();
	private Button btn;
	boolean isSdcardMount = false;
	File sdDir = null;
	File copyFileDir = null;
	
	@SuppressLint("HandlerLeak") 
	private class Myhandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case COPY_START:
					///TODO:
					break;
				case COPY_COMPLETE:
					Toast ts = Toast.makeText(CopyFilesActivity.this, "拷贝完成！", Toast.LENGTH_LONG);
					ts.show();
					break;
				default:
					break;
			}
		}  
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.copy_files);
		btn = (Button)findViewById(R.id.btn_copyfiles);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
			if(isSdcardMount)
			{	
				if(isDebug)Log.e(TAG,"JASON test coder.....");
				final ProgressDialog MyDialog = ProgressDialog.show(CopyFilesActivity.this," ","正在拷贝文件, 请等待... ",true);

				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try{
							copyFolder(from_files,to_files);
							MyDialog.cancel();
							H.sendEmptyMessage(COPY_COMPLETE);

						}catch(Exception e){
							if(isDebug)Log.e(TAG,"JASON test coder end.....");
						}	
					}
				}).start();
			}else
			{
				Toast ts = Toast.makeText(CopyFilesActivity.this, "没有文件可以复制", Toast.LENGTH_LONG);
				ts.show();
			}
		  }

		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isSdcardMount = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		sdDir = Environment.getExternalStorageDirectory();
		if(isDebug)Log.e(TAG,"JASON test coder ismount = "+ isSdcardMount);
		if(isDebug)Log.e(TAG,"JASON test coder sdDir = "+ sdDir);
		copyFileDir = new File(from_files);
		if(!copyFileDir.exists()){
			btn.setEnabled(false);
			btn.setText(R.string.copy_error);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/** 
     * COPY FILES
     * @param fromfiles String FROM：c:/files 
     * @param tofiles   String TO：f:/files/ff
     * @return boolean 
     */ 
   public void copyFolder(String fromfiles, String tofiles) { 

       try {
    	   if(isDebug)Log.e(TAG,"JASON copyFolder1.....");
           (new File(tofiles)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
  
           File root = new File(fromfiles); 
           if(isDebug)Log.e(TAG,"JASON copyFolder root = "+root);
           if(isDebug)Log.e(TAG,"JASON copyFolder root.canRead() = "+root.canRead());
           if(isDebug)Log.e(TAG,"JASON copyFolder root.canWrite() = "+root.canWrite());
           
           if(!root.exists())
        	   return;
           
           String[] file= root.list();
           if(isDebug)Log.e(TAG,"JASON copyFolder file = "+file);
           File temp=null; 
           if(isDebug)Log.e(TAG,"JASON copyFolder2.....");
           for (int i = 0; i < file.length; i++) { 
               
        	   if(isDebug)Log.e(TAG,"JASON copyFolder3.....");
            	   
                   if(fromfiles.endsWith(File.separator)){ 
                       temp=new File(fromfiles+file[i]); 
                   } 
                   else{ 
                       temp=new File(fromfiles+File.separator+file[i]); 
                   } 
              
               if(temp.isFile()){
            	   if(isDebug)Log.e(TAG,"JASON copyFolder4.....temp_file_name = "+temp);
                   FileInputStream input = new FileInputStream(temp); 
                   FileOutputStream output = new FileOutputStream(tofiles + "/" + 
                           (temp.getName()).toString()); 
                   byte[] b = new byte[1024 * 20]; 
                   int len; 
                   while ( (len = input.read(b)) != -1) { 
                       output.write(b, 0, len); 
                   } 
                   output.flush(); 
                   output.close(); 
                   input.close(); 
               } 
             //如果是子文件夹 
               //if(temp.isDirectory()){
                   //copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
              // } 
           } 
       } 
       catch (Exception e) { 
    	   
           System.out.println("复制整个文件夹内容操作出错"); 
           e.printStackTrace(); 

       } 

   }
   
}

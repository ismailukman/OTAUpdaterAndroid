package com.itcglobal.otaupdaterandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itcglobal.getapk.DownloadApk;
import com.itcglobal.updaterlib.AppUpdater;
import com.itcglobal.updaterlib.UpdateListener;
import com.itcglobal.updaterlib.UpdateModel;

import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    String TAG = this.getClass().getSimpleName();
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    private String version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AppUpdater(this, "https://raw.githubusercontent.com/ismailukman/SomeHowTosAndTexts/master/Updater/updater.json", new UpdateListener() {
            @Override
            public void onJsonDataReceived(final UpdateModel updateModel, JSONObject jsonObject) {
                if (AppUpdater.getCurrentVersionCode(MainActivity.this) < updateModel.getVersionCode()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Update available")
                            .setCancelable(updateModel.isCancellable())
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateModel.getUrl()));
                                    startActivity(browserIntent);
                                    finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onError(String error) {
                // Do something
            }
        }).execute();


        new AppUpdater(this, "https://raw.githubusercontent.com/ismailukman/SomeHowTosAndTexts/master/Updater/updater.json", new UpdateListener() {
            @Override
            public void onJsonDataReceived(final UpdateModel updateModel, JSONObject jsonObject) {
                if (AppUpdater.getCurrentVersionCode(MainActivity.this) < updateModel.getVersionCode()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Update available")
                            .setCancelable(updateModel.isCancellable())
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DownloadApk downloadApk = new DownloadApk(MainActivity.this);
                                    downloadApk.startDownloadingApk("https://github.com/Piashsarker/AndroidAppUpdateLibrary/raw/master/app-debug.apk", "Update 2.0");
                                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateModel.getUrl()));
                                    //startActivity(browserIntent);
                                    //finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onError(String error) {
                // Do something
            }
        }).execute();


        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //versionText=  findViewById(R.id.txt_version);
        //versionText.setText("Current Version "+version);
    }





    /** TODO: Must need to check the External Storage Permission Because we are storing the
     *  ApK in the External Or Internal Storage.
     */
    private void checkWriteExternalStoragePermission() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // If we have permission than we can Start the Download the task
            downloadTask();
        } else {
            //  If we don't have permission than requesting  the permission
            requestWriteExternalStoragePermission();
        }
    }

    private void requestWriteExternalStoragePermission() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,  new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadTask();
        } else {
            Toast.makeText(MainActivity.this, "Permission Not Granted.", Toast.LENGTH_SHORT).show();
        }
    }



    public void button(View view) {
        // First check the external storage permission
        checkWriteExternalStoragePermission();
    }

    private void downloadTask() {
        // This @DownloadApk class is provided by our library
        // Pass the Context when creating object of DownloadApk

        DownloadApk downloadApk = new DownloadApk(MainActivity.this);

        // For starting download call the method startDownloadingApk() by passing the URL and the optional filename
        downloadApk.startDownloadingApk("https://github.com/Piashsarker/AndroidAppUpdateLibrary/raw/master/app-debug.apk", "Update 2.0");
    }




}
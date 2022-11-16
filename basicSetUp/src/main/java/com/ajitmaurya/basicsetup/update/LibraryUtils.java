/*
 * Copyright 2017 Aleksandr Tarakanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ajitmaurya.basicsetup.update;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.ajitmaurya.basicsetup.BuildConfig;
import com.ajitmaurya.basicsetup.utility.BasicFunctions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class LibraryUtils {

    public static String appName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }

    public static String appPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    public static String currentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String appVersion(Context context) {
        String version = null;

        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static boolean isUpdateAvailable(String versionOld, String versionNew) {
        boolean res = false;

        if (!versionOld.equals("0.0.0.0") && !versionNew.equals("0.0.0.0")) {
            res = (versionCompare(versionOld, versionNew)) < 0;
        }

        return res;
    }

    public static void installApkAsFile(Context context, File filePath) {

        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());


        if (filePath != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            try {
                if (Build.VERSION.SDK_INT >= 24) {
                    intent = formatFileProviderIntent(context, filePath, intent, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(filePath), "application/vnd.android.package-archive");
                }
            } catch (Exception e) {
                e.printStackTrace();
                BasicFunctions.log(AppVerUpdater.TAG, "installApkAsFile: "+e.getLocalizedMessage() );
                intent.setDataAndType(Uri.fromFile(filePath), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } else {
            if (BuildConfig.DEBUG) {
                BasicFunctions.log(AppVerUpdater.TAG, "apk update not found");
            }
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static int versionCompare(String versionOld, String versionNew) {

        String[] vals1 = versionOld.split("\\.");
        String[] vals2 = versionNew.split("\\.");

        int i = 0;

        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }

    public static boolean isStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public static Intent formatFileProviderIntent(Context context, File file, Intent intent, String intentType) {

        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, intentType);
        return intent;

    }

    public static Uri formatFileProviderUri(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
        return uri;
    }

}

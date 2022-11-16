[![](https://jitpack.io/v/AjitkumarMaurya/basicSetUp.svg)](https://jitpack.io/#AjitkumarMaurya/basicSetUp)

BasicSetUp
============


Download
--------

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	



(https://jitpack.io/#AjitkumarMaurya/basicSetUp)	
	
Step 2. Add the dependency

    dependencies {
    
	      	   implementation 'com.github.AjitkumarMaurya:basicSetUp:9.0'
	      	   
	}
	

	
Use Permission
----------

add to manifest

    <activity
         android:name=".askPermission.PermissionsActivity"
                    android:theme="@style/Transparent"
                    android:windowSoftInputMode="stateHidden|adjustResize" />


add to style

     <style name="Transparent" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:backgroundDimEnabled">false</item>
    </style>

get permission

    Permissions.check(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, "request permission msg", null, new PermissionHandler() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);

                    }
                });
                
Calling AsyncTask
-----------

    class className extends AsyncTaskCoroutine<Void, String> {
  
          @Override
          public String doInBackground(Void... voids) {
  
              String newVersion = null;
  
              
              return newVersion;
  
          }
  
  
          @Override
          public void onPostExecute(String onlineVersion) {
  
              super.onPostExecute(onlineVersion);
  
            
          }
      }
                
	
License
-------
	
    Copyright 2021  AjitkumarMaurya
    
    Licensed under the Apache License, Version 2.0 (the “License”); you may not use this file except in compliance with the License. You may obtain a copy of the License at 
   
    http://www.apache.org/licenses/LICENSE-2.0 
   
    Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

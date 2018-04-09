VWO Android SDK
======================================

This open source library allows you to A/B Test your Android app.

Getting Started
---------------

1. Download the latest [VWO Android SDK](https://github.com/wingify/vwo-android/releases).
2. Have a look at [integrating SDK article](https://vwo.com/knowledge/integrating-android-sdk/)
   on the VWO Knowledge Base Website.

## Requirements

* Android 4.0 (API 14) or later

## Credentials

This SDK requires an app key. You can sign up for an account at [VWO](https://vwo.com). 
Once there, you can add a new Android App, and use the generated app key in the app.


## Setting up VWO account
* Sign Up for VWO account at https://vwo.com
* Create a new android app from create menu
* Use the app generated app key, while integrating SDK into android app.
* Create and run campaigns.

## How to import in gradle:
In your top level build.gradle file add the following code under repositories.

    buildscript {
        ...
        repositories {
            ...
        }
    }
    
    allprojects {
        repositories {
            ...
            maven {
                url 'https://raw.githubusercontent.com/wingify/vwo-mobile-android/master/'
            }
            ...
        }
    }
	
Add dependencies to app/build.gradle file

	dependencies {
	    ...
	    compile 'com.vwo:mobile:2.3.0@aar'
        compile ('io.socket:socket.io-client:1.0.0') {
            // excluding org.json which is provided by Android
            exclude group: 'org.json', module: 'json'
        }
        
        // Skip this if you are already including support library in your app.
        compile 'com.android.support:support-core-utils:27.1.1'
	    ...
	}
	
Update your project AndroidManifest.xml with following permissions:

```
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
	
## Initializing SDK

NOTE: Initialization of VWO SDK must be done in the `onCreate()` method of your Activity or Application.

##### Launching VWO SDK in Asynchronous mode

```java
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.vwo.mobile.VWO;

public class MainActivity extends AppCompatActivity {
    private static final String VWO_API_KEY = "YOUR_VWO_API_KEY";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      // Start VWO SDK in Async mode with callback
      VWO.with(this, VWO_API_KEY).launch(new VWOStatusListener() {
          @Override
          public void onVWOLoaded() {
              // VWO loaded successfully
          }
          @Override
          public void onVWOLoadFailure(String reason) {
              // VWO not loaded
          }
      });
    }
  }
```

##### Launching VWO SDK in synchronous mode

Launching VWO in Synchronous mode requires you to pass a timeout in milliseconds as a parameter. 
This request should be used carefully as it executes on UI thread and may lead to Application Not 
Responding(ANR) dialog.

```java
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.vwo.mobile.VWO;

public class MainActivity extends AppCompatActivity {
    private static final String VWO_API_KEY = "YOUR_VWO_API_KEY";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      
      // Start VWO SDK in sync mode
      VWO.with(this, VWO_API_KEY).launchSynchronously(3000);
    }
  }
```

## Advanced SDK configuration

You can setup VWO Config while initialising SDK
    
    // Config for adding custom user segmentation parameters before launch.
    Map<String, String> userSegmentationMapping = new HashMap<>();
    customKeys.put("userType", "free");
    
    VWOConfig vwoConfig = new VWOConfig
            .Builder()
            .setCustomSegmentationMapping(userSegmentationMapping)
            .disablePreview()                                             // To disable preview Mode.
            .setOptOut(true)                                              // To opt out of VWO SDK.
            .build();
                
This config can be set during the VWO SDK launch:

    VWO.with(Context, VWO_APP_KEY).config(vwoConfig).launch(null);

## Using Campaigns
You can use the following method to fetch a variation and pass a default value which is 
returned back in case no key matches.

```
String key2 = "another-campaign-key";
Object variation2 = VWO.getVariationForKey(key2, "default_value");
```

## Triggering goals

You can mark a goal conversion using following methods:

```
VWO.trackConversion("conversionGoal");

VWO.trackConversion("conversionGoal", 133.25);
```

second method is for marking a revenue goals you can pass the revenue value in double as second 
parameter to the function.

## Listening to User becoming part of campaign

You can register a Broadcast Receiver with intent filter ```VWO.NOTIFY_USER_TRACKING_STARTED``` 
for listening to the event of user becoming part of a 
campaign.

Below is the code snippet:

```java
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.vwo.mobile.VWO;

public class MainActivity extends AppCompatActivity {
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get the campaign data for which the user became part of.
                Bundle extras = intent.getExtras();
            	String campaignId = extras.getString(VWO.Constants.ARG_CAMPAIGN_ID);
            	String campaignName = extras.getString(VWO.Constants.ARG_CAMPAIGN_NAME);
            	String variationId = extras.getString(VWO.Constants.ARG_VARIATION_ID);
            	String variationName = extras.getString(VWO.Constants.ARG_VARIATION_NAME);
            	
            	//TODO: Write your analytics code here
            }
        };
    
    @Override
    protected void onStart() {
        super.onStart();
        // Create an intent filter for broadcast receiver.
        IntentFilter intentFilter = new IntentFilter(VWO.Constants.NOTIFY_USER_TRACKING_STARTED);
        
        // Register your broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister your broadcast receiver.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}
```

### Opt-Out
Use Following code to opt-out a user from SDK:
    
    VWOConfig vwoConfig = new VWOConfig.Builder()
                             .setOptOut(true)               // set your opt out flag here
                             .build();
                             
    VWO.with(Context, VWO_APP_KEY).config(vwoConfig).launch(null);
 
## Proguard

if you are using proguard. Add the following rules to your proguard file

```proguard
    # Support libraries
    -keep class android.support.v4.content.LocalBroadcastManager
    
    # VWO module
    -keep public class * extends com.vwo.mobile.models.Entry
    
    -keepclassmembers class * extends com.vwo.mobile.models.Entry{
     public <init>(android.os.Parcel);
    }
    
    # Socket.io
    -dontwarn io.socket.**
```


## License

By using this SDK, you agree to abide by the [VWO Terms & Conditions](http://vwo.com/terms-conditions).

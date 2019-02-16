# SmartLocationFetcher

This repository helps to prevents the location coming from GPS spoof'd Apps.

# Version
 [![](https://jitpack.io/v/rajendraprasad348/SmartLocationFetcher.svg)](https://jitpack.io/#rajendraprasad348/SmartLocationFetcher)
 
# Description

 Simple example shown in app section

If your GPS is jumping around from your real location to the spoof'd location non stop every few seconds here is a fix you can try. 
I'm on android had this problem for a while and it was super annoying and got me soft banned a few times. so for that here is the solution to prevent the data coming from GPS spoof'd apps and showing irrelevent user location.

GPS spoofing. A GPS spoofing attack attempts to deceive a GPS receiver by broadcasting incorrect GPS signals, structured to resemble a set of normal GPS signals, or by rebroadcasting genuine signals captured elsewhere or at a different time.

 # Demo
 https://www.youtube.com/watch?v=OIN1Ww8_LtE

# Installation

      Step1:  Add it in your root build.gradle at the end of repositories:
           allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}  
	
	  Step2:  Add the dependency
         dependencies {
	       	        implementation 'com.github.rajendraprasad348:SmartLocationFetcher:1.0'
	} 
  
 
 # Download APK
   https://www.dropbox.com/s/yse7ockm8mxexio/SmartLocationFetcher.apk?dl=0 
   
  # Usage
  
  You need to add some code in your class file
  
     SmartLocationFetcher smartLocationFetcher = new SmartLocationFetcher(MainActivity.this, MainActivity.this);
     smartLocationFetcher.fetchSmartLocation(MainActivity.this);
         
  and class file should implement with below listener 
          
    implements SmartLocationFetcher.OnLocationGetListener { 
          .....
    }
     
    @Override
    public void onLocationReady(Location location) {
        Lattitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
 
  
  

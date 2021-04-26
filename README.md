# PedometerEx
Pedometer exercise(s) - There are already many examples and articles on this topic in previous years. \
This project is the most recent with the addition of permission request.

Android has provided a number of solutions for tracking step count via accelerometer.
1. The most basic method is to count the local accelerometer by pattern of rise and drops.
2. An abstracted method is to use the sensor's TYPE_STEP_COUNTER data.
3. Addition feature includes step detection for pause and restart counting.

### Permission
Thanks to the added security on Android 10/11, it is now required for the user to explicitly permission recognition. \
<img width="200" src="https://user-images.githubusercontent.com/1282659/116013991-9b3ef900-a5f8-11eb-8fa3-013df1457a58.jpg">

### Sensor Count Steps
Current implementation utilizes TYPE_STEP_COUNTER but the count initializes incorrectly at start. \
Existing code is a work-in-progress with no display of failed permission nor disabling of state buttons 'active, stop, paused, clear'. \
<img width="200" src="https://user-images.githubusercontent.com/1282659/116013992-9c702600-a5f8-11eb-8874-d5c97723b3c6.jpg">

### Device
This application is tested on Samsung Galaxy S9 with Android 10

### Android Studio
Version 4.1 \
<img width="549" alt="Screen Shot 2021-04-25 at 7 04 40 PM" src="https://user-images.githubusercontent.com/1282659/116014090-1e604f00-a5f9-11eb-9de7-c58d9a7b3cfb.png">

# References

1. Motion Sensors | Android Documentation \
https://developer.android.com/guide/topics/sensors/sensors_motion

2. Create a Step Counter Fitness App for Android with Kotlin by Sylvain Saurel, May 3, 2018 \
https://ssaurel.medium.com/create-a-step-counter-fitness-app-for-android-with-kotlin-bbfb6ffe3ea7

3. "manifest.xml when using sensors" on StackOverflow \
https://stackoverflow.com/questions/20497087/manifest-xml-when-using-sensors

4. Androids Permission | Google Fit Documentation \
https://developers.google.com/fit/android/authorization



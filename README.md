# Smart Irrigation with IoT

by Ania Boutarene, Daniel Braun, Easin Ahmed

## Important information:

1. The app was tested in Android Studio using a virtual device with these configurations:
   *Pixel 3a, API 34*

2. After turning on the emulator in Android Studio, use the device explorer to create an empty file called irrigationinfo.txt in the path *data>data>com.irrigation.example>files*. Otherwise the application will not be able to start.

3. The app is using the weatherbit.io API. The credentials are limited to only the free version. Therefore the pythonscript can only be executed at maximum 50 times per day. Should this limit be exceeded and errors occur, line 53 in the IrrigationBroakcastReceiver should be commented out and line 55 should be uncommented to use an alternative file returning the value 0.
   Line 53: *run("python3 runFiles/precipitation.py");*
   Line 55: *// run("python3 runFiles/test.py");*

4. The automatic irrigation is executed once every 24 hours by default. For testing purposes line 33 can be commented out and line 36 can be uncommented to set this interval to 1 minute.
   Line 33: *long intervalMillis = 86400 * 1000;*
   Line 36: *// long intervalMillis = 60 * 1000;*
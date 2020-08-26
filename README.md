# KotlinNativeStarter

Multi platform Kotlin/Native Android and IOS project which serves as a playground to test new developments in Kotlin/Native.

# Running the project

For testing the project [Mockoon](https://mockoon.com/) is used to provided responses to the app. To setup Mockoon do the following:

- Download Mockoon from https://mockoon.com and install it
- Run Mockoon and from the top menu choose: Import/Export -> Mockoon's format -> Import from a file (JSON)
- Select the file docs/mockoon.json which is included in the project
- Run the environment by selecting it and pressing the green Play button. Mockoon is now listening for incoming connections at port 2500.

When running on Android the URL used by RealApi is http://10.0.2.2:2500 which works in the Android emulator. on iOS the URL is http://localhost:2500 which works in the iOS emulator.  The URL is determined automatically based on the platform the app is run on.


# Running the iOS project

- Startup XCode and open the iosApp/iosApp.xcworkspace
- Select an emulator from the list and click the play button
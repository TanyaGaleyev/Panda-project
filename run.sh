gradle -b SimpleAndroidApp/build.gradle assembleDebug;
adb -d install -r SimpleAndroidApp/build/outputs/apk/SimpleAndroidApp-debug-unaligned.apk;

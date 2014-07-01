dir="SimpleAndroidApp"
build_dir="${dir}/build/outputs/apk"
gradle -b "${dir}/build.gradle" assembleRelease;
jarsigner -sigalg SHA1withRSA -digestalg SHA1 -keystore "${dir}/panda.jks" "${build_dir}/SimpleAndroidApp-release-unsigned.apk" acropanda;
/c/OtherProgramms/Android/android-studio/sdk/tools/zipalign.exe -v 4 "${build_dir}/SimpleAndroidApp-release-unsigned.apk" "${build_dir}/AcroPanda.apk"

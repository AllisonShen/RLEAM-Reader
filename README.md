# RLEAM-Reader
[RLEAM Reader: Read, Learn, and Memorize](https://github.com/AllisonShen/RLEAM-Reader)
- [Github](https://github.com/AllisonShen/RLEAM-Reader)
- [Report](https://www.overleaf.com/read/hhpwthfhncgr)

## Introduction
RLEAM Reader is designed to build a seamless and immersive way for users to read, learn and memorize while reading ebook/documents which include complex vocabularies.

Reading is implemented with the read view, which integrates hand gestures like tapping and long pressing. The learning function is facilitated with flashcards design. The memorizing funciton is implemented based on forgetting curve. These three main functions comprise of the RLEAM Reader.

## Environment
* API 30
* Test Emulator: Pixel
* Android 11.0


## External Libraries
* epublib-core-latest.jar(https://github.com/psiegman/epublib)
* slf4j-android-1.7.32.jar(http://www.slf4j.org/download.html)
* slf4j-api-1.7.32.jar(http://www.slf4j.org/download.html)
* jsoup-1.14.3.jar(https://jsoup.org/download): for reading HTML files from EPUB files

## Other APIs
* [Oxford Dictionary](https://developer.oxforddictionaries.com/)

## Techniques
* UI: Fragment, WebView (& JavaScript), MenuInflater, PopupWindow, ScrollView, Adapter
* Intent
* Database: SQLite
* Thread, ExecutorService, Handler (In android calling network requests on the main thread forbidden by default)
* Gesture: Long pressed, Double Tap
* HttpURLConnection, JSONObject

## Useful Links
 - [Good README Files](https://courses.cs.washington.edu/courses/cse326/02wi/homework/hw5/good-readmes.html)
 - [How to add a jar in External Libraries in Android Studio?](https://stackoverflow.com/questions/25660166/how-to-add-a-jar-in-external-libraries-in-android-studio)
 - [Android studio logcat nothing to show](https://stackoverflow.com/questions/17432358/android-studio-logcat-nothing-to-show)
 - [Android Studio Run/Debug configuration error: Module not specified](https://stackoverflow.com/questions/29087882/android-studio-run-debug-configuration-error-module-not-specified)
 - [Type BuildConfig is defined multiple times](https://stackoverflow.com/questions/60507686/type-buildconfig-is-defined-multiple-times)
 - [Unable to resolve host "<URL here>" No address associated with host name [closed]](https://stackoverflow.com/questions/6355498/unable-to-resolve-host-url-here-no-address-associated-with-host-name)
 - [How to load external webpage in WebView](https://stackoverflow.com/questions/7305089/how-to-load-external-webpage-in-webview)

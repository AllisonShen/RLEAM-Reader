# RLEAM-Reader
[RLEAM Reader: Read, Learn, and Memorize](https://github.com/AllisonShen/RLEAM-Reader)
- [Github](https://github.com/AllisonShen/RLEAM-Reader)
- [Report](https://www.overleaf.com/read/hhpwthfhncgr)
- [Demo](https://drive.google.com/file/d/1cGSpRCIxW30z4DkDqQqYOaX_F2uMkqr7/view?usp=sharing)

## Introduction
RLEAM Reader is designed to build a seamless and immersive way for users to read, learn and memorize while reading ebook/documents which include complex vocabularies. (RLEAM = **R**ead, **LEA**rn, **M**emorize)


Reading is implemented with the read view, which integrates hand gestures like tapping and long pressing. The learning function is facilitated with flashcards design. The memorizing funciton is implemented based on forgetting curve. These three main functions comprise of the RLEAM Reader.

* Read 
    * The App can accept PDF and EPUB format 
    * Reading is implemented with the read view, which integrates hand gestures like double-tap to close and long press to open the customized menu
    * Corresponding files: PdfActivity.java, ReadViewEPUB.java, HomeFragment.java
* Learn
    * Customized Menu for long-press, a popup window, and a TextView is developed for showing the page with a specific URL and showing the definition retrieved from Oxford Dictionary API  
    * A database is created for saving each user’s account information and their personalized data including a favorite list (word & definition), remember count and forget count
    * Corresponding files: DBHelper.java, ReadViewEPUB.java, Login.java, RegisterActivity, ToastMenuItemListener

* Memorize
    * The memorizing function is facilitated with flashcards design. The memorizing function is implemented based on the forgetting curve. Memorizing is designed to present users with flashcards of the words in the personalized favorites list. It will first test if the user remembers the meaning of the word by making the meaning invisible. Users can either choose to remember or forget to show the memorization situation for that specific word. It will count and store the number of times that the user clicks remember or forget. The forgetting curve will utilize these records to calculate the retrievability of a user to remember a word. 
    * Corresponding files: FavouriteList.java, QuestionsModel.java, DashboardFragment.java

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


## Contack Information  
 * Xiaxin Shen: shen452@purdue.edu
 * Xiaoyu Guo: guo496@purdue.edu
 * Onyinyechi Blossom Nwogu: onwogu@purdue.edu
   

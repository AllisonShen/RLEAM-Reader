package com.cnit355.rr;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

import android.Manifest;
import android.R.color;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;


public class ReadViewEPUB extends AppCompatActivity implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    ImageView imageView;
    static TextView textViewShowMean;
    int index;
    private GestureDetectorCompat gDetector;
    protected final int PERMISSION_REQUEST = 42;
    private static final String TAG = "ReadViewEPUB";
    private RecyclerView mRecycler;
    private Book book;
    static Thread threadCheckDict;
    static String strMean = "None";
    public static WebView mWebView;
    public static WebView dictWebView;

    public String title;
    public String href;
    private List<String> indexTitleList = new ArrayList<>();
    private List<String> indexHrefList = new ArrayList<>();
    WebSettings settings;
    static String selectedWord = "nothing";
    static Boolean clickDict = false;
    static Boolean clickFavor = false;
    //use Executor instead
    static ExecutorService executor = Executors.newSingleThreadExecutor();
    static Handler handler = new Handler(Looper.getMainLooper());
    GestureDetector gs = null;
    static String favorStr = "None";
    static DBHelper db;  //as global variable
    String output_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHelper(this);
        setContentView(R.layout.activity_read_view_epub);
        mWebView = (WebView) findViewById(R.id.mWebView);
         textViewShowMean = (TextView) findViewById(R.id.textViewShowMean);
        dictWebView = (WebView) findViewById(R.id.dictWebView);
        dictWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gs == null) {
                    gs = new GestureDetector(
                            new GestureDetector.SimpleOnGestureListener() {
                                @Override
                                public boolean onDoubleTapEvent(MotionEvent e) {
                                    closeView();
                                    return true;
                                }


                            });
                }
                gs.onTouchEvent(event);

                return false;
            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

        Log.i("epublib", "author(s): " + "here is read view");
        index = 0;

        gDetector = new GestureDetectorCompat(this, this);
        gDetector.setOnDoubleTapListener(this);


        AssetManager assetManager = getAssets();
        try {
            // find InputStream for book
            InputStream epubInputStream = assetManager
                    .open("paper.epub");

            // Load Book from inputStream
            book = (new EpubReader()).readEpub(epubInputStream);

            // Log the book's authors
            Log.i("epublib", "author(s): " + book.getMetadata().getAuthors());

            // Log the book's title
            Log.i("epublib", "title: " + book.getTitle());

            // Log the book's coverimage property
//            Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage()
//                    .getInputStream());
//            Log.i("epublib", "Coverimage is " + coverImage.getWidth() + " by "
//                    + coverImage.getHeight() + " pixels");

            // Log the table of contents
            logTableOfContents(book.getTableOfContents().getTocReferences(), 0);

            //get raw data
            Metadata metadata = book.getMetadata();
            StringBuffer buffer = new StringBuffer();
            for (String s : metadata.getDescriptions()) {
                buffer.append(s + " ");
            }
            //Find chapters
            Spine spine = book.getSpine();
            List<SpineReference> spineReferences = spine.getSpineReferences();
            if (spineReferences != null && spineReferences.size() > 0) {
                Resource resource = spineReferences.get(1).getResource(); // get html pages
                Log.i(TAG, "initView: book=" + resource.getId() + "  " + resource.getTitle() + "  " + resource.getSize() + " ");
                //for debug
                byte[] data = resource.getData();
                String strHtml = bytes2Hex(data);
                Log.i(TAG, "initView: strHtml= " + strHtml);
                mWebView.getSettings().setJavaScriptEnabled(true);
                dictWebView.getSettings().setJavaScriptEnabled(true);

                mWebView.loadDataWithBaseURL(null, strHtml, "text/html", "utf-8", null);
//                parseHtmlData(strHtml);
                mWebView.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                Log.v(TAG, "Webview selected text: " + value);
                                selectedWord = value;
                            }
                        });


            } else {
                Log.i(TAG, "initView: spineReferences is null");
            }
        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
        }
    }
    private static String checkDictTwo(String selectedWord) throws IOException {
        String word = selectedWord;
        String key = "f9e6ba31-41a5-4778-984a-59d7a1d89666";
        final String word_id = word.toLowerCase();
        Log.v(TAG, "Webview meaning checkDict word_id: " + word_id);
        String restUrl = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"+word_id+"?key="+key;
        URL url = new URL(restUrl);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        // read the output from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        Log.v(TAG, "Webview meaning checkDict stringBuilder two: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private static String checkDict(String selectedWord) {
        String returnValue = "None";
        String result = "None";
        final String language = "en-gb";
        String word = selectedWord;
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        Log.v(TAG, "Webview meaning checkDict word_id: " + word_id);
//        final String restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
        final String restUrl = "https://od-api.oxforddictionaries.com/api/v2/entries/en-gb/"+word_id+"?fields=definitions&strictMatch=false";
//        final String restUrl = "https://od-api.oxforddictionaries.com/api/v2/entries/en-gb/recurrent?fields=definitions&strictMatch=false";
        //TODO: replace with your own app id and app key
        final String app_id = "0a267e15";
        final String app_key = "084035680d7a9925713be9abd914977a";

        Log.v(TAG, "Webview meaning checkDict restUrl: " + restUrl);


        try {
            URL url = new URL(restUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            Log.v(TAG, "Webview meaning checkDict stringBuilder: " + stringBuilder.toString());
//            System.out.println(stringBuilder.toString());
//            Log.v(TAG, "Webview meaning: " + stringBuilder);


            result = stringBuilder.toString();


            String def = "None";//
            try {//from String to Json

                JSONObject js = new JSONObject(result);
                //method one
//                HashMap<String, String> mapToUpdate = new HashMap<String, String>();
//                Iterator<String> keys = js.keys();
//                while (keys.hasNext()) {
//                    String key = keys.next();
//                    Log.v(TAG, "Webview meaning key: " + key);
//                    String value = js.getString(key);
//                    if(key == "definitions"){
//                        Log.v(TAG, "Webview meaning there is definitions: " + value);
//                        def = value;
//                        break;
//                    }
//
//                    // do whatever you want with key/value
//                }
//method two
//                updateData( js, mapToUpdate);


                JSONArray results = js.getJSONArray("results");
                for(int i = 0;i<results.length();i++){
                    JSONObject lentries = results.getJSONObject(i);
                    JSONArray la = lentries.getJSONArray("lexicalEntries");
                    for(int j=0;j<la.length();j++){
                        JSONObject entries = la.getJSONObject(j);
                        JSONArray e = entries.getJSONArray("entries");
                        for(int i1=0;i1<e.length();i1++){
                            JSONObject senses = e.getJSONObject(i1);
                            JSONArray s = senses.getJSONArray("senses");
                            for(int k=0;k<s.length();k++){
                                JSONObject d = s.getJSONObject(k);
                                JSONArray de = d.getJSONArray("definitions");
                                def = de.getString(0);
                                Log.v(TAG, "Webview meaning there is definitions: " + def);
                            }

                        }
                    }
                }
//                Log.e("def",def);
                returnValue = def;

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "network", e);

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "network", e);

        }

        return returnValue;
    }


    private void logTableOfContents(List<TOCReference> tocReferences, int depth) {
        if (tocReferences == null) {
            Log.e("epublib", "tocReferences is null");
            return;
        }
        for (TOCReference tocReference : tocReferences) {
            StringBuilder tocString = new StringBuilder();
            for (int i = 0; i < depth; i++) {
                tocString.append("\t");
            }
            tocString.append(tocReference.getTitle());
            Log.i("epublib", tocString.toString());

            logTableOfContents(tocReference.getChildren(), depth + 1);
        }
    }

    public String bytes2Hex(byte[] bs) {
        if (bs == null || bs.length <= 0) {
            return null;
        }

        Charset charset = Charset.defaultCharset();
        ByteBuffer buf = ByteBuffer.wrap(bs);
        CharBuffer cBuf = charset.decode(buf);
        return cBuf.toString();
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        closeView();


        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {


    }


    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
// Be sure to call the superclass implementation return super.onTouchEvent(event);

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);

        MenuInflater menuInflater = mode.getMenuInflater();
        Menu menu = mode.getMenu();

        menu.clear();
        menuInflater.inflate(R.menu.long_press_menu, menu);


        menu.findItem(R.id.dictionary).setOnMenuItemClickListener(new ToastMenuItemListener(this, mode, "Dictionary"));
        menu.findItem(R.id.favoriteList).setOnMenuItemClickListener(new ToastMenuItemListener(this, mode, "Favorite List"));
//        menu.findItem(R.id.custom_three).setOnMenuItemClickListener(new ToastMenuItemListener(this, mode, "Three!"));
    }


    private static class ToastMenuItemListener implements MenuItem.OnMenuItemClickListener {

        private final Context context;
        private final ActionMode actionMode;
        private final String text;


        private ToastMenuItemListener(Context context, ActionMode actionMode, String text) {
            this.context = context;
            this.actionMode = actionMode;
            this.text = text;
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            System.out.println("menuItemClicked");
            Log.v(TAG, "Webview selected text: " + selectedWord);
            mWebView.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.v(TAG, "Webview selected text: " + value);
                            selectedWord = value;
                        }
                    });


            switch (item.getItemId()) {
                case R.id.dictionary:
                    Log.v(TAG, "Webview selected text: " + selectedWord);
                    clickDict = true;

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            int len = selectedWord.length();
                            strMean = checkDict(selectedWord.substring(1,len-1));

                                Log.v(TAG, "Webview meaning return strMean: " + strMean);
                            textViewShowMean.setText(selectedWord+": "+strMean);



                            //Background work here

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dictWebView.setVisibility(View.VISIBLE);
                                    int len = selectedWord.length();
                                    selectedWord = selectedWord.substring(1,len-1);
                                    dictWebView.loadUrl("https://www.dictionary.com/browse/"+selectedWord);

                                    //UI Thread work here
                                }
                            });
                        }
                    });

                    break;
                case R.id.favoriteList:
//                    testWV.highlightWord();
                    clickFavor = true;
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            int len = selectedWord.length();
                            strMean = checkDict(selectedWord.substring(1,len-1));

                            Log.v(TAG, "Webview meaning return strMean: " + strMean);
                            textViewShowMean.setText(selectedWord+": "+strMean);}});

//                    to use: create

                     Boolean insertWord = db.insertFavouriteWord(Login.currentUser, selectedWord, strMean, "0", "0");
                    if(insertWord==true){
                        Toast.makeText(this.context, "Favourite word saved!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this.context, "Could not save favourite word", Toast.LENGTH_SHORT).show();
                    }

                    showPopup();

                    Log.v(TAG, "Webview favorite list: " + favorStr);
                    Toast.makeText(this.context, "Notes are added ", Toast.LENGTH_SHORT).show();
                    break;
            }
            actionMode.finish();
            clickDict = false;
            clickFavor = false;
            return true;
        }

        private void showPopup()
        {
            PopupWindow window = new PopupWindow(this.context);
            window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//            window.setBackground(Color.YELLOW);
            window.setTouchable(true);
            window.setFocusable(true);

            EditText text = new EditText(this.context);
            text.setTextColor(Color.WHITE);
            text.setText("Definition: "+strMean+"\n\nNotes for "+selectedWord+": ");

            window.setContentView(text);
            window.showAtLocation(text, Gravity.NO_GRAVITY, 30, 30);

            text.addTextChangedListener(new TextWatcher() {
                private Context context;

                @Override
                public void afterTextChanged(Editable s) {
//                    String notes = s.toString();
//                    Toast.makeText(this.context, "Saved!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {

                }
            });
        }
    }

    public void closeView(){
        dictWebView.setVisibility(View.INVISIBLE);
        dictWebView.loadUrl("");
    }
    public static void updateData(JSONObject fullResponse, HashMap<String, String> mapToUpdate) throws JSONException {


        for (Iterator<String> it = fullResponse.keys(); it.hasNext(); ) {
            String keyStr = it.next();
            Object keyvalue = fullResponse.get(keyStr);

            if (keyvalue instanceof JSONArray) {
                updateData(((JSONArray) keyvalue).getJSONObject(0), mapToUpdate);
            } else if (keyvalue instanceof JSONObject) {
                updateData((JSONObject) keyvalue, mapToUpdate);
            } else {
                // System.out.println();
                Log.v(TAG, "Webview meaning: key: " + keyStr + " value: " + keyvalue);
                if(keyStr == "definitions"){
                    strMean = (String) keyvalue;
                    Log.v(TAG, "Webview meaning: " + strMean);
                }
                if (mapToUpdate.containsKey(keyStr)) {
                    fullResponse.put(keyStr, mapToUpdate.get(keyStr));
                }
            }
        }

    }
}





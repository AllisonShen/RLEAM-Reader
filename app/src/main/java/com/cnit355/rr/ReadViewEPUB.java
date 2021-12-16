package com.cnit355.rr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import javax.net.ssl.HttpsURLConnection;
//implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener


public class ReadViewEPUB extends AppCompatActivity implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    ImageView imageView;
    int index;
    private GestureDetectorCompat gDetector;
    protected final int PERMISSION_REQUEST = 42;
    //    private ResultProfileBinding binding;
    private static final String TAG = "ReadViewEPUB";

    //    private MyAdatper mAdatper;
    private RecyclerView mRecycler;
    private Book book;
    static Thread threadCheckDict;
    //        View view = binding.getRoot();
//        setContentView(view);
//        recycler
//        mRecycler = (RecyclerView) findViewById(R.id.recycler);
    public static WebView mWebView;

    public String title;
    public String href;
    private List<String> indexTitleList = new ArrayList<>();
    private List<String> indexHrefList = new ArrayList<>();
    WebSettings settings;
    static String selectedWord = "nothing";


    //    private TextView output_text;
//    output_text = (TextView) findViewById(R.id.outputText);
    String output_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_view_epub);
        mWebView = (WebView) findViewById(R.id.mWebView);


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
        threadCheckDict = new Thread(new Runnable() {
            String strMean;

            @Override
            public void run() {
                try {
                    //Your code goes here
                    strMean = checkDict(selectedWord);
                    Log.v(TAG, "Webview meaning: " + strMean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private String checkDict(String selectedWord) {
        String returnValue = "None";
        final String language = "en-gb";
        String word = selectedWord;
        final String fields = "pronunciations";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        final String restUrl = "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
        //TODO: replace with your own app id and app key
        final String app_id = "0a267e15";
        final String app_key = "084035680d7a9925713be9abd914977a";
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
            System.out.println(stringBuilder.toString());
            returnValue = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
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
        mWebView.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.v(TAG, "Webview selected text: " + value);
                        selectedWord = value;
                    }
                });

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
        mWebView.evaluateJavascript("(function(){return window.getSelection().toString()})()",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.v(TAG, "Webview selected text: " + value);
                        selectedWord = value;
                    }
                });

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
//    private void emulateShiftHeld(WebView view)
//    {
//        try
//        {
//            KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
//                    KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
//            shiftPressEvent.dispatch(view);
//            Toast.makeText(this, "select_text_now", Toast.LENGTH_SHORT).show();
//        }
//        catch (Exception e)
//        {
//            Log.e("dd", "Exception in emulateShiftHeld()", e);
//        }
//    }


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
            String strMean;
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
                    threadCheckDict.start();
//                    testWV.searchInDict();
//                    if (mActionMode!=null){
//                        mActionMode.finish();
//                    }

                    break;
                case R.id.favoriteList:
//                    testWV.highlightWord();
                    break;
            }
            actionMode.finish();
            return true;
        }
    }
}





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


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class ReadViewEPUB extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    ImageView imageView;
    int index;
    private GestureDetectorCompat gDetector;
    protected final int PERMISSION_REQUEST = 42;
//    private ResultProfileBinding binding;
    private static final String TAG = "ReadViewEPUB";

//    private MyAdatper mAdatper;
    private RecyclerView mRecycler;
    private Book book;
    private WebView mWebView;

    public String title;
    public String href;
    private List<String> indexTitleList = new ArrayList<>();
    private List<String> indexHrefList = new ArrayList<>();




    //    private TextView output_text;
//    output_text = (TextView) findViewById(R.id.outputText);
    String output_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_view_epub);
//        View view = binding.getRoot();
//        setContentView(view);
//        recycler
//        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mWebView = (WebView) findViewById(R.id.mWebView);
//        mRecycler.setLayoutManager(new LinearLayoutManager(this));
//        mAdatper = new MyAdatper(indexTitleList,indexHrefList, this);
//        mRecycler.setAdapter(mAdatper);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }

        Log.i("epublib", "author(s): " + "here is read view");
        index = 0;

        gDetector = new GestureDetectorCompat(this,this);
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
            if (spineReferences != null && spineReferences.size() > 0){
                Resource resource = spineReferences.get(1).getResource(); // get html pages
                Log.i(TAG, "initView: book=" + resource.getId() + "  " + resource.getTitle() + "  " + resource.getSize() + " ");
                //for debug
                byte[] data = resource.getData();
                String strHtml = bytes2Hex(data);
                Log.i(TAG, "initView: strHtml= " + strHtml);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.loadDataWithBaseURL(null, strHtml, "text/html", "utf-8", null);
//                parseHtmlData(strHtml);

            }else {
                Log.i(TAG, "initView: spineReferences is null");
            }
        } catch (IOException e) {
            Log.e("epublib", e.getMessage());
        }
    }

    private void parseHtmlData(String strHtml) throws IOException {



        Document doc = Jsoup.parse(strHtml);
        Log.i(TAG, "parseHtmlData:  doc.title();=" + doc.title());
        Elements eles = doc.getElementsByTag("a");   // interate find "a" attribute
        for (Element link : eles) {
            String linkHref = link.attr("href"); //inter
            String text = link.text();
            href = linkHref;
            title = text;
            indexTitleList.add(title);
            indexHrefList.add(href);
            Log.i(TAG, "parseHtmlData: linkHref=" + linkHref + " text=" + text);
        }
    }
//    private class MyAdatper extends RecyclerView.Adapter<MyAdatper.ViewHolder>{
//        private List<String> mStrings;
//        private List<String> mHref;
//        private final LayoutInflater mInflater;
//
//        public MyAdatper(List<String> mStrings,List<String> mHref, Context context) {// title, hrefs
//            this.mStrings = mStrings;
//            this.mHref = mHref;
//            mInflater = LayoutInflater.from(context);
//        }
//        public class ViewHolder extends RecyclerView.ViewHolder {
////            TextView mTextView;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
////                mTextView = (TextView) itemView.findViewById(R.id.book_title);
//            }
//        }
//
//        @NonNull
//        @Override
//        public MyAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return null;
////            View view = mInflater.inflate(R.layout.layout_item, null);
////            ViewHolder holder = new ViewHolder(view);
////            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyAdatper.ViewHolder holder, int position) {
////            return null;
//
//
////            holder.mTextView.setText(mStrings.get(position));//title
////            holder.mTextView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    String href = mHref.get(position);
////                    Intent intent = new Intent(ReadViewEPUB.this, DetailActivity.class);
////                    intent.putExtra("href", href);
////                    startActivity(intent);
////                }
////            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
////            return mStrings.size();
//        }
//    }

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
    public  String bytes2Hex(byte[] bs) {
        if (bs == null || bs.length <= 0) {
            return null;
        }

        Charset charset = Charset.defaultCharset();
        ByteBuffer buf = ByteBuffer.wrap(bs);
        CharBuffer cBuf = charset.decode(buf);
        return cBuf.toString();
    }

    public void clickPrevious(View view){
//        imageView = (ImageView) findViewById(R.id.imageView);
//        long id_d = imageView.getUniqueDrawingId();
//        String tempStr = "_1";
//        int[] ids = {R.drawable._1, R.drawable._2, R.drawable._3, R.drawable._4, R.drawable._5, R.drawable._6};
//        index = (index - 1 +6)%6;
//        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), ids[index],
//                null);
////        srcCompat
//        imageView.setImageDrawable(myDrawable);

    }
    public void clickNext(View view){
//        imageView = (ImageView) findViewById(R.id.imageView);
//        int[] ids = {R.drawable._1, R.drawable._2, R.drawable._3, R.drawable._4, R.drawable._5, R.drawable._6};
//        index = (index + 1 +6)%6;
//        Drawable myDrawable = ResourcesCompat.getDrawable(getResources(), ids[index],
//                null);
//        imageView.setImageDrawable(myDrawable);

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Toast.makeText(ReadViewEPUB.this, "Single Tap: Show Explanation",
                Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
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
        Toast.makeText(ReadViewEPUB.this, "Add to Favorite List",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
// Be sure to call the superclass implementation return super.onTouchEvent(event);
    }
}




package com.cnit355.rr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfActivity extends AppCompatActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        Intent mIntent = getIntent();

        String qString = mIntent.getStringExtra("qString");
        Uri myUri = Uri.parse(qString);

        pdfView = findViewById(R.id.pdfView);
        showPdfFromUri(myUri);
    }

    private void showPdfFromUri(Uri uri){
        pdfView.fromUri(uri).defaultPage(0).spacing(10).load();
    }
}
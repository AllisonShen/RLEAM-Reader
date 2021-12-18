package com.cnit355.rr;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class PdfActivity extends AppCompatActivity {

    //private static final int PDF_SELECTION_CODE =99;

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        /*Intent mIntent = getIntent();

        String qString = mIntent.getStringExtra("qString");
        Uri myUri = Uri.parse(qString);

        pdfView = findViewById(R.id.pdfView);
        showPdfFromUri(myUri);

         */

        pdfView = findViewById(R.id.pdfView);
        Intent mIntent = getIntent();
        checkPdfAction(mIntent);







    }
/*
    private void showPdfFromUri(Uri uri){
        pdfView.fromUri(uri).defaultPage(0).spacing(10).load();
    }

 */
    private void checkPdfAction(Intent intent){
        if (intent.getStringExtra("ViewType").equals("storage")){
            selectPdfFromStorage();
        }
    }

    private void selectPdfFromStorage(){
        Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
        mIntent.setType("application/pdf");
        mIntent.addCategory(Intent.CATEGORY_OPENABLE);
        mIntent= Intent.createChooser(mIntent, "Choose a file");
        sActivityResultLauncher.launch(mIntent);

    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        showPdfFromUri(uri);
                    }
                }
            }

    );

    /*
    public void onActivityResult(Intent data, int resultCode, int requestCode){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && data != null && requestCode == PDF_SELECTION_CODE){
            Uri uri = data.getData();
            showPdfFromUri(uri);
        }
    }

     */

    public void showPdfFromUri(Uri uri){
        pdfView.fromUri(uri).load();
    }
}
package com.cnit355.rr.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cnit355.rr.PdfActivity;
import com.cnit355.rr.R;
import com.cnit355.rr.databinding.FragmentHomeBinding;
import com.github.barteksc.pdfviewer.PDFView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button mImportPdfButton;
    private Button mImportEpubButton;

    Context context;
    Uri uri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = rootView.getContext();//Assign your rooView to context

        mImportPdfButton = binding.button2;
        mImportPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPdf();
            }
        });

        mImportEpubButton = binding.button4;
        mImportEpubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEpub();
            }
        });



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
/*
    public void openPdf(){
        Intent mIntent = new Intent(context, PdfActivity.class);
        startActivity(mIntent);
    }

 */


    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                   if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        uri = data.getData();
                   }
                }
            }

    );



    public void openPdf(){
        //Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //data.setType("application/pdf");
        //data = Intent.createChooser(data, "Choose a file");
        //sActivityResultLauncher.launch(data);

        Intent mIntent = new Intent(context, PdfActivity.class);
        mIntent.putExtra("ViewType", "storage");
        startActivity(mIntent);

    }

    public void openEpub(){
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("application/epub+zip");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }


}
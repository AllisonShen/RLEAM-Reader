package com.cnit355.rr.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cnit355.rr.DBHelper;
import com.cnit355.rr.Login;
import com.cnit355.rr.QuestionsModel;
import com.cnit355.rr.R;
import com.cnit355.rr.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    DBHelper db;
    String user=Login.currentUser;

    //create buttons and textview for UI
    private Button mExplanationButton;
    private Button mKnowButton;
    private Button mForgetButton;
    private Button mNextButton;
    private Button mForgettingCurveButton;
    private TextView mWordTextView;
    private TextView mMeaningTextView;
    private TextView mRememberTextView;
    private TextView mForgetTextView;
    private TextView mForgettingCurveTextView;
    private int[] rememberCount = new int[20] ;
    private int[] forgetCount = new int[20];
    private int[] forgettingTime = new int[20];

    private int mCurrentIndex = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db=new DBHelper(getContext());
       /*
       final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        */

        mMeaningTextView = binding.explanation;
        mWordTextView = binding.word;
        mRememberTextView = binding.rememberCount;
        mForgetTextView = binding.forgetCount;
        mForgettingCurveTextView = binding.forgettingCurveTextView;


        if (!db.favouriteWords(user).isEmpty()){
            try{
            mWordTextView.setText(db.favouriteWords(user).get(mCurrentIndex));
            mMeaningTextView.setText("Explanation");
            mRememberTextView.setText("Remember: " + db.rememberCount(user, db.favouriteWords(user).get(mCurrentIndex))); //read from dbdb.rememberCount(user, word).get(mCurrentIndex)
                mForgetTextView.setText("Forget: " + db.forgetCount(user, db.favouriteWords(user).get(mCurrentIndex)));
                mForgettingCurveTextView.setText("Forgetting Curve");

                }catch (Exception e){

            }

        }



        mExplanationButton = binding.meaning;

        mExplanationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMeaning();
            }
        });



        mKnowButton = binding.know;
        mKnowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try{

                        int count=Integer.parseInt(db.rememberCount(user, db.favouriteWords(user).get(mCurrentIndex)));
                        count++;

                        mRememberTextView.setText("Remember: " + count);
                       // rememberCount[mCurrentIndex]= count;
                        //insert new rememberCount for specific user and word combination
                        db.updateRemember(user, db.favouriteWords(user).get(mCurrentIndex), Integer.toString(count));

                    }catch (Exception e){
                    }
        }
        });

        mForgetButton = binding.forget;
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    int count=Integer.parseInt(db.forgetCount(user, db.favouriteWords(user).get(mCurrentIndex)));
                    count++;

                    mForgetTextView.setText("Forget: " + count);
                    //forgetCount[mCurrentIndex]= count;
                    //insert new forgetCount for specific user and word combination
                    db.updateForget(user, db.favouriteWords(user).get(mCurrentIndex), Integer.toString(count));
                }catch (Exception e){
                }
            }
        });

        mNextButton = binding.next;
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;

                try {
                    mCurrentIndex = (mCurrentIndex + 1) % db.favouriteWords(user).size();
                    updateQuestion();
                }catch (Exception e){}
            }
        });

        mForgettingCurveButton = binding.forgettingCurveButton;
        mForgettingCurveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateForgettingCurve();
            }
        });

        /*
        mMeaningTextView = (TextView) getView().findViewById(R.id.explanation);

        mKnowButton = (Button) getView().findViewById(R.id.know);
        mKnowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mForgetButton = (Button) getView().findViewById(R.id.forget);
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) getView().findViewById(R.id.next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

         */


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateQuestion(){
        //int word = mQuestionBank[mCurrentIndex].getTextId();

        if (!db.favouriteWords(user).isEmpty()){
            try{
                String word = db.favouriteWords(user).get(mCurrentIndex); //word from db
                mWordTextView.setText(word);
                mMeaningTextView.setText("Explanation");
                mRememberTextView.setText("Remember: " + db.rememberCount(user, db.favouriteWords(user).get(mCurrentIndex))); //read from db
                mForgetTextView.setText("Forget: " + db.forgetCount(user, db.favouriteWords(user).get(mCurrentIndex))); //from db
                mForgettingCurveTextView.setText("Forgetting Curve");

            }catch (Exception e){

            }
           }
    }

    public void displayMeaning(){
        try {
            String meaning= db.favouriteWordExplanations(user).get(mCurrentIndex);
            mMeaningTextView.setText(meaning);
        }catch (Exception e){}

    }


    public void calculateForgettingCurve(){

        try{
            rememberCount[mCurrentIndex]= Integer.parseInt(db.rememberCount(user, db.favouriteWords(user).get(mCurrentIndex)));
            forgetCount[mCurrentIndex]= Integer.parseInt(db.forgetCount(user, db.favouriteWords(user).get(mCurrentIndex)));
        }catch (Exception e){
            Log.d("exception:", String.valueOf(e));
        }

        if (rememberCount[mCurrentIndex] != 0 || forgetCount[mCurrentIndex] != 0) {
            double probability =Math.round((double)rememberCount[mCurrentIndex] / (rememberCount[mCurrentIndex] + forgetCount[mCurrentIndex]) * 100) ;
            mForgettingCurveTextView.setText("The probability for retrieving this word is " + probability + " %");
        }else{
                 mForgettingCurveTextView.setText("No record for calculation");
        }
    }

}
package com.cnit355.rr.ui.dashboard;

import android.os.Bundle;
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

import com.cnit355.rr.QuestionsModel;
import com.cnit355.rr.R;
import com.cnit355.rr.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    //create buttons and textview for UI
    private Button mExplanationButton;
    private Button mKnowButton;
    private Button mForgetButton;
    private Button mNextButton;
    private TextView mWordTextView;
    private TextView mMeaningTextView;

    //create the question bank
    private QuestionsModel[] mQuestionBank = new QuestionsModel[]{
            new QuestionsModel(R.string.word_scrupulous, R.string.meaning_scrupulous, true),
            new QuestionsModel(R.string.word_vilify, R.string.meaning_vilify, true),
            new QuestionsModel(R.string.word_sanctimonious, R.string.meaning_sanctimonious, true),
            new QuestionsModel(R.string.word_turpitude, R.string.meaning_turpitude, true)
    };
    private int mCurrentIndex = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        mWordTextView.setText(mQuestionBank[mCurrentIndex].getTextId());

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
                checkAnswer(true);
            }
        });

        mForgetButton = binding.forget;
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = binding.next;
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
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
        int word = mQuestionBank[mCurrentIndex].getTextId();
        mWordTextView.setText(word);
        mMeaningTextView.setText("Explanation");
    }



    private void checkAnswer(boolean userPress){
        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswer();
        int messageResID = 0;
        if (userPress == answerTrue){
            messageResID = R.string.remember_toast;
        }else {
            messageResID = R.string.forget_toast;
        }
        Toast.makeText(getActivity().getApplicationContext(), messageResID, Toast.LENGTH_SHORT).show();
    }


    public void displayMeaning(){
        int meaning = mQuestionBank[mCurrentIndex].getTextMeaning();
        mMeaningTextView.setText(meaning);
    }


}
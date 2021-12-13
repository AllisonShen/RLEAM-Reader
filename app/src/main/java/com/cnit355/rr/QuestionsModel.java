package com.cnit355.rr;

public class QuestionsModel {
    private int mTextid;
    private int mTextMeaning;
    private boolean mAnswer;

    //create a constructor
    public QuestionsModel(int textId, int textMeaning, boolean answer){
        mTextid = textId;
        mAnswer = answer;
        mTextMeaning = textMeaning;
    }

    //create the getter and setters for the constructor
    //these can be autogenerate as well
    public void setTextid(int textId){
        mTextid = textId;
    }

    public void setAnswer(boolean answer){
        mAnswer= answer;
    }

    public void setTextMeaning(int textMeaning){
        mTextMeaning = textMeaning;
    }

    public int getTextId(){
        return mTextid;
    }

    public boolean isAnswer(){
        return mAnswer;
    }

    public int getTextMeaning(){
        return mTextMeaning;
    }
}

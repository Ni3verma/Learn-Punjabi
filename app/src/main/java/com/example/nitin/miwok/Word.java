package com.example.nitin.miwok;

/**
 * Created by nitin on 6/1/18.
 */

public class Word {
    private String englishWord;
    private String punjabiWord;
    private int imageResourceId=-1;
    private int soundResourceId;

    public Word(String english, String punjabi,int imageId,int soundId) {
        this.englishWord = english;
        this.punjabiWord = punjabi;
        this.imageResourceId=imageId;
        this.soundResourceId=soundId;
    }
    //constructor for PhrasesActivity which do not require any image
    public Word(String english, String punjabi,int soundId) {
        this.englishWord = english;
        this.punjabiWord = punjabi;
        this.soundResourceId=soundId;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getPunjabiWord() {
        return punjabiWord;
    }
    public int getImageResourceId(){
        return imageResourceId;
    }

    public int getSoundResourceId() {
        return soundResourceId;
    }
}

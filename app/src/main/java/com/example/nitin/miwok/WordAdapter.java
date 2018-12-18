package com.example.nitin.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nitin on 6/1/18.
 */

public class WordAdapter extends ArrayAdapter<Word> {
    private int backgroundColorResourceID;
    public WordAdapter(@NonNull Context context, ArrayList<Word> words,int colorID) {
        super(context, 0, words);
        this.backgroundColorResourceID=colorID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Word currentWord = getItem(position);

        TextView punjabiWord = listItemView.findViewById(R.id.punjabi);
        TextView englishWord = listItemView.findViewById(R.id.english);
        ImageView image = listItemView.findViewById(R.id.row_image);
        LinearLayout container= listItemView.findViewById(R.id.translationContainer);

        int backgroundColor= ContextCompat.getColor(getContext(),backgroundColorResourceID);
        container.setBackgroundColor(backgroundColor);
        punjabiWord.setText(currentWord.getPunjabiWord());
        englishWord.setText(currentWord.getEnglishWord());
        int imageResourceId = currentWord.getImageResourceId();
        if (imageResourceId == -1) {
            image.setVisibility(View.GONE);
        } else
            image.setImageResource(imageResourceId);

        return listItemView;
    }
}

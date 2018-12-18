package com.example.nitin.miwok;


import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {
    private MediaPlayer mp;
    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;
    ImageView audioIcon;

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int i) {
            if (i == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            } else if (i == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK || i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mp.pause();
                mp.seekTo(0);
            } else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                mp.start();
            }
        }
    };



    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.word_list,container,false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("red", "laal", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("green", "hra", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "pura", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("gray", "sleti", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("black", "kala", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("white", "chita", R.drawable.color_white, R.raw.color_white));
        words.add(new Word("yellow", "peela", R.drawable.color_mustard_yellow, R.raw.color_yellow));


        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        ListView listView = rootview.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                releaseMediaPlayer();
                int soundID = words.get(pos).getSoundResourceId();

                int result;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT).
                            setOnAudioFocusChangeListener(audioFocusChangeListener).
                            build();
                    result = audioManager.requestAudioFocus(focusRequest);
                } else {
                    result = audioManager.requestAudioFocus(audioFocusChangeListener,
                            AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    audioIcon = view.findViewById(R.id.audio_icon);
                    audioIcon.setImageResource(R.drawable.ic_pause_arrow);
                    mp = MediaPlayer.create(getActivity(), soundID);
                    mp.start();

                    mp.setOnCompletionListener(completionListener);
                }
            }
        });
        return rootview;
    }
    private void releaseMediaPlayer() {
        if (mp != null) {
            audioIcon.setImageResource(R.drawable.ic_play_arrow);
            mp.release();
            mp = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.abandonAudioFocusRequest(focusRequest);
            } else {
                audioManager.abandonAudioFocus(audioFocusChangeListener);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}

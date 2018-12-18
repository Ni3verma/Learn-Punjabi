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
public class PhrasesFragment extends Fragment {
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
            } else if (i == AudioManager.AUDIOFOCUS_GAIN) {
                mp.start();
            } else if (i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK) {
                mp.pause();
                mp.seekTo(0);
            }
        }
    };



    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.word_list,container,false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Who are you?", "Tusi kon ho?", R.raw.phrase_who_are_you));
        words.add(new Word("What is your name?", "Tuhada naam ki hai?", R.raw.phrase_what_is_your_name));
        words.add(new Word("Where do you live?", "Tusi kithe rehnde ho?", R.raw.phrase_where_do_you_live));
        words.add(new Word("Come here", "Idhr aayo", R.raw.phrase_come_here));
        words.add(new Word("How are you?", "Tuhada ki haal hai?", R.raw.phrase_how_are_you));
        words.add(new Word("I was sick", "Mai bimaar c.", R.raw.phrase_sick));
        words.add(new Word("I am feeling good", "Mai theek mehsoos kr reha haan.", R.raw.phrase_feeling_good));
        words.add(new Word("I am coming", "Mai aa rea haan.", R.raw.phrase_coming));
        words.add(new Word("Let's go", "Chlo chliye !", R.raw.phrase_lets_go));


        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

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

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
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

}

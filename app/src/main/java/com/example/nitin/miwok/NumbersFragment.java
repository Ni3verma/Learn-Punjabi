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
public class NumbersFragment extends Fragment {
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

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one", "ik", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "do", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tin", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "char", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "panj", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "cheh", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "satt", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "ath", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "no\'n", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "das", R.drawable.number_ten, R.raw.number_ten));


        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = rootView.findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                releaseMediaPlayer();
                Word wordClicked = words.get(pos);
                int soundID = wordClicked.getSoundResourceId();
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

        return rootView;
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

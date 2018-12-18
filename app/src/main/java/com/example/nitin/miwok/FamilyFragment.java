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
public class FamilyFragment extends Fragment {
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



    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.word_list,container,false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("father", "pita g", R.drawable.family_father, R.raw.family_father));
        words.add(new Word("mother", "mata g", R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word("brother", "bhra", R.drawable.family_older_brother, R.raw.family_brother));
        words.add(new Word("sister", "bhen", R.drawable.family_older_sister, R.raw.family_sister));
        words.add(new Word("son", "puttr", R.drawable.family_son, R.raw.family_son));
        words.add(new Word("daughter", "puttri", R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("grand mother", "dadi g", R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word("grand father", "dada g", R.drawable.family_grandfather, R.raw.family_grandfather));


        WordAdapter itemsAdapter = new WordAdapter(getActivity(), words, R.color.category_family);

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

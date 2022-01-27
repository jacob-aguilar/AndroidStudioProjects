package com.example.probook.piensarapido;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ConfigurarActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    RadioGroup radiog;
    RadioButton radiob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);
        radiog = (RadioGroup) findViewById(R.id.radiosonido);
        radiog.setOnCheckedChangeListener(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_homero2);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.seleccionsi:
//                MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_long);
  //              mediaPlayer.start();
                break;

            case R.id.seleccionno:
    //            MediaPlayer mediaPlayers = MediaPlayer.create(this, R.raw.sound_short);
      //          mediaPlayers.start();
                break;
    }

    /*public void consonido(View view) {
    int radioboton = radiog.getCheckedRadioButtonId();
    radiob = (RadioButton) findViewById(radioboton);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_long);
        mediaPlayer.start();
       // Toast.makeText(this, "Ponte a programarme", Toast.LENGTH_SHORT).show();
    }

    public void sinsonido(View view) {
        int botonradio = radiog.getCheckedRadioButtonId();
        radiob = (RadioButton) findViewById(botonradio);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound_long);
        mediaPlayer.stop();
        //Toast.makeText(this, "Ponte a programarme ya", Toast.LENGTH_SHORT).show();
    }*/
    }
}

package com.felhr.serialportexamplesync;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.felhr.serialportexamplesync.MainActivity.conectado;
import static com.felhr.serialportexamplesync.MainActivity.mandarByte;

public class Main2Activity extends ActionBarActivity {

    String[] listItem;
    Button teste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.rgb(11,76,61));

        toolbar.setTitle("Definições");
        setSupportActionBar(toolbar);

        teste = findViewById(R.id.button);

        teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conectado == false) {
                    mensagem("ERRO: Tem de conectar um robot primeiro.");
                } else {
                    listItem = new String[]{"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Main2Activity.this);
                    mBuilder.setTitle("Velocidades");
                    mBuilder.setSingleChoiceItems(listItem, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i){
                                case 0 :
                                    mandarByte("1");
                                    mensagem("A velocidade do Robot foi definida para 10%");
                                    break;
                                case 1 :
                                    mandarByte("2");
                                    mensagem("A velocidade do Robot foi definida para 20%");
                                    break;
                                case 2 :
                                    mandarByte("3");
                                    mensagem("A velocidade do Robot foi definida para 30%");
                                    break;
                                case 3 :
                                    mandarByte("4");
                                    mensagem("A velocidade do Robot foi definida para 40%");
                                    break;
                                case 4 :
                                    mandarByte("5");
                                    mensagem("A velocidade do Robot foi definida para 50%");
                                    break;
                                case 5 :
                                    mandarByte("6");
                                    mensagem("A velocidade do Robot foi definida para 60%");
                                    break;
                                case 6 :
                                    mandarByte("7");
                                    mensagem("A velocidade do Robot foi definida para 70%");
                                    break;
                                case 7 :
                                    mandarByte("8");
                                    mensagem("A velocidade do Robot foi definida para 80%");
                                    break;
                                case 8 :
                                    mandarByte("9");
                                    mensagem("A velocidade do Robot foi definida para 90%");
                                    break;
                                case 9 :
                                    mandarByte("0");
                                    mensagem("A velocidade do Robot foi definida para 100%");
                                    break;
                            }
                            dialogInterface.dismiss();
                        }
                    });
                    mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog mdialog = mBuilder.create();
                    mdialog.show();
                }
            }
        });

        CompoundButton switchCompat = (CompoundButton) findViewById(R.id.switch_1);
        switchCompat.setChecked(true);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked) {
                    if (conectado == false) {
                        mensagem("ERRO: Tem de conectar um robot primeiro.");
                        switchCompat.setChecked(false);
                    } else {
                        mandarByte("M");
                    }

                }
                else {
                    if (conectado == false) {
                        mensagem("ERRO: Tem de conectar o robot primeiro.");
                        switchCompat.setChecked(true);
                    } else {
                        mandarByte("L");
                    }
                }
            }
        });
    }

    void mensagem(String msg) {Toast.makeText(this.getBaseContext(), msg, Toast.LENGTH_SHORT).show();}

}

package com.felhr.serialportexamplesync;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.lang.ref.WeakReference;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    public static UsbService usbService;
    private MyHandler mHandler;

    boolean modo_cooldown;
    public static boolean conectado;
    static Handler garra_repeticao;


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB CONECTADO

                    modo_cooldown = true;
                    mensagem ("Dispositivo conectado!");
                    Utils.delay(500, new Utils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            if (modo_cooldown == true) {
                                mensagem ("Agora aguarde, por favor...");
                            }
                        }
                    });
                    Utils.delay(5000, new Utils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            if (modo_cooldown == true) {
                                conectado = true;
                                mandarByte("A");
                                modo_cooldown = false;
                            }
                        }
                    });

                    Utils.delay(6000, new Utils.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            mensagem ("Robot encontrado!");
                            repeticao_estado(false);
                        }
                    });
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // PERMISSAO NEGADA
                    mensagem ("Permissão negada.");
                    break;
                case UsbService.ACTION_NO_USB: // DISPOSITIVO AINDA NAO CONECTADO
                    mensagem ("Sem conexão a qualquer dispositivo.");
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DESCONECTADO
                    conectado = false;
                    if (modo_cooldown == true) {
                        mensagem("Desconectou um dispositivo enquanto estava a reiniciar.");
                        modo_cooldown = false;
                    } else {
                        mensagem ("Dispositivo desconectado com sucesso.");
                    }
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NAO SUPORTADO
                    mensagem ("Este dispositivo não é compatível.");
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(Color.rgb(11,76,61));
        toolbar.setTitle("BubbleBee™");
        setSupportActionBar(toolbar);



        mHandler = new MyHandler(this);
        garra_repeticao = new Handler();

        Button esquerda = (Button) findViewById(R.id.esquerda);
        esquerda.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (conectado == true) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                        mandarByte ("B");
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                        mandarByte ("F");
                    }

                }
                return true;
            }
        });


        Button direita = (Button) findViewById(R.id.direita);
        direita.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public  boolean onTouch(View v, MotionEvent event) {
                if (conectado == true) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                        mandarByte ("C");
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                        mandarByte ("F");
                    }

                }

                return true;
            }
        });

        Button frente = (Button) findViewById(R.id.cima);
        frente.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (conectado == true) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                        mandarByte ("D");
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                        mandarByte ("F");
                    }

                }

                return true;
            }
        });

        Button atras = (Button) findViewById(R.id.baixo);
        atras.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (conectado == true) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                        mandarByte ("E");
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                        mandarByte ("F");
                    }

                }

                return true;
            }
        });

        Button Abrir = (Button) findViewById(R.id.Abrir);
        Abrir.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                    acao_garra("abrir");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                    acao_garra("parar");
                }


                return true;
            }
        });

        Button Fechar = (Button) findViewById(R.id.fechar);
        Fechar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                    acao_garra("fechar");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                    acao_garra("parar");
                }

                return true;
            }
        });

        Button Levantar = (Button) findViewById(R.id.levantar);
        Levantar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                    acao_garra("subir");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                    acao_garra("parar");
                }

                return true;
            }
        });

        Button Baixar = (Button) findViewById(R.id.Baixar);
        Baixar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) { //QUANDO CLICAR
                    acao_garra("descer");
                }
                if (event.getAction() == MotionEvent.ACTION_UP) { //QUANDO PARAR DE CLICAR
                    acao_garra("parar");
                }

                return true;
            }
        });



    }


    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.app_bar_search:
                if (conectado == true) {
                    startActivity(new Intent(MainActivity.this, Main2Activity.class));
                } else {
                    mensagem("ERRO: Tem de conectar o robot primeiro para poder mexer nas definições.");
                }
                break;
            case R.id.Report:
                Intent sendintent=new Intent(Intent.ACTION_SEND);
                sendintent.setType("Text/plain");
                String []s={"neffex21@gmail.com", "ribeirojoao200101@gmail.com"};
                sendintent.putExtra(Intent.EXTRA_EMAIL, s);
                sendintent.putExtra(Intent.EXTRA_SUBJECT, "[BubbleBee] Report de BUG");
                String espacos = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
                sendintent.putExtra(Intent.EXTRA_TEXT, espacos + "Dispositivo: " + Build.BRAND + "\n Modelo: " + android.os.Build.MODEL);
                startActivity(sendintent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //comando
    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:
                    String buffer = (String) msg.obj;
                    break;
            }
        }
    }


    //SISTEMA DE LOOP PARA CONTROLAR A GARRA AO PRESSIONAR O ARDUINO
    static String byte_para_mandar;
    Boolean altura_acao, abertura_acao = false;

    public static void repeticao_estado (boolean estado) { //CONTROLO DO ESTADO DO LOOP
        if (estado == true) { //CASO SEJA PRESSIONADO O LOOP VAI SEMPRE CONTINUAR
            status_repeticao.run();
        } else { //CASO LARGUE O BOTAO O LOOP PARA
            garra_repeticao.removeCallbacks(status_repeticao);
        }

    }

    static Runnable status_repeticao = new Runnable() //AQUI SERA EXECUTADO AS TAREFAS QUANDO O DELAY FOR ACIONADO
    {
        @Override
        public void run() {
            mandarByte (byte_para_mandar);
            garra_repeticao.postDelayed(status_repeticao, 100);
        }
    };

    //SUB-PROGRAMAS
    public static void mandarByte (String texto) { //MANDAR BYTE DE INFORMACAO PARA O ROBOT
            if (usbService != null && conectado == true) { //SE TIVER ALGUM DISPOSITIVO CONECTADO, VAI MANDAR O BYTE
                usbService.write(texto.getBytes());
            }
    }

    public void mensagem (String msg) { //APARECER UMA MENSAGEM DE INFORMACAO NO ECRA
        Toast.makeText(this.getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    void acao_garra (String tarefa) { //PROCEDIMENTO PARA FACILITAR O CONTROLO DA GARRA
        if (tarefa == "parar" && conectado == true) {
            repeticao_estado (false);
            altura_acao = false;
            abertura_acao = false;
        } else if (conectado == true) {
            mandarByte("K"); //BYTE PARA DESBLOQUEAR O ACESSO A GARRA
            Utils.delay(5, new Utils.DelayCallback() {
                @Override
                public void afterDelay() {
                    switch (tarefa) {
                        case "subir":
                            byte_para_mandar = "G";
                            altura_acao = true;
                            repeticao_estado (true);
                            break;

                        case "descer":
                            byte_para_mandar = "H";
                            altura_acao = true;
                            repeticao_estado (true);
                            break;

                        case "abrir":
                            byte_para_mandar = "I";
                            abertura_acao = true;
                            repeticao_estado (true);
                            break;

                        case "fechar":
                            byte_para_mandar = "J";
                            abertura_acao = true;
                            repeticao_estado (true);
                            break;
                    }
                }
            });
        }

    }
}
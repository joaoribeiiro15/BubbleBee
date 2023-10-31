#include <BnrOneA.h>   // Bot'n Roll ONE A library
#include <EEPROM.h>    // EEPROM reading and writing
#include <SPI.h>       // SPI communication library required by BnrOne.cpp
#include <Servo.h>
BnrOneA robot;           // declaration of object variable to control the Bot'n Roll ONE A

//constants definition
#define SSPIN  2       // Slave Select (SS) pin for SPI communication

Servo gripper1;
Servo gripper2;

//constants definition
#define SSPIN  2       // Slave Select (SS) pin for SPI communication

//VARIAVEIS GLOBAIS DO PROGRAMA
int ByteRecebido, UltimoByteRecebido;                   //VARIAVEL QUE VAI LER OS BYTES DE MEMORIA RECEBIDOS
bool Pronto = false;                //VARIAVEL QUE DIZ QUE O ROBOT ESTA PRONTO
int altura = 0;
int abertura = 0;
int velocidade_robot = 40;
bool pode_usar_garra = true;
String mensagem = " PAP: 2018/2019 ";

//FUNCOES DA GARRA
void garra_abertura(int valor /*VALOR PERCENTAGEM*/) {
  int abertura = 20;
  switch (valor) {
    
    case 10:
      abertura = 25;
    break;

    case 20:
      abertura = 30;
    break;
    
    case 30:
      abertura = 32;
    break;

    case 40:
      abertura = 35;
    break;

    case 50:
      abertura = 40;
    break;

    case 60:
      abertura = 50;
    break;

    case 70:
      abertura = 60;
    break;

    case 80:
      abertura = 65;
    break;

    case 90:
      abertura = 70;
    break;

    case 100:
      abertura = 80;
    break;
    
  }
  gripper1.write (abertura);
}

void garra_altura(int valor) {
  int altura = 65;
  switch (valor) {
    
    case 10:
      altura = 60;
    break;
    
    case 20:
      altura = 55;
    break;

    case 30:
      altura = 50;
    break;

    case 40:
      altura = 45;
    break;

    case 50:
      altura = 40;
    break;

    case 60:
      altura = 30;
    break;

    case 70:
      altura = 20;
    break;

    case 80:
      altura = 15;
    break;

    case 90:
      altura = 10;
    break;

    case 100:
      altura = 0;
    break;
  }
  gripper2.write(altura);
}

void setup()
{
    Serial.begin(9600);     // set baud rate to 57600bps for printing values at serial monitor.
    robot.spiConnect(SSPIN);   // start SPI communication module
    robot.stop();              // stop motors
    
    gripper1.attach(3);
    gripper2.attach(5);
    
    garra_abertura (0);
    garra_altura (0);
    
    robot.lcd1("   BUUBLE BEE   ");
    robot.lcd2(mensagem);

    delay(3000);

    for (int I = 50; I >= 0; I--) { //EFEITO GARRA A SUBIR
      gripper2.write(I);
      delay (35);
    }

}

void loop()
{
    if (Pronto == false) {
      garra_altura (100);
      garra_abertura (50);
      
      robot.lcd2("  A PROCURA...  ");
      if (Serial.available() > 0) {
          ByteRecebido = Serial.read();
          if (ByteRecebido == 'A') {
            garra_altura (0);
            garra_abertura (0);
            
            int tempo = 250;
            robot.lcd2 ("-    PRONTO    -");
            delay (tempo);
            robot.lcd2 ("--   PRONTO   --");
            delay (tempo);
            robot.lcd2 ("---  PRONTO  ---");
            delay (tempo);
            robot.lcd2 ("---- PRONTO ----");
            delay (tempo);
            robot.lcd2 ("-----PRONTO-----");
            delay (tempo);
            
            robot.lcd2(mensagem);
            Pronto = true;
         }
      }
    } else {
        if (Serial.available() > 0) {
                ByteRecebido = Serial.read();
                
                switch (ByteRecebido) {

                    case 'B': // ANDAR PARA A ESQUERDA
                      robot.move (velocidade_robot / 4, velocidade_robot);
                    break;

                    case 'C': // ANDAR PARA A DIREITA
                      robot.move (velocidade_robot, velocidade_robot / 4);
                     break;
            
                    case 'D': // ANDAR PARA A FRENTE
                      robot.move (velocidade_robot, velocidade_robot);
                      Serial.write ('A');
                    break;

                    case 'E': // ANDAR PARA TRAS
                      robot.move (-velocidade_robot, -velocidade_robot);
                    break;

                    case 'F': // ANDAR PARA TRAS
                      if (UltimoByteRecebido == 'G' ||
                          UltimoByteRecebido == 'H' ||
                          UltimoByteRecebido == 'I' ||
                          UltimoByteRecebido == 'J') {
                            pode_usar_garra = false;
                       } else {
                          robot.stop();
                       }
                    break;

                    case 'G': // SUBIR A GARRA
                      if (altura <= 90 && altura >= 0 && pode_usar_garra == true) {
                        altura = altura + 10;
                        garra_altura (altura);
                      }
                    break;

                    case 'H': // DESCER A GARRA
                      if (altura <= 100 && altura >= 10 && pode_usar_garra == true) {
                        altura = altura - 10;
                        garra_altura (altura);
                      }
                    break;


                    case 'I': // ABRIR A GARRA
                      if (abertura <= 90 && abertura >= 0 && pode_usar_garra == true) {
                        abertura = abertura + 10;
                        garra_abertura (abertura);
                      }
                    break;

                    case 'J': // FECHAR A GARRA
                      if (abertura <= 100 && abertura >= 10 && pode_usar_garra == true) {
                        abertura = abertura - 10;
                        garra_abertura (abertura);
                      }
                    break;

                    case 'K': // PERMITIR PARA USAR A GARRA
                      pode_usar_garra = true;
                    break;

                    case 'L': // DESLIGAR LCD
                      robot.lcd1("");
                      robot.lcd2("");
                    break;

                    case 'M': // LIGAR LCD
                      robot.lcd1("   BUUBLE BEE   ");
                      robot.lcd2(mensagem);
                    break;

                    //APLICAR A QUANTIDADE VELOCIDADE ESCOLHIDA PELO DISPOSITIVO
                    case '1': //10 PERCENTO
                      velocidade_robot = 10;
                    break;

                    case '2': //20 PERCENTO
                      velocidade_robot = 20;
                    break;

                    case '3': //30 PERCENTO
                      velocidade_robot = 30;
                    break;

                    case '4': //40 PERCENTO
                      velocidade_robot = 40;
                    break;

                    case '5': //50 PERCENTO
                      velocidade_robot = 50;
                    break;

                    case '6': //60 PERCENTO
                      velocidade_robot = 60;
                    break;

                    case '7': //70 PERCENTO
                      velocidade_robot = 70;
                    break;

                    case '8': //80 PERCENTO
                      velocidade_robot = 80;
                    break;

                    case '9': //90 PERCENTO
                      velocidade_robot = 90;
                    break;

                    case '0': //100 PERCENTO
                      velocidade_robot = 100;
                    break;
                }
                UltimoByteRecebido = ByteRecebido;
        }
    }
}

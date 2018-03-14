#include <SoftwareSerial.h>
#include <SimpleDHT.h>

SoftwareSerial pmSerial(10, 11); //RX,TX
SoftwareSerial zigbeeSerial(5,6); //RX,TX
SimpleDHT11 dht11;

int dht11_pin = 2;
byte g5_h=0;
byte g5_l=0;
byte dht11_t = 0;
byte dht11_h = 0;
byte pmdata[50];
byte msgHead[]={0xFE,0x08,0x90,0x90,0x01,0x00};
byte msgEnd=0xFF;


void setup() {
  zigbeeSerial.begin(115200);
  G5Init();
}

void loop() {
  delay(30000);
  G5DataAsk();
  G5DataRead();
  DTHRead();
  MsgSend();
}

void G5DataRead(){
   if(pmSerial.available()>0){
    delay(50);
    int lenth=pmSerial.available();
    for(int i=0;i<lenth;i++){
      pmdata[i]=pmSerial.read();
      }
    }
}

void G5DataAsk(){
  byte temp[]={0x42,0x4d,0xe2,0x00,0x00,0x01,0x71};
  for(int t=0;t<=6;t++){
  pmSerial.write(temp[t]);
  }
  }

void G5Init(){
  pmSerial.begin(9600);
  byte ask[]={0x42,0x4d,0xe1,0x00,0x00,0x01,0x70};
  for(int i=0;i<=6;i++){
    pmSerial.write(ask[i]);
  }
}

void DTHRead(){
   dht11.read(dht11_pin, &dht11_t, &dht11_h, NULL);
}
  
void MsgSend(){
  if(pmdata[12]<3){
  for(int i=0;i<=5;i++){
    zigbeeSerial.write(msgHead[i]);
  }
  MsgCheak(pmdata[12]);
  MsgCheak(pmdata[13]);
  MsgCheak(dht11_t);
  MsgCheak(dht11_h);
  zigbeeSerial.write(msgEnd);
  }
}

void MsgCheak(byte num){
  if((num!=0xFF)&&(num!=0xFE)){
    zigbeeSerial.write(num);
  }else{
    num=0xFD;
    zigbeeSerial.write(num);
  }
}



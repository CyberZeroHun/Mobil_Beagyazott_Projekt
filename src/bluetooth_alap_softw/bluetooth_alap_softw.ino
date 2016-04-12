//NEM MŰKÖDIK MÉG
#include <SoftwareSerial.h>

SoftwareSerial bluetooth(10,11); //D2,D3 (RX,TX)

void setup()
{
  Serial.begin(9600);
  bluetooth.begin(9600);
}

void loop(){
  while(bluetooth.available()>0){
    String s =bluetooth.readString();
    Serial.println("Bluetooth valasza: " + s);
  } 
  while(Serial.available()>0){
    String s =Serial.readString();
    Serial.println("Terminalban kuldjuk: " + s);
    bluetooth.println(s);
  }
}

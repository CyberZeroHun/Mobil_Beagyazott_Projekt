void setup()
{
  Serial.begin(115200);
  Serial3.begin(115200);
}

void loop(){
  while(Serial3.available()>0){
    String s =Serial3.readString();
    Serial.println("Revceived from RX3: " + s);
  } 
  while(Serial.available()>0){
    String s =Serial.readString();
    Serial.println("Sent from TX0: " + s);
    Serial3.println(s);
  }
}

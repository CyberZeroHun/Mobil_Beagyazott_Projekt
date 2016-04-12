void setup()
{
  Serial.begin(9600);
  Serial3.begin(9600);
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

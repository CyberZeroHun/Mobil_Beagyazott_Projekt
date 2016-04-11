#include <Servo.h>
#include <NewPing.h>

int view = 120; //max 180;
int kezdo= 90 - (view/2); //180/2=90
int veg = kezdo + view;
int szog = kezdo;
int vezerlojel = 8; //D8 PWM servo vezerlojel
Servo srv;

#define TRIGGER 10
#define ECHO 9
#define MAX_TAV 450
NewPing sonar(TRIGGER, ECHO, MAX_TAV);


void setup() {
  Serial.begin(115200);
  srv.attach(vezerlojel);  
}

void loop(){
  unsigned int tav;
  
  for(szog=kezdo;szog<=veg;szog++){
    srv.write(szog);
    delay(2);
    tav = sonar.ping();
    delay(29);
    
    String ki="Szog: "+(String)(szog);
    ki+=".fok Tav: "+(String)(tav/US_ROUNDTRIP_CM);
    ki+="cm";
    Serial.println(ki);
  }
  
  for(szog=veg;szog>=kezdo;szog--){
    srv.write(szog);
    delay(2);
    tav = sonar.ping_median();
    delay(145); //4*29
           
    String ki="Szog: "+(String)(szog);
    ki+=".fok Tav: "+(String)(tav/US_ROUNDTRIP_CM);
    ki+="cm";
    Serial.println(ki);
  }
}

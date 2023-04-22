#define RX_ELEMENTS 16
#define TX_ELEMENTS 16

#define LIGHT_OFF 0
#define LIGHT_ON 1

#define MOTION_DETECTED 1
#define NO_MOTION 0

#define MANUAL_CONTROL 0
#define MOTION_CONTROL 1
#define DARKNESS_CONTROL 2

#define INVALID_VALUE 255

byte RxBuffer[RX_ELEMENTS] = {0};
/* Rx Buffer
* 0: SOB (Start Of Buffer)
* 1: Living Room LED State
* 2: Living Room LED Brightness
* 3: Kitchen LED State
* 4: Room 1 LED State
* 5: Room 1 LED Red (RGB) Value
* 6: Room 1 LED Green (RGB) Value
* 7: Room 1 LED Blue (RGB) Value
* 8: Room 2 LED State
* 9: Room 2 LED Red (RGB) Value
* 10: Room 2 LED Green (RGB) Value
* 11: Room 2 LED Blue (RGB) Value
* 12: Terrace LED State For Manual Control
* 13: Terrace LED Mode Control
* 14: Bathroom LED State
* 15: EOB (End Of Buffer) */

byte TxBuffer[TX_ELEMENTS] = {0};
/* Tx Buffer
* 0: SOB (Start Of Buffer)
* 1: Motion Sensor Value
* 2: Photoresistor Value
* 15: EOB (End Of Buffer) */

byte ReceivedData[RX_ELEMENTS] = {0};
byte TransmittedData[TX_ELEMENTS] = {0};
byte RecoveryBuffer[TX_ELEMENTS] = {0};

int serial_availability = 0;
int recovery_debounce = 0;

const int living_room_led_pin = 10;
const int kitchen_led_pin = 30;
const int bathroom_led_pin = 31;
const int terrace_led_pin = 32;
const int room1_red_value_led_pin = 11;
const int room1_green_value_led_pin = 12;
const int room1_blue_value_led_pin = 13;
const int room2_red_value_led_pin = 7;
const int room2_green_value_led_pin = 6;
const int room2_blue_value_led_pin = 5;

const int photoresitor_pin = A0;
const int motion_sensor_pin = 47;

int motion_state = NO_MOTION;


void setup() {
  
  Serial.begin(9600);

  pinMode(living_room_led_pin, OUTPUT);
  pinMode(kitchen_led_pin, OUTPUT);
  pinMode(bathroom_led_pin, OUTPUT);
  pinMode(terrace_led_pin, OUTPUT);

  pinMode(room1_red_value_led_pin, OUTPUT);
  pinMode(room1_green_value_led_pin, OUTPUT);
  pinMode(room1_blue_value_led_pin, OUTPUT);

  pinMode(room2_red_value_led_pin, OUTPUT);
  pinMode(room2_green_value_led_pin, OUTPUT);
  pinMode(room2_blue_value_led_pin, OUTPUT);
  
  pinMode(photoresitor_pin, INPUT);
  pinMode(motion_sensor_pin, INPUT);
  TxBuffer[0] = 255;
  TxBuffer[15] = 255;
}

void loop() {

  readSensors();
  
  if(Serial.available() > 0) {
    recovery_debounce = 0;
    
    if(serial_availability == 0) {
      serial_availability = 1;
      writeRecoveryData();
    }
    else {
      communicationWithBluetoothModule();
    }
  }
  else {
    recovery_debounce++;
    if(recovery_debounce == 6) {
      serial_availability = 0;
      recoverLastReceivedData();
    }
  }

  controlLights();

  delay(250);

}

void writeRecoveryData() {
  for(int i = 0; i < TX_ELEMENTS; i++) {
    Serial.write(RecoveryBuffer[i]);
  }
}

void readReceiverBuffer() {
  for(int i = 0; i < RX_ELEMENTS; i++) {
    ReceivedData[i] = Serial.read();
  }
  if(INVALID_VALUE == ReceivedData[0] || INVALID_VALUE == ReceivedData[15]) {
    //do nothing
  }
  else {
    for(int i = 0; i < RX_ELEMENTS; i++) {
      RxBuffer[i] = ReceivedData[i];
    }
  }
}

void writeTransmitterBuffer() {
  for(int i = 0; i < TX_ELEMENTS; i++) {
    Serial.write(TxBuffer[i]);
  }
}

void communicationWithBluetoothModule() {
  readReceiverBuffer();
  writeTransmitterBuffer();
}

void recoverLastReceivedData() {
  for(int i = 0; i < RX_ELEMENTS; i++) {
    RecoveryBuffer[i] = RxBuffer[i];
  }
}

void controlLights() {

  if(LIGHT_ON == RxBuffer[1]) {
    analogWrite(living_room_led_pin, RxBuffer[2]);
  }
  else if(LIGHT_OFF == RxBuffer[1]){
    analogWrite(living_room_led_pin, LIGHT_OFF);
  }

  if(LIGHT_ON ==  RxBuffer[3]) {
    digitalWrite(kitchen_led_pin, LIGHT_ON);
  }
  else if(LIGHT_OFF ==  RxBuffer[3]){
    digitalWrite(kitchen_led_pin, LIGHT_OFF);
  }

  if(LIGHT_ON ==  RxBuffer[14]) {
    digitalWrite(bathroom_led_pin, LIGHT_ON);
  }
  else if(LIGHT_OFF ==  RxBuffer[14]){
    digitalWrite(bathroom_led_pin, LIGHT_OFF);
  }

  if(MANUAL_CONTROL == RxBuffer[13]) {
    digitalWrite(terrace_led_pin, RxBuffer[12]);    
  }
  else if(DARKNESS_CONTROL == RxBuffer[13]) {
    if(TxBuffer[2] < 40) {
      digitalWrite(terrace_led_pin, LIGHT_ON);
    }
    else {
      digitalWrite(terrace_led_pin, LIGHT_OFF);
    }   
  }
  else if(MOTION_CONTROL == RxBuffer[13]) {
    if(MOTION_DETECTED == TxBuffer[1]) {
      digitalWrite(terrace_led_pin, LIGHT_ON);
    }
    else {
      digitalWrite(terrace_led_pin, LIGHT_OFF);
    }
  }
  
  if(LIGHT_ON == RxBuffer[4]) {
    analogWrite(room1_red_value_led_pin, RxBuffer[5]);
    analogWrite(room1_green_value_led_pin, RxBuffer[6]);
    analogWrite(room1_blue_value_led_pin, RxBuffer[7]);
  }
  else if(LIGHT_OFF == RxBuffer[4]) {
    analogWrite(room1_red_value_led_pin, 0);
    analogWrite(room1_green_value_led_pin, 0);
    analogWrite(room1_blue_value_led_pin, 0);    
  }

  if(LIGHT_ON == RxBuffer[8]) {
    analogWrite(room2_red_value_led_pin, RxBuffer[9]);
    analogWrite(room2_green_value_led_pin, RxBuffer[10]);
    analogWrite(room2_blue_value_led_pin, RxBuffer[11]);
  }
  else if(LIGHT_OFF == RxBuffer[8]){
    analogWrite(room2_red_value_led_pin, 0);
    analogWrite(room2_green_value_led_pin, 0);
    analogWrite(room2_blue_value_led_pin, 0);
  }

}

void readSensors() {
  int photoresistor_value = analogRead(photoresitor_pin);
  int pir_sensor_value = digitalRead(motion_sensor_pin);
  
  TxBuffer[2] =  map(photoresistor_value, 0, 1023, 0, 100);

  if(pir_sensor_value == HIGH) {
    if(NO_MOTION == motion_state) {
      motion_state = MOTION_DETECTED;
    }
  } 
  else {
    if(MOTION_DETECTED == motion_state) {
      motion_state = NO_MOTION;
    }
  }
  TxBuffer[1] = motion_state;
}
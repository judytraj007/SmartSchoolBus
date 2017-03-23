#include <ESP8266HTTPClient.h>

#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <SPI.h>
#include <MFRC522.h>
#include<ESP8266WiFi.h>


#define SS_PIN 4
#define RST_PIN 5
#define host http://smartlbus.esy.es/
MFRC522 mfrc522(SS_PIN, RST_PIN); // Create MFRC522 instance.

static const int RXPin = 0, TXPin = 16;
static const uint32_t GPSBaud = 9600;
 
// The TinyGPS++ object
TinyGPSPlus gps;

// The serial connection to the GPS device
double lat,lng,plat=0,plng=0;
float speed;
SoftwareSerial ss(RXPin, TXPin);
void setup() {

  Serial.begin(9600); // Initialize serial communications with the PC
  SPI.begin(); // Init SPI bus
  mfrc522.PCD_Init(); // Init MFRC522 card
  WiFi.mode(WIFI_STA);
  WiFi.begin("smartbus","qwerty123");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
  if(WiFi.status() == WL_CONNECTED){
    Serial.print("Wifi connected");
  }

}

void loop() {


  HTTPClient http;
  http.begin("http://www.smartlbus.esy.es/add.php");
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  while (ss.available() > 0)
   if (gps.encode(ss.read()))
     {
        if (gps.location.isValid())
              {
              lat=gps.location.lat();
              lng=gps.location.lng();
               speed=gps.speed.kmph();
              }
        if(!((lat==plat)&&(lng==plng))){
            String data = "lat1=" + (String)lat + "&lng1=" + (String)lng + "&speed=" + (String)speed + "&busid=101";
            int httpCode = http.POST(data);
            plat=lat;
            plng=lng;
        }
          http.end();
          delay(500);
      }
  
 // Look for new cards
  if ( ! mfrc522.PICC_IsNewCardPresent()) {
  return;
  }

  // Select one of the cards
  if ( ! mfrc522.PICC_ReadCardSerial()) {
    return;
  }

unsigned long UID_unsigned;
UID_unsigned = mfrc522.uid.uidByte[0] << 24;
UID_unsigned += mfrc522.uid.uidByte[1] << 16;
UID_unsigned += mfrc522.uid.uidByte[2] << 8;
UID_unsigned += mfrc522.uid.uidByte[3];

String UID_string = (String)UID_unsigned;
Serial.print(UID_string);
if(UID_string.indexOf("3133482000")!=-1)
  {   
  HTTPClient httpClient;
  httpClient.begin("http://www.smartlbus.esy.es/notify.php");
  httpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
  String data = "child=" + UID_string + "&lat1=" + (String)lat + "&lng1=" + (String)lng;
  int httpCode = httpClient.POST(data);
  httpClient.end();
  }
}


#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Definición de pines del teclado matricial
const byte filas = 4;
const byte columnas = 4;
char teclas[filas][columnas] = {
  {'1','2','3','A'},
  {'4','5','6','B'},
  {'7','8','9','C'},
  {'*','0','#','D'}
};
byte pinFilas[filas] = {4, 5, 6, 7};
byte pinColumnas[columnas] = {8, 9, 10, 11};

// Definición de pines para otros dispositivos
const int pirPin = 2;
const int buzzerPin = 13;

// Variables para almacenar la clave y estado del sistema
String claveCorrecta = "123A"; // Cambia la clave por la que desees
String claveIngresada = "";
bool estadoAlarma = false; // true=activado; false=desactivado
bool alarmaSonando = false;

// Inicialización de la pantalla LCD
LiquidCrystal_I2C lcd(0x27, 16, 2);

void setup() {
  // Inicialización del teclado matricial
  for (byte i = 0; i < filas; i++) {
    pinMode(pinFilas[i], INPUT);
    digitalWrite(pinFilas[i], HIGH);
  }
  for (byte col = 0; col < columnas; col++) {
    pinMode(pinColumnas[col], OUTPUT);
    digitalWrite(pinColumnas[col], HIGH);
  }

  pinMode(pirPin, INPUT);
  pinMode(buzzerPin, OUTPUT);
  digitalWrite(buzzerPin, LOW);

  // Inicialización de la pantalla LCD
  lcd.init();
  lcd.backlight();
  lcd.print("ProyectoF");
  lcd.setCursor(0, 1);
  lcd.print("FU y AH");
  delay(3000);
}

void loop() {
  lcd.clear();
  lcd.setCursor(0, 0);
  char key;

  if (estadoAlarma) {
    if (alarmaSonando) {
      lcd.print("Ingrese clave");
      key = getKey();
      if (key != 0) {
        claveIngresada += key;
        lcd.setCursor(claveIngresada.length() - 1, 1);
        lcd.print('*');

        if (claveIngresada.length() == 4) {
          if (claveIngresada == claveCorrecta) {
            lcd.clear();
            lcd.setCursor(0, 0);
            lcd.print("Desactivando...");
            apagarAlarma();
            delay(1000);

            lcd.clear();
            lcd.setCursor(0, 0);
            lcd.print("Desactivada");
            estadoAlarma = false;
            alarmaSonando = false;
            digitalWrite(buzzerPin, LOW); 
            claveIngresada = "";// Apaga el zumbador
            delay(2000);
          } else {
            lcd.clear();
            lcd.setCursor(0, 0);
            lcd.print("Clave incorrecta");
            delay(2000);
            lcd.clear();
            claveIngresada = "";
          }
        }
      }
    } else {
      if (digitalRead(pirPin) == HIGH) {
        dispararAlarma();
      }
    }
  } else {
    lcd.print("Ingrese clave");
    key = getKey();
    if (key != 0) {
      claveIngresada += key;
      lcd.setCursor(claveIngresada.length() - 1, 1);
      lcd.print('*');

      if (claveIngresada.length() == 4) {
        if (claveIngresada == claveCorrecta) {
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Activando...");
          delay(1000);

          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Activada");
          estadoAlarma = true;
          claveIngresada = "";
          delay(2000);
        } else {
          lcd.clear();
          lcd.setCursor(0, 0);
          lcd.print("Clave incorrecta");
          delay(2000);
          lcd.clear();
          claveIngresada = "";
        }
      }
    }
  }
}

char getKey() {
  char key = 0;
  
  for (byte col = 0; col < columnas; col++) {
    digitalWrite(pinColumnas[col], LOW);
    
    for (byte row = 0; row < filas; row++) {
      if (digitalRead(pinFilas[row]) == LOW) {
        key = teclas[row][col];
        delay(50); // Debounce
        while (digitalRead(pinFilas[row]) == LOW);
      }
    }
    
    digitalWrite(pinColumnas[col], HIGH);
  }
  
  return key;
}

void dispararAlarma() {
  alarmaSonando = true;
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Intruso detectado!");
  digitalWrite(buzzerPin, HIGH);
  tone(buzzerPin, 1000);  // Activa el zumbador a una frecuencia de 1000 Hz
}
void apagarAlarma(){

  alarmaSonando = false;
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Alarma Apagada");
  digitalWrite(buzzerPin, LOW);
  noTone(buzzerPin);
    // Activa el zumbador a una frecuencia de 1000 Hz

}

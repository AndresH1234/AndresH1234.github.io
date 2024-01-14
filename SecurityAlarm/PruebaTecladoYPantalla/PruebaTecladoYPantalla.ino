#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Definir el objeto LCD
LiquidCrystal_I2C lcd(0x27, 16, 2);  // Dirección I2C: 0x27, 16 columnas y 2 filas

const byte numRows = 4; // Número de filas
const byte numCols = 4; // Número de columnas

char keymap[numRows][numCols] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};

byte rowPins[numRows] = {4, 5, 6, 7}; // Conectar las filas al Arduino
byte colPins[numCols] = {8, 9, 10, 11}; // Conectar las columnas al Arduino

void setup() {
  // Inicializar el teclado matricial
  for (byte row = 0; row < numRows; row++) {
    pinMode(rowPins[row], INPUT);
    digitalWrite(rowPins[row], HIGH);
  }

  for (byte col = 0; col < numCols; col++) {
    pinMode(colPins[col], OUTPUT);
    digitalWrite(colPins[col], HIGH);
  }

  // Inicializar la pantalla LCD
  lcd.init();
  lcd.backlight();
  lcd.clear();
}

void loop() {
  char key = getKey();
  
  if (key) {
    lcd.setCursor(0, 0);
    lcd.print("Tecla presionada:");
    lcd.setCursor(0, 1);
    lcd.print(key);
    delay(1000); // Puedes ajustar el tiempo de visualización
    lcd.clear();
  }
}

char getKey() {
  char key = 0;
  
  for (byte col = 0; col < numCols; col++) {
    digitalWrite(colPins[col], LOW);
    
    for (byte row = 0; row < numRows; row++) {
      if (digitalRead(rowPins[row]) == LOW) {
        key = keymap[row][col];
      }
    }
    
    digitalWrite(colPins[col], HIGH);
  }
  
  return key;
}

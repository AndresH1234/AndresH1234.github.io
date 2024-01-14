int pirPin = 2;   // Pin del sensor de movimiento PIR
int buzzerPin = 13;  // Pin del zumbador
int pirState = LOW;  // Inicializa la variable pirState en LOW
unsigned long previousMillis = 0;
const long interval = 5000;  // Tiempo entre cada sonido del zumbador en milisegundos (5 segundos)

void setup() {
  pinMode(pirPin, INPUT);
  pinMode(buzzerPin, OUTPUT);
  digitalWrite(buzzerPin, LOW); 
  digitalWrite(pirPin, LOW); // Asegúrate de que el zumbador esté apagado al inicio
  Serial.begin(9600);
}

void loop() {
  unsigned long currentMillis = millis();
  pirState = digitalRead(pirPin);  // Lee el estado del sensor PIR

  if (pirState == HIGH) {  // Si se detecta movimiento
    Serial.println("Movimiento detectado!");
    tone(buzzerPin, 1000);  // Activa el zumbador a una frecuencia de 1000 Hz
    // Verifica si ha pasado el tiempo del intervalo
    if (currentMillis - previousMillis >= interval) {
      noTone(buzzerPin);  // Apaga el zumbador
      previousMillis = currentMillis;  // Actualiza el tiempo de referencia
    }
  else{
    Serial.println("No movimiento!");
    noTone(buzzerPin); 
  }
  }
}

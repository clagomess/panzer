int cmd;
int pChaveGeral = 2;
int pMotorL = 3;
int pMotorR = 4;
int pMotorLVl = 5;
int pMotorRVl = 6;
int currentVl = 30; //Maximo

void setup(){
  pinMode(pChaveGeral, OUTPUT);
  pinMode(pMotorL, OUTPUT);
  pinMode(pMotorR, OUTPUT);
  pinMode(pMotorLVl, OUTPUT);
  pinMode(pMotorRVl, OUTPUT);
  
  Serial.begin(9600);
  Serial.println("ok");
}

void loop(){
  
  if (Serial.available() > 0) {
    cmd = Serial.read();
    
    // Controla a velocidade
    velocidade(cmd);
    
    //Opcodes de controle
    if((cmd >> 4) == 1 && ((cmd >> 2) & 1) != 0){
      boolean re = false;
      
      // RE
      if(((cmd >> 3) &  1) == 1){
        digitalWrite(pMotorL, HIGH);
        digitalWrite(pMotorR, HIGH);
        re = true;
      }else{
        digitalWrite(pMotorL, LOW);
        digitalWrite(pMotorR, LOW);
      }
      
      // Sair do ponto morto
      if(((cmd & 4) >> 2) == 1){
        analogWrite(pMotorLVl, currentVl);
        analogWrite(pMotorRVl, currentVl);
        digitalWrite(pChaveGeral, HIGH);
      }
      
      //Direcao      
      if((cmd & 3) == 2){
        //esquerda
        if(re){
          digitalWrite(pMotorR, LOW);
        }else{
          digitalWrite(pMotorR, HIGH);
        }
        digitalWrite(pChaveGeral, HIGH);
      }else if((cmd & 1) == 1){
        //direita
        if(re){
          digitalWrite(pMotorL, LOW);
        }else{
          digitalWrite(pMotorL, HIGH);
        }
        digitalWrite(pChaveGeral, HIGH);
      }
    }else{
      resetEngine();
    }
  }
}

void velocidade(int cmd){
  if(cmd == 0x80 && currentVl > 20){
    currentVl -= 20;
  }
  
  if(cmd == 0x81 && currentVl < 120){
    currentVl += 20;
  }
}

void resetEngine(){
    digitalWrite(pChaveGeral, LOW);
    analogWrite(pMotorLVl, currentVl);
    analogWrite(pMotorRVl, currentVl);
    digitalWrite(pMotorL, LOW);
    digitalWrite(pMotorR, LOW);
}

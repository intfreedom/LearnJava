// Receives bluetooth input to control digital out 
// channels 2 - 5

// Initialise variables
int channelTwo = 2;
int channelThree = 3;
int channelFour = 4;
int channelFive = 5;
int bufferInt = -1;

void setup() {

  // Configure channels
  pinMode(channelTwo, OUTPUT);
  pinMode(channelThree, OUTPUT);
  pinMode(channelFour, OUTPUT);
  pinMode(channelFive, OUTPUT);
  //digitalWrite为Arduino中的一种函数，作用是设置引脚的输出电压为高电平或低电平，该函数无返回值，有两个参数pin和value。
  //在使用digitalWrite(pin, value)函数之前要将引脚设置为OUTPUT模式。例：pinMode(x, OUTPUT);其中x为引脚编号。pinMode函数用于设置引脚模式。
  // Set all channels to off
  digitalWrite(channelTwo, LOW);
  digitalWrite(channelThree, LOW);
  digitalWrite(channelFour, LOW);
  digitalWrite(channelFive, LOW);

  // Configure bluetooth connection rate
  Serial.begin(9600); 

}


void loop() {
  // Check for input
  //串口通讯函数Serial.available()：判断串口缓冲区的状态，返回从串口缓冲区读取的字节数。
  if(Serial.available() > 0){
  //串口通讯函数Serial.read()：读取串口数据，一次读一个字符，读完后删除已读数据
    bufferInt = Serial.read();

    if (bufferInt == '0') {
      digitalWrite(channelTwo, LOW);
      digitalWrite(channelThree, LOW);
      digitalWrite(channelFour, LOW);
      digitalWrite(channelFive, LOW);
    } else if (bufferInt == '1') {
      digitalWrite(channelTwo, HIGH);
    } else if (bufferInt == '2') {
      digitalWrite(channelTwo, LOW);
    } else if (bufferInt == '3') {
      digitalWrite(channelThree, HIGH);
    } else if (bufferInt == '4') {
      digitalWrite(channelThree, LOW);
    } else if (bufferInt == '5') {
      digitalWrite(channelFour, HIGH);
    } else if (bufferInt == '6') {
      digitalWrite(channelFour, LOW);
    } else if (bufferInt == '7') {
      digitalWrite(channelFive, HIGH);
    } else if (bufferInt == '8') {
      digitalWrite(channelFive, LOW);
    }

  }

} 

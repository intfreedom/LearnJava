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
  //digitalWriteΪArduino�е�һ�ֺ������������������ŵ������ѹΪ�ߵ�ƽ��͵�ƽ���ú����޷���ֵ������������pin��value��
  //��ʹ��digitalWrite(pin, value)����֮ǰҪ����������ΪOUTPUTģʽ������pinMode(x, OUTPUT);����xΪ���ű�š�pinMode����������������ģʽ��
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
  //����ͨѶ����Serial.available()���жϴ��ڻ�������״̬�����شӴ��ڻ�������ȡ���ֽ�����
  if(Serial.available() > 0){
	//����ͨѶ����Serial.read()����ȡ�������ݣ�һ�ζ�һ���ַ��������ɾ���Ѷ�����
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

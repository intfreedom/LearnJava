package c07.innerscopes;
//An anonymous inner class that performs

public class Parcel8 {
	//Argument must be final to use inside
	//anonymous inner class;
	public Destination dest(final String dest) {
		System.out.println("testtesttest");
		return new Destination(){
			private String label = dest;
			public String readLabel() {
				System.out.println("String: "+label);
				return label;
				}
			
		};
	}
	
	public static void main(String[] args) {
		Parcel8 p = new Parcel8();
		Destination d = p.dest("Tanzania");
		System.out.println("VSVSVStesttesttest");
		d.readLabel();
	}
}
//testtesttest
//VSVSVStesttesttest
//String: Tanzania
//���������Parcel6�У����ã�����ʹ��c1.MyContents.value()�����ã�
//��Ϊ�����d��Ȼ���ص������ࣻ��Parcel6.java�У�Ҳ����c1�Ѿ����ص�notice c1 return new MyContents();

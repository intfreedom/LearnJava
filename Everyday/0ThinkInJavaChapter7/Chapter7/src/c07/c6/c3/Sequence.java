package c07.c6.c3;

interface Selector{
	boolean end();
	Object current();
	void next();
}

public class Sequence {
	private Object[] o;
	private int next = 0;
	public Sequence(int size) {
		o = new Object[size];
	}
	public void add(Object x) {
		if(next < o.length) {
			o[next] = x;
			next++;
		}
	}
	private class SSelector implements Selector{
		int i = 0;
		public boolean end() {
			return i  == o.length;//�ж�i�Ƿ����o.length��o.length��10�̶��ģ�i�ǲ��ϵ����ģ�
		}
		public Object current() {
			return o[i];
		}
		public void next() {
			if(i<o.length) i++;
		}
	}
	
	public Selector getSelector() {
		return new SSelector();
	}
	
	public static void main(String[] args) {
		Sequence s = new Sequence(10);
		System.out.println(s.o.length+"**********");
		for(int i=0;i<10;i++)
			s.add(Integer.toString(i));//����Ϊ��ҪInteger.toString();
		Selector s1 = s.getSelector();
		while(!s1.end()) {
			System.out.println("O.length: "+s.o.length+s1.end());
			System.out.println((String)s1.current());
			s1.next();
		}
	}
}


//���ر�ʾָ��������{@code String}���� ������ת��Ϊ�����ŵ�ʮ���Ʊ�ʾ��ʽ����Ϊ�ַ������أ���������ͻ���10��Ϊ{@link #toString��int��int��}�����Ĳ���һ����
//@param��Ҫת����������
//@return������nbsp; 10�еĲ������ַ�����ʾ��
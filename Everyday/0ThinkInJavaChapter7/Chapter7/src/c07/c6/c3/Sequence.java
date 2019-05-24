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
			return i  == o.length;//判断i是否等于o.length，o.length是10固定的；i是不断递增的；
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
			s.add(Integer.toString(i));//这里为何要Integer.toString();
		Selector s1 = s.getSelector();
		while(!s1.end()) {
			System.out.println("O.length: "+s.o.length+s1.end());
			System.out.println((String)s1.current());
			s1.next();
		}
	}
}


//返回表示指定整数的{@code String}对象。 参数将转换为带符号的十进制表示形式并作为字符串返回，就像参数和基数10作为{@link #toString（int，int）}方法的参数一样。
//@param我要转换的整数。
//@return基数＆nbsp; 10中的参数的字符串表示。
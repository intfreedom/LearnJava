package exceptionDemo;

public class TrowTest {
	public static void main(String[] args) {
		try {throw new ArithmeticException();
		}catch(ArithmeticException ae) {
			System.out.println(ae);
		}
		try {throw new ArrayIndexOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException ai) {
			System.out.println(ai);
		}
		try {
			throw new StringIndexOutOfBoundsException();
		}catch(StringIndexOutOfBoundsException si) {
			System.out.println(si);
		}
	}

}

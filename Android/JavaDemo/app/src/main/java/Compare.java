public class Compare {
    public static void main(String[] args){
        String c1=new String("abc");
        String c2=new String("abc");
        String c3=c1;
        System.out.println("the result of C2==C3 is:"+(c2==c3));
        System.out.println("the result of c2.equals(c3) is :"+(c2.equals(c3)));
    }
}

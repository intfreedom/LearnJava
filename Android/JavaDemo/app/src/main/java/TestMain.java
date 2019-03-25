public class TestMain {
    int i=47;
    public void call(){
        System.out.println("Use call");
        for(i=0;i<3;i++){
            System.out.println(i+" ");
            if(i==2){
                System.out.println("\n");
            }
        }
    }

    public TestMain(){

    }
    public static void main(String[] args){
        TestMain t1=new TestMain();
        TestMain t2=new TestMain();
        t2.i=60;
        System.out.println("the first i is: "+t1.i++);
        t1.call();
        System.out.println("the second example is: "+t2.i);
        t2.call();
    }
}

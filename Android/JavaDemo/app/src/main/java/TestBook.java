public class TestBook{
    public static void moveDish(int level,char from, char inter, char to){
        if(level == 1){
            System.out.println("start " + from +" move to number 1 "+to);
        }
        else{
            moveDish(level-1,from,to,inter);
            System.out.println("start "+from+" move to disk " + level+" to "+to);
            moveDish(level-1,inter,from,to);
        }
    }

    public static void main(String[] args){
        int nDishs = 3;
        moveDish(nDishs,'A','B','C');
    }
}

//import java.util.Random;
//
//public class TestBook {
//    public static void main(String[] args){
//        String[] titles = {"Java","Python","C"};
//        for(int i=0;i<5;i++){
//            new Book(titles[new Random().nextInt(3)]);
//        }
//        System.out.println("the total is " + Book.getCounter() + "book!");
//
//    }
//}
//class Book{
//    private static int counter = 0;
//    public Book(String title){
//        System.out.println("sell book: "+title);
//        counter++;
//    }
//    public static int getCounter(){
//        return counter;
//    }
//}
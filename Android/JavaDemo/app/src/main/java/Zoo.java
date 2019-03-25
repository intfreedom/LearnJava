public class Zoo {
    public static void main(String[] args){
        Dog dog=new Dog();
        System.out.println("dog.cry result: ");
        dog.cry();
    }
}

class Dog extends Animal{
    public Dog(){
    }

    public void cry(){
        System.out.println("Dog eat");
    }
}


class Cat extends Animal{
    public Cat(){

    }
    public void cry(){
        System.out.println("Cat mimi");
    }
}
class Animal{
    public Animal(){
    }

    public void cry(){
        System.out.println("animal sound");
    }
}

class Sheep extends Animal{

}
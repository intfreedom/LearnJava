public class Cire implements ICalculate {
    public float getArea(float r){
        float area=PI*r*r;
        return area;
    }

    public float getCircumference(float r){
        float circumference=2*PI*r;
        return circumference;
    }

    public static void main(String[] args)
    {
        System.out.println("123");
        Cire cr = new Cire();
        System.out.println(cr.getCircumference(3));

    }
}


interface ICalculate{
    final float PI=3.14159f;
    float getArea(float r);
    float getCircumference(float r);
}

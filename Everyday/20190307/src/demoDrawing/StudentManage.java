package demoDrawing;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class StudentManage {

    public static void main(String[] args) {
        JFrame sf = new JFrame("Student Information System");//creat a window called sf
        FlowLayout fl = new FlowLayout(); //Use flow layout
        sf.setLayout(fl);//Modify layout management
        sf.setSize(600, 500); //the size of window
        sf.setLocation(200,200);//Set the initial position of the window
        
        JButton jb1 = new JButton("Button1"); //Create a button
        sf.add(jb1); //Put button jb1 into the window
        JButton jb2 = new JButton("Button2");//Create a button
        sf.add(jb2);//Put button jb2 into the window
        
        sf.setVisible(true); //Display window
        sf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Close the window
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import Map.earthFull;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Tests the fullEarth class
 * Expected results:
 * Earth is scaled to input size
 * Red line at sydney
 * Magenta line at equator (wrapping around)
 * Green line at london
 * Yellow line a honolulu
 * 
 * @author toddbodnar
 */
public class earthFullTests extends JPanel{
    public earthFullTests(int x,int y)
    {
        try {
            world = new earthFull(x,y, new Color(0,0,1.0f,0));
        } catch (IOException ex) {
            Logger.getLogger(earthFullTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void paint(Graphics g)
    {
        world.draw(g);
    }
    public earthFull world;
    
    public static void main(String args[])
    {
        int HEIGHT = Integer.parseInt(JOptionPane.showInputDialog(null, null, "Height", 0));
        int WIDTH = Integer.parseInt(JOptionPane.showInputDialog(null, null, "Width", 0));
        
        JFrame frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        earthFullTests test = new earthFullTests(WIDTH, HEIGHT);
        for(double lon = -1;lon<=1;lon+=0.01)
        {
            test.world.addPoint(Color.red, new Point2D.Double(lon+151.5, -33.8));
            test.world.addPoint(Color.magenta, new Point2D.Double(lon+180, 0));
            test.world.addPoint(Color.green, new Point2D.Double(lon+0.12, 51.5));
            test.world.addPoint(Color.yellow, new Point2D.Double(lon-157.8, 21.3));
        }
        
        frame.add(test);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import Map.earthFull;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import twitter.tweet;

/**
 *
 * @author toddbodnar
 */
public class mapProcess implements twitterprocess{
    public mapProcess(Point2D.Double[] box, boolean visual, int width, String name, float exp) throws IOException
    {
        double w = box[0].getX() - box[1].getX();
        double h = box[0].getY() - box[1].getY();
        
        int height = (int)Math.abs(1.0*width*h/w);
        
        map = new earthFull(width,height,Color.BLACK,box);
        this.name = name;
        exposure = exp;
        errors = 0;
    }
    @Override
    public void consume(tweet t) {
        try{
        map.addPoint(new Color(1.0f,1.0f,1.0f,exposure), t.loc);
        }catch(Exception e){errors++;System.err.println(e);e.printStackTrace();System.err.println(t.text);}
    }

    @Override
    public String end() {
        System.out.println("Saving");
        try
        {
            BufferedImage img = new BufferedImage(map.getDimensions().width, map.getDimensions().height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            map.draw(g);
            ImageIO.write(img, "png", new File(name+".png"));
            System.out.println("Done");
        }
        catch(IOException e)
        {
            System.out.println("Save Error: "+e);
            return name+" saving error:  "+e.toString();
        }
        
       
        
        return name+":    "+errors+" error(s)";
    }
    
    public boolean quitAtEnd()
    {
        return true;
    }
    private earthFull map;
    private String name = "map";
    private float exposure;
    private int errors;
}

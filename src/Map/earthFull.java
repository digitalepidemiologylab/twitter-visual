/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author toddbodnar
 */
public class earthFull implements map{
    //private final RenderedImage detail;
    /**
     * Constructor
     * @param width the width of the object in pixels
     * @param height the height of ...
     */
    public earthFull(int width, int height) throws IOException
    {
        this(width,height,new Color(0,0,0,0));
    }
    
    
    public earthFull(int width, int height, Color c) throws IOException
    {
        world = ImageIO.read(source).getScaledInstance(width, height, Image.SCALE_SMOOTH);
        canvas = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = canvas.getGraphics();
        g.setColor(c);
        g.fillRect(0,0,width,height);
        this.width= width;
        this.height = height;
        
        box = new Point2D.Double[2];
        
        box[0] = new Point2D.Double(-180, 90);
        box[1] = new Point2D.Double(180,-90);
    }
    
    /**
     * Same as above, but box[] is a bounding box (long/lat) of the area to view
     */
    public earthFull(int width, int height, Color c, Point2D.Double box[]) throws IOException
    {
        int widthfull = -(int) (1.0*360/((-box[1].x+box[0].x))*width);
        
        int heightfull = (int) (1.0*180/((-box[1].y+box[0].y))*height);
        
        world = new BufferedImage(width,height, BufferedImage.TYPE_INT_BGR);
        Graphics gworld = world.getGraphics();
            
        if(false)  //todo: replace with a test to see how far we are zoomed in!
        {
            
            //see : http://www.java.net/node/672134
            
            
            //todo: make work for other parts of the world!!!
            System.out.println("Detected high zoom level\nGetting high def map");
            ImageReader reader = ImageIO.getImageReadersByFormatName("PNG").next();
            System.out.println(reader);
            ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream("/Users/toddbodnar/Desktop/worlda1.png"));
            System.out.println(iis);
            //iis = ImageIO.createImageInputStream(new FileInputStream("/Users/toddbodnar/Desktop/10000_med.png"));
            
            System.out.println(new File("/Users/toddbodnar/Desktop/world.200407.3x21600x21600.panels.png/world.200407.3x21600x21600.A1.png").length());
            
            reader.setInput(iis);
            ImageReadParam param = reader.getDefaultReadParam();
            
            int x1,x2,y1,y2;
            int iwidth = 21600;
            
            x1 = (int)((180+box[0].x)*iwidth/180);
            y1 = (int)((90-box[0].y)*iwidth/90);
            x2 = (int)((180+box[1].x)*iwidth/180);
            y2 = (int)((90-box[1].y)*iwidth/90);
            
         //   x1=y1=2350;
           // x2=y2=2750;
            
            System.out.println(x1+","+x2+","+(x2-x1)+","+(y2-y1));
            
            param.setSourceRegion(new Rectangle(x1,y1,x2-x1,y2-y1));
            BufferedImage out = reader.read(0, param);
            if(!ImageIO.write(out, "png", new File("background.png")))
                    System.err.println("Error writing background!");
            
            gworld.setColor(Color.magenta);
            gworld.fillRect(0, 0, width, height);
            gworld.drawImage(out.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,0, null);
            
            System.out.println("Finished high def map selection");
        }
        else if(c.getAlpha() == 0 || true)
        {
            world = new BufferedImage(1,1, BufferedImage.TYPE_INT_BGR);
            gworld = world.getGraphics();
            gworld.setColor(c);
            gworld.fillRect(0,0,width,height);
          
        }
        else
        {
            Image whole = ImageIO.read(source).getScaledInstance(widthfull, heightfull, Image.SCALE_SMOOTH);
            gworld.setColor(Color.magenta);
            gworld.fillRect(0, 0, width, height);
            gworld.drawImage(whole, -(int)(1.0*widthfull*(180 + box[0].x)/360), -(int)(1.0*heightfull*(90 - box[0].y)/180), null);
        }
        canvas = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        Graphics g = canvas.getGraphics();
        g.setColor(c);
        g.fillRect(0,0,width,height);
        this.width= width;
        this.height = height;
        this.box = box;
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(world, 0, 0, null);
        g.drawImage(canvas,0,0,null);
    }

    @Override
    public void addPoint(Color c, Point2D location) {
        Graphics g = canvas.getGraphics();
        g.setColor(c);
        
        int widthfull = Math.abs((int) (1.0*360/((-box[1].x+box[0].x))*width));
        
        int heightfull = Math.abs((int) (1.0*180/((-box[1].y+box[0].y))*height));
        
        
        
        double x = (((location.getX()+180))*width/360);
        double y = ((-location.getY()+90)*height/180);
        
        
        x*= 1.0*widthfull/width;
        y*= 1.0*heightfull/height;
        
        x = x-(widthfull*(180 + box[0].x)/360);
        y = y-(heightfull*(90 - box[0].y)/180);
        
        
        
        g.fillRect((int)x, (int)y, 1, 1 );
    }
    
    public void addRectangle(Color c, Point2D[] bounds)
    {
        Graphics g = canvas.getGraphics();
        g.setColor(c);
        
        int widthfull = Math.abs((int) (1.0*360/((-box[1].x+box[0].x))*width));
        
        int heightfull = Math.abs((int) (1.0*180/((-box[1].y+box[0].y))*height));
        
        
        
        double x1 = (((bounds[0].getX()+180))*width/360);
        x1*=1.0*widthfull/width;
        double y1 = ((-bounds[0].getY()+90)*height/180);
        y1*=1.0*heightfull/height;
        
        double x2 = (((bounds[1].getX()+180))*width/360);
        x2*=1.0*widthfull/width;
        double y2 = ((-bounds[1].getY()+90)*height/180);
        y2*=1.0*heightfull/height;
        
        
 
        
        x1 = x1-(widthfull*(180 + box[0].x)/360);
        y1 = y1-(heightfull*(90 - box[0].y)/180);
        
        
        x2 = x2-(widthfull*(180 + box[0].x)/360);
        y2 = y2-(heightfull*(90 - box[0].y)/180);
        
        
        
        g.fillRect((int)x1, (int)y1, (int)Math.abs(x2-x1)+1, (int)Math.abs(y2-y1)+1 );
    }
    
    private Image world;
    private BufferedImage canvas;
    private final int width,height; 
    private Point2D.Double box[];
    
    /*
     * Download your own from http://earthobservatory.nasa.gov/Features/BlueMarble/BlueMarble_monthlies.php
     * The resolution / month does not matter
     */
    public static final File source = new File("/Users/toddbodnar/Desktop/world.200408.3x5400x2700.jpg");

    @Override
    public Dimension getDimensions() {
        return new Dimension(width,height);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experiments;

import Map.earthFull;
import Map.mapPanel;
import cleanData.Strip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import twitter.getTweet;
import twitter.*;

/**
 * Generates something like the lights at night earth map, but using tweets
 * @author toddbodnar
 */
public class nightMap {
    private static void processGZ(File f, mapPanel p) throws IOException
    {
        GZIPInputStream gzipin = new GZIPInputStream(new FileInputStream(f));
        BufferedReader in = new BufferedReader(new InputStreamReader(gzipin));
        
        while(true)
        {
            String result = in.readLine();
            if(result == null)
                break;
            if(result.equals(""))
                continue;
            try {
                twitter.tweet tweet = new twitter.tweet(result, twitter.tweet.DATAMINING_TWEET);
                
                if(tweet.boundingbox == null)
                     p.Map.addPoint(new Color(1.0f,1.0f,1.0f,.1f), tweet.loc);
                else
                {
                 //    System.err.print( tweet.boundingbox[0].x - tweet.boundingbox[1].x);
                   //  System.err.println(","+(tweet.boundingbox[0].y - tweet.boundingbox[1].y));
                     count++;
                     
                     double area = Math.abs((tweet.boundingbox[0].x - tweet.boundingbox[1].x)*(tweet.boundingbox[0].y - tweet.boundingbox[1].y));
                     
                     p.Map.addRectangle(new Color(1.00f,1.0f,1.00f,(float)Math.pow(Math.min(Math.max((1 / (area*800*800*1/10)),0),1),4)), tweet.boundingbox);
                }
                
            } catch (Exception ex) {
                System.err.println(result);
                System.err.println(tweet.preprocess(result));
                Logger.getLogger(Strip.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("\n\n ---------------- \n\n");
            }
            
        }
    }
    
    static double width=0,height=0,count=0;
    
    private static void processFile(File f, mapPanel p) throws IOException
    {
        if(f.isDirectory())
        {
          
            File list[] = f.listFiles();
            for( int ct=0;ct<list.length;ct++ )
            {
                processFile(list[ct], p);
                
            }
            
        }
        else
        {
            
            if(f.getName().split("\\.")[Math.max(f.getName().split("\\.").length-1,0)].equalsIgnoreCase("gz"))
            {
                processGZ(f,p);
               
            
            }
        }
    }
    
    public static void main(String args[]) 
    {
         JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showOpenDialog(null);
       
        
            try {
                System.out.println(jfc.getSelectedFile());
                run(jfc.getSelectedFile());
            } catch (IOException ex) {
                Logger.getLogger(nightMap.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
    public static void run(File f) throws IOException
    {
        Point2D.Double box[] = new Point2D.Double[2];
        
        box[0] = new Point2D.Double(-180, 80);
        box[1] = new Point2D.Double(-60,20);
        
        box[0] = new Point2D.Double(-140, 65);
        box[1] = new Point2D.Double(-30,15);
        

        box[0] = new Point2D.Double(-119.543152, 34.184542);        //L.A.  (2x1)
        box[1] = new Point2D.Double(-117.170105,33.36265);
        
              
        
        box[0] = new Point2D.Double(-123.448792, 38.337348);        //San Fran (1x1)
        box[1] = new Point2D.Double(-121.004334,37.129666);
        
        
        
        box[0] = new Point2D.Double(-88.393478, 42.184776);        //Chicago (1x1)
        box[1] = new Point2D.Double(-87.222061,41.558949);
        
        
        
        box[0] = new Point2D.Double(-77.094898, 38.972222);        //DC (1x2)
        box[1] = new Point2D.Double(-76.939373,38.820719);
        
        
        
        
    //    box[0] = new Point2D.Double(-74.255333, 40.817447);        //NY (1x1)
    //    box[1] = new Point2D.Double(-73.719063,40.509101);
        
        
        
    //    box[0] = new Point2D.Double(-77.927599, 40.830307);        //SC (1x1)
    //    box[1] = new Point2D.Double(-77.795076,40.751028);
        
        //todo: houston, ny, sce?
        
        
        
        
      //  box[0] = new Point2D.Double(-180,90);
        //box[1] = new Point2D.Double(180, -90);
        
        mapPanel panel = new mapPanel(new earthFull(800,1600, new Color(0.0f,0.0f,0.0f,1.0f), box));
        
        JFrame frame = new JFrame();
        
        JScrollPane scr = new JScrollPane(panel);
       
        frame.add(scr);
        frame.setSize(1600, 800);
        
        
      //  JFileChooser jfc = new JFileChooser();
      //  jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //jfc.showOpenDialog(null);
        
       // System.out.println(jfc.getSelectedFile());
        
        
        /*tweet tweets[] = getTweet.getRandomTweets(100);
        for(tweet t: tweets)
            panel.Map.addPoint(new Color(.5f,.5f,.5f,.5f), t.loc);*/
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        Thread t = new Thread(new refresh(frame));
        t.start();
      //  processFile(jfc.getSelectedFile(),panel);
       // processFile(new File("/Users/toddbodnar/data/"), panel);
        
        processFile(f, panel);
        System.out.println("Saving");
        
        BufferedImage img = new BufferedImage(800, 1600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        panel.Map.draw(g);
        
        System.out.println("Avg box size: "+(width/count)+","+(height/count));
        
        try
        {
            ImageIO.write(img, "png", new File("map"+f.getName()+".png"));
            System.out.println("Done");
        }
        catch(IOException e)
        {
            System.out.println("Save Error: "+e);
        }
    }
    
    private static class refresh implements Runnable
    {
        public refresh(JFrame fr)
        {
            f = fr;
        }
        @Override
        public void run() {
            while(true)
            {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(nightMap.class.getName()).log(Level.SEVERE, null, ex);
                }
                f.repaint();
            }
        }
        JFrame f;
    }
}

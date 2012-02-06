/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanData;

import java.io.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.swing.JFileChooser;
import twitter.tweet;

/**
 * Prints out the first line of a selected .gz file
 * If the selected file is a folder, it will process any files in it
 * 
 * This may be useful for when we clean the data and put it on the server
 * 
 * @author toddbodnar
 */
public class Strip {
    private static void processGZ(File f) throws IOException
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
                twitter.tweet tweet = new twitter.tweet(result);
                
                System.out.print(tweet.tweetId+";"+tweet.user_id+";"+tweet.screen_name+";"+tweet.time+";"+tweet.area_full_name+";"+tweet.place_type+";");
                if(tweet.boundingbox == null)
                {
                    System.out.print(tweet.loc.x+","+tweet.loc.y+"; null ; null ;");
                }
                else
                    System.out.print("null ; " + tweet.boundingbox[0].x + ","+tweet.boundingbox[0].y +";"+tweet.boundingbox[1].x + ","+tweet.boundingbox[1].y+";");
                
                System.out.println(tweet.text);
                
            } catch (Exception ex) {
                System.err.println(result);
                System.err.println(tweet.preprocess(result));
                Logger.getLogger(Strip.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
            
        }
    }
    
    private static void processFile(File f) throws IOException
    {
        if(f.isDirectory())
        {
          
            File list[] = f.listFiles();
            for( int ct=0;ct<list.length;ct++ )
            {
                processFile(list[ct]);
                
            }
            
        }
        else
        {
            
            if(f.getName().split("\\.")[Math.max(f.getName().split("\\.").length-1,0)].equalsIgnoreCase("gz"))
            {
                processGZ(f);
               
            
            }
        }
    }
            
    public static void main(String args[]) throws IOException
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showOpenDialog(null);
        
        System.out.println("ID; user id; screen name; time ; area full name ; type of place ; point location ; bounding box location 1 ; bounding box location 2; text");
        
        processFile(jfc.getSelectedFile());
        
       // System.out.println("Discovered a total of "+total_tweets+" tweets in "+jfc.getSelectedFile()+".");
    }
    
    static long total_tweets = 0;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cleanData;

import java.io.*;
import java.util.zip.GZIPInputStream;
import javax.swing.JFileChooser;

/**
 * Prints out the first line of a selected .gz file
 * If the selected file is a folder, it will process any files in it
 * 
 * This may be useful for when we clean the data and put it on the server
 * 
 * @author toddbodnar
 */
public class FirstLine {
    private static void processGZ(File f) throws IOException
    {
        GZIPInputStream gzipin = new GZIPInputStream(new FileInputStream(f));
        BufferedReader in = new BufferedReader(new InputStreamReader(gzipin));
        
        System.out.println(in.readLine());
        System.out.println(in.readLine());
       /* long total_in_f = 0;
        
        while(true)
        {
            if(in.readLine() == null)
                break;
            total_in_f++;
        }
        
        in.close();
        
        total_tweets += total_in_f / 2;     //divide by two because there is a blank line between tweets
        * 
        */
    }
    
    private static void processFile(File f) throws IOException
    {
        if(f.isDirectory())
        {
            System.out.println("Going into directory: "+f);
            File list[] = f.listFiles();
            for( int ct=0;ct<list.length;ct++ )
            {
                processFile(list[ct]);
                
            }
            System.out.println("Leaving directory: "+f);
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
        
        processFile(jfc.getSelectedFile());
        
       // System.out.println("Discovered a total of "+total_tweets+" tweets in "+jfc.getSelectedFile()+".");
    }
    
    static long total_tweets = 0;
}

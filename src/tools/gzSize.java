/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import javax.swing.JFileChooser;

/**
 * Determines the uncompressed size of a file, or recursivly if a folder is selected
 * @author toddbodnar
 */
public class gzSize {
    private static void process(File f, boolean head) throws IOException
    {
        if(f.isDirectory())
        {
            File list[] = f.listFiles();
            for( int ct=0;ct<list.length;ct++ )
            {
                process(list[ct], false);
                if(head)
                    System.out.println((ct+1)+"/"+list.length);
            }
        }
        else
        {
            long length = -1;
            
            if(f.getName().split("\\.")[Math.max(f.getName().split("\\.").length-1,0)].equalsIgnoreCase("gz"))
            {
                // from http://www.abeel.be/content/determine-uncompressed-size-gzip-file
                RandomAccessFile raf = new RandomAccessFile(f, "r");
                raf.seek(raf.length() - 4);
                int b4 = raf.read();
                int b3 = raf.read();
                int b2 = raf.read();
                int b1 = raf.read();
                int val = (b1 << 24) | (b2 << 16) + (b3 << 8) + b4;
                length = val;
                raf.close();
               
            }
            else
                length = f.length();
            
            bytes = bytes.add(new BigInteger(length+""));
        }
    }
    public static void main(String args[]) throws IOException
    {
        bytes = BigInteger.ZERO;
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jfc.showOpenDialog(null);
        process(jfc.getSelectedFile(),true);
        System.out.println(bytes.toString());
    }
    public static BigInteger bytes;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import java.awt.Color;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter.tweet;

/**
 *
 * @author toddbodnar
 */
public class colorGrabber implements twitterprocess{

    public colorGrabber(File out) throws IOException
    {
        output = out;
        output.delete();
        o = new OutputStreamWriter(new FileOutputStream(output, true));
        o.write("user_id\tname\tscreen_name\tlink_color\tborder_color\tfill_color\ttext_color\tuses_background_image\tlink_hue\tlink_sat\tlink_brightness\tborder_hue\tborder_sat\tborder_brightness\tfill_hue\tfill_sat\tfill_brightness\ttext_hue\ttext_sat\ttext_brightness\n");
    }
    
    public void consume(tweet t) {
        try{
            String s = t.user.id_str+"\t";
            s+=t.user.name+"\t";
            s+=t.user.screen_name+"\t";
            s+=t.user.profile_link_color+"\t";
            s+=t.user.profile_sidebar_border_color+"\t";
            s+=t.user.profile_sidebar_fill_color+"\t";
            s+=t.user.profile_text_color+"\t";
            s+=t.user.profile_use_background_image+"\t";
            s+=getHSV(t.user.profile_link_color)+"\t";
            s+=getHSV(t.user.profile_sidebar_border_color)+"\t";
            s+=getHSV(t.user.profile_sidebar_fill_color)+"\t";
            s+=getHSV(t.user.profile_text_color)+"\n";
            
            o.write(s);
        }
        catch(Exception e){System.err.println(e.toString()); e.printStackTrace();errors++;}
    }

    private String getHSV(String color)
    {
        if(color.equals(""))
            return "-1\t-1\t-1";
            
         Color c = new Color(Integer.parseInt(color,16));
         float vals[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
         return vals[0]+"\t"+vals[1]+"\t"+vals[2];
    }
    
    
    @Override
    public String end() {
        
        try {
            o.flush();
            o.close();
        } catch (IOException ex) {
            Logger.getLogger(colorGrabber.class.getName()).log(Level.SEVERE, null, ex);
            return "colorGrabber: Saving Error \""+ex.getMessage();
        }
        long length = output.length();
        long bytes, kb, mb;
        
        bytes = length%1024;
        length /=1024;
        
        kb = length%1024;
        length /=1024;
        
        mb = length;
        
        return "colorGrabber: errors:"+errors+"\nOutput size: "+mb+" mb "+kb+" kb "+bytes+" bytes";
    }

    @Override
    public boolean quitAtEnd() {
        return true;
    }
    
    int errors;
    File output;
    OutputStreamWriter o;
}

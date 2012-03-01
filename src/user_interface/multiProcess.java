/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import twitter.tweet;

/**
 *
 * @author toddbodnar
 */
public class multiProcess implements twitterprocess{
    public multiProcess()
    {
        processes = new LinkedList<twitterprocess>();
    }
    
    public void add(twitterprocess t)
    {
        processes.add(t);
    }
    
    @Override
    public void consume(tweet t) {
        for(twitterprocess tp : processes)
            tp.consume(t);
    }

    @Override
    public String end() {
        String s = "";
        for(twitterprocess tp : processes)
            s+=tp.end()+'\n';
        
        return s;
    }

    @Override
    public boolean quitAtEnd() {
        for(twitterprocess tp : processes)
            if(tp.quitAtEnd())
                return true;
        
        return false;
    }
    
    private List<twitterprocess> processes;
    
    
    /**
     * Adds location processes for LA, San Francisco, New York, DC, and Chicago
     */
    public void addLocations() throws IOException
    {
        
       
        Point2D.Double box[] = new Point2D.Double[2];
        
        box[0] = new Point2D.Double(-180, 80);
        box[1] = new Point2D.Double(-60,20);
         
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-140, 65);
        box[1] = new Point2D.Double(-30,15);
        
        
        
              
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-123.448792, 38.337348);        //San Fran (1x1)
        box[1] = new Point2D.Double(-121.004334,37.129666);
        add(new mapProcess(box, false, 3200, "SanFran",0.1f));
        
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-119.543152, 34.184542);        //L.A.  (2x1)
        box[1] = new Point2D.Double(-117.170105,33.36265);
        add(new mapProcess(box, false, 6400, "LA",0.1f));
        
        
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-88.393478, 42.184776);        //Chicago (1x1)
        box[1] = new Point2D.Double(-87.222061,41.558949);
        add(new mapProcess(box, false, 3200, "Chicago",0.1f));
        
        
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-77.094898, 38.972222);        //DC (1x2)
        box[1] = new Point2D.Double(-76.939373,38.820719);
        add(new mapProcess(box, false, 3200, "DC",0.1f));
        
        
        box = new Point2D.Double[2];      
        box[0] = new Point2D.Double(-74.255333, 40.817447);        //NY (1x1)
        box[1] = new Point2D.Double(-73.719063,40.509101);
        add(new mapProcess(box, false, 3200, "NY",0.1f));
        
        
        //low exposure
        /*
         box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-119.543152, 34.184542);        //L.A.  (2x1)
        box[1] = new Point2D.Double(-117.170105,33.36265);
        add(new mapProcess(box, false, 3200, "LA_low",0.01f));
        
              
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-123.448792, 38.337348);        //San Fran (1x1)
        box[1] = new Point2D.Double(-121.004334,37.129666);
        add(new mapProcess(box, false, 1600, "SanFran_low",0.01f));
        
        
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-88.393478, 42.184776);        //Chicago (1x1)
        box[1] = new Point2D.Double(-87.222061,41.558949);
        add(new mapProcess(box, false, 1600, "Chicago_low",0.01f));
        
        
        box = new Point2D.Double[2];
        box[0] = new Point2D.Double(-77.094898, 38.972222);        //DC (1x2)
        box[1] = new Point2D.Double(-76.939373,38.820719);
        add(new mapProcess(box, false, 1600, "DC_low",0.01f));
        
        
        box = new Point2D.Double[2];      
        box[0] = new Point2D.Double(-74.255333, 40.817447);        //NY (1x1)
        box[1] = new Point2D.Double(-73.719063,40.509101);
        add(new mapProcess(box, false, 1600, "NY_low",0.01f));*/
        
    }
    
}

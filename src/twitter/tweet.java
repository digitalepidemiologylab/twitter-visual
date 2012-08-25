/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.DateFormatter;
import toddSon.JSonObject;

/**
 *
 * @author toddbodnar
 */
public class tweet {
    
    private String getElement(String input, String key)
    {
        return getElement(input, key, "\"", 1, false);
    }
    
    private String getElement(String input, String key, boolean last)
    {
        return getElement(input, key, "\"", 1, last);
    }
    
    private String getElement(String input, String key, String breakStr, int skip, boolean last)
    {
        StringTokenizer st = new StringTokenizer(input, breakStr);
        String result = "";
        while(st.hasMoreTokens())
        {
            if(st.nextToken().equalsIgnoreCase(key))
            {
                for(int ct=0;ct<skip;ct++)
                    st.nextToken();
                result = st.nextToken();
                if(!last)
                    return result;
            }
        }
        return result;
    }
    
    public static String preprocess(String input)
    {
        return input.replace("\\\"", "''").replace("\\/", "/");
    }
    
    private void initDummy(String input)
    {
        int total = 0;
        String full = "";
        for(int ct=0;ct<input.length();ct++)
        {
            total++;
            full+=input.charAt(ct);
            
            if(Math.random()<.1)
                full="";
        }
        user = new user();
    }
    
    private void initDataMining(String input)
    {
       tweetId = (new StringTokenizer(input,"|")).nextToken();
       input = input.substring(tweetId.length()+2); //remove leading tweet id | and {
       input = input.substring(0, input.length()-1); //remove the trailing }
       
       JSonObject full = new JSonObject(input);
       JSonObject coordinates = null;
       
       if(full.contains("coordinates")&&!full.get("coordinates").equals("null"))
            coordinates = new JSonObject(full.get("coordinates"));
       
      
       
       
       
       
           
       if(full.contains("place") && !full.get("place").equals("null"))
       {
            JSonObject place = new JSonObject(full.get("place"));
            place_type = place.get("place_type");
            area_name = place.get("name");
            country_code = place.get("country_code");
            area_full_name = place.get("full_name");
            country = place.get("country");
            place_type = place.get("place_type");
            coordinates = new JSonObject(place.get("bounding_box"));
       }
       
           
           
       JSonObject entities = new JSonObject(full.get("entities"));
       
       {
           Pattern p;
           String part = entities.get("user_mentions");
                
                p = Pattern.compile("\"id_str\":\"[0-9]+\"");
                
                Matcher m = p.matcher(part);
                
                int total = 0;
                while(m.find())
                    total++;
                
                
                mentionId = new String[total];
                
           //     System.out.println(total);
         //       System.out.println(m.groupCount());
                m.reset();
                for(int ct=0;ct<mentionId.length;ct++)
                {
                    m.find();
                    String s = m.group().substring(10);
           //         System.out.println(s);
                    mentionId[ct] = s.replaceAll("\"", "");
                }
       }
       
       text = full.get("text");
       
       user = new user(full.get("user"));
       
       if(!full.get("created_at").equals("null"))
       {
            String Time = full.get("created_at");
        
            
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            try {
                time = formatter.parse(Time).getTime();
            } catch (ParseException ex) {
                Logger.getLogger(tweet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
       }
       
       
       if(coordinates != null)
       {
           String coords = "";
           if(coordinates.get("type").equals("Polygon"))
           {
               coords = coordinates.get("coordinates");
           }
           else if(coordinates.get("type").equals("Point"))
           {
               coords = "[["+coordinates.get("coordinates")+','+coordinates.get("coordinates")+','+coordinates.get("coordinates")+','+coordinates.get("coordinates")+"]]";
           }
           else
           {
               System.err.println("unknown coordinate type: "+ coordinates.get("type"));
               System.err.println(input+"\n\n\n"+coordinates+"\n\n\n\n\n");
           }
           StringTokenizer st = new StringTokenizer(coords.replaceAll("\\[*\\]*",""), ",");
           
           boundingbox = new Point2D.Double.Double[2];
           boundingbox[0] = new Point2D.Double.Double(999999, 999999);
           boundingbox[1] = new Point2D.Double.Double(-999999, -999999);
           
           for(int ct=0;ct<4;ct++)
           {
               double x = Double.parseDouble(st.nextToken());
               double y = Double.parseDouble(st.nextToken());
               
               if(x < boundingbox[0].x)
                   boundingbox[0].x = x;
               if(x > boundingbox[1].x)
                   boundingbox[1].x = x;
               
               if(y < boundingbox[0].y)
                   boundingbox[0].y = y;
               if(y > boundingbox[1].y)
                   boundingbox[1].y = y;
           }
           
           loc = new Point2D.Double.Double((boundingbox[0].x+boundingbox[1].x)/2, (boundingbox[0].y+boundingbox[1].y)/2);
       }
       else
           System.err.println(input+"\n\n\n ------- \n"+full+"\n\n\n");
    }
    
    public tweet(String input, int type)
    {
        switch(type)
        {
            case DATAMINING_TWEET:
                initDataMining(input);
                break;
            
            case DUMMY_TWEET:
                initDummy(input);
        }
    }
    
    public tweet(String input) throws ParseException
    {
        input = preprocess(input);
        
        tweetId = (new StringTokenizer(input,"|")).nextToken();
        
        area_name = getElement(input, "name");
        country = getElement(input,"country");
        area_full_name = getElement(input,"full_name");
        place_type = getElement(input, "place_type");
        country_code = getElement(input, "country_code");
        
        if(input.contains("\"type\":\"Polygon\",\"coordinates\""))
        {
            
            String geo_full = getElement(input, "bounding_box","\"", 3, false);
            if(geo_full.equals("Polygon"))
                geo_full = getElement(input, "bounding_box", "\"", 6, false);
            
            geo_full = geo_full.replaceAll("[\\[\\]\\}:]", "");
        
            StringTokenizer st = new StringTokenizer(geo_full, ",");
            
            String points[] = new String[2];
            boundingbox = new Point2D.Double[2];
            points[0] = st.nextToken()+','+st.nextToken();
            st.nextToken();
            st.nextToken();
            points[1] = st.nextToken()+','+st.nextToken();
            
            for(int ct=0;ct<2;ct++)
            {
                st = new StringTokenizer(points[ct],",");
                boundingbox[ct] = new Point2D.Double.Double(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
            }
            
            loc = new Point2D.Double.Double((boundingbox[0].x+boundingbox[1].x)/2, (boundingbox[0].y+boundingbox[1].y)/2);
        }
        else if(input.contains("\"type\":\"Point\",\"coordinates\""))
        {
            String geo_full = getElement(input, "coordinates", "\"}", 0, false);
            if(geo_full.equals(":{"))
                geo_full = getElement(input, "coordinates", "\"}", 6, false);
           // System.err.println(geo_full);
            geo_full = geo_full.replaceAll("[\\{\\}\\[:\\]]", "");
            
            StringTokenizer st = new StringTokenizer(geo_full, ",");
            loc = new Point2D.Double.Double(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
           // System.err.println(loc.x+","+loc.y);
        }else if(input.contains("\"bounding_box\":{\"coordinates\":[[["))
        {
            String geo_full = getElement(input, "bounding_box","\"", 2, false);
            
            geo_full = geo_full.replaceAll("[\\[\\]\\}:]", "");
        
            StringTokenizer st = new StringTokenizer(geo_full, ",");
            
            String points[] = new String[2];
            boundingbox = new Point2D.Double[2];
            points[0] = st.nextToken()+','+st.nextToken();
            st.nextToken();
            st.nextToken();
            points[1] = st.nextToken()+','+st.nextToken();
            
            for(int ct=0;ct<2;ct++)
            {
                st = new StringTokenizer(points[ct],",");
                boundingbox[ct] = new Point2D.Double.Double(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
            }
            
            loc = new Point2D.Double.Double((boundingbox[0].x+boundingbox[1].x)/2, (boundingbox[0].y+boundingbox[1].y)/2);
        }
        
        
        friendCount = Integer.parseInt(getElement(input, "friends_count", "\":,", 0, false).replaceAll("\\}",""));
        
        {
            String Time = getElement(input, "created_at", "\"", 1, true);
        
            
            DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
            time = formatter.parse(Time).getTime();
            
        }
        
        text = getElement(input, "text", "\"", 1, true);
        screen_name = getElement(input, "screen_name");
       // name = getElement(input, "name", true); does not work
        user_id = getElement(input, "id_str");
        
        {
            Pattern p = Pattern.compile("\"user_mentions\":\\[\\{.*\\}\\]\\}");
            Matcher m = p.matcher(input);
            
            if(m.find())
            {
                String part = m.group();
             //   System.out.println(part);
                
                p = Pattern.compile("\"id_str\":\"[0-9]+\"");
                
                m = p.matcher(part);
                
                int total = 0;
                while(m.find())
                    total++;
                
                
                mentionId = new String[total];
                
           //     System.out.println(total);
         //       System.out.println(m.groupCount());
                m.reset();
                for(int ct=0;ct<mentionId.length;ct++)
                {
                    m.find();
                    String s = m.group().substring(10);
           //         System.out.println(s);
                    mentionId[ct] = s.replaceAll("\"", "");
                }
            }
            else
                mentionId = new String[0];
        }
    }
    
    public tweet(String uid, String text, Point2D.Double loc, long time)
    {
        user_id = uid;
        this.text = text;
        this.loc = loc;
        this.time = time;
    }
    
    public String toString()
    {
        String s =  tweetId+"\n"+area_name+" "+ country+" : "+ area_full_name + " " 
                + country_code + "  " + place_type + "\n"+
                (boundingbox == null? loc.x+","+loc.y:boundingbox[0].x+","
                +boundingbox[0].y+"  "+boundingbox[1].x+","+boundingbox[1].y)
        
                +"\n\n"
                +"\n\nTime "+time+" : ''"+text+"''\n"
                + user +"\n\nMentioned: ";
        
        for(String st : mentionId)
            s = s+st+",";
        
        return s;
    }
    
    
    //place
    public Point2D.Double loc;
    public Point2D.Double boundingbox[];
    public String area_name, country, area_full_name, place_type, country_code;
    
    //user
    public boolean is_translator, geo_enabled;
    public String time_zone, description;
    public int friendCount, followerCount;
    public long signUp;
    public String user_id;
    public String name, screen_name, language;
    
    
    public long time;
    public String tweetId;
    public String text;
    public String mentionId[];
    public user user;
    
    public static final int DATAMINING_TWEET = 0, DUMMY_TWEET = 1;
    
    
}

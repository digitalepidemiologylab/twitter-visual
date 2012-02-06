/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import java.awt.geom.Point2D;

/**
 * Handles communication with the database
 * 
 * Todo: set up database access, currently it just returns random data
 * @author toddbodnar
 */
public class getTweet {
    public static void setIP(String ipAddress)
    {
        ip = ipAddress;
    }
    
    
    /*
     * Returns an array of randomly sampled tweets
     */
    public static tweet[] getRandomTweets(int number)
    {
        tweet tweets[] = new tweet[number];
        
        for(int ct=0;ct<number;ct++)
        {
            tweets[ct] = new tweet("FAKE DATA", "THIS IS DUMMY DATA AND SHOULD NOT BE USED FOR ANYTHING OTHER THAN TESTING", new Point2D.Double(Math.random()*360-180, Math.random()*9),0);
        }
        
        return tweets;
    }
    
    private static String ip, name;
    
}

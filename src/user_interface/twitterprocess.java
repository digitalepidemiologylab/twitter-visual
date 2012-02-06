/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import twitter.tweet;

/**
 * 
 * @author toddbodnar
 */
public interface twitterprocess {
    
    /*
     * After being parsed, the tweet will be fed to this twitter process
     */
    public void consume(tweet t);
    
    /*
     * This is called after all of the tweets have been processed
     */
    public String end();
    
    public boolean quitAtEnd();
}

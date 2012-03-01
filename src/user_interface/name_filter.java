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
public class name_filter implements twitterprocess{

    name_filter(String userName, twitterprocess next)
    {
        pipe = next;
        username = userName;
    }
    @Override
    public void consume(tweet t) {
        if(t.user.screen_name.equals(username))
        {
            pipe.consume(t);
            System.out.println(t);
        }
    }

    @Override
    public String end() {
        return pipe.end();
    }

    @Override
    public boolean quitAtEnd() {
        return pipe.quitAtEnd();
    }
    twitterprocess pipe;
    String username;
}

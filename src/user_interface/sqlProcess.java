/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter.tweet;

/**
 *
 * @author toddbodnar
 */
public class sqlProcess implements twitterprocess{
    
    public sqlProcess()
    {
        try {
             Class.forName("org.gjt.mm.mysql.Driver");
        
           // connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample_day", "root","");
          //  stat = connection.prepareStatement("INSERT INTO tweets (text,tweetId,x_coord,y_coord,time) VALUES(?,?,?,?,?)");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample_day", "root","");
            stat = connection.prepareStatement("INSERT INTO allTweets (text,tweetId,x_1,y_1,time,x_2,y_2,userId) VALUES(?,?,?,?,?,?,?,?)");
        } catch (Exception ex) {
            Logger.getLogger(sqlProcess.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-10);
        }
    }
    
    public static void main(String args[]) throws Exception
    {
        Class.forName("org.gjt.mm.mysql.Driver");
        
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample_day", "root","");
        
        PreparedStatement stat = connection.prepareStatement("INSERT INTO tweets (text) VALUES(?)");
        
        stat.setString(1, "Test");
        
        stat.executeUpdate();
    }

    @Override
    public void consume(tweet t) {
        
        try {
            stat.setString(1, t.text);
            stat.setLong(2, Long.parseLong(t.tweetId));
            stat.setDouble(3, t.boundingbox[0].getX());
            stat.setDouble(4, t.boundingbox[0].getY());
            stat.setTimestamp(5, new Timestamp(t.time));
            stat.setDouble(6, t.boundingbox[1].getX());
            stat.setDouble(7, t.boundingbox[1].getY());
            stat.setString(8, t.user.id_str);
            stat.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(sqlProcess.class.getName()).log(Level.SEVERE, null, ex);
            errors++;
        } catch (Exception e){
            System.err.println(e);
            errors++;
        }
      
    }

    @Override
    public String end() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(sqlProcess.class.getName()).log(Level.SEVERE, null, ex);
//            return "Error closing SQL: "+ex.toString();
        }
        return "SQL Closed: "+errors+" errors";
    }

    @Override
    public boolean quitAtEnd() {
        return true;
    }
    private Connection connection;
    private PreparedStatement stat;
    private int errors = 0;
}

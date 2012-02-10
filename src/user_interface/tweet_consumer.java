/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package user_interface;

import cleanData.Strip;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import twitter.tweet;

/**
 *
 * @author toddbodnar
 */
public class tweet_consumer {
    private static void getFiles(List<File> files, File current)
    {
        if(current.isDirectory())
            for(File f : current.listFiles())
                getFiles(files,f);
        if(current.toString().endsWith(".gz"))
            files.add(current);
    }
    public static void main (String args[]) throws FileNotFoundException, IOException
    {   
        File dataset = full;
        boolean progress = false, buffStat = false, endSms = false;
        for(String arg : args)
        {
            if(arg.equalsIgnoreCase("help"))
            {
                System.out.println("Twitter Data Reader\n\nCommand\t\tResult\n-------\t\t------\n\nhelp\t\tshows this screen");
                System.out.println("r\t\tGenerates a rough copy (less data)\np\t\tDisplays a JProgressBar\nb\t\tDisplays Buffer Stats\nt\t\tSends an SMS at the end of execution");
                System.exit(0);
            }
            if(arg.equalsIgnoreCase("r"))
            {
                dataset = rough;
            }
            if(arg.equalsIgnoreCase("p"))
                progress = true;
            if(arg.equalsIgnoreCase("b"))
            {
                progress = true;
                buffStat = true;
            }
            if(arg.equals("t"))
                endSms = true;
        }
        
        List<File> files = new LinkedList<File>();
        getFiles(files,dataset);
        
        Collections.shuffle(files);
        
        for(File f:files)
            System.out.println(f);
        
         BlockingQueue<String> input = new ArrayBlockingQueue<String>(100);
         BlockingQueue<tweet> queue = new ArrayBlockingQueue<tweet>(100);
    
        
        JLabel time_remaining = new JLabel("Time Remaining: TBD");
        JProgressBar prog = new JProgressBar();
        prog.setMaximum(files.size());
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout((buffStat?7:2),1));
        frame.add(prog);
        frame.add(time_remaining);
        if(buffStat)
        {
            frame.add(new JSeparator(JSeparator.HORIZONTAL));
            JProgressBar inbuf,tbuf;
            inbuf = new JProgressBar();
            tbuf = new JProgressBar();
            inbuf.setMaximum(100);
            tbuf.setMaximum(100);
            frame.add(new JLabel("Input Buffer"));
            frame.add(inbuf);
            frame.add(new JLabel("Tweet Buffer"));
            frame.add(tbuf);
            
            new Thread(new buffer_update(input,queue,inbuf,tbuf)).start();
        }
        frame.setSize(600, 150);
        prog.setStringPainted(true);
        prog.setValue(0);
        frame.setVisible(progress);
        
        
        Point2D.Double box[] = new Point2D.Double[2];
        
        
        box[0] = new Point2D.Double(-119.543152, 34.184542);        //L.A.  (2x1)
        box[1] = new Point2D.Double(-117.170105,33.36265);
        
        
        twitterprocess tp = new multiProcess();
        ((multiProcess)tp).addLocations();
        // public mapProcess(Point2D.Double[] box, boolean visual, int width) throws IOException
   
        
        twit_process_driver driver = new twit_process_driver(tp, queue, endSms);
        
        Thread t = new Thread(driver);
        t.start();
        
        
        twit_parse_driver tpd[] = new twit_parse_driver[2];
        
        for(int ct=0;ct<2;ct++)
        {
            tpd[ct] = new twit_parse_driver(input,queue);
            t = new Thread(tpd[ct]);
            t.start();
        }
        
        long startTime = System.currentTimeMillis();
        long last = 0;
        for (int ct = 0; ct < files.size(); ct++) {
            File current = files.get(ct);
            GZIPInputStream gzipin = new GZIPInputStream(new FileInputStream(current));
            BufferedReader in = new BufferedReader(new InputStreamReader(gzipin));
        
            startTime = System.currentTimeMillis();
            while (true) {
                String result = in.readLine();
                if (result == null) {
                    break;
                }
                if (result.equals("")) {
                    continue;
                }
                try {
                    input.put(result);
                    

                } catch (Exception ex) {
                    System.err.println(result);
                    System.err.println(tweet.preprocess(result));
                    Logger.getLogger(Strip.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("\n\n ---------------- \n\n");
                }

            }
            
            int timeSoFar = (int)(System.currentTimeMillis() - startTime);
            int timePerElement = timeSoFar / (ct+1);
            int timeRemaining = timePerElement*(files.size()-ct-1);
            
            int h,m,s;
            
            s = (timeRemaining/1000)%60;
            m = (timeRemaining/1000/60)%60;
            h = timeRemaining/1000/60/60;
            
            long timeSinceLast = System.currentTimeMillis() - startTime;
            if(last == 0)
                last = timeSinceLast;
            else
                last = (long)(.9*last + .1*timeSinceLast);
            
            timeRemaining = (int) (last * (files.size()-ct-1));
            
            s = (timeRemaining/1000)%60;
            m = (timeRemaining/1000/60)%60;
            h = timeRemaining/1000/60/60;
            
            time_remaining.setText("Time Remaining: "+h+" hours "+m+" minutes "+s+" seconds");
            
            prog.setValue(ct+1);
            
            System.gc();
        }
        
        for(int ct=0;ct<2;ct++)
        {
            tpd[ct].done = true;
        }
        System.out.println(input.size()+","+queue.size());
        while(!input.isEmpty())
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(tweet_consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        driver.done = true;
        
        //it is up to the twitterprocess to stop the program
    }
    
    private static class twit_parse_driver implements Runnable
    {
        public twit_parse_driver(BlockingQueue<String> in, BlockingQueue<tweet> out)
        {
            this.in = in;
            this.out = out;
        }
        
        
        public void run() {
            while((!done) || (!in.isEmpty()))
            {
              //  System.out.println(tweets.size());
                try {
                    tweet t = new tweet(in.poll(10, TimeUnit.SECONDS));
                    out.put(t);
                    tweetCount++;
                } catch (InterruptedException ex) {
                    //Logger.getLogger(tweet_consumer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e){System.err.println(e.toString());Logger.getLogger(tweet_consumer.class.getName()).log(Level.SEVERE, null, e);}//bad, I know
            }
        }
        
        
        BlockingQueue<String> in;
        BlockingQueue<tweet> out;
        public boolean done = false;
        
        
    }
    
    private static class buffer_update implements Runnable
    {
        public buffer_update(BlockingQueue<String> in, BlockingQueue<tweet> out, JProgressBar inb, JProgressBar outb)
        {
            this.in = in;
            this.out = out;
            
            this.inb = inb;
            this.outb = outb;
        }
        
        
        public void run() {
            while(true)
            {
              //  System.out.println(tweets.size());
                try {
                    inb.setValue(in.size());
                    outb.setValue(out.size());
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(tweet_consumer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e){}//bad, I know
            }
        }
        
        
        BlockingQueue<String> in;
        BlockingQueue<tweet> out;
        JProgressBar inb,outb;
        public boolean done = false;
        
        
    }
    
    private static class twit_process_driver implements Runnable
    {
        public twit_process_driver(twitterprocess tp, BlockingQueue<tweet> tweetQueue, boolean textatend)
        {
            t = tp;
            tweets = tweetQueue;
            textAtEnd = textatend;
        }
        
        
        private twitterprocess t;
        private BlockingQueue<tweet> tweets;
        private boolean done = false, textAtEnd;

        public void run() {
            long time = System.currentTimeMillis();
            while((!done) || (!tweets.isEmpty()))
            {
              //  System.out.println(tweets.size());
                try {
                    tweet tw = tweets.poll(10, TimeUnit.SECONDS);
                    
                    if(tw == null)
                        continue;
                    
                    t.consume(tw);
                } catch (InterruptedException ex) {
                    //Logger.getLogger(tweet_consumer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception e){}//bad, I know
            }
            String report = t.end();
            
            report = "Total tweets: "+tweetCount+"\n\n"+report;
            
            System.out.println(report);
            
            time = System.currentTimeMillis() - time;
            
            time /= 1000;
            
            report = time%60 +" seconds\n\nAdditional Info:\n"+report;
            
            time = time / 60;
            
            report = time%60 +" minutes "+report;
            
            time /=60;
            
            report = time +" hours "+report;
            
            System.out.println(report);
            
            if(textAtEnd)
                mailer.sendText("Job Complete", report);
                
            if(t.quitAtEnd())
                System.exit(0);
            
        }
    }
    
    static long tweetCount = 0;
    
    static File full = new File("/Users/toddbodnar/data/");
    static File rough = new File("/Users/toddbodnar/data/20110420/SB_DATA_SB/");
}

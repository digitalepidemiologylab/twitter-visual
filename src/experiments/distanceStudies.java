/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experiments;

import cells.seir;
import cells.sir;
import cells.unit;
import java.util.Vector;
import topologies.boundedGrid;
import topologies.map;

/**
 *
 * @author toddbodnar
 */
public class distanceStudies {
    public static void main(String args[])
    {
        /*
         * 
         * here we are trying to find P(get sick | someone is sick within distance x of you)
         * for various values of x
         * 
         * also try P(get sick | no one sick within distance)
         *          
         * slightly more difficult problem:
         *          P(get sick | i sick people within distance)
         */
        
        
        for(int ct=0;ct<100;ct++)
        //for (int realDistance = 1; realDistance < 10; realDistance +=3)
        {
            int realDistance = 5;
            for(int attemptedDistance = 1; attemptedDistance < 10; attemptedDistance +=1)
            {
                map world = new boundedGrid(200,200);
                world.fillAll(new sir(.01f/realDistance/realDistance,.01f,world,realDistance));
                
                Vector<unit> units = world.getAllUnits();
                
                int sick = 0;
                for(unit u: units)
                {
                    if(Math.random() < 0.001)
                    {
                        ((seir)u).infect();
                        sick++;
                    }
                }
                int timeNearSick=0;
                int timeNotNearSick = 0;
                int timeGetSick=0;
                int timeNearSickAll =0;
                int timeRandomSick = 0;
                for(int t=0;t<100;t++)
                {
                    for(unit u: units)
                        u.doStuff();
                    
                    for(unit u: units)
                    {
                        if(((seir)u).current != seir.state.S)
                            continue;
                        
                        Vector<unit> near = world.getNear(u.getLocation(), attemptedDistance);
                        
                        boolean nearsick = false;
                        for(unit n: near)
                        {
                            if(((seir)n).current == sir.state.I)
                            {
                                nearsick = true;
                                timeNearSickAll++;
                            }
                        }
                        
                        if(nearsick)
                        {
                            timeNearSick ++;
                            if(((seir)u).next == sir.state.I)
                                timeGetSick++;
                        }
                        else
                        {
                            timeNotNearSick++;
                            if(((seir)u).next == sir.state.I)
                                timeRandomSick++;
                        }
                    }
                    
                    for(unit u: units)
                        u.update();
                    
                }
                
                int totalsick = 0;
                
                for(unit u: units)
                {
                    if(((seir)u).current!=seir.state.S)
                        totalsick++;
                }
                
                System.out.println(realDistance+","+attemptedDistance+","+timeNearSick+","+timeGetSick+","+timeNearSickAll+","+(1.0*timeGetSick/timeNearSick)+","+(1.0*timeGetSick/timeNearSickAll)+","+(1.0*timeRandomSick/timeNotNearSick)+","+totalsick);
                
            }
        }
    }
}

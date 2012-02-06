/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author toddbodnar
 */
public class mapPanel extends JPanel{
    public mapPanel(map m)
    {
        Map = m;
        
        setSize(m.getDimensions());
        setMinimumSize(m.getDimensions());
        setPreferredSize(m.getDimensions());
    }
    
    public void paint(Graphics g)
    {
        Map.draw(g);
    }
    public map Map;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;

/**
 *
 * @author toddbodnar
 */
public interface map {
    public void draw(Graphics g);
    public void addPoint(Color c, Point2D location);
    public void addRectangle(Color c, Point2D[] bounds);
    public Dimension getDimensions();
}

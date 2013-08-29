/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto2d;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Jorge
 */
public class torre implements Shape{

    GeneralPath torre = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    private float x;
    private float y;
    private float w;
    private float h;
    
    public torre(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    public GeneralPath criarTorre(){
        
        float x0 = x;
        float y0 = y;
        float x1 = x;
        float y1 = y + h;
        float x2 = x + w;
        float y2 = y + h;
        float x3 = x + w;
        float y3 = y;
        
        
        torre.moveTo(x1, y1);
        torre.lineTo(x2, y2);
        torre.lineTo(x3, y3);
        torre.lineTo(x0, y0);
        
        torre.closePath();
        
        return torre;
    }

    public Rectangle getBounds() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public Rectangle2D getBounds2D() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public boolean contains(double x, double y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public boolean contains(Point2D p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public boolean intersects(double x, double y, double w, double h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public boolean intersects(Rectangle2D r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public boolean contains(double x, double y, double w, double h) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public boolean contains(Rectangle2D r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public PathIterator getPathIterator(AffineTransform at) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto2d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

/**
 *
 * @author Jorge
 */


class Star implements Shape{
 
    GeneralPath star = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    private int x;
    private int y;
    private int w;
    private int h;
    
    public Star(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    public GeneralPath criastar(){
        
        float x0 = x + w / 2;
        float y0 = y;
        float x1 = x + w / 3;
        float y1 = y + h / 3;
        float x2 = x;
        float y2 = y + h / 3;
        float x3 = x + w / 4;
        float y3 = y + 2*(h / 3.2f);//y +h/2;
        float x4 = x + w/6;
        float y4 = y + h;
        float x5 = x + w/2;
        float y5 = y + 3*(h/4);
        float x6 = x + (h-(h/6));
        float y6 = y + h;
        float x7 = x + (h-(h/4));
        float y7 = y + 2*(h/3.2f);
        float x8 = x + w;
        float y8 = y + h / 3;
        float x9 = x + (h-(h/3));
        float y9 = y + h/3;
        
        
        
        
              
        star.moveTo(x0, y0);
        star.lineTo(x1, y1);
        star.lineTo(x2, y2);
        star.lineTo(x3, y3);
        star.lineTo(x4, y4);
        star.lineTo(x5, y5);
        star.lineTo(x6, y6);
        star.lineTo(x7, y7);
        star.lineTo(x8, y8);
        star.lineTo(x9, y9);
        
        
        
        star.closePath();
        
        return star;
    }
    
  public boolean contains(Rectangle2D rect) {
    return star.contains(rect);
  }

  public boolean contains(Point2D point) {
    return star.contains(point);
  }

  public boolean contains(double x, double y) {
    return star.contains(x, y);
  }

  public boolean contains(double x, double y, double w, double h) {
    return star.contains(x, y, w, h);
  }

  public Rectangle getBounds() {
    return star.getBounds();
  }

  public Rectangle2D getBounds2D() {
    return star.getBounds2D();
  }

  public PathIterator getPathIterator(AffineTransform at) {
    return star.getPathIterator(at);
  }

  public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return star.getPathIterator(at, flatness);
  }

  public boolean intersects(Rectangle2D rect) {
    return star.intersects(rect);
  }

  public boolean intersects(double x, double y, double w, double h) {
    return star.intersects(x, y, w, h);
  }
}
        
        
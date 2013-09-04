/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.TransformGroup;

/**
 *
 * @author Jorge
 */
public class Projecto3D extends Applet{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainFrame(new Projecto3D(), 400, 400);
    }
    
    public void init(){
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        Canvas3D cv = new Canvas3D(gc);
        setLayout(new BorderLayout());
        add(cv, BorderLayout.CENTER);
        
        BranchGroup bg = createSceneGraph();
        bg.compile();
        
        SimpleUniverse su = new SimpleUniverse(cv);
        su.getViewingPlatform().setNominalViewingTransform();
        su.addBranchGraph(bg);
        
        
    }
    private BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        root.addChild(tg);
        
        //Object
        
        
        
        
        
        return root;
        
    }
}

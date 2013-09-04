/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Jorge
 */
public class Projecto3D extends Applet{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainFrame(new Projecto3D(), 790, 600);
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
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        root.addChild(tg);
        
        //Object
        Appearance ap = new Appearance();
        ap.setMaterial(new Material());
        ColoringAttributes ca = new ColoringAttributes();
        ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        ap.setColoringAttributes(ca);
                
        // blue
        Appearance blueApp = new Appearance();
        blueApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        Color3f blueColor = new Color3f(0.3f, 0.3f, 1.0f);
        ColoringAttributes blueCA = new ColoringAttributes();
        blueCA.setColor(blueColor);
        blueApp.setColoringAttributes(blueCA);
         
         
        // medium blue
        Appearance medBlueApp = new Appearance();
        Color3f medBlueColor = new Color3f(0.1f, 0.8f, 1.0f);
        ColoringAttributes medBlueCA = new ColoringAttributes();
        medBlueCA.setColor(medBlueColor);
        medBlueApp.setColoringAttributes(medBlueCA);
 
        // orange
        Appearance orangeApp = new Appearance();
        orangeApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        Color3f orangeColor = new Color3f(0.988f, 0.800f, 0.080f);
        ColoringAttributes orangeCA = new ColoringAttributes();
        orangeCA.setColor(orangeColor);
        orangeApp.setColoringAttributes(orangeCA);
        
        //margem cima
        Box margemCima = new Box(3.5f, 0.1f,0.2f, medBlueApp);
//        margemCima.setUserData(new String("Margem de Cima"));
        Transform3D trMC = new Transform3D();
        trMC.setTranslation(new Vector3f(0f,5.1f,0.1f));
        TransformGroup tgMC = new TransformGroup(trMC);
        tgMC.addChild(margemCima);
        
        //margem baixo
        
        
        //margem direita
        
        
        //margem esquerda
        
        
        
        
        //tampo
        Box tampo = new Box(3.3f, 5.0f, 0.01f, medBlueApp);
        Transform3D trTampo = new Transform3D();
        trTampo.set(new Vector3f(0f, 1f, 0f), 0.1f);
        TransformGroup tgTampo = new TransformGroup(trTampo);
        tgTampo.addChild(tampo);
        tgTampo.addChild(tgMC);
        tg.addChild(tgTampo);
        
        //Rotação
        Alpha alpha = new Alpha(-1, 4000);
        RotationInterpolator rodar = new RotationInterpolator(alpha, tg);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rodar.setSchedulingBounds(bounds);
        tg.addChild(rodar);
        
//        //Fundo
//        Background bb = new Background(0f, 1f, 1f);
//        bb.setApplicationBounds(bounds);
//        root.addChild(bb);
        
//        //criar rotação behavior
//        MouseRotate behavior = new MouseRotate();
//        behavior.setTransformGroup(tg);
//        root.addChild(behavior);
//        behavior.setSchedulingBounds(bounds);
        
//        root.addChild(tgTampo);
        
        return root;
        
    }
}

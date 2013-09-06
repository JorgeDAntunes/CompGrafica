/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.org.apache.bcel.internal.classfile.Code;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
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
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
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
        Box margemCima = new Box(3.5f, 0.1f,0.5f, orangeApp);
//        margemCima.setUserData(new String("Margem de Cima"));
        Transform3D trMC = new Transform3D();
        trMC.setTranslation(new Vector3f(0f,5.1f,0.5f));
        TransformGroup tgMC = new TransformGroup(trMC);
        tgMC.addChild(margemCima);
        
        //margem baixo
        Box margemBaixo = new Box(3.5f, 0.1f,0.5f, orangeApp);
        Transform3D trMB = new Transform3D();
        trMB.setTranslation(new Vector3f(0f,-5.1f,0.5f));
        TransformGroup tgMB = new TransformGroup(trMB);
        tgMB.addChild(margemBaixo);
        
        //margem direita
        Box margemDireita = new Box(0.3f, 5.2f,0.5f, orangeApp);
        Transform3D trMD = new Transform3D();
        trMD.setTranslation(new Vector3f(-3.3f,0f,0.5f));
        TransformGroup tgMD = new TransformGroup(trMD);
        tgMD.addChild(margemDireita);
        
        //margem esquerda
        Box margemEsquerda = new Box(0.3f, 5.2f,0.5f, orangeApp);
        Transform3D trME = new Transform3D();
        trME.setTranslation(new Vector3f(3.3f,0f,0.5f));
        TransformGroup tgME = new TransformGroup(trME);
        tgME.addChild(margemEsquerda);
        
        
        //tampo
        Box tampo = new Box(3.3f, 5.0f, 0.01f, medBlueApp);
        Transform3D trTampo = new Transform3D();
        trTampo.set(new Vector3f(0f, 1f, 0f), 0.1f);
        TransformGroup tgTampo = new TransformGroup(trTampo);
        tgTampo.addChild(tampo);
        tgTampo.addChild(tgMC);
        tgTampo.addChild(tgMB);
        tgTampo.addChild(tgMD);
        tgTampo.addChild(tgME);
        tg.addChild(tgTampo);
        
        
        //Rotação
//        Alpha alpha = new Alpha(-1, 4000);
//        RotationInterpolator rodar = new RotationInterpolator(alpha, tg);

//        rodar.setSchedulingBounds(bounds);
//        tg.addChild(rodar);
        
//        //Fundo
//        Background bb = new Background(0f, 1f, 1f);
//        bb.setApplicationBounds(bounds);
//        root.addChild(bb);
        
        //criar rotação behavior (botão esquerdo do rato)
        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(tg);
        root.addChild(behavior);
        behavior.setSchedulingBounds(bounds);
        
        
        // criar zoom behavior (scroll do rato)
        MouseZoom behavior2 = new MouseZoom();
        behavior2.setTransformGroup(tg);
        root.addChild(behavior2);
        behavior2.setSchedulingBounds(bounds);
 
        // Criar transação behavior (botão direito do rato)
        MouseTranslate behavior3 = new MouseTranslate();
        behavior3.setTransformGroup(tg);
        root.addChild(behavior3);
        behavior3.setSchedulingBounds(bounds);
 
        //luz Azul
        Appearance lAzul = new Appearance();
        lAzul.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        Color3f cLAzul = new Color3f(0.5f, 1.0f, 1.0f);
        ColoringAttributes caLAzul = new ColoringAttributes();
        caLAzul.setColor(cLAzul);
        lAzul.setColoringAttributes(caLAzul);
        
        //Criar esfera
        Sphere bola = new Sphere(0.25f, blueApp);
        Transform3D trBola = new Transform3D();
        trBola.setTranslation(new Vector3f(3.3f,0f,0.5f));
        TransformGroup tgBola = new TransformGroup(trBola);
        tgBola.addChild(bola);
//        root.addChild(tgTampo);
        
        
        
        //Luz
        AmbientLight luz = new AmbientLight(true, new Color3f(Color.WHITE));
        luz.setInfluencingBounds(bounds);
        root.addChild(luz);
        
        //cor
        PointLight ptlight = new PointLight(new Color3f(Color.YELLOW),
                new Point3f(0f, 0f, 2f), new Point3f(1f, 0f, 0f));
        ptlight.setInfluencingBounds(bounds);
        root.addChild(ptlight);
        PointLight ptlight2 = new PointLight(new Color3f(Color.YELLOW),
                new Point3f(-2f, 2f, 2f), new Point3f(1f, 0f, 0f));
        ptlight2.setInfluencingBounds(bounds);
        root.addChild(ptlight2);
        
        
        
        
        return root;
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto3d;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.org.apache.bcel.internal.classfile.Code;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import javax.media.j3d.*;
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
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author Jorge
 */
public class Projecto3D extends Applet{

    private float x = -1.0f;
    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    Alpha aBola;
    PositionInterpolator piMovBola;
    TransformGroup tgArma = new TransformGroup();
    TransformGroup tg = new TransformGroup();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainFrame(new Projecto3D(), 650, 800);
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
        
        OrbitBehavior orbit = new OrbitBehavior(cv);
        orbit.setSchedulingBounds(new BoundingSphere());
        su.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        su.addBranchGraph(bg);
        
        
    }
    
    private BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
//        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
//        TransformGroup tg = new TransformGroup();
        
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
        trTampo.set(new Vector3f(0f, 0f, 0f), 0.2f);
        TransformGroup tgTampo = new TransformGroup(trTampo);
        tgTampo.addChild(tampo);
        tgTampo.addChild(tgMC);
        tgTampo.addChild(tgMB);
        tgTampo.addChild(tgMD);
        tgTampo.addChild(tgME);
        tg.addChild(tgTampo);
        Node nbola = criarBola();
        TransformGroup tgArm = new TransformGroup();
        Transform3D tfBola = new Transform3D();
        tfBola.set(new Vector3d(0, -0.98, 0));
        tgArm.setTransform(tfBola);
        tgArm.addChild(nbola);
        tgArm.addChild(criarArma());
        tg.addChild(tgArm);
        Arma arma = new Arma(aBola, piMovBola, tgArma, bounds);
        root.addChild(arma);
        
        //Rotação
//        Alpha alpha = new Alpha(-1, 4000);
//        RotationInterpolator rodar = new RotationInterpolator(alpha, tg);

//        rodar.setSchedulingBounds(bounds);
//        tg.addChild(rodar);
        
  
        
        
        
        
        
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
//        
//        //Criar Bola
//        
//        TransformGroup tgBola = new TransformGroup();
//        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        
//        Transform3D trBola = new Transform3D();
//        trBola.setTranslation(new Vector3f(0f, 0.0f, 0.05f));
//        trBola.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI / 2.0));
//        tgBola.setTransform(trBola);        
//        
//        Sphere bola = new Sphere(0.05f, blueApp);
//        bola.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
//        tgBola.addChild(bola);
//        
//        //animação
//        TransformGroup tgAnim = new TransformGroup();
//        tgAnim.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        Transform3D trAnim = new Transform3D();
//        trAnim.setRotation(new AxisAngle4d(0.0, 0.0, x, Math.PI/2));
//        
//        Alpha alph = new Alpha(-1, 2000);
//        alph.setStartTime(System.currentTimeMillis());
//        PositionInterpolator posInt = new PositionInterpolator(alph, tgAnim, trAnim, 0f, 1.1f);
//        posInt.setSchedulingBounds(bounds);
//        
//        DetectorColisao dc = new DetectorColisao(tgBola, bounds);
//
//        
//        tgAnim.addChild(posInt);
//        tgAnim.addChild(dc);
//        tgAnim.addChild(tgBola);   
//        tg.addChild(tgAnim);
         
//        //Fundo
//        Background bb = new Background(1f, 1f, 1f);
//        bb.setApplicationBounds(bounds);
//        root.addChild(bb);
        
        
     
        
        //Luz
        AmbientLight luz = new AmbientLight(true, new Color3f(Color.WHITE));
        luz.setInfluencingBounds(bounds);
        root.addChild(luz);
        
        //cor
        PointLight ptlight = new PointLight(new Color3f(Color.red),
                new Point3f(0f, 0f, 2f), new Point3f(1f, 0f, 0f));
        ptlight.setInfluencingBounds(bounds);
        root.addChild(ptlight);
        PointLight ptlight2 = new PointLight(new Color3f(Color.red),
                new Point3f(-2f, 2f, 2f), new Point3f(1f, 0f, 0f));
        ptlight2.setInfluencingBounds(bounds);
        root.addChild(ptlight2);

        return root;
        
    }
      
    private BranchGroup criarArma(){
        BranchGroup rootArma = new BranchGroup();
        Appearance apArma = new Appearance();
        Color3f corAmb = new Color3f(0.5f, 0.5f, 0.5f);
        Color3f corEmis = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f corSpecular = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f corDiffuse = new Color3f(0.5f, 0.5f, 0.5f);
        float brilho = 20.0f;
        apArma.setMaterial(new Material(corAmb, corEmis, corDiffuse, corSpecular, brilho));
        TransformGroup tgInit = new TransformGroup();
        TransformGroup tgCano = new TransformGroup();
        Transform3D tArma = new Transform3D();
        Transform3D tCano = new Transform3D();
        tCano.set(new Vector3d(0.0, 2.0, 0.0));
        tgCano.setTransform(tCano);
        Matrix3d mArma = new Matrix3d();
//        mArma.rotX(Math.PI / 2);
        mArma.rotY(Math.PI);
//        tArma.set(mArma, new Vector3d(0.0, -0.98, 0.1), 0.05);
        tArma.set(mArma, new Vector3d(0.0, 0.0, 0.0), 0.05);
        tgInit.setTransform(tArma);
        tgArma.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArma.addChild(new Box(1.0f, 1.0f, 0.5f, apArma));
        tgCano.addChild(new Cylinder(0.3f, 2.0f, apArma));
        tgArma.addChild(tgCano);
        rootArma.addChild(tgInit);
        tgInit.addChild(tgArma);   
        
        return rootArma;        
    }
    
    private BranchGroup criarBola(){
        BranchGroup rootBola = new BranchGroup();
        
        Appearance apBola = new Appearance();
        Color3f corAmb = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f corEmis = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f corSpecular = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f corDiffuse = new Color3f(1.0f, 0.0f, 0.0f);
        float brilho = 20.0f;
        apBola.setMaterial(new Material(corAmb, corEmis, corDiffuse, corSpecular, brilho));
        
        Sphere bola  =new Sphere(0.02f, apBola);
        
        TransformGroup tgBola = new TransformGroup();
        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBola.addChild(bola);
        rootBola.addChild(tgBola);
        
        aBola = new Alpha(1, 0, 0, 1000, 0, 0);
        Transform3D tf = new Transform3D();
//        tf.set(new Vector3d(100.0, -0.98, 0.1));
        tf.rotY(-Math.PI / 1);
        piMovBola = new PositionInterpolator(aBola, tgBola, tf, 0.0f, 20.0f);
        piMovBola.setSchedulingBounds(bounds);
        rootBola.addChild(piMovBola);
        
        return rootBola;
    }
  
    
    class DetectorColisao extends Behavior{
    
    private final Color3f ccolisao = new Color3f(0.0f, 1.0f, 0.0f); // cor da deteção da colisão
    
    private Appearance app;
    private final ColoringAttributes ca = new ColoringAttributes(ccolisao, ColoringAttributes.SHADE_GOURAUD);//sombriamento suave
    
    private WakeupCriterion[] wc; //especifica deferentes critérios de activação
    
    private WakeupOr wo; //especifica que Java 3D deve despertar este comportamento quando qualquer um dos critérios de despertar constituintes do WakeupCondition torna-se válido
    
    private TransformGroup colisaoTg;
    
    Vector3d v = new Vector3d();
    
    public DetectorColisao(TransformGroup tg, Bounds b){
        colisaoTg = tg;
        setSchedulingBounds(b);
    }
    

//    @Override
    public void initialize() {
        wc = new WakeupCriterion[3];
        wc[0] = new WakeupOnCollisionEntry(colisaoTg, WakeupOnCollisionEntry.USE_GEOMETRY);
        wc[1] = new WakeupOnCollisionExit(colisaoTg, WakeupOnCollisionEntry.USE_GEOMETRY);
        wc[2] = new WakeupOnCollisionMovement(colisaoTg, WakeupOnCollisionEntry.USE_GEOMETRY);
        wo = new WakeupOr(wc);
        wakeupOn(wo);
    }

    @Override
    public void processStimulus(Enumeration en) {
        WakeupCriterion wakCri = (WakeupCriterion) en.nextElement();
        Node n;
        if(wakCri instanceof WakeupOnCollisionEntry){
//           n = ((WakeupOnCollisionEntry) wakCri).getTriggeringPath().getObject().getParent();
//            System.out.println("Colisão com: "+n.getUserData());
//            
//            app = ((Primitive) n).getAppearance();
//            app.getColoringAttributes();
//            app.setColoringAttributes(ca);
//            System.out.println("1");
//            
        }else if(wakCri instanceof WakeupOnCollisionExit){
//          n = ((WakeupOnCollisionExit) wakCri).getTriggeringPath().getObject().getParent();
//            System.out.println("Colisão parou com: "+n.getUserData());
//            
////            laranja
//            Appearance apLaranja = new Appearance();
//            apLaranja.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
//            Color3f cLaranja = new Color3f(0.988f, 0.800f, 0.080f);
//            ColoringAttributes caLaranja = new ColoringAttributes(cLaranja, ColoringAttributes.SHADE_GOURAUD);
//            caLaranja.setColor(cLaranja);
//            
//            app.setColoringAttributes(caLaranja);
//            System.out.println("2");
        x = 0.0f;
        }else {
//           n = ((WakeupOnCollisionMovement) wakCri).getTriggeringPath().getObject().getParent();
//            System.out.println("Movimento depois da colição: "+n.getUserData());
//            
//            app = ((Primitive) n).getAppearance();
//            app.getColoringAttributes();
//            app.setColoringAttributes(ca);
//              System.out.println("3");
        }
        wakeupOn(wo);
        
    }
}
    
    
    
}

class Arma extends Behavior{
    private WakeupCriterion wc;
    private Alpha dispararBola; //usada paar disparar a bola
    private PositionInterpolator piBola;//usado para a animação da bola
    private int ed = 0; //calcular direção actual da arma esquerda/direita
    private TransformGroup tgArma;//usada para a rotação da arma
    private Matrix3d apontarBola = new Matrix3d(); //usada para apontar a bola
    private Matrix3d apontarArma = new Matrix3d(); //usada para apontar a arma
    private Transform3D direcaoBola = new Transform3D();//usado para definir a direção da bola
    private Transform3D direcaoArma = new Transform3D();//usaa para definir a direção da arma
    
    
    public Arma(Alpha a, PositionInterpolator pi, TransformGroup tg, Bounds bounds){
        dispararBola = a;
        piBola = pi;
        setSchedulingBounds(bounds);
        tgArma = tg;
    }

    //estabelecer os criterios para acionar um comportamento, neste caso
    //queremos esperar que uma tecla seja premida
    public void initialize() {
        wc = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
        wakeupOn(wc);
    }

    /*
     * identificar qual tecla foi premida
     * seta esquerda: roda a arma para a esquerda
     * direita direito: roda a arma paraa a direita
     * barra de espaço: disparar a bola
     */
    public void processStimulus(Enumeration en) {
        while(en.hasMoreElements()){
            WakeupCriterion criterion = (WakeupCriterion) en.nextElement();
            if(criterion instanceof WakeupOnAWTEvent){
                AWTEvent[] evento = ((WakeupOnAWTEvent) criterion).getAWTEvent();
                //verificar se é um evento do teclado
                if(evento[0] instanceof KeyEvent){
                    int tecla = ((KeyEvent) evento[0]).getKeyCode();
                    if(tecla == KeyEvent.VK_LEFT){
                        //tecla seta esquerda premida
                        if(ed < 8){
                            ++ed;                            
                        }
                        apontarBola.rotY(((ed /32.0)+0.5)*Math.PI);
                        apontarArma.rotZ((ed /-32.0)*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    }else if (tecla == KeyEvent.VK_RIGHT){
                        //tecla seta direita premida
                        if (ed > -8){
                            --ed;
                        }
                        apontarBola.rotY(((ed /32.0)+0.5)*Math.PI);
                        apontarArma.rotZ((ed /(-32.0))*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    } else if(tecla == KeyEvent.VK_SPACE){
                        //tecla espaço premida
                        dispararBola.setStartTime(System.currentTimeMillis());
                    }//else if(tecla == KeyEvent.VK_DOWN){
//                        //tecla seta baixo premida
//                        if(cb < 8){
//                            ++cb;                            
//                        }
//                        System.out.println("Baixo: "+cb);
//                        apontarBola.rotZ(((cb /32.0)+0.5)*Math.PI);
//                        apontarArma.rotX((cb /-32.0)*Math.PI);
//                        direcaoBola.setRotation(apontarBola);
//                        direcaoArma.setRotation(apontarArma);
//                        tgArma.setTransform(direcaoArma);
//                        piBola.setAxisOfTranslation(direcaoBola);
//                    }else if(tecla == KeyEvent.VK_UP){
//                        //tecla seta cima premida
//                        if (cb > -8){
//                            --cb;
//                        }
//                        System.out.println("Cima: "+cb);
//                        apontarBola.rotZ(((cb /32.0)+0.5)*Math.PI);
//                        apontarArma.rotX((cb /(-32.0))*Math.PI);
//                        direcaoBola.setRotation(apontarBola);
//                        direcaoArma.setRotation(apontarArma);
//                        tgArma.setTransform(direcaoArma);
//                        piBola.setAxisOfTranslation(direcaoBola);
//                    }
                }
            }
        }
        wakeupOn(wc);
    }
    
    
    
    
}
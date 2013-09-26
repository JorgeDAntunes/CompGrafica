/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.ConfiguredUniverse;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.swing.BoxLayout;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;


//Class que permite rodar a "arma"  com as teclas do rato
//E disparar a bola quando a barra de espaço é pressionada
/**
 *
 * @author Jorge
 */
public class Projecto3D extends Applet implements ActionListener{

    Button bSair = new Button("Sair");
    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    Alpha aBola;
    PositionInterpolator piMovBola;
    TransformGroup tgArma = new TransformGroup();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainFrame(new Projecto3D(), 400, 700);
    }
    
    public void init(){
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        Canvas3D cv = new Canvas3D(gc);
        
        VirtualUniverse vu = new VirtualUniverse();
        Locale l = new Locale(vu);
        
        setLayout(new BorderLayout());
        add(cv, BorderLayout.CENTER);
        
        BranchGroup bg = createSceneGraph(cv);
        bg.compile();
        l.addBranchGraph(bg);
        bg = conteudoBranch();
        bg.compile();
        l.addBranchGraph(bg);
        bSair.addActionListener(this);
        add(bSair, BorderLayout.SOUTH);
        
        
//        SimpleUniverse su = new SimpleUniverse(cv);
//        su.getViewingPlatform().setNominalViewingTransform();
//        
//        OrbitBehavior orbit = new OrbitBehavior(cv);
//        orbit.setSchedulingBounds(new BoundingSphere());
//        su.getViewingPlatform().setViewPlatformBehavior(orbit);
//        
//        su.addBranchGraph(bg);
        
        setVisible(true);
        
    }
    
    private BranchGroup createSceneGraph(Canvas3D c) {
        BranchGroup root = new BranchGroup();
        Transform3D tf = new Transform3D();
        Matrix3d mt = new Matrix3d();
        mt.rotX(Math.PI / -6);
        tf.set(mt, new Vector3d(0.0, 10.0, 10.0),1.0);
        TransformGroup tg = new TransformGroup(tf);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        ViewPlatform vp = new ViewPlatform();
        PhysicalBody pb = new PhysicalBody();
        PhysicalEnvironment pe = new PhysicalEnvironment();
        tg.addChild(vp);
        root.addChild(tg);
        View v = new View();
        v.addCanvas3D(c);
        v.attachViewPlatform(vp);
        v.setPhysicalBody(pb);
        v.setPhysicalEnvironment(pe);   

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
        
        return root;
        
    }
    
    private BranchGroup conteudoBranch(){
        BranchGroup root = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        root.addChild(tg);
        
        
        Node nbola = criarBola();
        root.addChild(nbola);
        
        Arma arma = new Arma(aBola, piMovBola, tgArma, bounds);
        root.addChild(arma);
        root.addChild(criarArma());
        
        
        //Luzes
        Color3f corLuzAmb = new Color3f(0.5f, 0.5f, 0.5f);
        AmbientLight luzAmb = new AmbientLight(corLuzAmb);
        luzAmb.setInfluencingBounds(bounds);
        Color3f corLuzDir = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f vLuzDir = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight luzDir = new DirectionalLight(corLuzDir, vLuzDir);
        luzDir.setInfluencingBounds(bounds);
        root.addChild(luzAmb);
        root.addChild(luzDir);

        //background
        Background background = new Background(0f, 0.5f, 1f);
        background.setApplicationBounds(bounds);
        root.addChild(background);
        
        return root;
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
        
        Sphere bola  =new Sphere(0.2f, apBola);
        
        TransformGroup tgBola = new TransformGroup();
        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBola.addChild(bola);
        rootBola.addChild(tgBola);
        
        aBola = new Alpha(1, 0, 0, 1000, 0, 0);
        Transform3D tf = new Transform3D();
        tf.rotY(Math.PI / 1);
        piMovBola = new PositionInterpolator(aBola, tgBola, tf, 0.0f, 50.0f);
        piMovBola.setSchedulingBounds(bounds);
        rootBola.addChild(piMovBola);
        
        return rootBola;
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
        tCano.set(new Vector3d(0.0, -2.0, 0.0));
        tgCano.setTransform(tCano);
        Matrix3d mArma = new Matrix3d();
        mArma.rotX(Math.PI / 2);
        tArma.set(mArma, new Vector3d(0.0, 0.0, 0.0), 1.0);
        tgInit.setTransform(tArma);
        tgArma.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArma.addChild(new Box(1.0f, 1.0f, 0.5f, apArma));
        tgCano.addChild(new Cylinder(0.3f, 4.0f, apArma));
        tgArma.addChild(tgCano);
        rootArma.addChild(tgInit);
        tgInit.addChild(tgArma);
        
        
        return rootArma;
        
    }
    

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.exit(0);
    }
}
class Arma extends Behavior{
    private WakeupCriterion wc;
    private Alpha dispararBola; //usada paar disparar a bola
    private PositionInterpolator piBola;//usado para a animação da bola
    private int ed = 0; //calcular direção actual da arma esquerda/direita
    private int cb = 0; //calcular direção actual da arma cima/baixo
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
                        System.out.println("Esquerda: "+ed);
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
                        System.out.println("Direita: "+ed);
                        apontarBola.rotY(((ed /32.0)+0.5)*Math.PI);
                        apontarArma.rotZ((ed /(-32.0))*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    } else if(tecla == KeyEvent.VK_SPACE){
                        //tecla espaço premida
                        dispararBola.setStartTime(System.currentTimeMillis());
                    }else if(tecla == KeyEvent.VK_DOWN){
                        //tecla seta baixo premida
                        if(cb < 8){
                            ++cb;                            
                        }
                        System.out.println("Baixo: "+cb);
                        apontarBola.rotZ(((cb /32.0)+0.5)*Math.PI);
                        apontarArma.rotX((cb /-32.0)*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    }else if(tecla == KeyEvent.VK_UP){
                        //tecla seta cima premida
                        if (cb > -8){
                            --cb;
                        }
                        System.out.println("Cima: "+cb);
                        apontarBola.rotZ(((cb /32.0)+0.5)*Math.PI);
                        apontarArma.rotX((cb /(-32.0))*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    }
                }
            }
        }
        wakeupOn(wc);
    }
    
    
    
    
}


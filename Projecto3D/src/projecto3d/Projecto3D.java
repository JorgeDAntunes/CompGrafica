/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto3d;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.org.apache.bcel.internal.classfile.Code;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
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
import javax.swing.*;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4d;


/**
 *
 * @author Jorge
 */
public class Projecto3D extends JApplet implements ActionListener, MouseListener{

    private float x = -1.0f;
    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    public BackgroundSound bSom;
//    private BranchGroup som = som();
    Alpha aBola;
    PositionInterpolator piMovBola;
    TransformGroup tgArma = new TransformGroup();
    TransformGroup tg = new TransformGroup();
    Switch swAlvo;
    Alpha aAlvo;
    Switch sw;
    PickCanvas pc;
    private int pontos = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainFrame(new Projecto3D(), 650, 800);
    }
    
    public void init(){
        //Menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
//        menu opções
        JMenu menu = new JMenu("Opções");
        
        JMenuItem menuItem = new JMenuItem("Ligar Som");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Desligar Som");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Sair");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        JPanel pScore = new JPanel();
        
        pScore.add(new JLabel("Pontos:"));
        JTextField tPontos = new JTextField(""+pontos);
        tPontos.setEditable(false);
        pScore.add(tPontos);
        
        //Vista
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        Canvas3D cv = new Canvas3D(gc);
        setLayout(new BorderLayout());
        add(cv, BorderLayout.CENTER);
        add(pScore, BorderLayout.NORTH);
        
        TextArea ta = new TextArea("", 2, 30, TextArea.SCROLLBARS_NONE);
        ta.setText("'A':Mover canhão para a esquerda\n");
        ta.append("'S':Mover canhão para a direita\n");
        ta.append("'Espaço':disparar bola\n");
        ta.setEditable(false);
        add(ta, BorderLayout.SOUTH);
        
        
        

        OrbitBehavior orbit;
        SimpleUniverse su = new SimpleUniverse(cv);
        su.getViewingPlatform().setNominalViewingTransform();
        
        BranchGroup bg = createSceneGraph();
        bg.compile();
        su.addBranchGraph(bg);
        pc = new PickCanvas(cv, bg);
        pc.setMode(PickTool.GEOMETRY);
        //inicializar som
//        AudioDevice ad  =new JavaSoundMixer(su.getViewer().getPhysicalEnvironment());
//        AudioDevice ad = su.getViewer().createAudioDevice();
//        ad.initialize();
//        //**SOM**
//        som.compile();
//        su.addBranchGraph(som);
//        System.setProperty("j3d.audiodevice", "com.sun.j3d.audioengines.javasound.JavaSoundMixer");
//        su.getViewer().createAudioDevice();
        
        TransformGroup tgCamera = su.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0);
        Transform3D tfCamara  =new Transform3D();
        
        tfCamara.lookAt(new Point3d(0,0,3), new Point3d(0,0,1), new Vector3d(0,1,0));
        tfCamara.invert();
        tgCamera.setTransform(tfCamara);
        View view = su.getViewer().getView();
        view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
        view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment());
        
        //orbit
        ViewingPlatform vp = su.getViewingPlatform();
        BoundingSphere bs = new BoundingSphere(new Point3d(), 100000.0);
        orbit = new OrbitBehavior(cv);
        orbit.setSchedulingBounds(bs);
        vp.setViewPlatformBehavior(orbit);
                
        
    }
    
//    public BranchGroup som() {
//        som = new BranchGroup();
//
//        Transform3D trSom3D = new Transform3D();
//        TransformGroup tgSom = new TransformGroup(trSom3D);
//        tgSom.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        tgSom.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
//        som.addChild(tgSom);
//
//        URL url = this.getClass().getClassLoader().getResource("som/world.wav");
//        MediaContainer mc = new MediaContainer(url);
//        mc.setCacheEnable(true);
//
//        BoundingSphere soundBounds = new BoundingSphere();
//        bSom.setSchedulingBounds(soundBounds);
//
//        bSom.setSoundData(mc);
//        bSom.setLoop(Sound.INFINITE_LOOPS);
//        bSom.setEnable(true);
//        bSom.setInitialGain(1f);
//
//        tgSom.addChild(bSom);
//
//        return som;
//    }
    
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
        ap.setCapability(Appearance.ALLOW_POINT_ATTRIBUTES_WRITE);
        ap.setCapability(Appearance.ALLOW_LINE_ATTRIBUTES_WRITE);
        ap.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
        ColoringAttributes ca = new ColoringAttributes();
        ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        ap.setColoringAttributes(ca);
//                
//        // blue
//        Appearance blueApp = new Appearance();
//        blueApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
//        Color3f blueColor = new Color3f(0.3f, 0.3f, 1.0f);
//        ColoringAttributes blueCA = new ColoringAttributes();
//        blueCA.setColor(blueColor);
//        blueApp.setColoringAttributes(blueCA);
//         
//         
//        // medium blue
//        Appearance medBlueApp = new Appearance();
//        Color3f medBlueColor = new Color3f(0.1f, 0.8f, 1.0f);
//        ColoringAttributes medBlueCA = new ColoringAttributes();
//        medBlueCA.setColor(medBlueColor);
//        medBlueApp.setColoringAttributes(medBlueCA);
        
        
        //Aparencia Chão
        Appearance apChao = criarTexturaAppearance("imagens/texturaRelva1.jpg");
        apChao.setMaterial(new Material());
 
        
        //Aparencia Margens
        Appearance apMargem = criarTexturaAppearance("imagens/arbusto.jpg");
        apMargem.setMaterial(new Material());
        
        //margem cima
        Box margemCima = new Box(3.5f, 0.1f,0.5f,Primitive.GENERATE_TEXTURE_COORDS, apMargem);
//        margemCima.setUserData(new String("Margem de Cima"));
        Transform3D trMC = new Transform3D();
        trMC.setTranslation(new Vector3f(0f,5.1f,0.5f));
        TransformGroup tgMC = new TransformGroup(trMC);
        tgMC.addChild(margemCima);
        
        //margem baixo
        Box margemBaixo = new Box(3.5f, 0.1f,0.5f,Primitive.GENERATE_TEXTURE_COORDS, apMargem);
        Transform3D trMB = new Transform3D();
        trMB.setTranslation(new Vector3f(0f,-5.1f,0.5f));
        TransformGroup tgMB = new TransformGroup(trMB);
        tgMB.addChild(margemBaixo);
        
        //margem direita
        Box margemDireita = new Box(0.1f, 5.2f,0.5f,Primitive.GENERATE_TEXTURE_COORDS, apMargem);
        Transform3D trMD = new Transform3D();
        trMD.setTranslation(new Vector3f(-3.5f,0f,0.5f));
        TransformGroup tgMD = new TransformGroup(trMD);
        tgMD.addChild(margemDireita);
        
        //margem esquerda
        Box margemEsquerda = new Box(0.1f, 5.2f,0.5f,Primitive.GENERATE_TEXTURE_COORDS, apMargem);
        Transform3D trME = new Transform3D();
        trME.setTranslation(new Vector3f(3.5f,0f,0.5f));
        TransformGroup tgME = new TransformGroup(trME);
        tgME.addChild(margemEsquerda);
        
        
        //Chao
        Box chao = new Box(3.5f, 5.0f, 0.01f,Primitive.GENERATE_TEXTURE_COORDS, apChao);
        Transform3D trTampo = new Transform3D();
        trTampo.set(new Vector3f(0f, 0f, 0f), 0.2f);
        TransformGroup tgTampo = new TransformGroup(trTampo);
        tgTampo.addChild(chao);
        tgTampo.addChild(tgMC);
        tgTampo.addChild(tgMB);
        tgTampo.addChild(tgMD);
        tgTampo.addChild(tgME);
        tg.addChild(tgTampo);
        
        
        float pos = 0.5f;
        TransformGroup tgArm = new TransformGroup();
        //Arma e bola e Alvos
        Node nbola = criarBola();
        Node nAlvo = criarAlvos(0.0f,0.5f,0.1f,3000,-pos,pos);
        Node nAlvo2 = criarAlvos(0.0f,0.2f,0.1f,2000,pos,-pos);
        Node nAlvo3 = criarAlvos(0.0f,-0.1f,0.1f,1000,-pos,pos);
        Node nAlvo4 = criarAlvos2(0.4f,0.4f,0.1f,2000,0,1);
        Node nAlvo5 = criarAlvos2(-0.5f,0.35f,0.1f,2000,0,1);
        Node nAlvo6 = criarAlvos2(0.5f,-0.2f,0.1f,1000,0,1);
        Node nAlvo7 = criarAlvos2(-0.3f,-0.25f,0.1f,1000,0,1);
        tg.addChild(nAlvo);
        tg.addChild(nAlvo2);
        tg.addChild(nAlvo3);
        tg.addChild(nAlvo4);
        tg.addChild(nAlvo5);
        tg.addChild(nAlvo6);
        tg.addChild(nAlvo7);
        Transform3D tfBola = new Transform3D();
        tfBola.set(new Vector3d(0, -19.5, 2),0.05);
        tgArm.setTransform(tfBola);
        tgArm.addChild(nbola);
        tgArm.addChild(criarArma());
        tg.addChild(tgArm);
        acertarAlvo acertaAlvo = new acertarAlvo(nAlvo, swAlvo, aBola, bounds);
        Arma arma = new Arma(aBola, piMovBola, tgArma, bounds);
        tg.addChild(arma);
        tg.addChild(acertaAlvo);

        
        
        //Rotação
//        Alpha alpha = new Alpha(-1, 4000);
//        RotationInterpolator rodar = new RotationInterpolator(alpha, tg);

//        rodar.setSchedulingBounds(bounds);
//        tg.addChild(rodar);
        
        //**********************TEXTO*************************
        //Aparencia Texto, Material:Silver
        Appearance apTexto = new Appearance();
        Material material = new Material();
        material.setAmbientColor(0.23125f, 0.23125f, 0.23125f);
        material.setDiffuseColor(0.2775f, 0.2775f, 0.2775f);
        material.setSpecularColor(0.773911f, 0.773911f, 0.773911f);
        material.setShininess(89.6f);
        apTexto.setMaterial(material);
        
        //texto titulo
        Font3D fTitulo = new Font3D(new Font("Verdana", Font.BOLD, 1), new FontExtrusion());
        Text3D tTitulo = new Text3D(fTitulo, "Tiro ao Alvo");
//        tTitulo.setAlignment(0);
        
        Shape3D sTitulo = new Shape3D(tTitulo, apTexto);
        
        Transform3D tfTitulo = new Transform3D();
        tfTitulo.setScale(0.15);
        tfTitulo.setTranslation(new Vector3f(-0.45f, -1.02f, 0.05f));
        tfTitulo.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0), Math.PI/2));
        TransformGroup tgTitulo = new TransformGroup(tfTitulo);
        tg.addChild(tgTitulo);
        tgTitulo.addChild(sTitulo);
        
        //nome 
        Font3D fNome = new Font3D(new Font("Vani", Font.ITALIC, 1), new FontExtrusion());
        Text3D tNome = new Text3D(fNome, "Jorge Antunes");
        
        Shape3D sNome = new Shape3D(tNome, apTexto);
        
        Transform3D tfNome  =new Transform3D();
        tfNome.setScale(0.15);
        tfNome.setTranslation(new Vector3f(-0.45f, 1.02f, 0.05f));
        tfNome.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        TransformGroup tgNome = new TransformGroup(tfNome);
        tg.addChild(tgNome);
        tgNome.addChild(sNome);
        
 //**********************MOUSEBEHAVIOR/KEYNAVIGATORBEHAVIOR******************
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
        
        
        KeyNavigatorBehavior behaviorTec = new KeyNavigatorBehavior(tg);
        behaviorTec.setSchedulingBounds(bounds);
        tg.addChild(behaviorTec);
        
        
        //**BEHAVIORS PREDEFINIDOS (LOD)**
        float[] distances = new float[2];
        distances[0] = 20.0f;
        distances[1] = 30.0f;
        DistanceLOD lod = new DistanceLOD(distances);
        BoundingSphere boundslod = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10.0);
        lod.setSchedulingBounds(boundslod);
        tg.addChild(lod);
        
        
 //*********************SOM******************************
        //Som background
//        BoundingSphere bsSom = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
//        bSom = new BackgroundSound();
//        URL url = this.getClass().getClassLoader().getResource("som/world.wav");
//        MediaContainer mc = new MediaContainer(url);
//        mc.setCacheEnable(true);
//        bSom.setCapability(BackgroundSound.ALLOW_ENABLE_READ);
//        bSom.setCapability(BackgroundSound.ALLOW_ENABLE_WRITE);
//        bSom.setSoundData(mc);
//        bSom.setLoop(Sound.INFINITE_LOOPS);
//        bSom.setSchedulingBounds(bsSom);
//        bSom.setInitialGain(1.0f);
//        bSom.setEnable(true);
//        tg.addChild(bSom);
        
        //botao som
        Appearance apSom = criarTexturaAppearance("imagens/som.jpg");
        apSom.setMaterial(new Material());
        
        Sphere esferaSom = new Sphere(0.03f, Primitive.ENABLE_GEOMETRY_PICKING 
                | Primitive.GENERATE_TEXTURE_COORDS, apSom);
        Transform3D tfSom = new Transform3D();
        tfSom.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
        TransformGroup tgSom = new TransformGroup(tfSom);
        tgSom.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        tgSom.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(tgSom);
        tgSom.addChild(esferaSom);
        
        //billboard
        Billboard b = new Billboard(tgSom, Billboard.ROTATE_ABOUT_AXIS, new Vector3f(1f, 1f, 1f));
        b.setSchedulingBounds(bounds);
        tgSom.addChild(b);
        
        
        
         //Particulas
        PointParticles ptsFountain = new PointParticles(30000, 80);   // time delay

        TransformGroup tgPart = new TransformGroup();
        Transform3D tfPArt = new Transform3D();
        tfPArt.setScale(0.08);
        tfPArt.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        tfPArt.setTranslation(new Vector3d(2.1f, 0.0f, 0.0f));
        tgPart.setTransform(tfPArt);
        tgPart.addChild(ptsFountain);
        tg.addChild(tgPart);

        Behavior partBeh = ptsFountain.getParticleBeh();
        partBeh.setSchedulingBounds(bounds);
        tg.addChild(partBeh);
        
        
        //Montanha
        
        Shape3D mont = new Shape3D(createGeometry(), ap);
        GeometryArray gaMont = (GeometryArray) mont.getGeometry();
        Transform3D tfMont = new Transform3D();
        tfMont.setScale(0.08);
        tfMont.setTranslation(new Vector3f(0.5f, 0.8f, 0.16f));
        tfMont.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        TransformGroup tgMont = new TransformGroup(tfMont);
        tg.addChild(tgMont);
        tgMont.addChild(mont);
        
        //sombra
        
        
        //montanha2
        
        Shape3D mont2 = new Shape3D(createGeometry(), ap);
        
        Transform3D tfMont2 = new Transform3D();
        tfMont2.setScale(0.08);
        tfMont2.setTranslation(new Vector3f(0.17f, 0.8f, 0.16f));
        tfMont2.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        TransformGroup tgMont2 = new TransformGroup(tfMont2);
        tg.addChild(tgMont2);
        tgMont2.addChild(mont2);
        
        //sombra
        
        
        //montanha3        
        
        Shape3D mont3 = new Shape3D(createGeometry(), ap);
        
        Transform3D tfMont3 = new Transform3D();
        tfMont3.setScale(0.08);
        tfMont3.setTranslation(new Vector3f(-0.15f, 0.8f, 0.16f));
        tfMont3.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        TransformGroup tgMont3 = new TransformGroup(tfMont3);
        tg.addChild(tgMont3);
        tgMont3.addChild(mont3);
        
        ptsFountain = new PointParticles(30000, 80);   // time delay

        TransformGroup tgPart2 = new TransformGroup();
        Transform3D tfPArt2 = new Transform3D();
        tfPArt2.setScale(0.08);
        tfPArt2.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        tfPArt2.setTranslation(new Vector3d(1.1f, 0.0f, 0.0f));
        tgPart2.setTransform(tfPArt2);
        tgPart2.addChild(ptsFountain);
        tg.addChild(tgPart2);

        Behavior partBeh2 = ptsFountain.getParticleBeh();
        partBeh2.setSchedulingBounds(bounds);
        tg.addChild(partBeh2);
        
        //sombra
        
        
        //montanha2
        
        Shape3D mont4 = new Shape3D(createGeometry(), ap);
        
        Transform3D tfMont4 = new Transform3D();
        tfMont4.setScale(0.08);
        tfMont4.setTranslation(new Vector3f(-0.5f, 0.8f, 0.16f));
        tfMont4.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0),Math.PI/2));
        TransformGroup tgMont4 = new TransformGroup(tfMont4);
        tg.addChild(tgMont4);
        tgMont4.addChild(mont4);
        
        
        
        
        
        
        
        
        
        
        
        
        //Criar Bola
//        
//         swAlvo = new Switch(0);
//        swAlvo.setCapability(Switch.ALLOW_SWITCH_WRITE);
//        
//         TransformGroup tgBola = new TransformGroup();
//        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        
//        Transform3D trBola = new Transform3D();
//        trBola.setTranslation(new Vector3f(0f, 0.0f, 0.05f));
//        trBola.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI / 2.0));
//        tgBola.setTransform(trBola);        
//        
//        Sphere bola = new Sphere(0.2f, blueApp);
//        bola.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
//        tgBola.addChild(bola);
//        tgBola.addChild(swAlvo);
//        swAlvo.addChild(tgBola);
//        
//        tg.addChild(swAlvo);
////        
//        
        
        
        
        
        
        
        
        
        
        
        
        
//        
//        TransformGroup tgBola = new TransformGroup();
//        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        
//        Transform3D trBola = new Transform3D();
//        trBola.setTranslation(new Vector3f(0f, 0.0f, 0.05f));
//        trBola.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI / 2.0));
//        tgBola.setTransform(trBola);        
//        
//        Sphere bola = new Sphere(0.2f, blueApp);
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
//        tg.addChild(tgBola);
         
        
        //Fundo
        Background bb = criarBackground();
        bb.setApplicationBounds(bounds);
        root.addChild(bb);
      
        //Luz
        //AMBIENT
        AmbientLight aLight = new AmbientLight(true, new Color3f(Color.BLUE));
        aLight.setInfluencingBounds(bounds);
        aLight.setCapability(PointLight.ALLOW_STATE_WRITE | PointLight.ALLOW_STATE_WRITE);
        root.addChild(aLight);
        
        //DIRECIONAL
        DirectionalLight dLight = new DirectionalLight(new Color3f(0.3f, 0.15f, 0.0f), new Vector3f(0f, -1f, 0f));
        dLight.setCapability(PointLight.ALLOW_STATE_WRITE | PointLight.ALLOW_STATE_WRITE);
        dLight.setInfluencingBounds(bounds);
        root.addChild(dLight);
        
        //SPORTLIGHT
        SpotLight sLight = new SpotLight(new Color3f(Color.GREEN), new Point3f(0.7f, 0.7f, 0.7f), new Point3f(1f, 0f, 0f), new Vector3f(-1.1f,-0.8f,-0.7f), (float) (Math.PI / 6.0), 0f);
        sLight.setCapability(PointLight.ALLOW_STATE_WRITE | PointLight.ALLOW_STATE_WRITE);
        sLight.setInfluencingBounds(bounds);
        root.addChild(sLight);
        
        //PONTUAl
        PointLight ptlight = new PointLight(new Color3f(Color.RED),
                new Point3f(0f, 0f, 2f), new Point3f(1f, 0f, 0f));
        ptlight.setCapability(PointLight.ALLOW_STATE_WRITE | PointLight.ALLOW_STATE_WRITE);
        ptlight.setInfluencingBounds(bounds);
        root.addChild(ptlight);
        PointLight ptlight2 = new PointLight(new Color3f(Color.RED),
                new Point3f(0f, 2f, 2f), new Point3f(1f, 0f, 0f));
        ptlight2.setCapability(PointLight.ALLOW_STATE_WRITE | PointLight.ALLOW_STATE_WRITE);
        ptlight2.setInfluencingBounds(bounds);
        root.addChild(ptlight2);

        
        
        return root;
        
    }
      
    
    private GeometryArray createShadow(GeometryArray ga, Point3f light, Point3f plane) {
        GeometryInfo gi = new GeometryInfo(ga);
        gi.convertToIndexedTriangles();
        IndexedTriangleArray ita = (IndexedTriangleArray) gi.getIndexedGeometryArray();
        Vector3f v = new Vector3f();
        v.sub(plane, light);
        double[] mat = new double[16];
        for (int i = 0; i < 16; i++) {
            mat[i] = 0;
        }
        mat[0] = 1;
        mat[5] = 1;
        mat[10] = 1 - 0.001;
        mat[14] = -1 / v.length();
        Transform3D proj = new Transform3D();
        proj.set(mat);
        Transform3D u = new Transform3D();
        u.lookAt(new Point3d(light), new Point3d(plane), new Vector3d(0, 1, 0));
        proj.mul(u);
        Transform3D tr = new Transform3D();
        u.invert();
        tr.mul(u, proj);
        int n = ita.getVertexCount();
        int count = ita.getIndexCount();
        IndexedTriangleArray shadow = new IndexedTriangleArray(n,
                GeometryArray.COORDINATES, count);
        for (int i = 0; i < n; i++) {
            Point3d p = new Point3d();
            ga.getCoordinate(i, p);
            Vector4d v4 = new Vector4d(p);
            v4.w = 1;
            tr.transform(v4);
            Point4d p4 = new Point4d(v4);
            p.project(p4);
            shadow.setCoordinate(i, p);
        }
        int[] indices = new int[count];
        ita.getCoordinateIndices(0, indices);
        shadow.setCoordinateIndices(0, indices);
        return shadow;
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
        
        Cylinder cyl = new Cylinder(0.3f, 2.0f,Primitive.ENABLE_GEOMETRY_PICKING 
                | Primitive.GENERATE_NORMALS, apArma);

        Switch s = new Switch();
        s.setCapability(Switch.ENABLE_PICK_REPORTING);
        s.setCapability(Switch.ALLOW_SWITCH_READ);
        s.setCapability(Switch.ALLOW_SWITCH_WRITE);
        s.addChild(cyl);
        
        
        
        
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
        tArma.set(mArma, new Vector3d(0.0, 0.0, 0.0), 1.0);
        tgInit.setTransform(tArma);
        tgArma.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgArma.addChild(new Box(1.0f, 1.0f, 0.5f, apArma));
        tgCano.addChild(s);
        
        tgArma.addChild(tgCano);
        Box box = new Box(0.3f, 2.0f, 0.3f,Primitive.ENABLE_GEOMETRY_PICKING 
                | Primitive.GENERATE_NORMALS, apArma);
        s.addChild(box);
        s.setWhichChild(0);
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
        
        Sphere bola  =new Sphere(0.2f, apBola);
        
        TransformGroup tgBola = new TransformGroup();
        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgBola.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgBola.addChild(bola);
        rootBola.addChild(tgBola);
        
        aBola = new Alpha(1, 0, 0, 1000, 0, 0);
        Transform3D tf = new Transform3D();
//        tf.set(new Vector3d(100.0, -0.98, 0.1));
//        tf.rotY(Math.PI / 6);
//        tf.rotX(Math.PI / 4);
        tf.rotZ(Math.PI / 2);
        piMovBola = new PositionInterpolator(aBola, tgBola, tf, 0.0f, 50.0f);
        piMovBola.setSchedulingBounds(bounds);
        rootBola.addChild(piMovBola);
        
        return rootBola;
    }
    
    private BranchGroup criarAlvos(float x, float y, float z, int vel, float inicio, float fim){
        BranchGroup rootAlvo = new BranchGroup();
        swAlvo = new Switch(0);
        swAlvo.setCapability(Switch.ALLOW_SWITCH_WRITE);
        
        
        
        //Aparencia
        Appearance apAlvo = criarTexturaAppearance("imagens/alvo.jpg");
        apAlvo.setMaterial(new Material());
        
        Box alvo1 = new Box(1, 1, 0.01f, Primitive.GENERATE_TEXTURE_COORDS, apAlvo);
        
         
        //Alvo1
        Transform3D tfAlvo = new Transform3D();
        tfAlvo.set(new Vector3f(0.0f, 0.0f, 0.0f), 0.1f);
        tfAlvo.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0), Math.PI/2));
        tfAlvo.setTranslation(new Vector3f(x, y, z));
        TransformGroup tgAlvo = new TransformGroup(tfAlvo);
//        rootAlvo.addChild(tgAlvo);
        tgAlvo.addChild(swAlvo);
        swAlvo.addChild(alvo1);
        
        
        TransformGroup tgMovAlvo = new TransformGroup();
        tgMovAlvo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgMovAlvo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgMovAlvo.addChild(tgAlvo);
        
        aAlvo = new Alpha(-1, 0, 0, vel, 0, 0);
        Transform3D tfMovAlvo = new Transform3D();
        PositionInterpolator piMovAlvo = new PositionInterpolator(aAlvo, tgMovAlvo, 
                tfMovAlvo, inicio, fim);

        piMovAlvo.setSchedulingBounds(bounds);
        rootAlvo.addChild(piMovAlvo);
        rootAlvo.addChild(tgMovAlvo);
                

        
        return rootAlvo;
    }
    
    private BranchGroup criarAlvos2(float x, float y, float z, int vel, float inicio, float fim){
        BranchGroup rootAlvo = new BranchGroup();
        swAlvo = new Switch(0);
        swAlvo.setCapability(Switch.ALLOW_SWITCH_WRITE);
        
        
        
        //Aparencia
        Appearance apAlvo = criarTexturaAppearance("imagens/alvo.jpg");
        apAlvo.setMaterial(new Material());
        
        Box alvo1 = new Box(1, 1, 0.01f, Primitive.GENERATE_TEXTURE_COORDS, apAlvo);
        
        
         
        //Alvo1
        Transform3D tfAlvo = new Transform3D();
        tfAlvo.set(new Vector3f(0.0f, 0.0f, 0.0f), 0.1f);
        tfAlvo.setRotation(new AxisAngle4d(new Vector3d(1, 0, 0), Math.PI/2));
        tfAlvo.setTranslation(new Vector3f(x, y, z));
        TransformGroup tgAlvo = new TransformGroup(tfAlvo);
//        rootAlvo.addChild(tgAlvo);
        tgAlvo.addChild(swAlvo);
        swAlvo.addChild(alvo1);
        
        
        TransformGroup tgMovAlvo = new TransformGroup();
        tgMovAlvo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgMovAlvo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgMovAlvo.addChild(tgAlvo);
        
        aAlvo = new Alpha(-1, 0, 0, vel, 0, 0);
        Transform3D tfMovAlvo = new Transform3D();
        ScaleInterpolator siMovAlvo = new ScaleInterpolator(aAlvo, tgMovAlvo, 
                tfMovAlvo, inicio, fim);
        siMovAlvo.setSchedulingBounds(bounds);
        rootAlvo.addChild(siMovAlvo);
        rootAlvo.addChild(tgMovAlvo);
                

        
        return rootAlvo;
    }
    
   
    private Appearance criarTexturaAppearance(String caminho){
        Appearance ap = new Appearance();
        URL url = getClass().getClassLoader().getResource(caminho);
        TextureLoader tloader = new TextureLoader(url, this);
        ImageComponent2D imagem = tloader.getImage();
        if(imagem == null){
            System.out.println("Textura não encontrada");
        }
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, imagem.getWidth(), imagem.getHeight());
        texture.setImage(0, imagem);
        texture.setEnable(true);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        ap.setTexture(texture);       
        return ap;                
    }
    
    Background criarBackground(){
        Background fundo = new Background();
        BranchGroup bg = new BranchGroup();
        Appearance ap = new Appearance();
        Sphere s = new Sphere(0.1f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_NORMALS_INWARD
                | Sphere.GENERATE_TEXTURE_COORDS, ap);
//        Appearance ap = s.getAppearance();
        bg.addChild(s);
        fundo.setGeometry(bg);
        
        URL url = getClass().getClassLoader().getResource("imagens/fundo.jpg");
        TextureLoader tl = new TextureLoader(url, this);
        Texture t = tl.getTexture();
        ap.setTexture(t);
        return  fundo;
                
    }
    
    private Geometry createGeometry() {
        int m = 30;
        int n = 30;
        Point3f[] pts = new Point3f[m * n];
        int idx = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                float x = (i - m / 2) * 0.2f;
                float z = (j - n / 2) * 0.2f;
                float y = 1.5f * (float) (Math.cos(x * x * z * z * z * z * z * z * z * z * z * z) + Math.sin(z * z) + Math.sin(x * x) + Math.cos(z * z))
                        / ((float) Math.exp(0.35 * (x * x + z * z))) - 2.0f;
                pts[idx++] = new Point3f(x, y, z);
            }
        }

        int[] coords = new int[2 * n * (m - 1)];
        idx = 0;
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                coords[idx++] = i * n + j;
                coords[idx++] = (i - 1) * n + j;
            }
        }

        int[] stripCounts = new int[m - 1];
        for (int i = 0; i < m - 1; i++) {
            stripCounts[i] = 2 * n;
        }

        GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
        gi.setCoordinates(pts);
        gi.setCoordinateIndices(coords);
        gi.setStripCounts(stripCounts);

        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);

        return gi.getGeometryArray();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String op = ae.getActionCommand();
        if(op.equals("Sair")){
            System.exit(0);
        }else if(op.equals("Ligar Som")){
            bSom.setEnable(true);
        }else if(op.equals("Desligar Som")){
            bSom.setEnable(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        pc.setShapeLocation(me);
        
        PickResult pr = pc.pickClosest();
        if(pr != null){
            sw = (Switch) (pr.getNode(PickResult.SWITCH));
            if(sw!= null){
                sw.setWhichChild(1-sw.getWhichChild());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
  

}
class acertarAlvo extends Behavior{
    
    private Node colisao;
    private WakeupCriterion[] wc;
    private WakeupOr wor;
    private Switch sw;
    private Alpha alpha;
    private boolean alvo = false;
    
    
    public acertarAlvo(Node node, Switch s, Alpha a, Bounds b){
        colisao = node;
        sw = s;
        alpha = a;
        setSchedulingBounds(b);
    }
    

//    @Override
    public void initialize() {
        wc = new WakeupCriterion[2];
        wc[0] = new WakeupOnCollisionEntry(colisao);
        wc[1] = new WakeupOnElapsedTime(1);
        wor = new WakeupOr(wc);
        wakeupOn(wor);
    }

    @Override
    public void processStimulus(Enumeration en) {
        WakeupCriterion wakCri = (WakeupCriterion) en.nextElement();
        Node n;
        if(wakCri instanceof WakeupOnCollisionEntry){
            //existe uma colisao
//            n = ((WakeupOnCollisionEntry) wakCri)
//                    .getTriggeringPath().getObject();
//            System.out.println("Collided with " + n.getUserData());            
            
            
            
//            if(alvo == false){
////                sw.setWhichChild(1);
////                alvo = true;
                System.out.println("colisão");
//            }

        }else if(wakCri instanceof WakeupOnElapsedTime){
            //não existe colição
            if(alpha.value() < 0.1){
//                sw.setWhichChild(0);
//                alvo = true;
//                System.out.println("Não colisão");
            }

        }
        wakeupOn(wor);
        
    }
}

class Arma extends Behavior{
    private WakeupCriterion wc;
    private Alpha dispararBola; //usada paar disparar a bola
    private PositionInterpolator piBola;//usado para a animação da bola
    private int ed = 0; //calcular direção actual da arma esquerda/direita
    private int cb = 0;
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
                    if(tecla == KeyEvent.VK_A){
                        //tecla seta esquerda premida
                        if(ed < 8){
                            ++ed;                            
                        }
                        apontarBola.rotZ(((ed /32.0)+0.5)*Math.PI);
                        apontarArma.rotZ((ed /-32.0)*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    }else if (tecla == KeyEvent.VK_S){
                        //tecla seta direita premida
                        if (ed > -8){
                            --ed;
                        }
                        apontarBola.rotZ(((ed /32.0)+0.5)*Math.PI);
                        apontarArma.rotZ((ed /(-32.0))*Math.PI);
                        direcaoBola.setRotation(apontarBola);
                        direcaoArma.setRotation(apontarArma);
                        tgArma.setTransform(direcaoArma);
                        piBola.setAxisOfTranslation(direcaoBola);
                    } else if(tecla == KeyEvent.VK_SPACE){
                        //tecla espaço premida
                        dispararBola.setStartTime(System.currentTimeMillis());
                    }
                }
            }
        }
        wakeupOn(wc);
    }
    
    
    
    
}
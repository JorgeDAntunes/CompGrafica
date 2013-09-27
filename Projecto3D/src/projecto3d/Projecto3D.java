/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto3d;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import com.sun.org.apache.bcel.internal.classfile.Code;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
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
public class Projecto3D extends JApplet implements ActionListener{

    private float x = -1.0f;
    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    public BackgroundSound bSom;
    Alpha aBola;
    PositionInterpolator piMovBola;
    TransformGroup tgArma = new TransformGroup();
    TransformGroup tg = new TransformGroup();
    Switch swAlvo;
    Alpha aAlvo;
    
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
        
        //menu opções
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
        
        //Vista
        
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        Canvas3D cv = new Canvas3D(gc);
        setLayout(new BorderLayout());
        add(cv, BorderLayout.CENTER);
        
        OrbitBehavior orbit;
        SimpleUniverse su = new SimpleUniverse(cv);
        su.getViewingPlatform().setNominalViewingTransform();
        
        BranchGroup bg = createSceneGraph();
        bg.compile();
        su.addBranchGraph(bg);
        
        //inicializar som
        AudioDevice ad  =new JavaSoundMixer(su.getViewer().getPhysicalEnvironment());
        ad.initialize();
        
        
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
    
    private BranchGroup createSceneGraph() {
        BranchGroup root = new BranchGroup();
//        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
//        TransformGroup tg = new TransformGroup();
        
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        root.addChild(tg);
        
//        //Object
//        Appearance ap = new Appearance();
//        ap.setMaterial(new Material());
//        ColoringAttributes ca = new ColoringAttributes();
//        ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
//        ap.setColoringAttributes(ca);
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
        
        TransformGroup tgArm = new TransformGroup();
        //Arma e bola
        Node nbola = criarBola();
//        Node nAlvo = criarAlvos();
//        tgArm.addChild(nAlvo);
        Transform3D tfBola = new Transform3D();
        tfBola.set(new Vector3d(0, -19.5, 2),0.05);
        tgArm.setTransform(tfBola);
        tgArm.addChild(nbola);
        tgArm.addChild(criarArma());
        tg.addChild(tgArm);
        acertarAlvo acertaAlvo = new acertarAlvo(nbola, swAlvo, aBola, bounds);
        Arma arma = new Arma(aBola, piMovBola, tgArma, bounds);
        tg.addChild(arma);
        tg.addChild(acertaAlvo);
        
        //Rotação
//        Alpha alpha = new Alpha(-1, 4000);
//        RotationInterpolator rodar = new RotationInterpolator(alpha, tg);

//        rodar.setSchedulingBounds(bounds);
//        tg.addChild(rodar);
        
        //TEXTO
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
 
        //Som background
        BoundingSphere bsSom = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        bSom = new BackgroundSound();
        URL url = this.getClass().getClassLoader().getResource("som/world.wav");
        MediaContainer mc = new MediaContainer(url);
        bSom.setCapability(BackgroundSound.ALLOW_ENABLE_READ);
        bSom.setCapability(BackgroundSound.ALLOW_ENABLE_WRITE);
        bSom.setSoundData(mc);
        bSom.setLoop(Sound.INFINITE_LOOPS);
        bSom.setSchedulingBounds(bsSom);
        bSom.setInitialGain(0.1f);
        bSom.setEnable(true);
        tg.addChild(bSom);
        
        //botao som
        Appearance apSom = criarTexturaAppearance("imagens/som.jpg");
        apSom.setMaterial(new Material());
        
        Sphere esferaSom = new Sphere(0.03f, Primitive.ENABLE_GEOMETRY_PICKING | Primitive.GENERATE_TEXTURE_COORDS, apSom);
        Transform3D tfSom = new Transform3D();
        tfSom.setTranslation(new Vector3d(0, 0, 1));
        TransformGroup tgSom = new TransformGroup(tfSom);
        tgSom.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        tgSom.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(tgSom);
        tgSom.addChild(esferaSom);
        
        
        
        
        //luz Azul
        Appearance lAzul = new Appearance();
        lAzul.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        Color3f cLAzul = new Color3f(0.5f, 1.0f, 1.0f);
        ColoringAttributes caLAzul = new ColoringAttributes();
        caLAzul.setColor(cLAzul);
        lAzul.setColoringAttributes(caLAzul);
        
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
        Background bb = new Background(1f, 1f, 1f);
        bb.setApplicationBounds(bounds);
        root.addChild(bb);
        
        
     
        
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
        tArma.set(mArma, new Vector3d(0.0, 0.0, 0.0), 1.0);
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
    
    private BranchGroup criarAlvos(){
        BranchGroup rootAlvo = new BranchGroup();
        swAlvo = new Switch(0);
        swAlvo.setCapability(Switch.ALLOW_SWITCH_WRITE);
        
        // blue
        Appearance blueApp = new Appearance();
        blueApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        Color3f blueColor = new Color3f(0.3f, 0.3f, 1.0f);
        ColoringAttributes blueCA = new ColoringAttributes();
        blueCA.setColor(blueColor);
        blueApp.setColoringAttributes(blueCA);
        
         //Criar Bola
        Sphere bola = new Sphere(0.2f, blueApp);
        bola.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        
        
        TransformGroup tgRotAlvo = new TransformGroup();
        Transform3D tfRotAlvo = new Transform3D();
        Matrix3d mRotAlvo = new Matrix3d();
        mRotAlvo.rotY(Math.PI / 2);
        tfRotAlvo.set(mRotAlvo, new Vector3d(0.0, 0.0, -30.0), 1.0);
        tgRotAlvo.setTransform(tfRotAlvo);
        tgRotAlvo.addChild(bola);
        swAlvo.addChild(tgRotAlvo);

        TransformGroup tgMovAlvo = new TransformGroup();
        tgMovAlvo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgMovAlvo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        aAlvo = new Alpha(-1, 0, 0, 3000, 0, 0);
        Transform3D tf = new Transform3D();
        PositionInterpolator piMovAlvo = new PositionInterpolator(aBola, tgMovAlvo, tf, -30.0f, 30.0f);
        piMovAlvo.setSchedulingBounds(bounds);
        rootAlvo.addChild(piMovAlvo);
        rootAlvo.addChild(tgMovAlvo);
        
        
        
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
////        tgBola.addChild(swAlvo);
//        swAlvo.addChild(tgBola);
//        
//        TransformGroup tgAnim = new TransformGroup();
//        tgAnim.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
//        tgAnim.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        tgAnim.addChild(tgBola);
//        
//        aAlvo = new Alpha(-1, 0, 0, 200, 0, 0);
//        Transform3D tfAnim = new Transform3D();
//        PositionInterpolator piAlvo = new PositionInterpolator(aBola, tgAnim, tfAnim, -30.0f, 30.0f);
//        piAlvo.setSchedulingBounds(bounds);
//        rootAlvo.addChild(piAlvo);
//        rootAlvo.addChild(tgAnim);
        
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
            if(alvo == false){
                sw.setWhichChild(1);
                alvo = true;
                System.out.println("colisão");
            }

        }else if(wakCri instanceof WakeupOnElapsedTime){
            //não existe colição
            if(alpha.value() < 0.1){
                sw.setWhichChild(0);
                alvo = true;
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
                    if(tecla == KeyEvent.VK_LEFT){
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
                    }else if (tecla == KeyEvent.VK_RIGHT){
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
                    }//else if(tecla == KeyEvent.VK_UP){
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
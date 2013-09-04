/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

/**
 *
 * @author Jorge
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
 
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
 
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
 
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Node;
import java.util.Enumeration;
 
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.Bounds;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;
 
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
 
import com.sun.j3d.utils.geometry.Primitive;
 
 
public class TestAlpha extends JApplet {
 
    Alpha alpha = null;
    PositionInterpolator posInt = null;
 
    private Background background = null;
    Canvas3D canvas3D =
        new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
 
    // Create a bounds for the background and behaviours
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
 
    // Create the root of the branch graph
    BranchGroup scene = createSceneGraph();
    TransformGroup tg = simpleU.getViewingPlatform().getViewPlatformTransform();
 
    public TestAlpha(){
        launchApplication();
    }
 
    private void launchApplication() {
 
        background = new Background();
        background.setCapability(Background.ALLOW_IMAGE_WRITE);
        background.setCapability(Background.ALLOW_COLOR_WRITE);
        background.setApplicationBounds(bounds);
        scene.addChild(background);
 
        this.setLayout(new BorderLayout());
        this.add(canvas3D, BorderLayout.CENTER);
        positionCamera(simpleU);
 
        scene.compile();
        simpleU.addBranchGraph(scene);
    }
 
    private void positionCamera(SimpleUniverse su) {
        // Translation + rotation
        Transform3D translation = new Transform3D();
        translation.setTranslation(new Vector3f(0, -1, 14));
        Transform3D rotationX = new Transform3D();
        rotationX.rotX(45f * Math.PI/180f);
        Transform3D trans = new Transform3D();
        trans.mul(rotationX, translation);
        tg.setTransform(trans);
    }
 
    private BranchGroup createSceneGraph() {
        BranchGroup parent = new BranchGroup();
        parent.addChild(createTable());
 
        return parent;
    }
 
    private BranchGroup createTable(){
        // If you were writing a pool game you could use a PickSegment (PickRaySegment) to represent the pool cue
        // and you could pick with BoundingSphere to detecting collisions between the balls.
 
         
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
 
        BranchGroup parent = new BranchGroup();
        //Shape3D hockeyTableSlate = new Box(3.3f, 5.0f, 0.001f);
        Box hockeyTableSlate = new Box(3.3f, 5.0f, 0.001f, medBlueApp);
        //hockeyTableSlate.setUserData(new String("table bottom"));
 
        // the top table margin
        Box tableMarginTop = new Box(3.5f, 0.1f, 0.2f, orangeApp);
        tableMarginTop.setUserData(new String("top margin1"));
        Transform3D tableMarginToptr = new Transform3D();
        tableMarginToptr.setTranslation(new Vector3f(0f, 5.1f, 0.1f));
        TransformGroup tableMarginTopTG = new TransformGroup(tableMarginToptr);
        tableMarginTopTG.addChild(tableMarginTop);
        tableMarginTopTG.setUserData(new String("top margin2"));
 
 
        // the bottom table margin
        Box tableMarginBottom = new Box(3.5f, 0.1f, 0.2f, orangeApp);
        tableMarginBottom.setUserData(new String("bottom margin"));
        //SideCollisionDetector pcd2 = new SideCollisionDetector(tableMarginBottom);
        //pcd2.setSchedulingBounds(bounds);
        Transform3D tableMarginBottomtr = new Transform3D();
        tableMarginBottomtr.setTranslation(new Vector3f(0f, -5.1f, 0.1f));
        TransformGroup tableMarginBottomTG = new TransformGroup(
                tableMarginBottomtr);
        tableMarginBottomTG.addChild(tableMarginBottom);
        //parent.addChild(tableMarginBottomTG);
 
        TransformGroup mainTG = new TransformGroup();       
        mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        mainTG.addChild(hockeyTableSlate);
        mainTG.addChild(tableMarginTopTG);
        mainTG.addChild(tableMarginBottomTG);
        //parent.addChild(mainTG);
 
        // Create the rotate behavior node
        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(mainTG);
        parent.addChild(behavior);
        behavior.setSchedulingBounds(bounds);
 
        // Create the zoom behavior node
        MouseZoom behavior2 = new MouseZoom();
        behavior2.setTransformGroup(mainTG);
        parent.addChild(behavior2);
        behavior2.setSchedulingBounds(bounds);
 
        // Create the translate behavior node
        MouseTranslate behavior3 = new MouseTranslate();
        behavior3.setTransformGroup(mainTG);
        parent.addChild(behavior3);
        behavior3.setSchedulingBounds(bounds);
 
 
        //light blue
        Appearance ltBlueApp = new Appearance();
        ltBlueApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        Color3f ltBlueColor = new Color3f(0.5f, 1.0f, 1.0f);
        ColoringAttributes ltBlueCA = new ColoringAttributes();
        ltBlueCA.setColor(ltBlueColor);
        ltBlueApp.setColoringAttributes(ltBlueCA);
 
 
        // puck
        TransformGroup transPuckTG = new TransformGroup();
        transPuckTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
 
        Transform3D puckTranslation3D = new Transform3D();
        puckTranslation3D.setTranslation(new Vector3f(0f, 0.0f, .2f)); //0.05
        puckTranslation3D.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI / 2.0));
        transPuckTG.setTransform(puckTranslation3D);
 
        Cylinder puck = new Cylinder(0.22f, 0.075f, ltBlueApp);
        puck.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        transPuckTG.addChild(puck);
        //scd.setSchedulingBounds(bounds);
 
        // animation
        TransformGroup animationTG = new TransformGroup();
        animationTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D axe = new Transform3D();
        axe.setRotation(new AxisAngle4d(0.0, 0.0, 1.0, Math.PI/2));
 
        alpha = new Alpha(-1, 5000);
        alpha.setStartTime(System.currentTimeMillis());
        posInt = new PositionInterpolator(alpha, animationTG, axe, 0f, 6.1f);
        posInt.setSchedulingBounds(bounds);
 
        SideCollisionDetector scd = new SideCollisionDetector(transPuckTG,/* axe, alpha, posInt, */bounds);
        // try adding the alpha
 
        animationTG.addChild(posInt);
 
 
        animationTG.addChild(transPuckTG);
        animationTG.addChild(scd);
        mainTG.addChild(animationTG);
 
        parent.addChild(mainTG);
 
 
        parent.compile();
        return parent;
    }
 
 
    public static void main(String[] args) {
        Frame frame = new MainFrame(new TestAlpha(), 790, 600);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
 
            public void windowClosing(WindowEvent winEvent) {
                System.exit(0);
            }
        }); 
    }
 
} //TestAlpha.java

//**********************+

class SideCollisionDetector extends Behavior{
 
    private static final Color3f highlightColor = new Color3f(0.0f, 1.0f, 0.0f);
 
    private Appearance sideAppearance;
    private static final ColoringAttributes highlight = new ColoringAttributes(
            highlightColor, ColoringAttributes.SHADE_GOURAUD); //smooth shading model
 
    /** The separate criteria used to wake up this behaviour. */
    private WakeupCriterion[] theCriteria;
 
    /** The OR of the separate criteria. */
    private WakeupOr oredCriteria;
 
    /** The transformgroup that is watched for collision. */
    private TransformGroup collidingTG;
 
    Vector3d v = new Vector3d();
 
    /**
     * @param transG
     *          TransformGroup that is to be watched for collisions.
     * @param theBounds
     *          Bounds that define the active region for this behaviour.
     */
    public SideCollisionDetector(TransformGroup transTG, /*Transform3D trans3D, Alpha a, PositionInterpolator positionInt, */Bounds theBounds) {
        collidingTG = transTG;
        //collidingTrans3D = trans3D;
        //collidingAlpha = a;
        //collidingPos = positionInt;
        setSchedulingBounds(theBounds);
        //inCollision = false;
    }
 
    /**
     * This creates an entry, exit and movement collision criteria. These are then OR'ed together,
     * and the wake up condition set to the result.
     */
    public void initialize() {
        theCriteria = new WakeupCriterion[3];
        theCriteria[0] = new WakeupOnCollisionEntry(collidingTG, WakeupOnCollisionEntry.USE_GEOMETRY);
        theCriteria[1] = new WakeupOnCollisionExit(collidingTG, WakeupOnCollisionEntry.USE_GEOMETRY);
        theCriteria[2] = new WakeupOnCollisionMovement(collidingTG, WakeupOnCollisionEntry.USE_GEOMETRY);
        oredCriteria = new WakeupOr(theCriteria);
        wakeupOn(oredCriteria);
 
    }
 
    public void processStimulus(Enumeration criteria) {
 
        WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
        if (theCriterion instanceof WakeupOnCollisionEntry) {
            Node theLeaf = ((WakeupOnCollisionEntry) theCriterion)
            .getTriggeringPath().getObject().getParent();
            System.out.println("Collided with " + theLeaf.getUserData());
 
            sideAppearance = ((Primitive) theLeaf).getAppearance();
            sideAppearance.getColoringAttributes();
            sideAppearance.setColoringAttributes(highlight);
 
        } else if (theCriterion instanceof WakeupOnCollisionExit) {
            Node theLeaf = ((WakeupOnCollisionExit) theCriterion)
            .getTriggeringPath().getObject().getParent();
            System.out.println("Stopped colliding with  "
                    + theLeaf.getUserData());
 
            // orange
            Appearance orangeApp = new Appearance();
            orangeApp.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
            Color3f orangeColor = new Color3f(0.988f, 0.800f, 0.080f);
            ColoringAttributes orangeCA = new ColoringAttributes(orangeColor, ColoringAttributes.SHADE_GOURAUD);
            orangeCA.setColor(orangeColor);
 
            sideAppearance.setColoringAttributes(orangeCA);
        } else {
 
            Node theLeaf = ((WakeupOnCollisionMovement) theCriterion)
            .getTriggeringPath().getObject().getParent();
            System.out.println("Moved whilst colliding with "
                    + theLeaf.getUserData());
 
            sideAppearance = ((Primitive) theLeaf).getAppearance();
            sideAppearance.getColoringAttributes();
            sideAppearance.setColoringAttributes(highlight);
        }
        wakeupOn(oredCriteria); 
    }
}
//}
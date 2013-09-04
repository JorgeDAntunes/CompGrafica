/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

/**
 *
 * @author Jorge
 */
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.Morph;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
/**
 * This uses the class SimpleMorphBehaviour to animate a shape between two key
 * shapes: a cube and a pyramid. The Morph object is the same as that used in
 * the program SimpleMorph, but this time we use an alpha generator to drive the
 * animation.
 * 
 * @author I.J.Palmer
 * @version 1.0
 * @see SimpleMorphBehaviour
 * @see SimpleMorph
 */
public class SimpleMorph2 extends Frame implements ActionListener {
  protected Canvas3D myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
  protected Button exitButton = new Button("Exit");
  /** This performs the animation */
  protected Morph myMorph;
  /** This drives the Morph object */
  protected SimpleMorphBehaviour myBehave;
  /** The active bounds for the behaviour */
  protected BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,
      0.0), 100.0);
  /**
   * Build the view branch of the scene graph
   * 
   * @return BranchGroup that is the root of the view branch
   */
  protected BranchGroup buildViewBranch(Canvas3D c) {
    BranchGroup viewBranch = new BranchGroup();
    Transform3D viewXfm = new Transform3D();
    viewXfm.set(new Vector3f(0.0f, 0.0f, 5.0f));
    TransformGroup viewXfmGroup = new TransformGroup(viewXfm);
    ViewPlatform myViewPlatform = new ViewPlatform();
    PhysicalBody myBody = new PhysicalBody();
    PhysicalEnvironment myEnvironment = new PhysicalEnvironment();
    viewXfmGroup.addChild(myViewPlatform);
    viewBranch.addChild(viewXfmGroup);
    View myView = new View();
    myView.addCanvas3D(c);
    myView.attachViewPlatform(myViewPlatform);
    myView.setPhysicalBody(myBody);
    myView.setPhysicalEnvironment(myEnvironment);
    return viewBranch;
  }
  /**
   * Add some lights to the scene graph
   * 
   * @param b
   *            BranchGroup that the lights are added to
   */
  protected void addLights(BranchGroup b) {
    Color3f ambLightColour = new Color3f(0.5f, 0.5f, 0.5f);
    AmbientLight ambLight = new AmbientLight(ambLightColour);
    ambLight.setInfluencingBounds(bounds);
    Color3f dirLightColour = new Color3f(1.0f, 1.0f, 1.0f);
    Vector3f dirLightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
    DirectionalLight dirLight = new DirectionalLight(dirLightColour,
        dirLightDir);
    dirLight.setInfluencingBounds(bounds);
    b.addChild(ambLight);
    b.addChild(dirLight);
  }
  /**
   * Create the Morph from the given shapes
   * 
   * @param theShapes
   *            GeometryArray that stores the shapes for the Morph
   * @param app
   *            Appearnce used for the shapes
   * @return Morph that uses the given shapes
   */
  protected Morph createMorph(GeometryArray[] theShapes, Appearance app) {
    double[] weights = { 1.0, 0.0 };
    Alpha morphAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE
        | Alpha.DECREASING_ENABLE, 0, 0, 4000, 2000, 0, 4000, 2000, 0);
    myMorph = new Morph(theShapes, app);
    myMorph.setWeights(weights);
    myMorph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
    myBehave = new SimpleMorphBehaviour(morphAlpha, myMorph);
    myBehave.setSchedulingBounds(bounds);
    return myMorph;
  }
  /**
   * Build the content branch for the scene graph
   * 
   * @return BranchGroup that is the root of the content
   */
  protected BranchGroup buildContentBranch() {
    Appearance app = new Appearance();
    Color3f ambientColour = new Color3f(1.0f, 0.0f, 0.0f);
    Color3f emissiveColour = new Color3f(0.0f, 0.0f, 0.0f);
    Color3f specularColour = new Color3f(1.0f, 1.0f, 1.0f);
    Color3f diffuseColour = new Color3f(1.0f, 0.0f, 0.0f);
    float shininess = 20.0f;
    app.setMaterial(new Material(ambientColour, emissiveColour,
        diffuseColour, specularColour, shininess));
    //Make the cube key shape
    IndexedQuadArray indexedCube = new IndexedQuadArray(8,
        IndexedQuadArray.COORDINATES | IndexedQuadArray.NORMALS, 24);
    Point3f[] cubeCoordinates = { new Point3f(1.0f, 1.0f, 1.0f),
        new Point3f(-1.0f, 1.0f, 1.0f),
        new Point3f(-1.0f, -1.0f, 1.0f),
        new Point3f(1.0f, -1.0f, 1.0f), new Point3f(1.0f, 1.0f, -1.0f),
        new Point3f(-1.0f, 1.0f, -1.0f),
        new Point3f(-1.0f, -1.0f, -1.0f),
        new Point3f(1.0f, -1.0f, -1.0f) };
    Vector3f[] cubeNormals = { new Vector3f(0.0f, 0.0f, 1.0f),
        new Vector3f(0.0f, 0.0f, -1.0f),
        new Vector3f(1.0f, 0.0f, 0.0f),
        new Vector3f(-1.0f, 0.0f, 0.0f),
        new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f) };
    int cubeCoordIndices[] = { 0, 1, 2, 3, 7, 6, 5, 4, 0, 3, 7, 4, 5, 6, 2,
        1, 0, 4, 5, 1, 6, 7, 3, 2 };
    int cubeNormalIndices[] = { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3,
        3, 3, 4, 4, 4, 4, 5, 5, 5, 5 };
    indexedCube.setCoordinates(0, cubeCoordinates);
    indexedCube.setNormals(0, cubeNormals);
    indexedCube.setCoordinateIndices(0, cubeCoordIndices);
    indexedCube.setNormalIndices(0, cubeNormalIndices);
    //Make the pyramid key shape. Although this needs
    //only five vertices to create the desired shape, we
    //need to use six vertices so that it has the same
    //number as the cube.
    IndexedQuadArray indexedPyramid = new IndexedQuadArray(8,
        IndexedQuadArray.COORDINATES | IndexedQuadArray.NORMALS, 24);
    Point3f[] pyramidCoordinates = { new Point3f(0.0f, 1.0f, 0.0f),
        new Point3f(0.0f, 1.0f, 0.0f), new Point3f(-1.0f, -1.0f, 1.0f),
        new Point3f(1.0f, -1.0f, 1.0f), new Point3f(0.0f, 1.0f, 0.0f),
        new Point3f(0.0f, 1.0f, 0.0f),
        new Point3f(-1.0f, -1.0f, -1.0f),
        new Point3f(1.0f, -1.0f, -1.0f) };
    Vector3f[] pyramidNormals = { new Vector3f(0.0f, 0.0f, 1.0f),
        new Vector3f(0.0f, 0.0f, -1.0f),
        new Vector3f(1.0f, 0.0f, 0.0f),
        new Vector3f(-1.0f, 0.0f, 0.0f),
        new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0.0f, -1.0f, 0.0f) };
    int pyramidCoordIndices[] = { 0, 1, 2, 3, 7, 6, 5, 4, 0, 3, 7, 4, 5, 6,
        2, 1, 0, 4, 5, 1, 6, 7, 3, 2 };
    int pyramidNormalIndices[] = { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3,
        3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5 };
    indexedPyramid.setCoordinates(0, pyramidCoordinates);
    indexedPyramid.setNormals(0, pyramidNormals);
    indexedPyramid.setCoordinateIndices(0, pyramidCoordIndices);
    indexedPyramid.setNormalIndices(0, pyramidNormalIndices);
    //Set the contents of the array to the two shapes
    GeometryArray[] theShapes = new GeometryArray[2];
    theShapes[0] = indexedCube;
    theShapes[1] = indexedPyramid;
    BranchGroup contentBranch = new BranchGroup();
    //Create a transform to rotate the shape slightly
    Transform3D rotateCube = new Transform3D();
    rotateCube.set(new AxisAngle4d(1.0, 1.0, 0.0, Math.PI / 4.0));
    TransformGroup rotationGroup = new TransformGroup(rotateCube);
    contentBranch.addChild(rotationGroup);
    addLights(contentBranch);
    //Call the function to build the morph
    rotationGroup.addChild(createMorph(theShapes, app));
    //Add the behaviour to the scene graph to activate it
    rotationGroup.addChild(myBehave);
    return contentBranch;
  }
  public void actionPerformed(ActionEvent e) {
    dispose();
    System.exit(0);
  }
  public SimpleMorph2() {
    VirtualUniverse myUniverse = new VirtualUniverse();
    Locale myLocale = new Locale(myUniverse);
    myLocale.addBranchGraph(buildViewBranch(myCanvas3D));
    myLocale.addBranchGraph(buildContentBranch());
    setTitle("SimpleMorph");
    setSize(400, 400);
    setLayout(new BorderLayout());
    Panel bottom = new Panel();
    bottom.add(exitButton);
    add(BorderLayout.CENTER, myCanvas3D);
    add(BorderLayout.SOUTH, bottom);
    exitButton.addActionListener(this);
    setVisible(true);
  }
  public static void main(String[] args) {
    SimpleMorph2 sw = new SimpleMorph2();
  }
}
/**
 * This class uses an alpha generator to change the weights of a Morph node. It
 * morphs a shape between two key shapes repeatedly once a key has been pressed.
 * Subsequent key presses toggle the running state of the animation.
 * 
 * @author I.J.Palmer
 * @version 1.0
 * @see SimpleMorph2
 */
class SimpleMorphBehaviour extends Behavior {
  /** Used to drive the animation */
  protected Alpha theAlpha;
  /** The weights of this are changed by the alpha values */
  protected Morph theMorph;
  /** Used to define the Morph weights */
  protected double theWeights[] = new double[2];
  /** Defines whether the animation is running or not */
  protected boolean running = false;
  /** The triggers for the animation to start */
  protected WakeupCriterion[] wakeConditions;
  /** The combined triggers */
  protected WakeupOr oredConditions;
  /** Set up the criteria to trigger after zero time or when a key is pressed */
  public void initialize() {
    wakeConditions = new WakeupCriterion[2];
    wakeConditions[0] = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
    wakeConditions[1] = new WakeupOnElapsedFrames(0);
    oredConditions = new WakeupOr(wakeConditions);
    wakeupOn(wakeConditions[0]);
  }
  /**
   * If the behaviour is not running and and a key has been pressed, start the
   * animation and vice-versa. Otherwise calculate a new set of weights.
   */
  public void processStimulus(Enumeration criteria) {
    WakeupCriterion theCriteria;
    theCriteria = (WakeupCriterion) criteria.nextElement();
    //If a key has been pressed, toggle the running state
    if (theCriteria instanceof WakeupOnAWTEvent) {
      running = !running;
    }
    if (running) {
      //Get the alpha value
      double alphaValue = theAlpha.value();
      //Set the two weights according to this value
      theWeights[0] = 1.0 - alphaValue;
      theWeights[1] = alphaValue;
      //Use the weights in the Morph
      theMorph.setWeights(theWeights);
    }
    //Set the trigger conditions again
    wakeupOn(oredConditions);
  }
  /**
   * Set up the data for the behaviour.
   * 
   * @param a
   *            Alpha that is used to drive the animation
   * @param m
   *            Morph that is affected by this behaviour
   */
  public SimpleMorphBehaviour(Alpha a, Morph m) {
    theAlpha = a;
    theMorph = m;
  }
}
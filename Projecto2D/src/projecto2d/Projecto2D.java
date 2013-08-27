/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projecto2d;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.*;
import javax.swing.event.*;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.TitledBorder;

//Class para desenhar as imagens
/**
 *
 * @author Jorge
 */
public class Projecto2D extends JApplet implements ActionListener, ChangeListener {

    //GUI Swing/AWT
    private Container con;
    private JPanel pBotoes;
    private JPanel pOpcoes;
    private JPanel pMenu;
    private JMenuBar mb;
    private JMenu menu;
    private JMenuItem mi;
    private JSlider sContraste;
    private JButton bIniciar;
    private JButton bStop;
    private JButton bSugestao;
    private JButton bSolucao;
    private JLabel lInfo;
    private JSpinner sNPecas;
    private JLabel lNPecas;
    private JLabel lMovimentos;
    private JLabel lTempo;
    private JLabel lVelocidade;
    private JSlider sVelocidade;
    private JLabel lTransparencia;
    private JSlider sTransparencia;
    private JTextField tMovimentos;
    private JTextField tTempo;
    
    
    //variaveis
    private PAreaJogo pAJ;
    private float transparencia = 0.4f;
    private PLoading pLoaging;
    private String opImagem = "Original";
    private PrinterJob pj;
    private int nPecas = 3; //numero de pecas
    
    
    
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Torre de Hanoi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Projecto2D applet = new Projecto2D();
        applet.init();
        frame.getContentPane().add(applet);
        frame.pack();
//        frame.setSize(800, 450);//Correto
        frame.setSize(1000, 450);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null); //applet aparecer no centro do ecrã
        frame.setResizable(false);
    }
    
    
    public void init() {
                //***********MENU*********************
        mb = new JMenuBar();
        setJMenuBar(mb);

        menu = new JMenu("Opções");
        mi = new JMenuItem("Imprimir");
        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Voltar");
        mi.addActionListener(this);
        menu.add(mi);

        mb.add(menu);
        menu = new JMenu("Imagem");
        mi = new JMenuItem("Repor Imagem");
        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Tom Cinzento");
        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Aumentar contraste");
        mi.addActionListener(this);
        menu.add(mi);

        menu.addSeparator();

        mi = new JMenuItem("Suavizar");
        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Realçar");
        mi.addActionListener(this);
        menu.add(mi);

        mi = new JMenuItem("Destacar Arestas");
        mi.addActionListener(this);
        menu.add(mi);

        mb.add(menu);

        //******************PAINEIS*****************************
        //-----------painel Area de Jogo------------
        pAJ = new PAreaJogo();
        //Print
        pj = PrinterJob.getPrinterJob();
        pj.setPrintable(pAJ);
        add(pAJ, BorderLayout.CENTER);
        
        //-----------painel Botões-----------------
        pBotoes = new JPanel();
        pBotoes.setDoubleBuffered(true);
        pBotoes.setBackground(Color.red);
        //titulo no painel
        TitledBorder title;
        title = BorderFactory.createTitledBorder("Botões");
        title.setTitleJustification(TitledBorder.CENTER);
        pBotoes.setBorder(title);
        
        //Botao Iniciar
        bIniciar = new JButton("Iniciar");
        bIniciar.addActionListener(this);
        bIniciar.setEnabled(true);
        pBotoes.add(bIniciar);
        
        //adicionar o painel 
        add(pBotoes, BorderLayout.SOUTH);

        //----------------painel Opções----------------
        pOpcoes = new JPanel();
        pOpcoes.setDoubleBuffered(true);
        pOpcoes.setBackground(Color.YELLOW);
        //titulo no painel
        title = BorderFactory.createTitledBorder("Opções");
        title.setTitleJustification(TitledBorder.RIGHT);
        pOpcoes.setBorder(title);
        
        //Numero de peças
        lNPecas = new JLabel("Número de Peças:");
//        pOpcoes.add(lNPecas);
        sNPecas = new JSpinner(new SpinnerNumberModel(3, 3, 8, 1));
        sNPecas.addChangeListener(this);
        pOpcoes.add(sNPecas);
        
        //adicionar painel
        add(pOpcoes, BorderLayout.EAST);


        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        
    }

    //******************CLASS PAINEL AREA DE JOGO**************
    class PAreaJogo extends JPanel implements Runnable, ActionListener, MouseListener, MouseMotionListener, Printable {

        private int movActual; //movimento actual
        private BufferedImage bi;
        private Projecto2D th;
        private Imagem pecas;
        private Movimento[] movimentos;
        private Posicao[] posicao;
        private int n;
        private int[] torre;
        private int peca;
        private int x, y;
        private boolean movCompleto;
        private int paso;
        private Timer tempo;
                
        public PAreaJogo() {
//            this.nPecas = nPecas;
//            this.th = th;
            pecas = new Imagem();
            inicializarAnimação();
            
        }
      
        
        private void inicializarAnimação(){
            n = 0;
            torre = new int[4];
            torre[1] = nPecas;
            torre[2] = 0;
            torre[3] = 0;
            peca = 1;
            movimentos = new Movimento[(int) Math.pow(2, nPecas)];
            algoritmoHanoi(nPecas, 1, 2, 3);
            posicao = new Posicao[9];
            for (int i = 1; i <= nPecas; i++) {
                int w = nPecas - i + 1;
                posicao[i] = new Posicao(posicaoXPeca(i, 1), posicaoYPeca(w));
            }
            x = posicao[1].getX();
            y = posicao[1].getY();
            movActual = 1;
            movCompleto = false;
            paso = 1;
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            //criar uma imagem para juntar todos os componentes na mesma
            Graphics2D gImage = image.createGraphics();
            Color castanho = new Color(0xA67D3D);
//            GradientPaint grad = new GradientPaint(100, 50, Color.YELLOW, 400, 500, castanho);
//            gImage.setPaint(grad);
            
            
            //desemhar base e torres
            Shape base = new torre(25f, 295f, 630f, 40f).criarTorre();
            Area b = new Area(new torre(25f, 295f, 630f, 40f).criarTorre());
            Area t1 = new Area(new torre(127.5f, 70f, 15f, 225f).criarTorre());
            Area t2 = new Area(new torre(327.5f, 70f, 15f, 225f).criarTorre());
            Area t3 = new Area(new torre(527.5f, 70f, 15f, 225f).criarTorre());
            b.add(t1);
            b.add(t2);
            b.add(t3);
            
            URL url = getClass().getClassLoader().getResource("imagens/textura12.jpg");
            try {
                bi = ImageIO.read(url);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            Rectangle2D tr = new Rectangle2D.Double(0, 0, bi.getWidth(), bi.getHeight());
            //texturepaint
            TexturePaint tp = new TexturePaint(bi, tr);
            gImage.setPaint(tp);
            gImage.fill(b);
            
            //desenhar peças
            for (int i = nPecas; i >= 1; i--) {

            try {
                    bi = ImageIO.read(getClass().getClassLoader().getResource("pecas/" + i + ".png"));
                    switch (opImagem) {
                        case "Original":
                            gImage.drawImage(pecas.setImage(bi), posicao[i].getX(), posicao[i].getY(), this);
                            break;
                        case "Repor Imagem":
                            gImage.drawImage(pecas.setImage(bi), posicao[i].getX(), posicao[i].getY(), this);
                            break;
                        case "Destacar Arestas":
                            gImage.drawImage(pecas.setImage(arestar(bi)), posicao[i].getX(), posicao[i].getY(), this);
                            break;
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            gImage.setColor(Color.WHITE);
            Font font = new Font("Serif", Font.BOLD, 20);
            gImage.setFont(font);
            gImage.drawString("Torre 1", 105, 320);
            gImage.drawString("Torre 2", 305, 320);
            gImage.drawString("Torre 3", 505, 320);
            
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_OVER, transparencia);
            gImage.setComposite(ac);

            Toolkit.getDefaultToolkit().sync();



            //Desenhar imagem 
            g2.drawImage(image, null, 0, 0);
            g.dispose();
            
        }
        
        public void algoritmoHanoi(int m, int origen, int temporal, int destino){
            if (m == 0) {
            return;
        }
        algoritmoHanoi(m - 1, origen, destino, temporal);
        n++;
        movimentos[n] = new Movimento(m, origen, destino);
        algoritmoHanoi(m - 1, temporal, origen, destino);
        }
        public int posicaoXPeca(int ficha, int torre) {
        int k = (torre - 1) * 200;
        switch (ficha) {
            case 1:
                return 110 + k;
            case 2:
                return 100 + k;
            case 3:
                return 90 + k;
            case 4:
                return 80 + k;
            case 5:
                return 70 + k;
            case 6:
                return 60 + k;
            case 7:
                return 50 + k;
            case 8:
                return 40 + k;
        }
        return 0;
    }

    public int posicaoYPeca(int nivel) {
        switch (nivel) {
            case 1:
                return 260;
            case 2:
                return 233;
            case 3:
                return 206;
            case 4:
                return 179;
            case 5:
                return 152;
            case 6:
                return 125;
            case 7:
                return 98;
            case 8:
                return 71;
        }
        return 0;
    }
        public void iniciarAnimação(){
            //animação
            
            //music
            
        }
        
        public void pausarAnimação(){
            //animação
            
            //music
            
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            
        }

        @Override
        public void mousePressed(MouseEvent me) {
            
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            
        }

        @Override
        public void mouseExited(MouseEvent me) {
            
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            
        }

        @Override
        public void mouseMoved(MouseEvent me) {
            
        }

        @Override
        public void run() {
            while(true){
                repaint();
            }
        }
        
        @Override
        public int print(Graphics g, PageFormat pf, int i) throws PrinterException {
            switch (i) {
                case 0:
                    paintComponent(g);
                    break;

                default:
                    return NO_SUCH_PAGE;
            }
            return PAGE_EXISTS;
        }

        private int iterCount(float cr, float ci) {
            
            return 0;
        }
        
    }
    
    //*****EFEITOS DAS IMAGENS**************
    public static BufferedImage arestar(BufferedImage arestar) {
        AffineTransformOp op = null;
        BufferedImageOp bio = null;
        float[] data = {0.0f, -1.0f, 0.0f, -1.0f, 4.0f, -1.0f, 0.0f, -1.0f, 0.0f};
        try {
            Kernel k = new Kernel(3, 3, data);
            bio = new ConvolveOp(k);
        } catch (Exception e) {
            //Do something here
        }
        return (new ConvolveOp(new Kernel(9, 1, data))).filter(arestar, null);
    }
    public BufferedImage Contraste(BufferedImage bi) {
        BufferedImage temp = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_BGR);
        int a = 0;
        int b = 255;
        int c = 255;
        int d = 0;
        Color pixel = null;

        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                pixel = new Color(bi.getRGB(x, y));
                if (pixel.getRed() < c) {
                    c = pixel.getRed();
                }
                if (pixel.getRed() > d) {
                    d = pixel.getRed();
                }
            }
        }

        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                pixel = new Color(bi.getRGB(x, y));
                int i = (int) ((pixel.getRed() - c) * ((b - a) / (d - c)) + a);

                Color ci = new Color(i, i, i);
                temp.setRGB(x, y, ci.getRGB());
                //if (i>100) temp.setRGB(x, y, Color.BLACK.getRGB());
                //else temp.setRGB(x, y, Color.WHITE.getRGB()); 
            }
        }
        return temp;
    }

    public BufferedImage RGB2GRAY(BufferedImage bi) {
        BufferedImage temp = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_BGR);
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                Color pixel = new Color(bi.getRGB(x, y));
                int i = (int) ((pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3.0);

                Color ci = new Color(i, i, i);
                temp.setRGB(x, y, ci.getRGB());
                //if (i>100) temp.setRGB(x, y, Color.BLACK.getRGB());
                //else temp.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        return temp;
    }
    
    //painel do loading
class PLoading extends JPanel implements Runnable{
    int load=0;
    int alpha = 0;
    boolean loading = true;
    public PLoading() {
        setPreferredSize(new Dimension(200, 200));
        setBackground(new Color(9, 182, 64));
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        
        while (true) {
            load += 1;
            if (load <= 100) {
                repaint();
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ex) {
                }
            } else{
                loading = false;
                return;
            }
        }  
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("Serif", Font.BOLD, 30);
        g2.setFont(font);
        g2.translate(500, 195);
        AffineTransform at = new AffineTransform();
        for (int i = 0; i < 8; i++) {
            
            Shape shape = new Ellipse2D.Double(42, 42, 105, 105);
            if (alpha < 254) {
                alpha += (int)(255/8);
                if(alpha>255)alpha=255;
            } else {
                alpha = 15;
            }
            at.rotate(Math.PI / 4);
            shape = at.createTransformedShape(shape);
            g2.setColor(new Color(185, 192, 195, alpha));
            g2.fill(shape);
        }
        g2.drawString("LOADING...", -80, 0);
        g2.drawString(""+load+"%", -20, 30);

    }
}
}
class Imagem extends JPanel {

    private BufferedImage image = null;
    private int x,y;
    private Posicao[] posicao;

    public Imagem() {
        image = null;
//        setPreferredSize(new Dimension(500, 500));
        setBackground(Color.white);
        posicao = new Posicao[9];
    }
    

    public Image setImage(BufferedImage bi) {
        this.image = bi;
        this.x = 0;
        this.y = 0;
        setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
        invalidate();
        repaint();
        return image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (image != null) {
            g2.drawImage(image, x, y, this);
        } else {
            g2.drawRect(x, y, this.getWidth() - 1, this.getHeight() - 1);
        }
    }
}

//painel do menu, onde escolhemos o modo de jogo entre outros
class PMenu extends JPanel implements MouseListener {

    public PMenu() {
        setPreferredSize(new Dimension(400, 600));
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    }

    public void mouseClicked(MouseEvent me) {
    }

    public void mousePressed(MouseEvent me) {
    }

    public void mouseReleased(MouseEvent me) {
    }

    public void mouseEntered(MouseEvent me) {
    }

    public void mouseExited(MouseEvent me) {
    }
}
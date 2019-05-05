
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class GUI_MC extends JPanel implements ActionListener {

    final private Border bordeDibujo = BorderFactory.createTitledBorder("Gráfico");
    final private Border bordemenuDer = BorderFactory.createLoweredBevelBorder();
    final private Border bordeOpciones = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Opciones");
    
    private String nombreAlgoritmo;
    final private SpinnerModel model = new SpinnerNumberModel(1,1, 100000000,1);
    final private JSpinner selectorNum = new JSpinner(model);
    private panelDibujo dib = new panelDibujo();
    private panelGrafico graf = new panelGrafico();
    private dibujoEntropia dibujaEntropia = new dibujoEntropia();
    private dibujoHamming H = new dibujoHamming();
    final private randomGenerator numAleatorio = new randomGenerator();
    private JLabel algSeleccion = new JLabel("Ninguno");
    private int fronteraSeleccionada;
    private int fTransicion;
    private Color [] colorCell;
    
    private ca1DSimulator ac;
    
    final private int CanvaX = 500, CanvaY = 500;
    
    public class dibujoHamming extends Canvas
    {
        dibujoHamming()
        {
            this.setPreferredSize(new Dimension(600,100));
            this.setBackground(Color.white);
        }
        
        @Override
        public void paint(Graphics g)
        {
            int d = 0 ; 
            int gen = 1;
            ArrayList<Integer> distancias = new ArrayList<Integer>();
            while(d != -1)
            {
                d = ac.dHamming(gen);
                int descala = d * 100 /500;
                int dinversa = 100 + descala*-1;
                
                distancias.add(dinversa);
                
                System.out.println("escalado a 100: " + descala + " inversa: " + dinversa);
                
                gen++;                
            }
            
            for(int i = 1 ; i < distancias.size() - 1; i++)
            {
                g.setColor(Color.red);
                g.drawLine(i-1, distancias.get(i-1), i, distancias.get(i));
            }
            
        }
    }
    
    public class dibujoEntropia extends Canvas
    {
        dibujoEntropia()
        {
           this.setPreferredSize(new Dimension(600,100));
           this.setBackground(Color.white);
        }
        
        int estadoMax, numMaxCells, nGen;
        int [] conteo;
        int [][] resultados;
        
        public void cargaDatosEntropia(int k, int cellmax, int [] cont, int gen, int[][] res)
        {
            estadoMax = k;
            numMaxCells = cellmax;
            nGen = gen;
            
            resultados = new int[nGen][numMaxCells];
            conteo = new int[cont.length];
        
            for(int i = 0 ; i < cont.length ; i++)
            {
                conteo[i] = cont[i];
            }

            for(int i = 0 ; i < nGen ; i++)
                for(int j = 0 ; j < numMaxCells ; j++)
                    resultados[i][j] = res[i][j];

        }
        
        @Override
        public void paint(Graphics g)
        {
            
            ac.dibujaEntropia();
            double [] p = new double[estadoMax];
            double [] sumas = new double[nGen];
            
            System.out.println(conteo.length);
            
            for(int i = 0 ; i < conteo.length ; i++)
                p[i] = conteo[i]/(double)(nGen * numMaxCells);
            
            for(int i = 0 ; i < nGen ; i++)
            {
                double sum = 0;
                for(int j = 0 ; j < numMaxCells ; j++)
                {
                    double pxi = p[resultados[i][j]];
                    //System.out.println("pxi: " +pxi);
                    sum += (pxi * (Math.log10(pxi)/Math.log10(2)));
                }
                sum = (double)sum/numMaxCells*-1;
                System.out.println(sum);
                sumas[i] = sum;
            }
            
            for(int i = 1 ; i < nGen ; i++)
            {
                double valorInvertido1 = sumas[i] * -1 * 100 + 100;
                //double valorInvertido0 = sumas[i - 1] * -1 * 100 +100;
                g.setColor(Color.red);
                //g.drawLine(i * 600 / nGen, (int) valorInvertido0, i * 600 /nGen, (int) valorInvertido1);
                g.drawOval(i, (int) valorInvertido1, 1, 1);
                g.fillOval(i, (int) valorInvertido1, 1, 1);
            }
            
        }
    }
   
    public class panelGrafico extends Canvas
    {
        panelGrafico()
        {
            this.setPreferredSize(new Dimension(600, 100));
            this.setBackground(Color.white);
        }
        
        int [][] numCeldasTipo;
        int nGen;
        int numEstado;
        int maxValue = 1;
        int numMaxCells;
        
        public void parseData(int [][] resultado, int g, int n, int numMax)
        {
            nGen = g;
            numEstado = n;
            numCeldasTipo = new int[nGen][n];
            numMaxCells = numMax;
            
            for(int i = 0 ; i < nGen ; i++)
            {
                for(int j = 0 ; j < numMaxCells ; j++)
                {
                    numCeldasTipo[i][resultado[i][j]]++;
                }
            }
            
            repaint();
        }
               
        @Override 
        public void paint(Graphics g)
        {
            
            g.drawLine(10, 0, 10, 200);
            int divisiones = 200 / numEstado;
            g.drawLine(0, 0, 10, 0);
            
            System.out.println("VAMOS A PINTAR");
            for(int i = 0 ; i < nGen - 1 ; i++)
            {
                for(int j = 0 ; j < numEstado ; j++)
                {
                    int dato = numCeldasTipo[i][j];
                    int dato1 = numCeldasTipo[i+1][j];
                    
                    System.out.print("[X: " + i + ", Y: " + j + " data: " + dato +"] ");
                    
                    g.setColor(colorCell[j]);
                    g.drawLine(i + 10, dato % 200, i + 1 + 10, dato1 % 200);
                    
                    //if(maxValue < dato) { maxValue = dato; repaint(); }
                }
            }
        }
    }
    
    
    public class panelDibujo extends Canvas
    {
        panelDibujo()
        {
            this.setPreferredSize(new Dimension(500, 500));
            //this.setMinimumSize(new Dimension(100, 100));
            //this.setBackground(new Color(255, 255, 230)); 
            this.setBackground(Color.white);

        }
       
        //@Override
       // public void update(Graphics g)
        //{
          // paint(g);
        //}
        
        int [][] tipoCell;
        int ind, ite;
        int nGen;
        
        /**
        public void parseCell(int [] contCell, int iter)
        {
            for(int i = 0 ; i < 500 ; i++)
            tipoCell[iter - 1][i] = contCell[i];
        }**/
        
        public void nGen(int nGen)
        {
              tipoCell = new int[nGen][500];
              this.nGen = nGen;
        }
        
        public void parse(int [][] resultados)
        {
            for(int i = 0 ; i < nGen ; i++)
                for(int j = 0 ; j < 500 ; j++)
                    tipoCell[i][j] = resultados[i][j];
        }
                
        @Override
        public void paint(Graphics g) 
        {
            
            for(int Y = 0 ; Y < nGen ; Y++)
            {
                System.out.println("Generacion: " + Y);
                for(int X = 0 ; X < tipoCell[Y].length ; X++)
                {
                    System.out.print("[" + tipoCell[Y][X] + "] ");
                    g.setColor(colorCell[tipoCell[Y][X]]);
                    g.drawOval(X, Y % 500, 1, 1);
                    g.fillOval(X, Y % 500, 1, 1);
                }
                                    System.out.println("");
                    System.out.println("");
            }
            
            
            /**
            //System.out.println("pintando en X: " + ind + " Y: " + ite);
            for(int i = 0 ; i < tipoCell.length ; i++)
            {
                g.setColor(colorCell[tipoCell[i]]);
                g.drawOval(i, ite, 1, 1);
                g.drawOval(i, ite, 1, 1);
                
            }
            **/

               
        }
        
        /**
        @Override
        public void paint(Graphics g)
        {           
          switch(nombreAlgoritmo)
            {
                case "generator_26.1a": 
                    for(int i = 0 ; i < (int)selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.generator261a() * CanvaX);
                        int y = (int) (numAleatorio.generator261a() * CanvaY);
                                                
                        g.setColor(Color.red);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4);
                    }
                    ;break;
                case "generator_26.1b":
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.generator261b() * CanvaX);
                        int y = (int) (numAleatorio.generator261b() * CanvaY);
                        
                        g.setColor(Color.BLUE);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4);
                        
                    };break;
                    
                case "generator_26.2":
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.generator262() * CanvaX);
                        int y = (int) (numAleatorio.generator262() * CanvaY);
                        
                        g.setColor(Color.GREEN);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4);
                    };break;
                    
                case "generator_26.3":
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x  = (int) (numAleatorio.generator263() * CanvaX);
                        int y = (int) (numAleatorio.generator263() * CanvaY);
                                                
                       g.setColor(Color.black);
                       g.drawOval(x, y, 4, 4);
                       g.fillOval(x, y, 4, 4);
                       
                    };break;
                    
                case "generatorCombinated":
                {
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.generadorCombinado() * CanvaX);
                        int y = (int) (numAleatorio.generadorCombinado() * CanvaY);
                                                
                        g.setColor(Color.pink);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4);
                        
                    }
                };break;
                
                case "fishmanMoore_1":
                {
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.fishmanMoore_1() * CanvaX);
                        int y = (int) (numAleatorio.fishmanMoore_1() * CanvaY);
                        
                        g.setColor(Color.YELLOW);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4);
                        
                    }
                };break;
                
                case "fishmanMoore_2":
                {
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.fishmanMoore_2() * CanvaX);
                        int y = (int) (numAleatorio.fishmanMoore_2() * CanvaY);
                                                
                        g.setColor(Color.orange);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4); 
                    }
                };break;
                
                case "randuGenerator":
                {
                    for(int i = 0 ; i < (int) selectorNum.getValue(); i++)
                    {
                        int x = (int) (numAleatorio.Randu() * CanvaX);
                        int y = (int) (numAleatorio.Randu() * CanvaY);
                        
                        g.setColor(Color.CYAN);
                        g.drawOval(x, y, 4, 4);
                        g.fillOval(x, y, 4, 4);
                        
                    }
                };break;
                
            }
        }**/
        
        
    }
    
   
    public GUI_MC() {
        
        JPanel panelIzq = new JPanel();
        panelIzq.setBorder(bordeDibujo);
        panelIzq.add(dib, BorderLayout.NORTH);
        panelIzq.add(graf, BorderLayout.SOUTH);
        
        JPanel panelDer = new JPanel();
     
        JFrame frame = new JFrame("GUI_MC");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1300,700));
                
        creaMenu(frame);

        /** ************************** **/
        
        //Panel derecho
        
        panelDer.setBackground(Color.lightGray);
        panelDer.setBorder(bordeOpciones);
        panelDer.setPreferredSize(new Dimension(350,350));

        JButton algoritmoButton = new JButton("Aceptar");
        JLabel nGenText = new JLabel("Nº generaciones: ");
        JLabel seleccionado = new JLabel("Algoritmo seleccionado: ");
        JPanel panelSeleccion = new JPanel();
        
        panelSeleccion.setPreferredSize(new Dimension(330, 100));
        panelSeleccion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Selección de Algoritmo"));
                       
        panelSeleccion.add(seleccionado);
        panelSeleccion.add(algSeleccion);
        panelSeleccion.add(nGenText);
        panelSeleccion.add(selectorNum);
        
        panelDer.add(panelSeleccion);
        
        JPanel panelAC = new JPanel();
        panelAC.setPreferredSize(new Dimension(330, 160));
        panelAC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuración AC"));
        
        JLabel fronteraText = new JLabel("Tipo de frontera: ");
        String [] tipoFrontera = {"Frontera nula", "Frontera cilíndrica"};
        JComboBox typeFrontier = new JComboBox(tipoFrontera);
        
        JLabel numEstadoText = new JLabel("Nº estados por célula: ");
        JSpinner selectorNumEstado = new JSpinner(new SpinnerNumberModel(2,2, 100000000,1));
        
        JLabel vecindadText = new JLabel("Rango de vecindad: ");
        JSpinner selectorVecindad = new JSpinner(new SpinnerNumberModel(1,1, 100000000,1));
        
        JLabel fTransicionText = new JLabel("Función de selección: ");
        JSpinner chooseFTransicion = new JSpinner(new SpinnerNumberModel(90,1, 100000000, 1));
        
        panelAC.add(fronteraText);
        panelAC.add(typeFrontier);
        panelAC.add(numEstadoText);
        panelAC.add(selectorNumEstado);
        panelAC.add(vecindadText);
        panelAC.add(selectorVecindad);
        panelAC.add(fTransicionText);
        panelAC.add(chooseFTransicion);
        panelAC.add(algoritmoButton);
        
        panelDer.add(panelAC);
        
        JPanel entropiaTemporal = new JPanel();
        entropiaTemporal.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Entropia temporal"));
        entropiaTemporal.setPreferredSize(new Dimension(330, 100));
        
        JLabel cell = new JLabel("Celula: ");
        JSpinner selectorCell = new JSpinner(new SpinnerNumberModel(0,0, 499,1));
        JLabel entropia = new JLabel("Entropia: ");
        JLabel resEntropia = new JLabel(" ");
        JButton buttonEntropia = new JButton("Calcular");
        
        entropiaTemporal.add(cell);
        entropiaTemporal.add(selectorCell);
        entropiaTemporal.add(entropia);
        entropiaTemporal.add(resEntropia);
        entropiaTemporal.add(buttonEntropia);
        
        panelDer.add(entropiaTemporal);
        
        JPanel entropiaP = new JPanel();
        entropiaP.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Entropia y D. Hamming"));
        entropiaP.setPreferredSize(new Dimension(330,60));
        JButton entropiaButtonGraphic = new JButton("Entropia");
        entropiaP.add(entropiaButtonGraphic);
        JButton HammingButton = new JButton("Hamming");
        entropiaP.add(HammingButton);
        
        panelDer.add(entropiaP, BorderLayout.CENTER);
        
        JButton panelCifrado = new JButton("Cifrado");
        panelDer.add(panelCifrado);
        
        panelCifrado.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new cifrado(ac);
            }
        }
        );
        
        HammingButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame hammingGraphic = new JFrame("Hamming");
                JPanel container = new JPanel();
                container.setPreferredSize(new Dimension(600,100));
                container.add(H);
                hammingGraphic.setPreferredSize(new Dimension(700,200));
                hammingGraphic.add(container);
                hammingGraphic.setBackground(Color.lightGray);
                
                new Thread()
                {
                    public void run()
                    {
                        H.repaint();
                    }
                }.start();
                
                hammingGraphic.pack();
                hammingGraphic.setVisible(true);
            }
        }
        );
        
        entropiaButtonGraphic.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFrame entropiaGraphic = new JFrame("Entropia");
                JPanel container = new JPanel();
                container.setPreferredSize(new Dimension(600, 100));
                container.add(dibujaEntropia);
                entropiaGraphic.setPreferredSize(new Dimension(700, 200));
                entropiaGraphic.add(container);
                entropiaGraphic.setBackground(Color.lightGray);
                
                new Thread()
                {
                    public void run()
                    {
                        dibujaEntropia.repaint();
                    }
                }.start();
                
                entropiaGraphic.pack();
                entropiaGraphic.setVisible(true);
            }
        }
        );
        
        
        
        buttonEntropia.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                double resultado = ac.getEntropia((int) selectorCell.getValue());
                
                //System.out.println(resultado);
                
                resEntropia.setText(String.valueOf(resultado));
                
            }
        }
        );
  
        algoritmoButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fTransicion = (int) chooseFTransicion.getValue();
                fronteraSeleccionada = (int)typeFrontier.getSelectedIndex();
                automata1D(selectorVecindad, selectorNumEstado);
            }
        });
        
        //Añadimos el menu creado a la barra

       frame.getContentPane().add(panelDer, BorderLayout.EAST);
       frame.getContentPane().add(panelIzq, BorderLayout.CENTER);
        
        //Mostrar ventana
        frame.pack();
        frame.setVisible(true);
    }
    
    public void automata1D(JSpinner selectorVecindad, JSpinner selectorNumEstado)
    {
        ac = new ca1DSimulator(fronteraSeleccionada, nombreAlgoritmo, numAleatorio, (int)selectorNumEstado.getValue(), fTransicion , (int)selectorVecindad.getValue(), dib, graf, dibujaEntropia, 0, 0);
        colorCell = new Color[(int)selectorNumEstado.getValue()];
        Random r = new Random(5);
        
        colorCell[0] = Color.lightGray;
        
        for(int i = 1 ; i < colorCell.length ; i++)
            colorCell[i] = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
                
        ac.caComputation((int)selectorNum.getValue());
    }
        
    public void creaMenu(JFrame frame)
    {
        JMenuBar menuBar;
        JMenu item1Alg, item2, itemHelp, subItem1, subItem2, subItem3;
        
        // Item Algoritmos
        JMenuItem generator261A, generator261B, generator262, generator263, generatorCombinated, fishmanMoore_1, fishmanMoore_2, randuGenerator;
        
        //Otros items
        JMenuItem ayuda, acerca;
        
        
         //Creación barra
        menuBar = new JMenuBar();
        
        //Creación de un menu
        item1Alg = new JMenu("Algoritmos");
        item2 = new JMenu("Item 2");
        itemHelp= new JMenu("Más...");
        
        //Creacion de submenu
        subItem2 = new JMenu("SubItem2 - nivel 1");
        subItem3 = new JMenu("SubItem3 - nivel 1");

        /*** ITEMS PARA LA SELECCION DE ALGORITMOS ***/
        generator261A = new JMenuItem("generator_26.1a");
        generator261B = new JMenuItem("generator_26.1b");
        generator262 = new JMenuItem("generator_26.2");
        generator263 = new JMenuItem("generator_26.3");
        generatorCombinated = new JMenuItem("generatorCombinated");
        fishmanMoore_1 = new JMenuItem("fishmanMoore_1");
        fishmanMoore_2 = new JMenuItem("fishmanMoore_2");
        randuGenerator = new JMenuItem("randuGenerator");
        
        /*** ITEMS PARA OTRAS OPCIONES ***/
        ayuda = new JMenuItem("Ayuda");
        acerca = new JMenuItem("Acerca de...");
        
        //Añadimos el evento
        generator261A.addActionListener(this);
        generator261B.addActionListener(this);
        generator262.addActionListener(this);
        generator263.addActionListener(this);
        generatorCombinated.addActionListener(this);
        fishmanMoore_1.addActionListener(this); 
        fishmanMoore_2.addActionListener(this);
        randuGenerator.addActionListener(this);
        
        ayuda.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JFrame f = new JFrame(e.getActionCommand());
                JPanel p = new JPanel();
                f.setPreferredSize(new Dimension(200,200));
                
                p.add(new JLabel(e.getActionCommand()));
                
                f.getContentPane().add(p);
                
                f.pack();
                f.setVisible(true);
                
            }
        }
        );
        
        acerca.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
            {
                JFrame f = new JFrame(e.getActionCommand());
                JPanel p = new JPanel(new GridLayout(3,1,5,5));
                
                p.add(new JLabel("GUI para la asignatura Modelos de Computación\n"));
                p.add(new JLabel("Curso 2018/19\n"));
                p.add(new JLabel("\nJuan Pastor Rodríguez Moreno"));
                
                f.getContentPane().add(p);
                
                f.setPreferredSize(new Dimension(300,300));
                f.pack();
                f.setVisible(true);
                
            } 
        }
        );
               
        
        //Añadimos los items al menu Algoritmo
        item1Alg.add(generator261A);
        item1Alg.add(generator261B);
        item1Alg.add(generator262);
        item1Alg.add(generator263);
        item1Alg.add(generatorCombinated);
        item1Alg.add(fishmanMoore_1);
        item1Alg.add(fishmanMoore_2);
        item1Alg.add(randuGenerator);
        
        /*** Añadimos items al menu Help **/
        itemHelp.add(ayuda);
        itemHelp.add(acerca);
        
        menuBar.add(item1Alg);  //Añadimos el menu a la barra de navegacion
        menuBar.add(itemHelp);
           
        frame.setJMenuBar(menuBar); //Añadimos la barra de navegacion a nuestro marco
    }
    
    public void actionPerformed(ActionEvent e)
    {       
        JFrame f = new JFrame(e.getActionCommand());
        f.setPreferredSize(new Dimension(200,200));
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        
        panel.add(label);
        f.getContentPane().add(panel, BorderLayout.CENTER);
        
        
        switch(e.getActionCommand())
        {
            case "generator_26.1a": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.red); break;
            case "generator_26.1b": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.BLUE); break;
            case "generator_26.2": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.GREEN);break;
            case "generator_26.3": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.black);break;
            case "generatorCombinated": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.pink); break;
            case "fishmanMoore_1": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.yellow);break;
            case "fishmanMoore_2": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.orange);break;
            case "randuGenerator": nombreAlgoritmo = e.getActionCommand(); algSeleccion.setText(e.getActionCommand()); algSeleccion.setForeground(Color.cyan);break;
            default: label.setText(e.getActionCommand()); f.pack(); f.setVisible(true);
        }
        
 
    }
                                
}


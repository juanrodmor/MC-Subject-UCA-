
    import java.awt.BorderLayout;
    import java.awt.Canvas;
    import java.awt.Color;
    import java.awt.Dimension;
    import java.awt.Graphics;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.util.ArrayList;
    import java.util.Random;
    import java.util.concurrent.CyclicBarrier;
    import java.util.concurrent.LinkedBlockingQueue;
    import java.util.concurrent.ThreadPoolExecutor;
    import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
    import javax.swing.JButton;
    import javax.swing.JFrame;
    import javax.swing.JLabel;
    import javax.swing.JPanel;
    import javax.swing.JSpinner;
import javax.swing.JTextField;
    import javax.swing.SpinnerNumberModel;
    import javax.swing.Timer;


    /**
     *
     * @author Juan Pastor Rodríguez Moreno
     */
    public class automata2D {

       static canvas panelDib;
       static canvasGrafica panelGraf;

       static ThreadPoolExecutor ej = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 
                                                             Runtime.getRuntime().availableProcessors(),
                                                             1L, TimeUnit.HOURS, new LinkedBlockingQueue());
       static CyclicBarrier bar = new CyclicBarrier(Runtime.getRuntime().availableProcessors());

       static int [][] celulasA;
       static int [][] celulasB;

       static int [][] datosGrafica;

       static int [] cont = {0 ,0};
       ArrayList aL = new ArrayList();
       static Object obj = new Object();

       static int d;
       static int gen = 0;
       static int contGen = 0;
       static Timer t;
       int genAnt = 0;
       static boolean pintaGraf = false;
       static boolean ready = false;
       
       public class tarea implements Runnable
       {
           int inf, sup;

           tarea(int i, int s) 
           {
               inf = i;
               sup = s;
           }

           @Override
           public void run()
           {
               for(int i = inf ; i < sup ; i++)
               {
                   for(int j = 0 ; j < d ; j++)
                   {
                       regla(i, j); 
                   }
               }

                try { bar.await(); } catch(Exception e) {};

                for(int i = inf ; i < sup ; i++)
                    for(int j = 0 ; j < d ; j++)
                        celulasA[i][j] = celulasB[i][j];
           }
       }

       public void regla(int i, int j)
       {
           int vivas = 0;
           for(int k = i - 1 ; k <= i+1 ; k++)
           {
               for(int q = j-1; q <= j+1 ; q++)
               {
                   if(k > 0 && q > 0 && k < d && q < d && celulasA[k][q] == 1 && k != i && q != j)
                       vivas++;                  
               }
           }

           if(vivas >= 2)
           {
               if((vivas == 2 || vivas == 3 ))
               {
                   celulasB[i][j] = celulasA[i][j];
                   synchronized(obj){cont[celulasB[i][j]]++;}
               }
               else
               {
                   if(vivas == 3 && celulasA[i][j] == 0)
                   {
                       celulasB[i][j] = 1;
                       synchronized(obj){cont[celulasB[i][j]]++;}
                   }
                   else 
                   {
                       if(vivas > 3) celulasB[i][j] = 0;
                       synchronized(obj){cont[celulasB[i][j]]++;}
                   }       
               }
           }
           else
           {
               celulasB[i][j] = 0;
               synchronized(obj){cont[celulasB[i][j]]++;}
           }

       }

       public class canvasGrafica extends Canvas
       {
           canvasGrafica()
           {
               this.setPreferredSize(new Dimension(700, 200));
               this.setBackground(Color.white);
           }

           @Override
           public void paint(Graphics g)
           {

                for(int i = 0 ; i < gen - 1 && pintaGraf; i++)
                {
                    if(datosGrafica[i] != null)
                    {
                        g.setColor(Color.black);

                       // System.out.println("ROJO. X0: " + i + " datos: " + y1 + " X1: " + i+1 + " Y1: " + y2);
                        g.drawLine(i*700/gen, ((datosGrafica[i][0]*200)/(d*d)) * -1 + 200, (i+1)*700/gen, ((datosGrafica[i+1][0]*200)/(d*d))*-1+200);

                        g.setColor(Color.blue);
                        g.drawLine(i*700/gen, ((datosGrafica[i][1]*200)/(d*d))*-1+200, (i+1)*700/gen, ((datosGrafica[i+1][1]*200)/(d*d))*-1+200);
                    }
                }


           }
       }


        public class canvas extends Canvas
        {
            canvas()
            {
                this.setPreferredSize(new Dimension(200, 200));
                //this.setMinimumSize(new Dimension(100, 100));
                //this.setBackground(new Color(255, 255, 230)); 
                this.setBackground(Color.black);

            }


            @Override
            public void paint(Graphics g) 
            {
                g.setColor(Color.blue);
                for(int i = 0 ; i < d ; i++)
                {
                    for(int j = 0 ; j < d ; j++)
                    {
                        if(celulasA[i][j] == 1)
                        {
                            g.drawOval(i, j, 1, 1);
                        }
                    }
                }
            }

        }

        automata2D()
        {
            JFrame f = new JFrame("Automata 2D");
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setPreferredSize(new Dimension(400,100));

            JPanel p = new JPanel();

            JLabel textD = new JLabel("Dimension (dxd): ");
            JSpinner dim = new JSpinner(new SpinnerNumberModel(200,200, 1500,1));

            JLabel textG = new JLabel("Generaciones: ");
            JSpinner nGen = new JSpinner(new SpinnerNumberModel(1,1, 10000, 1));    
            

            JButton b = new JButton("Calcular Life!");


            p.add(textD);
            p.add(dim);
            p.add(textG);
            p.add(nGen);
            p.add(b);

            b.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    d = (int) dim.getValue();
                    celulasA = new int[d][d]; celulasB = new int[d][d];
                    contGen = 0;
                    gen = (int) nGen.getValue();
                    datosGrafica = new int[gen][2]; ready = true;

                    iniciaCelulas();

                    panelDib = new canvas();
                    panelGraf = new canvasGrafica();

                    JFrame fGrafico = new JFrame("Grafico Life!");
                    fGrafico.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fGrafico.setPreferredSize(new Dimension(900, d + 400));
                    JPanel p = new JPanel();
                    JTextField cuadroText = new JTextField();
                    cuadroText.setPreferredSize(new Dimension(400, 50));
                    cuadroText.setBorder(BorderFactory.createTitledBorder("Console"));
                    cuadroText.setBackground(Color.black);
                    cuadroText.setForeground(Color.white);

                    p.add(panelDib, BorderLayout.NORTH);
                    JPanel q = new JPanel();
                    q.setPreferredSize(new Dimension(900, 300));
                    q.add(cuadroText, BorderLayout.NORTH);
                    q.add(panelGraf, BorderLayout.SOUTH);
                    
                    fGrafico.getContentPane().add(p, BorderLayout.NORTH);
                    fGrafico.getContentPane().add(q, BorderLayout.SOUTH);
                    
                    fGrafico.pack();
                    fGrafico.setVisible(true);

                    t = new Timer(100, new ActionListener(){
                      public void actionPerformed(ActionEvent e)
                      {
                        caComputacion(cuadroText);
                        contGen++;
                      }
                    });

                    new Thread() { public void run() {t.start();}}.start();
                    
                }
            });

            f.getContentPane().add(p);
            f.pack();
            f.setVisible(true);
        }
       
        public void iniciaCelulas()
        {
            Random r = new Random();
            randomGenerator g = new randomGenerator();

            for(int i = 0 ; i < d ; i++)
                for(int j = 0 ; j < d ; j++)
                    celulasA[i][j] = (int) (g.generadorCombinado()*2);
                    //celulasA[i][j] = r.nextInt(2);

        }
        
        public void caComputacion(JTextField cuadroText)
        {      
            int inf = 0; 
            int sup = (d / Runtime.getRuntime().availableProcessors());
            
            
            for(int i = 0; i < Runtime.getRuntime().availableProcessors(); i++)
            {
                ej.execute(new tarea(inf, sup));
                inf = sup;
                sup += (d/Runtime.getRuntime().availableProcessors());
            }

            try { ej.awaitTermination(1L, TimeUnit.SECONDS); }catch(Exception ex) {}
                           
            cuadroText.setText("Generacion: " + contGen + ". Vivas: " + cont[1] + ", muertas: " + cont[0]);

            if(contGen < gen) { 
                datosGrafica[contGen][0] = cont[0]; datosGrafica[contGen][1] = cont[1]; }

            cont[0] = 0; cont[1] = 0;
            
            pintaGraf = true;
            panelGraf.repaint();
            panelDib.repaint();
                       
            if(contGen >= gen){ t.stop(); pintaGraf = true; panelGraf.repaint();}
            //else contGen++;

        }

    }


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class menuPrincipal {
    
    
    menuPrincipal()
    {
        JFrame frame = new JFrame("GUI_MC");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(200,400));
        JPanel p = new JPanel();        
        
        creaMenu(frame);  
        
        JButton automata1D = new JButton("Automata 1D");
        p.add(automata1D);
        
        JButton automata2D = new JButton("Automata 2D");
        p.add(automata2D);
        
        JButton automataBelousov = new JButton("AC 2D - Belousov");
        p.add(automataBelousov);
        
        JButton automataTumor = new JButton("AC 2D - Crecimiento tumoral");
        p.add(automataTumor);
        
        JButton mandelbrot = new JButton("Conjunto de Mandelbrot");
        p.add(mandelbrot);
        
        JButton urm = new JButton("URM Interpreter");
        p.add(urm);
        
        urm.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new urmInterpreter();
            }
        }
        );
        
        mandelbrot.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFrame f = new JFrame("Conjunto de Mandelbrot");
                JPanel i = new JPanel();
                JPanel top = new JPanel(); top.setPreferredSize(new Dimension(200, 50));

                JLabel lb = new JLabel("SpeedUP: ");
                JLabel speedUPValue = new JLabel(" ");
                
                i.add(new Mandelbrot(speedUPValue));
                top.add(lb);
                top.add(speedUPValue);
                
                f.getContentPane().add(top, BorderLayout.NORTH);
                f.getContentPane().add(i, BorderLayout.SOUTH);
                
                
                f.pack();
                f.setVisible(true);
                //new Mandelbrot().setVisible(true);
            }
        }
        );
        
        automataTumor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new usaTumoralGrowth();
            }
        }
        );
        
        automataBelousov.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new usaBelousov();
            }
        }
        );
        
        automata1D.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new GUI_MC();
            }     
        });
        
        automata2D.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new automata2D();
            }
        }
        );
        
        frame.getContentPane().add(p);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void creaMenu(JFrame frame)
    {
        
        JMenuBar menuBar;
        JMenu itemHelp;
        

        //Otros items
        JMenuItem ayuda, acerca;
        
        
         //Creación barra
        menuBar = new JMenuBar();
        
        //Creación de un menu
        itemHelp= new JMenu("Más...");

        /*** ITEMS PARA OTRAS OPCIONES ***/
        ayuda = new JMenuItem("Ayuda");
        acerca = new JMenuItem("Acerca de...");
                
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
              
        
        /*** Añadimos items al menu Help **/
        itemHelp.add(ayuda);
        itemHelp.add(acerca);
        
        menuBar.add(itemHelp);
        
        
        
           
        frame.setJMenuBar(menuBar); //Añadimos la barra de navegacion a nuestro marco
    }
    
    public static void main(String [] args)
    {
        new menuPrincipal();
    }
    
}

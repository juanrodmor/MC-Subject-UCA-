
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 *
 * @author Juan
 */
public class usaBelousov {
    
    usaBelousov()
    {
        JFrame f = new JFrame("Simulacion Belousov");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(400, 400));
        
        JPanel config = new JPanel(new GridLayout(6, 2, 15, 20)); config.setPreferredSize(new Dimension(300, 300)); 
        
        JLabel gen = new JLabel("Generaciones: ");
        JLabel dim = new JLabel("Dimension: ");
        JLabel alfa = new JLabel("Alfa: ");
        JLabel beta = new JLabel("Beta: ");
        JLabel gamma = new JLabel("Gamma: "); 
        
        JTextField introGen = new JTextField(); introGen.setPreferredSize(new Dimension(40,25));
        JTextField introDim = new JTextField(); introDim.setPreferredSize(new Dimension(40,25));
        JTextField introAlfa = new JTextField(); introAlfa.setPreferredSize(new Dimension(40,25));
        JTextField introBeta = new JTextField(); introBeta.setPreferredSize(new Dimension(40,25));
        JTextField introGamma = new JTextField(); introGamma.setPreferredSize(new Dimension(40,25));
        
        String [] tipo = {"Secuencial", "Paralelo"};
        JComboBox desplegable = new JComboBox(tipo);
        
        JButton boton = new JButton("Iniciar");
        
        config.add(gen); config.add(introGen);
        config.add(dim); config.add(introDim);
        config.add(alfa); config.add(introAlfa);
        config.add(beta); config.add(introBeta);
        config.add(gamma); config.add(introGamma);
        config.add(desplegable); config.add(boton);
                

        boton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                switch((String)desplegable.getSelectedItem())
                {
                    case "Secuencial":
                    {
                        belZab b = new belZab(Integer.parseInt(introDim.getText()), Integer.parseInt(introGen.getText()), Float.parseFloat(introAlfa.getText()),
                                            Float.parseFloat(introBeta.getText()), Float.parseFloat(introGamma.getText()));
                        JFrame frame = new JFrame("Secuencial");
                
                        frame.add(b.dibuja);
                        b.dibuja.repaint();
                
                        frame.pack();
                        frame.setVisible(true);                       
                    };break;
                    
                    case "Paralelo":
                    {
                        parallelBelZab b = new parallelBelZab(Integer.parseInt(introDim.getText()), Integer.parseInt(introGen.getText()), Float.parseFloat(introAlfa.getText()),
                                            Float.parseFloat(introBeta.getText()), Float.parseFloat(introGamma.getText()));
                        JFrame frame = new JFrame("Paralelo");
                
                        frame.add(b.dibuja);
                        b.dibuja.repaint();
                
                        frame.pack();
                        frame.setVisible(true);                          
                        
                    };break;
                }
                

                


            }
        }
        );
               
       // f.getContentPane().add(grafico, BorderLayout.WEST);
        f.add(config);
        f.pack();
        f.setVisible(true);
    }
    
    
}

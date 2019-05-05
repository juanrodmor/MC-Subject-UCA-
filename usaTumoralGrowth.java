
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
 * @author Juan Pastor Rodríguez Moreno
 */
public class usaTumoralGrowth {
    
    usaTumoralGrowth()
    {
        JFrame f = new JFrame("Simulador de crecimiento tumoral");
        f.setPreferredSize(new Dimension(400, 400));
        
        JPanel config = new JPanel(new GridLayout(8, 2, 15, 20)); config.setPreferredSize(new Dimension(300, 300));
        
        JLabel textPs = new JLabel("Prob. supervivencia: ");
        JLabel textPm = new JLabel("Prob. migrar: ");
        JLabel textPp = new JLabel("Prob. proliferar: ");
        JLabel textNP = new JLabel("NP: " );       
        JLabel textn = new JLabel("Nº celulas: ");
        JLabel textGen = new JLabel("Generaciones: ");
                
        String [] tipo = {"Central", "Aleatorio"};
        JComboBox desplegable = new JComboBox(tipo);
        
        String [] tipoAlgoritmo = {"Secuencial", "Paralelo"};
        JComboBox desplegable2 = new JComboBox(tipoAlgoritmo);
        
        JTextField Ps = new JTextField(); Ps.setPreferredSize(new Dimension(20,25));
        JTextField Pm = new JTextField(); Pm.setPreferredSize(new Dimension(20,25));
        JTextField Pp = new JTextField(); Pp.setPreferredSize(new Dimension(20,25));
        JTextField NP = new JTextField(); NP.setPreferredSize(new Dimension(20,25));
        JTextField n = new JTextField(); n.setPreferredSize(new Dimension(20,25));
        JTextField gen = new JTextField(); gen.setPreferredSize(new Dimension(20,25));
        
        JButton button = new JButton("Iniciar");
        
        config.add(textPs); config.add(Ps);
        config.add(textPm); config.add(Pm);
        config.add(textPp); config.add(Pp);
        config.add(textNP); config.add(NP);
        config.add(textn); config.add(n);
        config.add(textGen); config.add(gen);
        config.add(desplegable);// config.add(desplegable2);
        config.add(button);
                
        
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                
                switch((String) desplegable2.getSelectedItem())
                {
                    case "Secuencial":
                    {
                        tumoralGrowth t = new tumoralGrowth(Float.parseFloat(Ps.getText()), Float.parseFloat(Pm.getText()), 
                        Float.parseFloat(Pp.getText()), Integer.parseInt(NP.getText()), Integer.parseInt(n.getText()), Integer.parseInt(gen.getText()),
                        (String) desplegable.getSelectedItem());
                        
                        JFrame frame = new JFrame("Simulacion tumoral");
                        
                        frame.add(t.lienzo);
                        t.lienzo.repaint();

                        frame.pack();
                        frame.setVisible(true);   
                        
                    }; break;
                    case "Paralelo":
                    {
                        parallelTumoralGrowth p = new parallelTumoralGrowth(Float.parseFloat(Ps.getText()), Float.parseFloat(Pm.getText()), 
                        Float.parseFloat(Pp.getText()), Integer.parseInt(NP.getText()), Integer.parseInt(n.getText()), Integer.parseInt(gen.getText()),
                        (String) desplegable.getSelectedItem());         
                        
                        JFrame frame = new JFrame("Simulacion tumoral");
                
                        frame.add(p.lienzo);
                        p.lienzo.repaint();

                        frame.pack();
                        frame.setVisible(true);  
                        
                    }; break;
                }
                
             
                
 
            }
        }
        );
        
        
        f.getContentPane().add(config);
        f.pack();
        f.setVisible(true);
    }
    
    
}

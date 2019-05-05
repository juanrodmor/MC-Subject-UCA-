
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class cifrado {
    
    final private ca1DSimulator automata;
    int k = 2;
    int r = 1;
    int numReglas;
    private static int [] vecGeneracion;
    
    cifrado(ca1DSimulator ac)
    {
        
        automata = new ca1DSimulator(k);
        vecGeneracion = new int[4000];
        
        numReglas = (int) Math.pow(k, Math.pow(k, (2*r+1)*(k-1)));
        
        JFrame f = new JFrame("Ventana de cifrado");
        f.setPreferredSize(new Dimension(600,500));
        
        JFileChooser fileChooser = new JFileChooser();
        
        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem abrirFich = new JMenuItem("Abrir...");
        
        archivo.add(abrirFich);
        barra.add(archivo);
   
        
        JPanel panelIzq = new JPanel();
        panelIzq.setPreferredSize(new Dimension(198,400));
        
        JPanel panelDer = new JPanel();
        panelDer.setPreferredSize(new Dimension(202,400));
        
        JTextArea textoIzq = new JTextArea();
        textoIzq.setPreferredSize(new Dimension(190, 400))
                ;
        JTextArea textoDer = new JTextArea();
        textoDer.setPreferredSize(new Dimension(198, 400));
        
        panelIzq.add(textoIzq, BorderLayout.CENTER);
        panelDer.add(textoDer, BorderLayout.CENTER);
        
        f.getContentPane().add(panelIzq, BorderLayout.WEST);
        f.getContentPane().add(panelDer, BorderLayout.EAST);
        f.setJMenuBar(barra);
        
        FileNameExtensionFilter ff = new FileNameExtensionFilter("Archivos txt ", "txt");
        fileChooser.addChoosableFileFilter(ff);
        fileChooser.setFileFilter(ff);
        
        abrirFich.addActionListener(new ActionListener()
        {
             @Override
             public void actionPerformed(ActionEvent e)
             {
                 fileChooser.showOpenDialog(null);              
                 File file = fileChooser.getSelectedFile();
                 String aux="";
                 String texto="";
                 
                 try{
                 if(file!=null)
                    {     
                        FileReader archivos=new FileReader(file);
                        BufferedReader lee=new BufferedReader(archivos);
                        while((aux=lee.readLine())!=null)
                        {
                           texto+= aux+ "\n";
                        }
                           lee.close();
                    }    
                 }catch(Exception ex) {};
                 textoIzq.setText(texto);
             }
        }
             
        );        
        
        
        //int choosenRule = filtroRule();
        int choosenRule = 255;


        automata.changeRule(choosenRule);
        
        for(int i = 0 ; i < vecGeneracion.length ; i++)
            System.out.print(vecGeneracion[i]+", ");
        
        
        JButton button = new JButton("Cifrar");
        
        JLabel text = new JLabel("Texto descifrado: ");
        
        JPanel panelTextDesc = new JPanel();
        panelTextDesc.setPreferredSize(new Dimension(190,200));
        JTextArea textDesc = new JTextArea();
        textDesc.setBackground(Color.white);
        JTextArea textClave = new JTextArea();
        textClave.setBackground(Color.white);
        textClave.setPreferredSize(new Dimension(180,100));
        
        panelTextDesc.add(new JLabel("Clave: "));
        panelTextDesc.add(textClave);
        panelTextDesc.add(button);
        panelTextDesc.add(text);
        panelTextDesc.add(textDesc);
        
        f.getContentPane().add(panelTextDesc, BorderLayout.CENTER);
        
        //textDesc.setPreferredSize(new Dimension(190, 200));
        //f.getContentPane().add(textDesc, BorderLayout.CENTER);
        
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                automata.setEstadoInicial(textClave.getText());
                vecGeneracion = automata.generacionSinPintado();
                String textCifrado = cifrar(textoIzq.getText(), vecGeneracion);   
                textoDer.setText(String.valueOf(textCifrado));
                textDesc.setText(descifrado(textCifrado, vecGeneracion));
            }
        }
        );
        

        f.pack();
        f.setVisible(true);
    }
    
    String cifrar(String s, int [] v)
    {
        
        char [] c = new char[s.length()];
        
        for(int i = 0 ; i < s.length() ; i++)
        {
            if(s.charAt(i) == '\n')
            {
                c[i] = '\n';
            }
            else
            {
                c[i] = (char) (((int) s.charAt(i) ^ v[i%v.length])%127);
            }
        }
       
        return String.copyValueOf(c);
        
    }
    
    String descifrado(String s, int [] v)
    {
        char [] des = new char[s.length()];
        
        for(int i = 0 ; i < s.length() ; i++)
        {
            if(s.charAt(i) == '\n')
            {
                des[i] = '\n';
            }
            else
            {
                des[i] = (char) (((int) s.charAt(i) ^ v[i%v.length])%127);
            }
        }
        
        return String.copyValueOf(des);
    }
    
    private int filtroRule()
    {
        double entropiaTempMax = 0;
        double entropiaEspMax = 0;
        double hammingMax = 0;
        int regla = 0;
        
        System.out.println(numReglas);
        
        for(int i = 0 ; i < numReglas ; i++)
        {
            automata.changeRule(i);
            automata.generacionSinPintado();
            double EE = automata.entropiaEspacial();
            //System.out.println("Entropia espacial OK");
            double ET = automata.getEntropia((1000-1)/2);
            double H = automata.dHamming(4000);
            
            if(EE > entropiaEspMax && ET > entropiaTempMax && H > hammingMax)
            {
                entropiaEspMax = EE;
                entropiaTempMax = ET;
                hammingMax = H;
                regla = i;
            }
            
        }
        
        return regla;
    }
        
}

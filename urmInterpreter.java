
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;


/**
 *
 * @author Juan Pastor Rodríguez Moreno
 */
public class urmInterpreter {
    ArrayList<Integer> urmMemory;   //Podemos hacer la configuracion <K, urmMemory>
    ArrayList<String> program;
    int K;
    int memSize;
    
    static JTextArea configurations;
    
    urmInterpreter()
    {
        GUI();
        memSize = 15;
        urmMemory = new ArrayList(); 
        for(int i = 0 ; i < memSize + 1; i++)
            urmMemory.add(i, 0);
                
        program = new ArrayList();
        K = 1;
    }
    
    private void Z(int N)
    {
        //int N = (n - 48);
        
        urmMemory.set(N, 0);
        
        K++;
    }
    
    private void S(int N)
    {
        //int N = (n - 48);
        
        urmMemory.set(N, urmMemory.get(N) + 1);
        
        K++;
    }
    
    private void T(int M, int N)
    {
        //int M = (m - 48), N = (n-48);
        
        urmMemory.set(N, urmMemory.get(M));
        
        K++;
    }
            
    private void J(int M, int N, int I)
    {
        //int M = (m - 48), N = (n - 48), I = (i - 48);
        
        System.out.println("M: " + M + " N: " + N + " I: " + I);
        
        if(urmMemory.get(M) == urmMemory.get(N))
            K = I;
        else
            K++;
        
    } 
    
    private void readProgram(File f)
    {
        try {
            Scanner input = new Scanner(f);
            program.add(" ");   //Primera posicion vacia. Empezamos a partir de 1 = primera instruccion del URM - Programa.
            
            while(input.hasNextLine())
            {
                //System.out.println(input.nextLine());
                program.add(input.nextLine());
            }
            
        }catch(Exception e){}
               
    }
    
    void computaURM(File f)
    {
        readProgram(f);
        while(K != 0 && K < program.size())
        {
            String s = program.get(K);
            char op = s.charAt(0);
            //System.out.println("op: " + s.charAt(0));
            System.out.println("(" + K + "<R1: " + urmMemory.get(1) + " R2: " + urmMemory.get(2) + " R3:" + urmMemory.get(3));
            
            switch(op)
                {
                    case 'Z': // Z() 
                    {   
                        String cadena = String.valueOf(s.charAt(2)); 
                        for(int i = 3; s.charAt(i) != ')'; i++)
                        {
                           cadena.concat(String.valueOf(s.charAt(i)));
                        }
                        
                        Z(Integer.valueOf(cadena));
                    } break;
                    case 'S': // S()
                    {
                        String cadena = String.valueOf(s.charAt(2)); 
                        for(int i = 3; s.charAt(i) != ')'; i++)
                        {
                            //System.out.println("valor" + s.charAt(i));
                            cadena.concat(String.valueOf(s.charAt(i)));
                        }
                        
                        S(Integer.valueOf(cadena));                        
                    } break;
                    case 'T': 
                    {
                        int i = 3;
                        String cadena = String.valueOf(s.charAt(2)); 
                        for(i = 3; s.charAt(i) != ','; i++)
                        {
                            //System.out.println("valor" + s.charAt(i));
                            cadena.concat(String.valueOf(s.charAt(i)));
                        }

                        int param1 = Integer.valueOf(cadena);
                        
                       String cadena2 = String.valueOf(s.charAt(i+1)); 
                        for(i = i+2; s.charAt(i) != ')'; i++)
                        {
                            //System.out.println("valor" + s.charAt(i));
                            cadena2.concat(String.valueOf(s.charAt(i)));
                        }
                        
                        int param2 = Integer.valueOf(cadena2);
                        
                        T(param1, param2);
                    } break;
                    case 'J': 
                    {
                        int i = 3;
                        String cadena = String.valueOf(s.charAt(2)); 
                        for(i = 3; s.charAt(i) != ','; i++)
                        {
                            //System.out.println("valor" + s.charAt(i));
                            cadena.concat(String.valueOf(s.charAt(i)));
                        }

                        int param1 = Integer.valueOf(cadena);
                        
                        String cadena2 = String.valueOf(s.charAt(i+1)); 
                        for(i = i+2; s.charAt(i) != ','; i++)
                        {
                            //System.out.println("valor" + s.charAt(i));
                            cadena2.concat(String.valueOf(s.charAt(i)));
                        }
                                                
                        int param2 = Integer.valueOf(cadena2);  
                        
                        String cadena3 = String.valueOf(s.charAt(i+1)); 
                        for(i = i+2; s.charAt(i) != ')'; i++)
                        {
                            //System.out.println("valor" + s.charAt(i));
                            cadena3 += (String.valueOf(s.charAt(i)));
                            System.out.println("cadena3: " + cadena3);
                        }
                        
                        int param3 = Integer.valueOf(cadena3);
                        
                        J(param1, param2, param3);
                    } break;   
                }     
            
            
            
        }
        
        K = 1;
        configurations.setText("Resultado: " + urmMemory.get(1));
        

    }
    
    // ESTA ES LA FUNCION A REHACER PARA CONTAR NUMERO DE VARIOS DIGITOS
    private void refreshMemory(String s)
    {
        int cont = 1;
        int i = 0;
        while(i < s.length())
        {
            String num = "";
            //char [] num = new char[1000];
            System.out.println("numero inicial: " + num);
            while(i < s.length() && s.charAt(i) != ' ')
            {
                //System.out.println("valor: " + s.charAt(i));
                num += s.charAt(i);
                System.out.println("valor: " + num);
                i++;
            }
            
            urmMemory.set(cont, Integer.parseInt(num));
            cont++;
            
            while(i < s.length() && s.charAt(i) == ' ') {i++;}
            
        }
        
    }
    
    //Borrar memoria
    private void reset()
    {
        K = 1;
        
        program.clear();
        
        for(int i = 1 ; i <= memSize ; i++)
            urmMemory.set(i, 0);
        
        
        configurations.setText("Memoria limpiada con exito.");
        
    }
    
    //Muestra el GUI
    private void GUI()
    {
        JFrame f = new JFrame();
        int WIDTH = 800, HEIGHT = 600;
        f.setPreferredSize(new Dimension(800,600));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem abrirFich = new JMenuItem("Abrir...");
        
        archivo.add(abrirFich);
        barra.add(archivo);
        f.setJMenuBar(barra);
        
        
        //Panel izquierdo (norte = URM-programa; sur = configuraciones)
        JPanel pIzquierdo = new JPanel();
        pIzquierdo.setPreferredSize(new Dimension(WIDTH*3/4 - 10, HEIGHT - 10));
        
        JTextArea program = new JTextArea(); program.setPreferredSize(new Dimension(WIDTH*3/4-20, HEIGHT/2 - 20));
        configurations = new JTextArea(); configurations.setPreferredSize(new Dimension(WIDTH*3/4-20, HEIGHT/2 - 20));
              
        //pIzquierdo.add(program, BorderLayout.NORTH);
        pIzquierdo.add(configurations, BorderLayout.SOUTH);
        
        f.getContentPane().add(pIzquierdo, BorderLayout.WEST);
        
        
        //Panel derecho (opciones)
        JPanel pDerecho = new JPanel();
        pDerecho.setPreferredSize(new Dimension(WIDTH*1/4 - 10, HEIGHT - 10));
        
        
        
        JButton calcular = new JButton("Calcular");
        JButton limpiar = new JButton("Limpiar Memoria");
        
        JLabel textVariables = new JLabel("Parametros:");
        JTextField parametros = new JTextField();parametros.setPreferredSize(new Dimension(70, 20));
        JLabel funcionText = new JLabel("Funcion: ");
        String [] funciones = {"Suma", "Incremento", "Resta restringida", "Producto"};
        JComboBox selectFunciones = new JComboBox(funciones);
        JLabel sizeText = new JLabel("Memory Size: ");
        JTextField memorySize = new JTextField(); memorySize.setPreferredSize(new Dimension(40, 20));
      
        pDerecho.add(textVariables); pDerecho.add(parametros);
        pDerecho.add(funcionText); pDerecho.add(selectFunciones);
        //pDerecho.add(sizeText); pDerecho.add(memorySize);
        
        pDerecho.add(calcular); pDerecho.add(limpiar);
        
        pDerecho.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Configuracion"));
        f.getContentPane().add(pDerecho, BorderLayout.EAST);
        
        limpiar.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                reset();
            }
        }
        );
        
        calcular.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //memSize = Integer.parseInt((String) memorySize.getText());
                System.out.println("Cadena de parametros: " + (String) parametros.getText());
                refreshMemory((String) parametros.getText());
                
                //for(int i = 0 ; i < urmMemory.size() ; i++)
                //    System.out.println(urmMemory.get(i));
                File s = new File("suma.urm");
                File r = new File("restarestringida.urm");
                File i = new File("masuno.urm");
                File p = new File("producto.urm");
                
                switch((String) selectFunciones.getSelectedItem())
                {
                    case "Suma": computaURM(s); break;
                    case "Resta restringida": computaURM(r); break;
                    case "Incremento": computaURM(i); break;
                    case "Producto": computaURM(p); break;
                }
                
            }
        }
        );
        
        //Visualizacion frame
        f.pack();
        f.setVisible(true);
    }
    
}

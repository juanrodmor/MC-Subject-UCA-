import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mandelbrot extends JPanel {
  private final int MAX_ITER = 100000;
  private final double ZOOM = 150;
  private static BufferedImage Imagen;
  private int nTareas = Runtime.getRuntime().availableProcessors();

  
  public class parallelTask extends Thread
  {
      int limInf, limSup;
      private double zx, zy, cX, cY, tmp;
      
      parallelTask(int i, int s)
      {
          limInf = i;
          limSup = s;         
      }
      
      @Override
      public void run()
      {
        //aqui comienza la rutina a paralelizar
        for (int y = 0; y < getHeight(); y++) {
          for (int x = limInf; x < limSup; x++) {
                  zx = zy = 0;
                  cX = (x - 400) / ZOOM;
                  cY = (y - 300) / ZOOM;
                  int iter = MAX_ITER;
                  while (zx * zx + zy * zy < 4 && iter > 0) {
                          tmp = zx * zx - zy * zy + cX;
                          zy = 2.0 * zx * zy + cY;
                          zx = tmp;
                          iter--;
                  }
                  Imagen.setRGB(x, y, iter | (iter << 8));
          }
        }//aqui finaliza la rutina a paralelizar          
      }
      
  }
  
  double calculoMandelbrootSecuencial()
  {
      long timeStart = System.nanoTime();
      double zx, zy, cX, cY, tmp;      
        for (int y = 0; y < getHeight(); y++) {
          for (int x = 0; x < getWidth(); x++) {
                  zx = zy = 0;
                  cX = (x - 400) / ZOOM;
                  cY = (y - 300) / ZOOM;
                  int iter = MAX_ITER;
                  while (zx * zx + zy * zy < 4 && iter > 0) {
                          tmp = zx * zx - zy * zy + cX;
                              zy = 2.0 * zx * zy + cY;
                              zx = tmp;
                          iter--;
                  }
                  //Imagen.setRGB(x, y, iter | (iter << 8));
          }
        }//aqui finaliza la rutina a paralelizar       
      
      
      double time = (System.nanoTime() - timeStart)/1e9;
      
      return time;
  }

 
  private boolean listo = false;
  public Mandelbrot(JLabel lb) {
    setBounds(100, 100, 800, 600);
    //setVisible(true);
    //setResizable(false);
    //setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setPreferredSize(new Dimension(800, 600));
    Imagen = new BufferedImage(getWidth(), getHeight(),
   	   BufferedImage.TYPE_INT_RGB);
     
    // Ejecutor
    ExecutorService ej = Executors.newCachedThreadPool();
    int ventana = Imagen.getWidth()/nTareas,inf = 0, sup = ventana;
    
    long timeStart = System.nanoTime();
    for(int i = 0 ; i < nTareas; i++)
    {
        ej.execute(new parallelTask(inf, sup));
        inf += ventana;
        sup += ventana;
    }
    
    ej.shutdown();
    try { ej.awaitTermination(1L, TimeUnit.DAYS); } catch(Exception e) {}
    
    double timeParallel = (System.nanoTime() - timeStart)/1e9;
    double timeSecuential = calculoMandelbrootSecuencial();
      
    lb.setText(Double.toString((timeSecuential/timeParallel)));
    System.out.println("SPEEDUP: " + (timeSecuential/timeParallel));
    
   listo = true;
    
  }
  public void paint(Graphics g) {
   if(listo) g.drawImage(Imagen, 0, 0, this);
  }
  
  /*public static void main(String[] args) {
  	  new Mandelbrot().setVisible(true);
  }*/
}

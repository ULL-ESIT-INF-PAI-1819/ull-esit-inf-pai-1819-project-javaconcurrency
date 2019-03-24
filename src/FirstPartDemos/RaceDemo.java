

public class RaceDemo implements Runnable{

  public int numero = 0;

  public synchronized void run(){
    for(int i = 0; i < 1000000; i++){
      numero += 1;
    }
  }


  public static void main(String args[]) throws InterruptedException{
    RaceDemo rc1 = new RaceDemo();
    Thread hilo1 = new Thread(rc1);
    Thread hilo2 = new Thread(rc1);
    hilo1.start();
    hilo2.start();
    Thread.sleep(400);
    System.out.println("Numero: " + rc1.numero);
  }

}

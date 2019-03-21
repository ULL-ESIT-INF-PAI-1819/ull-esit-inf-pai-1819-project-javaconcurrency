
public class InterruptDemo implements Runnable{

    public synchronized void run(){

    try{
      for (int i = 0; i < 10; i++){
        System.out.println(i);
        if(i == 5){
          wait();
        }
      }
    }catch(InterruptedException e){
        System.out.println("He sido interrumpido!\n");
    }

    }


    public static void main(String[] args){

      InterruptDemo d = new InterruptDemo();
      Thread hilo = new Thread(d);
      hilo.start();
      hilo.interrupt();

    }



}

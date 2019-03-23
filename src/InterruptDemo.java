
public class InterruptDemo implements Runnable{

    public synchronized void run(){
    try{
      for (int i = 0; i < 10; i++){
        System.out.println(i);
        if(i == 5){
          wait();
        }
      }
    // Here we declare how the thread is going to
    // behave in case of receiving an interruption signal.
    }catch(InterruptedException e){
        System.out.println("I've been interrupted!\n");
    }

    }


    public static void main(String[] args){

      InterruptDemo d = new InterruptDemo();
      Thread hilo = new Thread(d);
      hilo.start();
      hilo.interrupt();

    }

}

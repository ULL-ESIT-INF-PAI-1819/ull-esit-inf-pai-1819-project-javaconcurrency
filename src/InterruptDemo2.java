

public class InterruptDemo2 implements Runnable{


  public void run(){

    boolean break_ = false;
    System.out.println("Beginning!\n");

    while(true){
      // Checks if the interrupted flag is activated
      if(Thread.interrupted()){
        break_ = true;
      }

      if(break_){
        break;
      }
    }

    System.out.println("I got out!\n");
  }

  public static void main(String[] args){
    InterruptDemo2 id = new InterruptDemo2();
    Thread t = new Thread(id);
    t.start();
    t.interrupt();

  }

}

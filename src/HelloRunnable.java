

public class HelloRunnable implements Runnable{

  /**
   * This is an abstract method that is declared
   * in the Runnable Interface. Everytime you start
   * the thread it causes the object's run method to
   * be called in that separately executing thread.
   */
  public void run(){
    System.out.println("Hello from a Thread!");
  }

  public static void main(String args[]){
    // It automatically calls the HelloRunnable class "run" method.
    (new Thread(new HelloRunnable())).start();
  }

}

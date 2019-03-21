
/**
 * Similar to previous class but extends from
 * Thread instead of implementing Runnable interface.
 */
public class Agent extends Thread{

  private String name;

  public Agent(String name){
    this.name = name;
  }

  public void run(){
    System.out.println("I am agent " + name);
  }

  public static void main(String args[]){
      Agent smith = new Agent("Smith");
      smith.start();
  }

}

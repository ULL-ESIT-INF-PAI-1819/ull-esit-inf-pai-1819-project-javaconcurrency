

public class BladeRunner implements Runnable{

  private String name;

  /**
   * Constructor of the class
   * @param name of the blade runner.
   */
  public BladeRunner(String name){
    this.name = name;
  }

  /**
   * This is an abstract method that is declared
   * in the Runnable Interface. Everytime you start
   * the thread it causes the object's run method to
   * be called in that separately executing thread.
   */
    public void run(){
      System.out.println("Hello I'm " + name + ", I swear I'm not an android!");
    }

  public static void main(String args[]){
    BladeRunner deckard = new BladeRunner("Deckard");
    Thread hilo = new Thread(deckard);
    hilo.start();
  }

}

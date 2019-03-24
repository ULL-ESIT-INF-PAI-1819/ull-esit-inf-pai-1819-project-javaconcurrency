import java.util.Random;

/*
 * This is a simple class that contains an integer which
 * will increment or decrement its value.
 */
class Counter{

  static int count;

  public void increment(){
    count++;
  }

  public static int getCount(){
    return count;
  }

}


public class JoinDemo implements Runnable{

  public void run(){
    Counter c = new Counter();

    for (int i = 0; i < 100000; i++){
      c.increment();
    }

  }

  public static void main(String args[]) throws InterruptedException{

    Thread t1 = new Thread(new JoinDemo());
    t1.start();
    // If you comment this part this try-catch
    // you'll see that the counter displays a different
    // value that the one set in the for loop.
    try{
      t1.join();
    }catch(Exception e){}
    System.out.println("Count: " + Counter.getCount());

  }

}

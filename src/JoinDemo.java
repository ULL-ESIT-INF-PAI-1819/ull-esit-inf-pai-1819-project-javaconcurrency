import java.util.Random;

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

    for (int i = 0; i < 100000000; i++){
      c.increment();
    }

  }

  public static void main(String args[]){

    Thread t1 = new Thread(new JoinDemo());
    t1.start();
    try{
      t1.join();
      //Thread.sleep(1);
    }catch(Exception e){}
    System.out.println("Count: " + Counter.getCount());

  }

}

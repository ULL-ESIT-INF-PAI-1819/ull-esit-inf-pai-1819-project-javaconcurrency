

public class SleepDemo{


  public static void main(String args[]) throws InterruptedException{
    System.out.println("This will take 1 second");
    // 1000 milliseconds of sleeping, equals to 1 second.
    Thread.sleep(10);
    System.out.println("to print");
  }

}

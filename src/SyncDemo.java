// Example from https://www.javatpoint.com/synchronization-in-java

class Table{


  // synchronized void printTable(int incrementer){
 void printTable(int incrementer){
  //  synchronized(this){
    for (int i = 0 ; i < 5; i++){
      System.out.println(incrementer * i);
      try{Thread.sleep(400);}catch(Exception e){}
    }
  }
  //}
}

class FirstThread extends Thread{
  Table t;
  public FirstThread(Table t){
    this.t = t;
  }
  public void run(){
    t.printTable(-1);
  }
}

class SecondThread extends Thread{
  Table t;
  public SecondThread(Table t){
    this.t = t;
  }
  public void run(){
    t.printTable(1);
  }
}

public class SyncDemo{
  public static void main(String[] args){
    Table t = new Table();
    FirstThread t1 = new FirstThread(t);
    SecondThread t2 = new SecondThread(t);
    t1.start();
    t2.start();
  }
}

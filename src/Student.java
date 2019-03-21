import java.util.Random;


public class Student extends Thread{

  private String name;

  public Student(String name){
    this.name = name;
  }

  public void Study() throws InterruptedException{

    Random rand = new Random();
    int option;

    while(true){
    option = rand.nextInt(2);

    if (option == 0){
      System.out.println("I'm studying DAA");
    }else if (option == 1){
      System.out.println("I'm studying PAI");
    }else{
      System.out.println("I need to practice my programming skills "
                   + "because this option should never be executed.");
    }

    Thread.sleep(1000);
    }
  }

  public void run(){

    try{
      Study();
    // InterruptedException must be caught if you're using Thread.sleep()
    }catch(InterruptedException e){
      System.out.println("Stopped studying! Pachacho time!");
    }

  }

  public static void main(String args[]){
    Boolean newResistenciaProgram = true;
    Student adrian = new Student("Adrian");
    adrian.start();

    if(newResistenciaProgram == true){
      adrian.interrupt();
    }
  }

}

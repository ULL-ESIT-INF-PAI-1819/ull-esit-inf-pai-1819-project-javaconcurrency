# Java Concurrency

## What’s concurrency?

Concurrency consists in the ability of ***dealing with multiple tasks***, starting and running them **in overlapping time periods**, this means that a concurrent application **doesn’t execute tasks in the same time period** but rather schedule task belonging to a same set of tasks in a non sequential manner.

Concurrency was actually derived from early work on railroads and telegraphy, that’s why names such as _semaphores_ are employed, _how would you safely manage multiple trains on the same railroad system in such a way that no casualties would occur?_

In 1960’s the interest in the field raised thanks to **Edsger W.Dijkstra** who is credited with having published the _first paper_ in this field, where he identified and solved the _mutual exclusion problem_. Dijkstra then went on to define fundamental concurrency concepts such as _semaphores, mutual exclusions and deadlocks._


## How does it works?

When a user runs a program, the operating system creates a process. A **process** consists of several entities:

*    The ***executable machine language program***.
*    A ***block of memory***, which will include the executable code, a call stack that keeps track of active functions, a heap, and some other memory locations.
*    ***Descriptors of resources*** that the operating system has allocated to the process - for example, file descriptors.
*    **Security information** - for example, information specifying which hardware and software resources the process can access.
*    **Information about the state of the process**, such as whether the process is ready to run or is waiting on some resource, the content of the registers, and information about the process’ memory.

Most modern systems are **multitasking**. This means that the OS provides support for the “simultaneous” execution of multiple process, this is possible even on a system with a single core, since each process runs for a fixed amount of time ( typically a few milliseconds ) often called **time slice**.

In a multitasking OS if a process needs to wait for a resource—for example, it needs to read data from external storage—it will **block**. This means that it will stop executing and the operating system can run another process. However, many programs can continue to do useful work even though the part of the program that is currently executing must wait on a resource. For example, _an airline reservation system that is blocked waiting for a seat map for one user could provide a list of available flights to another user._

**Threading** provides a mechanism for programmers to divide their programs into more or less independent tasks with the property that when one thread is blocked another thread can be run. Furthermore, in most systems it’s possible to switch between threads much faster than it’s possible to switch between processes. This is because threads are “lighter weight” than processes.


**Threads** are contained within processes, so they can use the same executable, and they usually share the *same memory* and the *same I/O devices*. In fact, two threads belonging to one process can share most of the *process’ resources*. The two most important exceptions are that they’ll need a record of their own program counters and they’ll need their own call stacks so that they can execute independently of each other.

## Thread Objects

### Defining and Starting a Thread

Threads are *instances* of the class Thread. There are *two ways* to create a Thread object:

Provide an object of a class that implements the Runnable interface and pass it to the Thread constructor:

```java
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

```

Or extending thread into the object’s class:

```java
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
```

We’ll use the first approach as is more _flexible_ and _applicable_ to the high-level thread management APIs covered later.

### Pausing Execution with Sleep

Thread sleep causes the current thread to _suspend execution for a specific period_. This is efficient because it provides to other threads of an application with CPU time for running themselves.

Two overloaded versions of sleep are provided, first one *specifies the sleep time to the millisecond* while the other one s*pecifies it to the nanosecond.* However, these sleep times are not guarantee to be precise as they’re limited by the facilities provided by the underlying OS.

```java
public class SleepDemo{


  public static void main(String args[]) throws InterruptedException{
    System.out.println("This will take 1 second");
    // 1000 milliseconds of sleeping, equals to 1 second.
    Thread.sleep(1000);
    System.out.println("to print");
  }

}
```


### Interrupts

An interrupt is an *indication to a thread that it should stop what it is doing*. It’s up to the programmer to decide exactly how a thread responds to an interrupt, but is very common for the thread to terminate.

If we want to interrupt a thread we must invoke the *Thread.interrupt()* method on it’s Thread object. For the interrupt mechanism to work correctly, the interrupted thread *must support its own interruption*, that means it should declare somehow how it’s gonna behave if it receives an interrupt signal.

```java
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
```

Many methods are pre-design to cancel their current operation as soon as an interrupt is received and throw an _Interrupted Exception_ for example _Thread.sleep()_ or _Object.wait()._

But what if a Thread doesn’t invoke a method that throws InterruptedException? Then if we call _Thread.interrupt()_ it wouldn’t stop, in this case we must periodically invoke Thread.interrupted, which returns true if an interrupt has been received.


```java


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

```






### Joins

The _join method_ blocks the caller thread until the called thread object has finished its execution. This means that when a thread calls _t.join()_ it blocks itself until t finishes.

```java
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

    for (int i = 0; i < 100000000; i++){
      c.increment();
    }

  }

  public static void main(String args[]){

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
```

### Synchronization

Threads communicate between themselves primarily *by sharing access to some fields*, this form of communication can lead to *thread interference* and *memory consistency errors*.

To prevent this kind of errors we use a tool called *synchronization*, is provides a way to access in an organized ways to the shared resources, however synchronization can introduce thread contention, which occurs when two or more threads try to access the same resource simultaneously and *cause the Java runtime to slow down one or more threads* ( or even suspend their execution ).

Java provides two basic ways of synchronization :
1. ***Synchronized methods.***
2. ***Synchronized statements.***

In the first case we’ve got:

```java
// Example from https://www.javatpoint.com/synchronization-in-java

class Table{

/* In this case we've a synchronized method */
  synchronized void printTable(int incrementer){
    for (int i = 0 ; i < 5; i++){
      System.out.println(incrementer * i);
      try{Thread.sleep(400);}catch(Exception e){}
    }
  }
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

```

There’s no possible way for two invocations of the synchronized method on the same object to interleave, _while one of the threads is executing the method all other threads that invoke that same method must suspend their own execution until the first thread is done with the object._ Also, when a synchronized method exits, it automatically established a _happens-before relationship_ with any subsequent invocation of that same method ( in the same object ) that means that the actions that the threads perform in the objects are visible to the other threads.

```java

  public static int incrementer;

  synchronized void printTable(){

    for (int i = 0 ; i < 5; i++){
      System.out.println(incrementer * i);
      try{Thread.sleep(400);}catch(Exception e){}
    }
    // Because the happens-before relationship exists
    // the following thread to execute this piece of
    // code we'll be seeing the change in the incrementer
    // variable.
    incrementer = 20;
  }

}
```

The other way of create synchronized code is with the previously mentioned *synchronized statements*. Unlike synchronized method, the synchronized statements *must specify the object that provides the intrinsic lock*.

```java
  void printTable(int incrementer){
   synchronized(this){
    for (int i = 0 ; i < 5; i++){
      System.out.println(incrementer * i);
      try{Thread.sleep(400);}catch(Exception e){}
    }
   }
  }
}
```
---

## Liveness of a concurrent application:

A concurrent application’s ability to execute in a finite amount of time is known as its **liveness**. As much of a trivial matter as it may seem, there are several common problems that have to be faced when developing this type of applications.

For the record, we’ll be discussing *deadlock*, *starvation* and *livelock*.

---

### Deadlock:
Describes a situation where two or more threads are blocked **forever**, waiting for each other.

A Java application may block because the synchronized keyword causes the executing thread to block while waiting for the lock associated with the specified object.

**This is ultimately better off explained with examples:**

*Alphonse and Gaston are friends, and great believers in courtesy. A strict rule of courtesy is that when you bow to a friend, you must remain bowed until your friend has a chance to return the bow. Unfortunately, this rule does not account for the possibility that two friends might bow to each other at the same time. This example application, Deadlock, models this possibility:*

```java
public class Deadlock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        public synchronized void bow(Friend bower) {
            System.out.format("%s: %s"
                + "  has bowed to me!%n",
                this.name, bower.getName());
            bower.bowBack(this);
        }
        public synchronized void bowBack(Friend bower) {
            System.out.format("%s: %s"
                + " has bowed back to me!%n",
                this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
        final Friend alphonse =
            new Friend("Alphonse");
        final Friend gaston =
            new Friend("Gaston");
        new Thread(new Runnable() {
            public void run() { alphonse.bow(gaston); }
        }).start();
        new Thread(new Runnable() {
            public void run() { gaston.bow(alphonse); }
        }).start();
    }
}

```

Try this code! It will most likely freeze when threads try to call bowBack.

So, as a general rule: **When does a concurrent application face deadlock?**

- Simple. This often occurs when an executing thread is waiting for a reasource whose lock is owned by another thread, who is **also** waiting for a resource owned by the first thread.
- This causes that they can never respond each other back. The program will likely freeze if a main action thread is involved.

There is another great code examle [here](https://github.com/ULL-ESIT-INF-PAI-1819/ull-esit-inf-pai-1819-project-javaconcurrency/blob/master/src/Hostages%20(Deadlock)/src/Hostages.java).

As for solutions to this problem, one is provided [here](https://github.com/ULL-ESIT-INF-PAI-1819/ull-esit-inf-pai-1819-project-javaconcurrency/blob/master/src/Deadlock%20Solved/src/Safelock.java), and is also discussed in the **High level Concurrency Objects** section.


### Starvation:
Describes a situation where **a thread doesn’t gain access to shared resources** because there are other **"very greedy"** threads that use them for very large periods of time.

This can ultimately lead to massively slowing down our application - specially if the blocked thread(s) are of sheer importance.

The **solution** to this problem is almost entirely *design-based*. You can adapt different **policies** to manage thread activity.
There are several high-level concurrency-related objects in Java that implement these policies automatically.

One of the most popular ones is the ***Fair policy***, which establishes a simple, set *rule*:
**When a thread abandons a lock, and immediately queues up to obtain it again, other threads have preference.**

#### Thread.yield():

There is, however, a much more straightforward way of notifying the scheduler that the currently executing thread is willing to yield its current use of a processor to another thread.

As described in the [Official Oracle Refference](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#yield%28%29), this prevents starvation, but is rarely considered the perfect solution.

An example of *yield()* is shown here:

```java
// Java program to illustrate yield() method
// in Java
import java.lang.*;

// MyThread extending Thread
class MyThread extends Thread
{
    public void run()
    {
        for (int i=0; i<5 ; i++)
            System.out.println(Thread.currentThread().getName()
                                + " in control");
    }
}

// Driver Class
public class yieldDemo
{
    public static void main(String[]args)
    {
        MyThread t = new MyThread();
        t.start();

        for (int i=0; i<5; i++)
        {
            // Control passes to child thread
            Thread.yield();

            // After execution of child Thread
            // main thread takes over
            System.out.println(Thread.currentThread().getName()
                                + " in control");
        }
    }
}
```

### Livelock:

Although not as common as deadlock, it results on livelocked threads being unable to make further progress as well.
However, they are not blocked; they are simply too busy communicating with other threads, and not being able to focus on their respective tasks.

**Again, lets go over a brief example:**

*Imagine two very polite people attempting to pass each other in a corridor. The first moves to the left to let the other pass - but the other moves to the right, ALSO letting the other person through. When they find out they are still blocked, they BOTH move to the other side, resulting on them being blocked yet again. And so on ..*

A simulation for this example can be found in [here](https://github.com/ULL-ESIT-INF-PAI-1819/ull-esit-inf-pai-1819-project-javaconcurrency/blob/master/src/Livelock/src/Livelock.java).


---

## Guarded Blocks:

Imagine you have an app which runs multiple threads, and you need to let them know of when to be active and when to sleep. Threads need to coordinate their actions, and one of the most common ways of achieving this is by using **guarded blocks.**

A **guarded block** consists in a loop structure with a shared condition variable. This condition variable **can (and will)** be modified by another thread, which will wake up the threads that are affected by said condition.

A naive and **extremely inefficient** approach to achieving this would be using a plain, simple while loop with our shared condition blocking the way.

```java
public void guardedJoy() {
    // Simple loop guard. Wastes
    // processor time. Don't do this!
    while(!joy) {}        
    System.out.println("Joy has been achieved!");
}
```

This does what we need, but not *how* we want to. The question is:
**How can we avoid to waste CPU cycles while waiting for the condition to be true?**

We can call **wait()** inside the loop, which suspends its execution until it is later notified by another thread.
Java provides a method **notifyAll()**, which issues a notification to every other thread and “recovers” them - which is, by the way, the most used approach *(there is a second notification method called **notify()** that wakes up a single thread, but effectively does NOT let you specify which).*

Here is how it would look like now:

```java
public synchronized void guardedJoy() {
    // This guard only loops once for each special event, which may not
    // be the event we're waiting for.
    while(!joy) {
        try {
            wait();
        } catch (InterruptedException e) {}
    }
    System.out.println("Joy and efficiency have been achieved!");
}
```

With this idea in mind, note that the loop surrounding the **wait()** call is important: the waiting thread may have been woken up, but this doesn’t mean it’s been due to a change in the condition variable we are interested in, or at least, we can’t be sure 100% on a large scale application, where multiple threads notify each other.

---

## Immutable objects:

A widely spread and accepted approach to building thread-safe applications is the use, and actually maximum reliance in **immutable objects.**
*An object is considered immutable if its state cannot change after it is constructed. *

They are particularly useful in concurrent applications, since data cannot be corrupted by thread interference - nor can it be observed in an inconsistent state. This may avoid you having write synchronized blocks of code to ensure consistency. Additionally, it decreases the overhead due to garbage collection.

### How to make an object “immutable”?

The following are very general rules for creating immutable objects - not all classes documented as “immutable” follow this strategy - but it will surely help to give a broad understanding on how to transform mutable class definitions to their more robust counterpart, immutable classes.

1. **Don’t provide “setter” methods** or methods that modify fields or objects referred to by fields.

    In the second case, we have ensure that:
    - **Methods that modify the mutable object are not provided.**
    - **There are no shared references to the mutable objects passed to the constructor.** These should never be stored in collections directly, but through a *copy*. Similarly, it is needed to make sure no references to the mutable object are returned from a method.  
2. **Make all fields final and private**.
3. **Don’t allow subclasses to override methods.**
    This accepts two approaches:
    - Declare the class as final.
    - Make the constructor private and construct instances in factory methods. This is arguably more sophisticated, but also trickier. An example of factory classes is provided [here](https://github.com/ULL-ESIT-INF-PAI-1819/ull-esit-inf-pai-1819-project-javaconcurrency/tree/master/src/Factory/src).

---

## High Level Concurrency Objects:

This section will discuss the advanced features introduced with version Java 5.0 regarding concurrency, provided in the java.util.concurrent packages.

### Lock Objects:
**Lock objects** work pretty much like the implicit locks placed in synchronized blocks of code - only one thread can own a Lock object at a time. They support waits and notifications as well through their associated Condition objects.

**Remember the Deadlock problem? Here is a way to solve it with intelligent locks.**

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Safelock {
    static class Friend {
        private final String name;
        private final Lock lock = new ReentrantLock();

        public Friend(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public boolean impendingBow(Friend bower) {
            Boolean myLock = false;
            Boolean yourLock = false;
            try {
                myLock = lock.tryLock();
                yourLock = bower.lock.tryLock();
            } finally {
                if (! (myLock && yourLock)) {
                    if (myLock) {
                        lock.unlock();
                    }
                    if (yourLock) {
                        bower.lock.unlock();
                    }
                }
            }
            return myLock && yourLock;
        }

        public void bow(Friend bower) {
            if (impendingBow(bower)) {
                try {
                    System.out.format("%s: %s has"
                        + " bowed to me!%n",
                        this.name, bower.getName());
                    bower.bowBack(this);
                } finally {
                    lock.unlock();
                    bower.lock.unlock();
                }
            } else {
                System.out.format("%s: %s started"
                    + " to bow to me, but saw that"
                    + " I was already bowing to"
                    + " him.%n",
                    this.name, bower.getName());
            }
        }

        public void bowBack(Friend bower) {
            System.out.format("%s: %s has" +
                " bowed back to me!%n",
                this.name, bower.getName());
        }
    }

```

This example provides Alphonse and Gaston a way of noticing when a friend is already bowing. This time, our **Friend** Objects need to acquire a lock for *both* participants to ensure their bows are coordinated.

To demonstrate the versatility of this language, we'll consider that Alphonse and Gaston are so infatuated with their newlyfound ability to bow safely, that they'll be doing so in a loop!

```java
    static class BowLoop implements Runnable {
        private Friend bower;
        private Friend bowee;

        public BowLoop(Friend bower, Friend bowee) {
            this.bower = bower;
            this.bowee = bowee;
        }

        public void run() {
            Random random = new Random();
            for (;;) {
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {}
                bowee.bow(bower);
            }
        }
    }


    public static void main(String[] args) {
        final Friend alphonse =
            new Friend("Alphonse");
        final Friend gaston =
            new Friend("Gaston");
        new Thread(new BowLoop(alphonse, gaston)).start();
        new Thread(new BowLoop(gaston, alphonse)).start();
    }
}
```

The biggest advantage that Lock objects bring to the table is their feature that allows **trying the lock** before actually entering the protected code. This allows threads to back out if the method is occupied by another thread or to do so after a (if specified) given period of time.

The syntax for a **timeout behaviour** is the following:

```java
lock.tryLock(timeout, unit)

// timeout: the time to wait for the lock
// unit: thi time unit for the timeout
```

Also, remember the aforementioned **Fair Policy**? Reentranlock objects can be constructed with a flag set so they follow this scheduling strategy.

For the last example, we'd simply need to pass a parameter to its constructor. This activates the policy.

```java
 private final Lock lock = new ReentrantLock(true);
```


### Executors:

Up until now, there's been a close connection between the task being done by a new Thread, as defined by its Runnable object, and the Thread *itself*, as defined by a Thread object.

This works well for small applications, but in large-scale applications, it makes sense to separate thread management and creation from the rest of the application. Objects that encapsulate these functions are known as **Executors**.

#### Executor Interfaces:

The **Executor interface** provides a single method, execute, designed to be a drop-in replacement for a common thread-creation idiom. If **r is a Runnable object**, and **e is an Executor object** you can replace

```java
new Thread(r).start();
```

with

```java
e.execute(r);
```

Some Executor implementations allow running **Callable Objects**. These are slightly different than Runnable implementations, fundamentally because they *return a value*.

The returned value will be stored in a **Future Object**. This is a wonderful tool that provides a different approach to synchronization and is fairly versatile.

For further reference on the topic, check out [Oracle Reference.](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html)

#### Thread Pools:

Most of the executors provided use **Thread pools** which are a set of **working threads** to which the workload is forced.

The most basic idea for this is the following:

- Whenever you call on an executor to run a task, it is passed down to an available working thread. The thread is responsible for running said task and next submitted tasks are queued up for other working threads to get.
- Thread Pools provide their own scheduler on implementation, and it varies on each class.
- Thread creation is very demanding, so having a fixed amount of working threads is actually a huge improvement performance-wise.

There are two comprehensive examples regarding Future Objects and Thread Pools in the Github repo: [Promise](https://github.com/ULL-ESIT-INF-PAI-1819/ull-esit-inf-pai-1819-project-javaconcurrency/blob/master/src/Promise/src/Promise.java) and [MegaPromise](https://github.com/ULL-ESIT-INF-PAI-1819/ull-esit-inf-pai-1819-project-javaconcurrency/blob/master/src/MegaPromise/src/MegaPromise.java) (an improved version).

#### Fork/Join:

The **fork/join framework** is an implementation of the **ExecutorService** interface that helps you take advantage of multiple processors. It is designed for work that can be broken into smaller pieces recursively. The goal is to use all the available processing power to enhance the performance of your application.

For further reading, visit [Oracle Tutorials.](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html)


### Concurrent Collections:
Basically, modified collection interfaces that define atomic methods that help avoiding memory consistency errors. They go from **ConcurrentHashMap** to **ConcurrentSkipListMap** which is a concurrent analog to **TreeMap.**

Since there are a lot of this kind of objects, we won't be detailing their functions. If you are interested in further reading, submit to the following [link](https://docs.oracle.com/javase/tutorial/essential/concurrency/collections.html).

### Atomic Variables:
What if you needed to change the value of a shared variable on a regular basis on different threads? This seems rather usual, but it’s not that easy to do so safely without the tools.
**Atomic variables** are a very decent alternative to an excessive use of synchronized methods, which can repercute roughly in a program’s liveness.
Included in **java.util.concurrent.atomic** and implemented for most base data types.

### Concurrent Random Numbers:
Interestingly enough, there is a more thread-efficient and reliable alternative to calling **Math.random** inside threads.
It is provided directly by **java.util.concurrent** and it’s called **ThreadLocalRandom**. Calling one of its methods ensures to generate a local random number instead of a global one.

---

### Bibliography

- [How does Join work in java](https://stackoverflow.com/questions/23422970/how-does-join-work-in-java-does-it-guarantee-the-execution-before-main)
- [Concurrency vs Parallelism](https://www.google.com/url?q=https://howtodoinjava.com/java/multi-threading/concurrency-vs-parallelism/&sa=D&ust=1553469367261000&usg=AFQjCNGnbQq8WV1LQH4i_g4WvYV1mAvF0w)
- [Elliot Forbes Learning Concurrency In Python](https://www.amazon.com/Learning-Concurrency-Python-concurrent-applications-ebook/dp/B01NARDH3B)
- [Peter Pachecho An Introduction to Parallel Programming](https://www.amazon.com/Introduction-Parallel-Programming-Peter-Pacheco/dp/0123742609)
- [Official Oracle Docs Web Page](https://docs.oracle.com/javase/tutorial/)
- [Java Essentials Tutorial](https://www.tutorialspoint.com/java/)
- [Classic Constructor VS Static Factory Methods](https://www.baeldung.com/java-constructors-vs-static-factory-methods)
- [Thread.yield](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#yield%28%29)
- [Livelock base example](https://www.logicbig.com/tutorials/core-java-tutorial/java-multi-threading/thread-livelock.html)
- [Yield Example Code](https://www.geeksforgeeks.org/java-concurrency-yield-sleep-and-join-methods/)
- [Future Objects](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html)
> - Icons in presentation made by https://www.freepik.com/ from https://www.flaticon.com/ is licensed by http://creativecommons.org/licenses/by/3.0/ CC 3.0 BY

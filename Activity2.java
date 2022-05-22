import java.util.Scanner; 
import java.util.Random;

enum State {
    THINKING,
    HUNGRY,
    EATING
}

class Philosopher implements Runnable {
    public DiningPhilosophers dp;
    public int id;

    public Philosopher(int id, DiningPhilosophers dp) {
        this.id = id;
        this.dp = dp;
    }

    public void run() {

        Random random = new Random();

        while (true) {
            try {
                System.out.println("Philosopher " + id + " is thinking");
                Thread.sleep(random.nextInt(5000));
                dp.pickup(id);
                Thread.sleep(random.nextInt(5000));
                dp.putdown(id);
                Thread.sleep(random.nextInt(5000));
            } catch(InterruptedException e) {
                System.out.println("Interrupted " + Thread.currentThread().getName());
                Thread.currentThread().interrupt();
                return;
            } 

        }

    }
}

class DiningPhilosophers {

    public State state[];
    public Thread self[];
    public int count;

    public DiningPhilosophers(int count) {
        this.count = count;
        this.state = new State[count];
        this.self = new Thread[count];

        for(int i = 0; i < count; i++) {
            state[i] = State.THINKING;
            self[i] = new Thread(new Philosopher(i, this)); /* pass this DiningPhilosophers object */
        }
    }

    public void startPhilosophers() {
        for(int i = 0; i < count; i++) {
            self[i].start();
        }
    }

    public synchronized void pickup(int i) throws InterruptedException {
        state[i] = State.HUNGRY;
        test(i);

        while (state[i] != State.EATING) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
    }

    public synchronized void putdown(int i) {
        state[i] = State.THINKING;
        System.out.println("Philosopher " + i + " released its left and right chopsticks");
        test((i + 4) % 5);
        test((i + 1) % 5);  
    }

    public synchronized void test(int i) {
        if ((state[(i + 4) % 5] != State.EATING) && (state[i] == State.HUNGRY) && (state[(i + 1) % 5] != State.EATING)) {
            state[i] = State.EATING;
            System.out.println("Philosopher " + i + " acquired its left and right chopsticks");
            System.out.println("Philosopher " + i + " is eating");
            notifyAll();
        }
    }

}

public class Activity2 {

	public static void main(String[] args) {

        DiningPhilosophers dp = new DiningPhilosophers(5);
        dp.startPhilosophers();

    }

}

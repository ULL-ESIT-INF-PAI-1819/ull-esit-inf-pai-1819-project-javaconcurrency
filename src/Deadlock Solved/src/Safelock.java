package src;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

		/*
		 * The magic happens here. If a Friend calling bow notices the other
		 * Friend is about to bow too, he will stop and wait for his friend to
		 * give him an opportunity to bow back safely
		 */
		public boolean impendingBow(Friend bower) {
			Boolean myLock = false;
			Boolean yourLock = false;

			try {
				// Note that:
				// tryLock returns true when the lock is acquirable, but waits
				// otherwise. This means two friends bowing at each other will
				// result in one holding the lock until the routine is performed
				// and then
				// the lock is likely redistributed
				myLock = lock.tryLock();
				yourLock = bower.lock.tryLock();
			} finally {
				// If by any chance the waiting time elapsed (which can happen)
				// one of the locks will return false.
				// To ensure no lock is preserved, we unlock them safely
				if (!(myLock && yourLock)) {
					if (myLock) {
						lock.unlock();
					}
					if (yourLock) {
						bower.lock.unlock();
					}
				}
			}

			// Return value false means the calling friend could not perform his
			// action
			return myLock && yourLock;
		}

		public void bow(Friend bower) { // This does not need to be synchornized
			if (impendingBow(bower)) { // Sync happens here <-----
				try {
					System.out.format("%s: %s has bowed to me!%n", this.name,
							bower.getName());
					bower.bowBack(this);
				} finally {
					lock.unlock();
					bower.lock.unlock();
				}
			}
		}

		public void bowBack(Friend bower) { // Absolutely no sync happens here
											// now
			System.out.format("%s: %s has bowed back to me!%n", this.name,
					bower.getName());
		}
	}

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
				} catch (InterruptedException e) {
				}
				bowee.bow(bower);
			}
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");

		new Thread(new BowLoop(alphonse, gaston)).start();
		new Thread(new BowLoop(gaston, alphonse)).start();

	}

}

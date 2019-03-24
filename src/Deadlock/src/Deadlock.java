package src;

public class Deadlock {
	static class Friend {
		private final String name;

		public Friend(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		/*
		 * If Alphonse calls this method:
		 * 
		 * - He sees his Friend bowing to him and reacts accordingly.
		 * 
		 * - He is summoned in bow back, so Gaston sees he's been bowed back
		 */
		public synchronized void bow(Friend bower) {
			System.out.format("%s: %s" + " has bowed to me!%n", this.name,
					bower.getName());
			bower.bowBack(this); // Synchornization stops both threads here
			// They are both trying to acquire each others locks!
		}

		/*
		 * If Gaston calls this method.
		 * 
		 * - He was the first one to bow down.
		 * 
		 * - He notices his friend and gets back up.
		 */
		public synchronized void bowBack(Friend bower) {
			System.out.format("%s: %s" + " has bowed back to me!%n", this.name,
					bower.getName());
		}
	}

	public static void main(String[] args) {
		final Friend alphonse = new Friend("Alphonse");
		final Friend gaston = new Friend("Gaston");

		// This is a tricky situation. Alphonse and Gaston are Friends, and true
		// believers in courtesy. A very strict rule states that, when greeting
		// a Friend, you have to do so with a bow.
		// You cant leave the bow until your Friend has a chance to bow back

		// Unfortunately, this rule does not account for when both Friends bow
		// at the same time!

		new Thread(new Runnable() {
			public void run() {
				alphonse.bow(gaston);
			}
		}).start();
		// If you comment out one of the threads, things will go smoothly.
		// Try it!

		new Thread(new Runnable() {
			public void run() {
				gaston.bow(alphonse);
			}
		}).start();
	}

}

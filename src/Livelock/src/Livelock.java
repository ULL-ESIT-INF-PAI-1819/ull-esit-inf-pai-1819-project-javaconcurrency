/**
 * 
 */
package src;

/**
 * @author Fernando
 * 
 */
public class Livelock {

	private static class PolitePerson {
		private boolean left0right1;
		private String name;

		public PolitePerson(String name, boolean side) {
			this.left0right1 = side;
			this.name = name;
		}

		public boolean side() {
			return this.left0right1;
		}

		public synchronized void swapSide() {
			System.out.println(name + ": Oh, my bad. Excuse me. Sorry.");
			this.left0right1 = (left0right1) ? false : true;
		}

		public static synchronized boolean openPath(PolitePerson p1,
				PolitePerson p2) {
			return (p1.side() != p2.side());
		}
	}

	/**
	 * This program results in undefined behavior and may livelock threads in
	 * their place for variable amounts of time
	 */
	public static void main(String[] args) {

		final PolitePerson chris = new PolitePerson("Chris", true);
		final PolitePerson sharla = new PolitePerson("Sharla", true);

		/*
		 * Chris and Sharla are very Polite People (tm) and will swap sides on
		 * the corridor every time they bump into each other. They are not very
		 * ccordinated, so it may take a while before they get out of the loop
		 */

		new Thread(new Runnable() {
			public void run() {
				while (!PolitePerson.openPath(sharla, chris)) {
					sharla.swapSide();
					try {
						Thread.sleep(100); // Simulates action performance
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				while (!PolitePerson.openPath(sharla, chris)) {
					chris.swapSide();
					try {
						Thread.sleep(100); // Simulates action performance
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();

	}
}

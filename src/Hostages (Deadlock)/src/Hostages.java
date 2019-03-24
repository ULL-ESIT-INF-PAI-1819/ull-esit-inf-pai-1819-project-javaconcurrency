package src;

/*
 * There a policeman and a criminal both have hostages for each other on gun
 * point and they shout at the same time:
 * "Don't move or I won't release the hostage!"
 * 
 * What happens?
 * 
 * Nothing. They do nothing because they own each other's locks and
 * therefore wait forever
 */
public class Hostages {

	/*
	 * Hostages could be any kind of resource in your app
	 */
	private static class Hostage {
		private String name;
		private boolean isHostage = false;

		public String getName() {
			return name;
		}

		public Hostage(String name) {
			this.name = name;
		}

		public boolean isHostage() {
			return isHostage;
		}

		public void makeHostage() {
			isHostage = true;
		}

		public void release() {
			isHostage = false;
		}
	}

	/*
	 * Wants to retrieve the civilian and stop the theft
	 */
	private static class PoliceOff {
		private Hostage myHostage;

		public Hostage getHostage() {
			return this.myHostage;
		}

		public PoliceOff(Hostage hos) {
			this.myHostage = hos;
		}

		public boolean hasHostage(Thief facing) {
			return (facing.getHostage().isHostage());
		}

		public void releaseHost(Thief facing) {
			myHostage.release();
			notifyAll();
		}

		public synchronized void warnFacingThief(Thief facing) {
			System.out.println("PoliceOff: Hey you! Release the civilian");
			myHostage.makeHostage();
			while (hasHostage(facing)) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			// Release hostage and act
			releaseHost(facing);
			System.out.println("PoliceOff: Hands up! You are under arrest!");
		}
	}

	/*
	 * Wants to save his companion and get the hell out of here ASAP
	 */
	private static class Thief {
		private Hostage myHostage;

		// private PoliceOff facing;

		public Hostage getHostage() {
			return this.myHostage;
		}

		public Thief(Hostage hos) {
			this.myHostage = hos;

		}

		public boolean hasHostage(PoliceOff facing) {
			return (facing.getHostage().isHostage());
		}

		public void releaseHost(PoliceOff facing) {
			myHostage.release();
			notifyAll();
		}

		public synchronized void warnFacingOff(PoliceOff facing) {
			myHostage.makeHostage();
			System.out.format("Thief: Hey you! Release my bro %s!%n", facing
					.getHostage().getName());
			while (hasHostage(facing)) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			// Release hostage and act
			releaseHost(facing);
			System.out
					.println("Thief: Hands up! Dont move or I blow your ass up!");
		}
	}

	/*
	 * Main thread. Instance the objects and prepare the scenario
	 */
	public static void main(String[] args) {
		Hostage civilian = new Hostage("Phillip");
		Hostage thief2 = new Hostage("Lupin");

		final Thief thief1 = new Thief(civilian);
		final PoliceOff off = new PoliceOff(thief2);

		// Comment out one of the threads to prevent deadlock
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(100); // This ensures that the events tale
					// place more or less at the same time
				} catch (InterruptedException e) {
				}
				off.warnFacingThief(thief1);
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(100); // This ensures that the events tale
										// place more or less at the same time
				} catch (InterruptedException e) {
				}
				thief1.warnFacingOff(off);
			}
		}).start();
	}

}

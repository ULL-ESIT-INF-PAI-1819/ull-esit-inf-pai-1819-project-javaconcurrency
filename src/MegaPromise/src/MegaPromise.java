package src;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MegaPromise {

	private static final ExecutorService execS = Executors
			.newFixedThreadPool(5);
	// A CompletionService is a Container that returns queued tasks from a
	// thread pool. This is elegant, efficient and useful
	// We can basically perform tasks asyncronously and then invoke results
	// through Future objects.
	private static CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(
			execS);

	// ------------------------------------------------------------------

	// It's a good practice to encapsulate the working units
	// Worker classes are meant to ... well, do the work
	private static class Worker implements Runnable {
		@Override
		public void run() {
			int received = 0;
			boolean errors = false;
			while (received < 4 && !errors) {
				try {
					// Once a task is ready, it's loaded in the Future result
					Future<Integer> result = completionService.take();
					System.out.println("I received the data: " + result.get());

					// Get creative! Do your stuff! Have fun!

					received++;

				} catch (Exception e) {
					e.printStackTrace();
					errors = true;
				}
			}
		}
	}

	// -----------------------------------------------------------------

	public static void main(String[] args) {

		// Callables are summoned performing tasks
		// This is a very easy example, but note that you can try it
		// with any callables you want at whichever order
		for (int i = 0; i < 4; i++) {
			final int it = i;
			completionService.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					Thread.sleep(100);
					return it;
				}
			});
		}

		// This over here works with the data that has been set up
		// once its ready
		new Thread(new Worker()).start();

		execS.shutdown();
	}
}

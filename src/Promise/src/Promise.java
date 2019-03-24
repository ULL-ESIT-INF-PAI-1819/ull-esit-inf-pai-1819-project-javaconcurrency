package src;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Promise {

	// This is a pool with 10 working threads
	private static final ExecutorService execS = Executors
			.newFixedThreadPool(10);

	/*
	 * This brief example shows a clever way to order tasks with Future objects
	 * returned by Callable objects in
	 */
	public static void main(String[] args) {

		// CAREFUL WITH DECLARING A FUTURE AS FINAL.
		// DON'T DO IT
		// ONLY VILLAINS DO THAT
		@SuppressWarnings("unchecked")
		Future<Integer>[] futureValuesOrdered = new Future[10];
		// This is not very pretty. Check MegaPromise for an improved version

		for (int i = 0; i < 10; i++) {
			final int it = i;
			futureValuesOrdered[i] = execS.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return it;
				}
			});

			System.out
					.println("This would happen asynchronously if it wasnt for the get()");

			try {
				System.out.println(futureValuesOrdered[i].get());
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}
		}

		execS.shutdown();
	}
}

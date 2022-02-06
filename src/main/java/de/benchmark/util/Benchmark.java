package de.benchmark.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Benchmark {
	private static Logger LOG = LogManager.getLogger();
	private static long runningTime = 0L;

	/**
	 * Logs the runningtime of the given predicates and returns their names
	 * afterwards.
	 * 
	 * @param <T>       type to test
	 * @param pres      list of predicates to test
	 * @param testParam parameter to test with
	 * @return names of tested predicates
	 */
	public static <T> List<String> benchmark(List<BenchmarkPredicate<T>> pres, T testParam) {
		List<String> benched = new ArrayList<>();
		long startTime = System.currentTimeMillis();

		pres.forEach(pre -> {
			LOG.info("type: {}, result: {}, took: {} ms",
					pre.getName(),
					pre.getPredicate().test(testParam),
					(System.currentTimeMillis() - startTime));

			benched.add(pre.getName());
		});

		return benched;
	}

	/**
	 * Calculates the runningtime of a function.
	 *
	 * @param <T>   Parametertype of the measured function
	 * @param <R>   Returntype of the measured function
	 * @param fn    Function the runningtime gets measured for
	 * @param param Parameter of the function
	 * @return Return value if the measured function
	 */
	public static <T, R> R measurePerformance(String title, Function<T, R> fn, T param) {
		reset();

		LOG.traceEntry(title);

		final long startTime = System.currentTimeMillis();
		R returnValue = fn.apply(param);
		runningTime = System.currentTimeMillis() - startTime;

		LOG.traceExit("Runningtime: " + runningTime);
		return returnValue;
	}

	/**
	 * current running time.
	 *
	 * @return running time
	 */
	public static long getRunningTime() {
		return runningTime;
	}

	/**
	 * resets current running time.
	 */
	private static void reset() {
		runningTime = 0L;
	}
}
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
	 * Logs the running time of the given predicates and returns their names
	 * afterwards. An error occurring during a test won't interfere with other tests
	 * and only be logged, although the failed test will not be in the returned list
	 * of executed tests. This method is intended for simple duration comparison of
	 * different algorithm implementations.
	 * 
	 * @param <T>       type to test
	 * @param pres      list of predicates to test
	 * @param testParam parameter to test with
	 * @return names of tested predicates
	 */
	public static <T> List<String> benchmark(List<BenchmarkPredicate<T>> pres, T testParam) {
		List<String> benched = new ArrayList<>();

		pres.forEach(pre -> {
			try {
				long startTime = System.currentTimeMillis();
				boolean result = pre.getPredicate().test(testParam);
				long duration = System.currentTimeMillis() - startTime;

				LOG.info("measuring performance of: {}, result: {}, took: {} ms",
						pre.getName(),
						result,
						duration);

				benched.add(pre.getName());
			} catch (Exception ex) {
				LOG.error("measuring performance of: {} failed with message: {}", pre.getName(), ex.getMessage());
			}
		});

		return benched;
	}

	/**
	 * Calculates the running time of a function. This method is intended for
	 * measuring the duration of a given function where the actual return value
	 * should be obtained too. The duration time of execution is available by
	 * calling Benchmark.getRunningTime(). It is available till the next call of
	 * this method.
	 *
	 * @param <T>   Parameter type of the measured function
	 * @param <R>   Return type of the measured function
	 * @param fn    Function the running time gets measured for
	 * @param param Parameter of the function
	 * @return Return value if the measured function
	 */
	public static <T, R> R measurePerformance(String title, Function<T, R> fn, T param) {
		reset();

		LOG.debug("measuring performance of: {}", title);

		final long startTime = System.currentTimeMillis();
		R returnValue = fn.apply(param);
		runningTime = System.currentTimeMillis() - startTime;

		LOG.debug("Runningtime: {}", runningTime);
		return returnValue;
	}

	/**
	 * resets current running time.
	 */
	private static void reset() {
		runningTime = 0L;
	}

	/**
	 * current running time.
	 *
	 * @return running time
	 */
	public static long getRunningTime() {
		return runningTime;
	}
}
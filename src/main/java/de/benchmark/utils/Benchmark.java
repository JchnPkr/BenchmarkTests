package de.benchmark.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Benchmark {
	private static Logger LOG = LogManager.getLogger();

	/**
	 * Tests the given predicates and return their names afterwards.
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
}

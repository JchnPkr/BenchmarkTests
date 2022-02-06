package de.benchmark.stringcheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import de.benchmark.util.Benchmark;
import de.benchmark.util.BenchmarkPredicate;

class StringEvaluationBenchmarkTest {
	@Test
	void benchmarkFailTest() {
		Predicate<String> npe = (str) -> {
			return str.isEmpty() ? false : true;
		};

		List<BenchmarkPredicate<String>> pres = Arrays.asList(
				new BenchmarkPredicate<>("npe", npe));

		List<String> benched = Benchmark.benchmark(pres, null);

		assertThat(benched).hasSameElementsAs(Collections.emptyList());
	}

	@Test
	void stringIsEmptyBenchmarkTest() {
		Predicate<String> elvis = (str) -> {
			return str == null || str.isEmpty() ? true : false;
		};

		Predicate<String> simpleOrElseTrue = (str) -> {
			if (str == null || str.isEmpty()) {
				return true;
			}
			return false;
		};

		Predicate<String> simpleAndElseFalse = (str) -> {
			if (str != null && !str.isEmpty()) {
				return false;
			}
			return true;
		};

		Predicate<String> apacheCommons = (str) -> {
			return StringUtils.isEmpty(str);
		};

		Predicate<String> optional = (str) -> {
			return Optional.ofNullable(str).orElse(StringUtils.EMPTY).isEmpty();
		};

		List<BenchmarkPredicate<String>> pres = Arrays.asList(
				new BenchmarkPredicate<>("elvis", elvis),
				new BenchmarkPredicate<>("simpleOrElseTrue", simpleOrElseTrue),
				new BenchmarkPredicate<>("simpleAndElseFalse", simpleAndElseFalse),
				new BenchmarkPredicate<>("apacheCommons", apacheCommons),
				new BenchmarkPredicate<>("optional", optional));
		String str = StringUtils.EMPTY;

		List<String> benched = Benchmark.benchmark(pres, str);

		// workaround, predicate is not spyable and spy on BenchmarkPredicate ruins
		// performance time
		assertThat(benched).hasSameElementsAs(
				pres.stream().map(pre -> pre.getName()).collect(Collectors.toList()));
	}

	@Test
	void stringContainsPerformanceTest() {
		String content = "this is a test string";
		String contains = "is a";

		String titleStringContains = "String.contains";
		Function<Object[], Boolean> stringFn = (arr) -> ((String) arr[0]).contains((String) arr[1]);

		String titleApacheStringUtilsContains = "ApacheStringUtils.contains";
		Function<Object[], Boolean> apacheFn = (arr) -> StringUtils.contains((String) arr[0], (String) arr[1]);

		Boolean resultSimpleString = Benchmark.measurePerformance(titleStringContains, stringFn,
				new Object[] { content, contains });
		long stringContainsRunningtime = Benchmark.getRunningTime();

		Boolean resultApacheString = Benchmark.measurePerformance(titleApacheStringUtilsContains, apacheFn,
				new Object[] { content, contains });
		long apacheContainsRunningtime = Benchmark.getRunningTime();

		assertAll(
				() -> assertTrue(resultSimpleString),
				() -> assertTrue(resultApacheString),
				() -> assertTrue(stringContainsRunningtime < apacheContainsRunningtime));
	}
}
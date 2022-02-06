package de.benchmark.stringcheck;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import de.benchmark.util.Benchmark;
import de.benchmark.util.BenchmarkPredicate;

class StringEvaluationBenchmarkTest {
	@Test
	void stringIsEmptyBenchmarkTest() {
		Predicate<String> elvis = (str) -> {
			return str == null || str.isEmpty() ? false : true;
		};

		Predicate<String> simpleOrElseTrue = (str) -> {
			if (str == null || str.isEmpty()) {
				return false;
			}
			return true;
		};

		Predicate<String> simpleAndElseFalse = (str) -> {
			if (str != null && !str.isEmpty()) {
				return true;
			}
			return false;
		};

		Predicate<String> apacheCommons = (str) -> {
			return !StringUtils.isEmpty(str);
		};

		Predicate<String> optional = (str) -> {
			return !Optional.ofNullable(str).orElse(StringUtils.EMPTY).isEmpty();
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
}

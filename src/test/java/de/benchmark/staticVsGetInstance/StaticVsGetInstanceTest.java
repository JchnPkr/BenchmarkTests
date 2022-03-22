package de.benchmark.staticVsGetInstance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.RepeatedTest;

import de.benchmark.staticVsGetInstance.builder.SingeltonTestClazz;
import de.benchmark.staticVsGetInstance.builder.StaticTestClazz;
import de.benchmark.util.Benchmark;
import de.benchmark.util.BenchmarkSupplier;

class StaticVsGetInstanceTest {
	@RepeatedTest(30)
	void staticClassMethodVsSingletonClassMethodTest() {
		String titleStatic = "Static Class method call";
		Supplier<String> staticClassMethod = () -> StaticTestClazz.iDoStuffStatic();

		String titleSingleton = "Singleton Class method call";
		Supplier<String> singletonClassMethod = () -> SingeltonTestClazz.getInstance().iDoStuffAsSingleton();

		List<BenchmarkSupplier<String>> supps = Arrays.asList(
				new BenchmarkSupplier<>(titleStatic, staticClassMethod),
				new BenchmarkSupplier<>(titleSingleton, singletonClassMethod));

		List<String> benched = Benchmark.benchmark(supps);

		// workaround, supplier is not spyable and spy on BenchmarkSupplier ruins
		// performance time
		assertThat(benched).hasSameElementsAs(
				supps.stream().map(supp -> supp.getName()).collect(Collectors.toList()));
	}
}

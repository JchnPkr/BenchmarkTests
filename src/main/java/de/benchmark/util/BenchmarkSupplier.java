package de.benchmark.util;

import java.util.function.Supplier;

public class BenchmarkSupplier<T> {
	private String name;
	private Supplier<T> supp;

	public BenchmarkSupplier(String name, Supplier<T> supp) {
		this.name = name;
		this.supp = supp;
	}

	public String getName() {
		return this.name;
	}

	public Supplier<T> getSupplier() {
		return this.supp;
	}
}

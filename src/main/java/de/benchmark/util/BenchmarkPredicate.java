package de.benchmark.util;

import java.util.function.Predicate;

public class BenchmarkPredicate<T>{
	private String name;
	private Predicate<T> pre;
	
	public BenchmarkPredicate(String name, Predicate<T> pre) {
		this.name = name;
		this.pre = pre;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Predicate<T> getPredicate() {
		return this.pre;
	}
}
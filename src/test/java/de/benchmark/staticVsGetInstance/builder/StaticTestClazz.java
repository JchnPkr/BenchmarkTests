package de.benchmark.staticVsGetInstance.builder;

public final class StaticTestClazz {
	public static final String RETURNVALUE = "Static stuff done.";

	private StaticTestClazz() {
	}

	public static String iDoStuffStatic() {
		return RETURNVALUE;
	}
}

package de.benchmark.staticVsGetInstance.builder;

public final class SingeltonTestClazz {
	public final String RETURNVALUE = "Singleton stuff done.";
	private static final SingeltonTestClazz INSTANCE = new SingeltonTestClazz();

	private SingeltonTestClazz() {
	}

	public String iDoStuffAsSingleton() {
		return RETURNVALUE;
	}

	public static SingeltonTestClazz getInstance() {
		return INSTANCE;
	}
}

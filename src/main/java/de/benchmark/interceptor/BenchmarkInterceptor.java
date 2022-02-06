package de.benchmark.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

/**
 * Interceptor to log the runningtime of a method. The log level for this class
 * has to be set to LEVEL.Trace for this class in the log configuration of the
 * referring project. The interceptor needs a binding in ejb.xml.
 */
@Interceptor
@Benchmark
public class BenchmarkInterceptor {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * logging method.
	 *
	 * @param context Context of invocation
	 * @return return value of logged method
	 * @throws Exception the exception of the invoked method
	 */
	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
		if (!LOGGER.isTraceEnabled()) {
			return context.proceed();
		}

		final String className = context.getTarget().getClass().getName();
		final String methodName = context.getMethod().getName();

		final long startTime = System.currentTimeMillis();

		Object returnValue = null;
		try {
			returnValue = context.proceed();
			return returnValue;
		} finally {
			LOGGER.trace("{};{};{}", MarkerManager.getMarker("BENCHMARK"), className, methodName,
					(System.currentTimeMillis() - startTime));
		}
	}
}

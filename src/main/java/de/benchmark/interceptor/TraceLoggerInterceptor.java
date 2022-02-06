package de.benchmark.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;

/**
 * Interceptor to log the entry and exit parameters of a method. The log level
 * for this class has to be set to LEVEL.Trace for this class in the log
 * configuration of the referring project. The interceptor needs a binding in
 * ejb.xml
 */
@Interceptor
@TraceLogger
public class TraceLoggerInterceptor {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Die generische Log-Methode, die vom LoggingInterceptor für zu loggende
	 * Methoden aufgerufen werden. Anhand des InvocationContext wird die aufrufende
	 * Methode sowie deren Parameter bestimmt. Das Loggin erfolgt nur, wenn
	 * TRACE-Level aktiviert ist.
	 * 
	 * @param context Context of the invoked method
	 * @return Return value of the invoked method
	 * @throws Exception of the invoked method
	 */
	@AroundInvoke
	public Object log(InvocationContext context) throws Exception {
		if (!LOGGER.isTraceEnabled()) {
			return context.proceed();
		}

		final String className = context.getTarget().getClass().getName();
		final String methodName = context.getMethod().getName();

		if (LOGGER.isTraceEnabled(AbstractLogger.ENTRY_MARKER)) {
			LOGGER.trace(AbstractLogger.ENTRY_MARKER, entryMsg(className, methodName, context.getParameters()));
		}

		Object returnValue = null;
		try {
			returnValue = context.proceed();
			return returnValue;
		} finally {
			if (LOGGER.isTraceEnabled(AbstractLogger.EXIT_MARKER)) {
				LOGGER.trace(AbstractLogger.EXIT_MARKER, exitMsg(className, methodName, returnValue), returnValue);
			}
		}
	}

	private String entryMsg(final String className, final String methodName, final Object[] params) {
		final StringBuilder sb = new StringBuilder("entry method [" + methodName + "] in class [" + className + "]");

		if (params.length > 0) {
			sb.append(" with params (");
			int index = 0;
			for (final Object parm : params) {
				if (null != parm) {
					sb.append(parm.toString());
				} else {
					sb.append("null");
				}
				if (++index < params.length) {
					sb.append(", ");
				}
			}
			sb.append(')');
		}

		return sb.toString();
	}

	private String exitMsg(final String className, final String methodName, final Object returnValue) {
		final StringBuilder sb = new StringBuilder("exit method [" + methodName + "] in class [" + className + "]");

		if (null != returnValue) {
			sb.append(" with (" + returnValue + ")");
		}

		return sb.toString();
	}
}
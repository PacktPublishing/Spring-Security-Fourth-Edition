package com.packtpub.springsecurity.web.configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * http://localhost:8080/calendar/async/events
 */
@WebServlet(urlPatterns = { "/async/*" }, asyncSupported = true, name = "async")

public class AsyncDispatcherServlet extends DispatcherServlet {

	/**
	 * The constant NUM_ASYNC_TASKS.
	 */
	private static final int NUM_ASYNC_TASKS = 15;

	/**
	 * The constant TIME_OUT.
	 */
	private static final long TIME_OUT = 10 * 1_000;

	/**
	 * The constant logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AsyncDispatcherServlet.class);

	/**
	 * The Exececutor.
	 */
	private ExecutorService exececutor;

	/**
	 * Init.
	 *
	 * @param config the config
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		LOGGER.info("**** AsyncDispatcherServlet.init(): {}", config.getServletName());
		exececutor = Executors.newFixedThreadPool(NUM_ASYNC_TASKS);
	}

	/**
	 * Destroy.
	 */
	@Override
	public void destroy() {
		exececutor.shutdownNow();
		super.destroy();
	}

	/**
	 * Do dispatch.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws Exception the exception
	 */
	@Override
	protected void doDispatch(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		final AsyncContext ac = request.startAsync(request, response);

		ac.setTimeout(TIME_OUT);

		FutureTask task = new FutureTask(new Runnable() {

			@Override
			public void run() {
				try {
					LOGGER.debug("Dispatching request " + request);
					AsyncDispatcherServlet.super.doDispatch(request, response);
					LOGGER.debug("doDispatch returned from processing request " + request);
					ac.complete();
				}
				catch (Exception ex) {
					LOGGER.error("Error in async request", ex);
				}
			}
		}, null);

		ac.addListener(new AsyncDispatcherServletListener(task));
		exececutor.execute(task);
	}

	/**
	 * The type Async dispatcher servlet listener.
	 */
	private class AsyncDispatcherServletListener implements AsyncListener {

		/**
		 * The Future.
		 */
		private FutureTask future;

		/**
		 * Instantiates a new Async dispatcher servlet listener.
		 *
		 * @param future the future
		 */
		public AsyncDispatcherServletListener(FutureTask future) {
			this.future = future;
		}

		/**
		 * On timeout.
		 *
		 * @param event the event
		 * @throws IOException the io exception
		 */
		@Override
		public void onTimeout(AsyncEvent event) throws IOException {
			LOGGER.warn("Async request did not complete timeout occured");
			handleTimeoutOrError(event, "Request timed out");
		}

		/**
		 * On complete.
		 *
		 * @param event the event
		 * @throws IOException the io exception
		 */
		@Override
		public void onComplete(AsyncEvent event) throws IOException {
			LOGGER.debug("Completed async request");
		}

		/**
		 * On error.
		 *
		 * @param event the event
		 * @throws IOException the io exception
		 */
		@Override
		public void onError(AsyncEvent event) throws IOException {
			String error = (event.getThrowable() == null ? "UNKNOWN ERROR" : event.getThrowable().getMessage());
			LOGGER.error("Error in async request " + error);
			handleTimeoutOrError(event, "Error processing " + error);
		}

		/**
		 * On start async.
		 *
		 * @param event the event
		 * @throws IOException the io exception
		 */
		@Override
		public void onStartAsync(AsyncEvent event) throws IOException {
			LOGGER.debug("Async Event started..");
		}

		/**
		 * Handle timeout or error.
		 *
		 * @param event   the event
		 * @param message the message
		 */
		private void handleTimeoutOrError(AsyncEvent event, String message) {
			PrintWriter writer = null;
			try {
				future.cancel(true);
				HttpServletResponse response = (HttpServletResponse) event.getAsyncContext().getResponse();
				writer = response.getWriter();
				writer.print(message);
				writer.flush();
			}
			catch (Exception ex) {
				LOGGER.error(ex.getMessage(), ex);
			}
			finally {
				event.getAsyncContext().complete();
				if (writer != null) {
					writer.close();
				}
			}
		}
	}
}



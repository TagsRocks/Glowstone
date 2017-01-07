package com.github.glowstone.io.core.api;

import com.google.common.base.Preconditions;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class ApiService {

    private static ApiService instance;
    private final Server server;
    private final ResourceConfig resourceConfig;

    /**
     * ApiService constructor
     *
     * @param port int
     */
    public ApiService(ResourceConfig resourceConfig, int port) {
        Preconditions.checkNotNull(resourceConfig);
        Preconditions.checkNotNull(port);

        instance = this;
        this.server = new Server(port);
        this.resourceConfig = resourceConfig;

        HandlerList handlers = new HandlerList();

        // Context Handler
        ServletContainer container = new ServletContainer(this.resourceConfig);
        ServletHolder holder = new ServletHolder(container);
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.addServlet(holder, "/*");
        handlers.addHandler(contextHandler);

        // Gzip Handler
        GzipHandler gzipHandler = new GzipHandler();
        gzipHandler.setHandler(handlers);
        gzipHandler.setMinGzipSize(0);
        gzipHandler.setCheckGzExists(false);
        gzipHandler.setCompressionLevel(-1);
        gzipHandler.setExcludedAgentPatterns(".*MSIE.6\\.0.*");
        gzipHandler.setIncludedMethods("GET", "POST", "PUT");

        this.server.setHandler(gzipHandler);
        this.start();
    }

    /**
     * Get this ApiService instance
     *
     * @return ApiService
     */
    public static ApiService getInstance() {
        return instance;
    }

    /**
     * Start the api service
     */
    public void start() {
        try {
            this.server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the api service
     */
    public void stop() {
        try {
            this.server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

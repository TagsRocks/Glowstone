package com.github.glowstone.io.core.api;

import com.google.common.base.Preconditions;
import org.eclipse.jetty.server.Server;
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

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");

        ServletContainer container = new ServletContainer(this.resourceConfig);
        ServletHolder holder = new ServletHolder(container);
        handler.addServlet(holder, "/*");

        this.server.setHandler(handler);
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

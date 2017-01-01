package com.github.glowstone.io.core.http;

import com.github.glowstone.io.core.Glowstone;
import com.github.glowstone.io.core.http.resources.PlayerResource;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class IncomingServer {

    private Server server = null;

    public IncomingServer() {

        int port = 8766; // Todo: make this configureable
        this.server = new Server(port);

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");

        ResourceConfig resources = new ResourceConfig();
        resources.register(new PlayerResource());

        ServletContainer container = new ServletContainer(resources);
        ServletHolder holder = new ServletHolder(container);
        handler.addServlet(holder, "/*");

        this.server.setHandler(handler);
        this.start();
    }

    /**
     * Start the incoming server
     */
    public void start() {

        try {
            server.start();
        } catch (Exception e) {
            Glowstone.getLogger().error(String.format("Error starting %s server: %s", Glowstone.NAME, e.getMessage()));
        }

    }

    /**
     * Stop the incoming server
     */
    public void stop() {

        try {
            server.stop();
        } catch (Exception e) {
            Glowstone.getLogger().error(String.format("Error stopping %s server: %s", Glowstone.NAME, e.getMessage()));
        }

    }

}

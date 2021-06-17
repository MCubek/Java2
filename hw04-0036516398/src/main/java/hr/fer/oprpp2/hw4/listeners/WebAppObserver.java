package hr.fer.oprpp2.hw4.listeners;

import hr.fer.oprpp2.hw4.GlobalData;
import hr.fer.oprpp2.hw4.Keys;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Instant;

/**
 * WebListener for initializing data used by servlets when
 * server is started.
 * Class initializes global data and set's the start time to
 * servlet context global parameters.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebListener
public class WebAppObserver implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute(Keys.KEY_START_TIME, Instant.now());
        sce.getServletContext().setAttribute(Keys.KEY_GLOBAL_DATA, new GlobalData());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

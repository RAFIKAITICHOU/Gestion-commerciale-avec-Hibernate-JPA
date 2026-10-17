package org.example.web;

import org.example.dao.GenericDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Libère l'EntityManagerFactory à l'arrêt de l'application. */
@WebListener
public class AppLifecycleListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        GenericDAO.closeEntityManagerFactory();
    }
}

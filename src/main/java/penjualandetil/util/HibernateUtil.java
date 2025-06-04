package penjualandetil.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // volatile keyword ensures that multiple threads handle the instance variable correctly
    private static volatile SessionFactory sessionFactoryInstance;

    // Private constructor to prevent instantiation
    private HibernateUtil() {
        // Mencegah instansiasi melalui refleksi jika diperlukan
        if (sessionFactoryInstance != null) {
            throw new RuntimeException("Use getSessionFactory() method to get the single instance of this class.");
        }
    }

    public static SessionFactory getSessionFactory() {
        // Double-checked locking for thread-safe lazy initialization.
        if (sessionFactoryInstance == null) { // First check (not synchronized)
            synchronized (HibernateUtil.class) {
                if (sessionFactoryInstance == null) { // Second check (synchronized)
                    try {
                        // Create the SessionFactory from hibernate.cfg.xml
                        sessionFactoryInstance = new Configuration().configure().buildSessionFactory();
                    } catch (Throwable ex) {
                        System.err.println("Initial SessionFactory creation failed." + ex);
                        throw new ExceptionInInitializerError(ex);
                    }
                }
            }
        }
        return sessionFactoryInstance;
    }

    public static void shutdown() {
        if (sessionFactoryInstance != null && !sessionFactoryInstance.isClosed()) {
            sessionFactoryInstance.close();
            sessionFactoryInstance = null; // Reset instance on shutdown
        }
    }
}
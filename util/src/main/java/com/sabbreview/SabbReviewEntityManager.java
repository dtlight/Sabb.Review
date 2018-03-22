package com.sabbreview;

import java.net.URI;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

/**
 * Creates and maintains a JPA entity manager for pulling and pushing data to the database.
 */
public class SabbReviewEntityManager {
  private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
  private static final String DB_ENV_VARIABLE = "DATABASE_URL";
  private static EntityManager entityManager;

  public static EntityManager getEntityManager() {
    if (entityManager == null) {
      entityManager = createInstance();
    }
    return entityManager;
  }

  private static EntityManager createInstance() {
    EntityManagerFactory entityManagerFactory;
    if (System.getenv(DB_ENV_VARIABLE) != null) {
      URI uri = URI.create(System.getenv(DB_ENV_VARIABLE));
      HashMap<String, String> persistenceMap = new HashMap<>();
      String[] userDetails = uri.getUserInfo().split(":");
      String jdbcURL = String
          .format("jdbc:postgresql://%s:%d%s?user=%s&password=%s&sslmode=require", uri.getHost(),
              uri.getPort(), uri.getPath(), userDetails[0], userDetails[1]);
      persistenceMap.put("javax.persistence.jdbc.url", jdbcURL);
      persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
      entityManagerFactory =
          Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceMap);
    } else {
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.setFlushMode(FlushModeType.COMMIT);
    return entityManager;
  }

}

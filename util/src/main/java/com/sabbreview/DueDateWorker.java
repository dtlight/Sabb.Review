package com.sabbreview;

import com.sabbreview.controller.AssignmentController;
import com.sabbreview.model.Assignment;

import java.net.URI;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import javax.persistence.*;

public class DueDateWorker implements Runnable{
    //Date currDate = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
    private static final String DB_ENV_VARIABLE = "DATABASE_URL";
    public void run(){
        //AssignmentController.createAssignment("user","1","2");
        EntityManagerFactory emf;
        if (System.getenv(DB_ENV_VARIABLE) != null) {
            URI uri = URI.create(System.getenv(DB_ENV_VARIABLE));
            HashMap<String, String> persistenceMap = new HashMap<>();
            String[] userDetails = uri.getUserInfo().split(":");
            String jdbcURL = String
                    .format("jdbc:postgresql://%s:%d%s?user=%s&password=%s&sslmode=require", uri.getHost(),
                            uri.getPort(), uri.getPath(), userDetails[0], userDetails[1]);
            persistenceMap.put("javax.persistence.jdbc.url", jdbcURL);
            persistenceMap.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, persistenceMap);
        } else {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
        /*
        em.getTransaction().begin();
        Assignment assignment = em.find(Assignment.class, 1);
        System.out.println(assignment.toString());
        em.getTransaction().commit();
        */
        /*
        em.getTransaction().begin();
        String s = em.createNamedQuery("get-all-assignments-by-duedate").
                setParameter("dueDate","2018-03-14 00:00:00").getResultList().toString();
        System.out.println(s);
        */


        em.close();
        emf.close();
    }
}
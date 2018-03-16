package com.sabbreview;

import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Assignment;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.*;


/**
 * Tread worker that checks if assignments are past due.
 * If they are it sets their state to COMPLETED.
 */
public class DueDateWorker implements Runnable{

    private static final String PERSISTENCE_UNIT_NAME = "SabbReview";
    private static final String DB_ENV_VARIABLE = "DATABASE_URL";
    public void run(){
        //Creates entity manager separate to the one in the Controller class.
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

        //Creates list of all assignments and updates state, depending on due date.
        TypedQuery<Assignment> query =em.createNamedQuery("get-all-assignments", Assignment.class);
        List <Assignment> assignmentList = query.getResultList();
        try{
            em.getTransaction().begin();
            for (int i = 0; i < query.getResultList().size(); i++){
                Assignment assignment = assignmentList.get(i);
                if(assignment.getDueDate().before(new Date()) && assignment.getState() != AcceptanceState.COMPLETED){
                    //System.out.println("ID:" + assignment.getId() + " STATE:" + assignment.getState());
                    assignment.setState(AcceptanceState.COMPLETED);
                }

            }
            em.getTransaction().commit();
        } catch (RollbackException e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
        }

        em.close();
        emf.close();

    }

}
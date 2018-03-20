package com.sabbreview;

import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Assignment;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;


/**
 * Thread worker that checks if assignments are past due.
 * If they are it sets their state to COMPLETED.
 */
public class DueCheckWorker implements Runnable{

    public void run(){

        EntityManager em = SabbReviewEntityManager.getEntityManager();

        //Creates list of all assignments and updates state, depending on due date.
        TypedQuery<Assignment> query =em.createNamedQuery("get-all-assignments", Assignment.class);
        List <Assignment> assignmentList = query.getResultList();

        try{
            em.getTransaction().begin();
            for (int i = 0; i < assignmentList.size(); i++){
                Assignment assignment = assignmentList.get(i);
                if(assignment.getDueDate().before(new Date()) && assignment.getState() != AcceptanceState.COMPLETED){
                    assignment.setState(AcceptanceState.COMPLETED);
                }


            }
            em.getTransaction().commit();
        } catch (RollbackException e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
        }
    }

}
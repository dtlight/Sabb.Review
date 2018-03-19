package com.sabbreview;

import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Assignment;

import java.util.Date;
import java.util.List;
import javax.persistence.*;


/**
 * Thread worker that checks if assignments are past due.
 * If they are it sets their state to COMPLETED.
 */
public class DueDateWorker implements Runnable{

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
                    Assignment assignment1 = em.find(Assignment.class, Integer.parseInt(assignment.getId()));
                    System.out.println("ID:"+assignment1.getId());
                    assignment.setState(AcceptanceState.COMPLETED);
                    //pass string
                    em.getTransaction().commit();
                }

            }
        } catch (RollbackException e) {
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
        }

    }

}
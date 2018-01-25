package com.sabbreview.controller;

import com.sabbreview.SabbReview;
import com.sabbreview.model.AcceptanceState;
import com.sabbreview.model.Application;
import com.sabbreview.model.Assignment;
import com.sabbreview.model.User;
import com.sabbreview.responses.TransactionState;
import com.sabbreview.responses.TransactionStatus;
import com.sabbreview.responses.ValidationException;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;

import static spark.Spark.delete;
import static spark.Spark.post;
import static spark.Spark.put;


public class AssignmentController extends Controller {

    private static TransactionState<Assignment> createAssignment(String principle, Assignment assignment) {
        try {
            em.getTransaction().begin();
            em.persist(assignment); //telling jpa to store
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
        } catch (RollbackException e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "Could not create application");
        }
    }

    private static TransactionState<Assignment> deleteAssignment(String principle, Assignment assignment) {
        try{
            em.getTransaction().begin();
            em.persist(assignment);
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
        } catch (RollbackException e){
            rollback();
            return new TransactionState<>( null, TransactionStatus.STATUS_ERROR, "Could not delete application");
        }
    }

    private static TransactionState<Assignment> changeAssignment(String principle, Assignment assignment ){
        try{
            em.getTransaction().begin();
            em.persist(assignment);
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
        } catch (RollbackException e) {
            rollback();
            return new TransactionState<>( null, TransactionStatus.STATUS_ERROR, "Could not change application");
        }
    }


    public static TransactionState<> setAcceptanceState(Application applicationID, AcceptanceState acceptanceState){
        Assignment assignment;
        try{
            em.getTransaction().begin();
            assignment = em.find(Assignment.id, applicationID); //change id to static in Assignment model?
            assignment.setState(acceptanceState);
            em.getTransaction().commit();
            return new TransactionState<>(assignment, TransactionStatus.STATUS_OK);
        } catch (RollbackException e) {
            rollback();
            return new TransactionState<>(null, TransactionStatus.STATUS_ERROR, "");
        }
    }


    public static void attach() {
        post("/api/assignment", (req, res) -> requireAuthentication(req,
                (principle -> toJson(createAssignment(principle, fromJson(req.body(), Assignment.class))))));

        delete("/api/assignment", (req, res) -> requireAuthentication(req,
                (principle -> toJson(deleteAssignment(principle, fromJson(req.body(), Assignment.class))))));

        put("/api/assignment", (req, res) -> requireAuthentication(req,
                (principle -> toJson(changeAssignment(principle, fromJson(req.body(), Assignment.class))))));

        put("/api/assignment/:id/state/:state", (req, res) -> toJson(ApplicationController
                .setAcceptanceState(req.params(":id"), AcceptanceState.valueOf(req.params(":state")))));

        //set acceptance state app controller
    }
}
package com.sabbreview.controller;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.queue.EmailQueueInstance;

import javax.persistence.EntityManager;

/**
 * Patent class for all controllers.
 * Provides static entity manager and email queue functionality.
 * When creating a new entity manager object, it's probably a good idea to
 * create a controller extending this class.
 * @see SabbReviewEntityManager
 * @see EmailQueueInstance
 */
class Controller {

  static EntityManager em = SabbReviewEntityManager.getEntityManager();

  static EmailQueueInstance queueInstance;

  static {
    try {
      queueInstance = new EmailQueueInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static void rollback() {
    if(em.getTransaction().isActive()){
      em.getTransaction().rollback();
    }
  }
}

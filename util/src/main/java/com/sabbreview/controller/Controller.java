package com.sabbreview.controller;

import com.sabbreview.SabbReviewEntityManager;
import com.sabbreview.queue.EmailQueueInstance;

import javax.persistence.EntityManager;

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

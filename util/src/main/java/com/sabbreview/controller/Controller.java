package com.sabbreview.controller;

import com.sabbreview.SabbReviewEntityManager;

import javax.persistence.EntityManager;

class Controller {

  static EntityManager em = SabbReviewEntityManager.getEntityManager();

  static void rollback() {
    if(em.getTransaction().isActive()){
      em.getTransaction().rollback();
    }
  }
}

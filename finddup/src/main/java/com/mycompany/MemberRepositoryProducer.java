package com.mycompany;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MemberRepositoryProducer
{
   @Produces
   @MemberRepository
   @PersistenceContext
   private EntityManager em;
}

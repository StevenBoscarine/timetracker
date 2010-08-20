package com.mycompany;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

//@Stateful // enable when JBoss AS M4 is out
@Model
public class MemberRegistration
{
   @Inject
   @MemberRepository
   private EntityManager em;

   @Inject
   private UserTransaction utx;
   
   private Member newMember;

   @Produces
   @Named
   public Member getNewMember()
   {
      return newMember;
   }
   @Inject
   private Event<Member> memberEventSrc;

   public void register() throws Exception
   {
      System.out.println("Registering " + newMember.getName());
      utx.begin();
      em.joinTransaction();
      em.persist(newMember);
      utx.commit();
      memberEventSrc.fire(newMember);
      initNewMember();
   }

   @PostConstruct
   public void initNewMember()
   {
      newMember = new Member();
   }
}

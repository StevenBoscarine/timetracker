package com.mycompany;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RequestScoped
public class MemberListProducer
{
   @Inject
   @MemberRepository
   private EntityManager em;

   private List<Member> members;

   @Produces
   @Named
   public List<Member> getMembers()
   {
      return members;
   }

   public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Member member)
   {
      fetch();
   }

   @PostConstruct
   public void fetch()
   {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
      Root<Member> widget = criteria.from(Member.class);
      criteria.select(widget).orderBy(cb.asc(widget.get(Member_.name)));
      members = em.createQuery(criteria).getResultList();
   }
}

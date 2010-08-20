package com.mycompany;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/members")
@RequestScoped
public class MemberResource
{
   @Inject @MemberRepository
   private EntityManager em;

   @GET
   public List<Member> getMembers()
   {
      return em.createQuery("select m from Member m order by m.name").getResultList();
   }

   @GET
   @Path("/{id:[1-9][0-9]*}")
   public Member getMember(@PathParam("id") long id)
   {
      return em.find(Member.class, id);
   }
}

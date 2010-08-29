package com.boscarine.finddup.jpa;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by IntelliJ IDEA.
 * User: steven
 * Date: Aug 29, 2010
 * Time: 12:15:07 AM
 * To change this template use File | Settings | File Templates.
 */
/**
 * Backing bean
 */
@Named
@Stateless
public class FindDupsUIJPA {
	@Inject
	private EntityManager em;

//	private List<FileEntryJPA> members;
//
//	@Produces
//	@Named
//	public List<FileEntryJPA> listAllFiles() {
//		return members;
//	}
//
//	@PostConstruct
//	public void retrieveAllFilesOrderedByName() {
//
//		members = em.createQuery("from FileEntryJPA").getResultList();
//	}
}

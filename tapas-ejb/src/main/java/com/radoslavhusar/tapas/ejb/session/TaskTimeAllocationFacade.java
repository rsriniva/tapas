package com.radoslavhusar.tapas.ejb.session;

import com.radoslavhusar.tapas.ejb.entity.TaskTimeAllocation;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author <a href="mailto:me@radoslavhusar.com">Radoslav Husar</a>
 * @version 2011
 */
@Stateless
@Local(TaskTimeAllocationFacadeLocal.class)
public class TaskTimeAllocationFacade extends AbstractFacade<TaskTimeAllocation> implements TaskTimeAllocationFacadeLocal {

   @PersistenceContext(unitName = "MyPersistenceUnit")
   private EntityManager em;

   @Override
   protected EntityManager getEntityManager() {
      return em;
   }

   public TaskTimeAllocationFacade() {
      super(TaskTimeAllocation.class);
   }
}
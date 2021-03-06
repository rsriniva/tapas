package com.radoslavhusar.tapas.war.server.services;

import com.radoslavhusar.tapas.ejb.entity.Project;
import com.radoslavhusar.tapas.ejb.entity.ProjectPhase;
import com.radoslavhusar.tapas.ejb.entity.Resource;
import com.radoslavhusar.tapas.ejb.entity.ResourceGroup;
import com.radoslavhusar.tapas.ejb.entity.ResourceAllocation;
import com.radoslavhusar.tapas.ejb.stats.ResourceAllocationStatsEntry;
import com.radoslavhusar.tapas.ejb.entity.Task;
import com.radoslavhusar.tapas.ejb.entity.TimeAllocation;
import com.radoslavhusar.tapas.ejb.entity.Trait;
import com.radoslavhusar.tapas.ejb.session.ProjectFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.ProjectPhaseFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.ResourceFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.ResourceGroupFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.ResourceAllocationFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.TaskFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.TimeAllocationFacadeLocal;
import com.radoslavhusar.tapas.ejb.session.TraitFacadeLocal;
import com.radoslavhusar.tapas.ejb.solver.SolverFacadeLocal;
import com.radoslavhusar.tapas.ejb.stats.ProjectStats;
import com.radoslavhusar.tapas.ejb.stats.ResourcePhaseStatsEntry;
import com.radoslavhusar.tapas.ejb.stats.TaskAllocationPlanMeta;
import com.radoslavhusar.tapas.war.shared.services.TaskResourceService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import net.sf.gilead.configuration.ConfigurationHelper;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.core.hibernate.jpa.HibernateJpaUtil;
import org.jboss.logging.Logger;

public class TaskResourceServiceImpl extends PersistentRemoteService implements TaskResourceService {

   private static final long serialVersionUID = 1L;
   // Using jboss Logger via log4j
   private static final Logger log = Logger.getLogger(TaskResourceServiceImpl.class.getName());
   // Workaround for injection https://issues.jboss.org/browse/JBAS-5646
   @EJB
   private TaskFacadeLocal taskBean;
   @EJB
   private ProjectFacadeLocal projectBean;
   @EJB
   private ProjectPhaseFacadeLocal phaseBean;
   @EJB
   private ResourceFacadeLocal resourceBean;
   @EJB
   private TimeAllocationFacadeLocal timeAllocBean;
   @EJB
   private ResourceAllocationFacadeLocal allocBean;
   @EJB
   private ResourceGroupFacadeLocal groupBean;
   @EJB
   private TraitFacadeLocal traitBean;
   @EJB
   private SolverFacadeLocal solverBean;

   public TaskResourceServiceImpl() {
      EntityManagerFactory emf = null;
      try {
         InitialContext ic = new InitialContext();
         emf = (EntityManagerFactory) ic.lookup("java:/ManagerFactory");
      } catch (NamingException ex) {
         log.error("Problem getting ManagerFactory for GILEAD: " + ex);
      }

      HibernateJpaUtil gileadHibernateUtil = new HibernateJpaUtil(emf);
      setBeanManager(ConfigurationHelper.initStatefulBeanManager(gileadHibernateUtil));
   }

   @Override
   public void editProject(Project project) {
      // Persist the phases
      for (ProjectPhase p : project.getPhases()) {
         if (p.getId() == null) {
            phaseBean.create(p);
         } else {
            phaseBean.edit(p);
         }
      }
      projectBean.edit(project);
   }

   @Override
   public List<Project> findAllProjects() {
      return projectBean.findAll();
   }

   @Override
   public ProjectStats tallyProjectStats(long projectId) {
      return projectBean.tallyProjectStats(projectId);
   }

   @Override
   public List<ResourceGroup> findAllGroups() {
      return groupBean.findAll();
   }

   @Override
   public Map<Long, ResourceAllocationStatsEntry> tallyResourceStatsForProject(long projectId) {
      List<Resource> list = resourceBean.findAllForProject(projectId);
      Map<Long, ResourceAllocationStatsEntry> result = new HashMap<Long, ResourceAllocationStatsEntry>();

      // Make it better presentable
      for (Resource res : list) {
         result.put(res.getId(), resourceBean.tallyResourceStatsForProject(res.getId(), projectId));
      }

      return result;
   }

   @Override
   public List<Resource> findAllResourcesForProject(long projectId) {
      return resourceBean.findAllForProject(projectId);
   }

   @Override
   public void editResourcesForProject(long projectId, Collection<Resource> resources) {
      for (Resource res : resources) {
         if (res.getId() == null) {
            // Hm should I do this before that? res.setResourceAllocations(null);

            // its a new resource, persist it
            resourceBean.create(res);
            // now persist the NEW allocation
            for (ResourceAllocation rpa : res.getResourceAllocations()) {
               if (rpa.getPercent() > 0) {
                  // save only if allocated to more than 0%
                  allocBean.create(rpa);
               }
            }
         } else {
            // resource is not new, save changes
            // allocations changed?
            for (ResourceAllocation rpa : res.getResourceAllocations()) {
               if (rpa.getPercent() <= 0) {
                  // remove if allocated to 0%
                  allocBean.remove(rpa);
               } else {
                  // update it
                  allocBean.edit(rpa);
               }
            }
            // secondly - so that references are already persisted
            res.setResourceAllocations(null);
            resourceBean.edit(res);
         }
      }
   }

   @Override
   public Task editTask(Task task) {
      if (task.getId() == null) {
         // its is new, persist it
         taskBean.create(task);

         // now persist the NEW time alloc
         for (TimeAllocation ta : task.getTimeAllocations()) {
            if (ta.getAllocated() > 0) {
               // dont save if allocated to 0%
               timeAllocBean.create(ta);
            }
         }
      } else {
         // it is not new, only merge in changes
         // allocations changed?
         Iterator<TimeAllocation> iterator = task.getTimeAllocations().iterator();
         while (iterator.hasNext()) {
            TimeAllocation ta = iterator.next();

            if (ta.getAllocated() > 0) {
               timeAllocBean.editOrCreate(ta);
            } else {
               // also remove from collection
               iterator.remove();

               // remove if its zero
               timeAllocBean.remove(ta);
            }
         }

         // We want to reuse the object, so this is not good to do:
         // task.setTimeAllocations(null);

         taskBean.edit(task);
      }
      // Return updated task and its references

      return task;
   }

   /*@Override
   @Deprecated
   public void editTasks(Collection<Task> tasks) {
   for (Task t : tasks) {
   if (t.getId() == null) {
   // its is new, persist it
   taskBean.create(t);
   // now persist the NEW time alloc
   for (TimeAllocation ta : t.getTimeAllocations()) {
   if (ta.getAllocated() > 0) {
   // dont save if allocated to 0%
   timeAllocBean.create(ta);
   }
   }
   } else {
   // it is not new, only merge changes
   // allocations changed?
   for (TimeAllocation ta : t.getTimeAllocations()) {
   if (ta.getAllocated() > 0) {
   timeAllocBean.edit(ta);
   } else {
   // remove if its zero
   timeAllocBean.remove(ta);
   }
   }
   // secondly - so that references are already persisted
   t.setTimeAllocations(null);
   taskBean.edit(t);
   }
   }
   }*/
   @Override
   public List<Task> findAllTasksForProject(long projectId) {
      return taskBean.findAllForProject(projectId);
   }

   @Override
   public Project findProject(long projectId) {
      return projectBean.find(projectId);
   }

   @Override
   public List<Trait> findAllTraits() {
      return traitBean.findAll();
   }

   @Override
   public void createTrait(Trait trait) {
      traitBean.create(trait);
   }

   @Override
   public List<Resource> findAllResourcesNotOnProject(long projectId) {
      return resourceBean.findAllNotOnProject(projectId);
   }

   @Override
   public Long createProject(Project project) {
      projectBean.create(project);

      // As persisted.
      return project.getId();
   }
   /*
   @Override
   public List<ResourcePhaseStatsEntry> tallyResourcesStatsForPhase(long phaseId) {
   return resourceBean.tallyResourcesStatsForPhase(phaseId);
   }
    */

   @Override
   public Map<Long, List<ResourcePhaseStatsEntry>> tallyResourcePhaseStatsForProject(long projectId) {
      Project p = projectBean.find(projectId);
      System.out.println(p);
      Map<Long, List<ResourcePhaseStatsEntry>> prs = new HashMap<Long, List<ResourcePhaseStatsEntry>>();

      for (ProjectPhase pp : p.getPhases()) {
         prs.put(pp.getId(), resourceBean.tallyResourcesStatsForPhase(pp.getId()));
      }

      return prs;
   }

   @Override
   public TaskAllocationPlanMeta predictAllocation(long projectId) {
      return solverBean.solveAssignments(projectId);
   }
}

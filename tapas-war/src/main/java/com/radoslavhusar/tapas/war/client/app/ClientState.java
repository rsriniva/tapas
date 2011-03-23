package com.radoslavhusar.tapas.war.client.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.radoslavhusar.tapas.ejb.entity.Project;
import com.radoslavhusar.tapas.ejb.entity.Resource;
import com.radoslavhusar.tapas.ejb.entity.ResourceGroup;
import com.radoslavhusar.tapas.ejb.entity.ResourceAllocation;
import com.radoslavhusar.tapas.war.client.tasks.TasksPlace;
import com.radoslavhusar.tapas.war.shared.services.TaskResourceServiceAsync;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Singleton which controls state and cache of the application.
 */
@Singleton
public class ClientState {

   private Map<Resource, Double[]> resources;
   private Project project;
   private List<ResourceAllocation> resourceAllocations = new ArrayList();
   private List<ResourceGroup> groups = new ArrayList();
   private volatile int toSync = 3;

   @Inject
   public ClientState(TaskResourceServiceAsync res) {

      res.findAllProjects(new AsyncCallback<List<Project>>() {

         @Override
         public void onFailure(Throwable caught) {
            GWT.log("ClientState error fetching Project.");
            toSync = 0;
         }

         @Override
         public void onSuccess(List<Project> result) {
            project = result.get(0);

            /*Application.getInjector().getService().findAllResourceStatsForProject(project.getId(), new AsyncCallback<Map<Resource, Double[]>>() {

               @Override
               public void onFailure(Throwable caught) {
                  throw new UnsupportedOperationException("Not supported yet.");
               }

               @Override
               public void onSuccess(Map<Resource, Double[]> result) {
                  setResources(result);
                  doSync();
               }
            });*/

            doSync();
         }
      });

      res.findAllGroups(new AsyncCallback<List<ResourceGroup>>() {

         @Override
         public void onFailure(Throwable caught) {
            GWT.log("ClientState error fetching ResourceGroup.");
            toSync = 0;
         }

         @Override
         public void onSuccess(List<ResourceGroup> result) {
            groups = result;
            doSync();
         }
      });


   }

   public void doSync() {
      toSync--;
      GWT.log("Initial loading services to synchronize remaining: " + toSync);

      if (toSync == 0) {
         Application.getInjector().getPlaceController().goTo(new TasksPlace());
      }
   }

   public Project getProject() {
      return project;
   }

   public void setProject(Project project) {
      this.project = project;

      resourceAllocations = new ArrayList();
   }

   @SuppressWarnings("ReturnOfCollectionOrArrayField")
   public List<ResourceAllocation> getResourceAllocations() {
      return resourceAllocations;
   }

   public void setResourceAllocations(List<ResourceAllocation> resourceAllocations) {
      this.resourceAllocations = resourceAllocations;
   }

   public Map<Resource, Double[]> getResources() {
      return resources;
   }

   public void setResources(Map<Resource, Double[]> resorces) {
      this.resources = resorces;
   }

   @SuppressWarnings("ReturnOfCollectionOrArrayField")
   public List<ResourceGroup> getGroups() {
      return groups;
   }

   public void setGroups(List<ResourceGroup> groups) {
      this.groups = groups;
   }
}

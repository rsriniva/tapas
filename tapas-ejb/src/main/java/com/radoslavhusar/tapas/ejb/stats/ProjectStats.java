package com.radoslavhusar.tapas.ejb.stats;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ProjectStats implements Serializable {

   private long projectId;
   private double remaining;
   // Public structure actually
   // TODO: how to sort?
   private Map<Long, ProjectPhaseStats> projection = new HashMap<Long, ProjectPhaseStats>();
   private double mandayRate;

   public ProjectStats() {
   }

   public ProjectStats(long projectId) {
      this.projectId = projectId;
   }

   public Map<Long, ProjectPhaseStats> getProjection() {
      return projection;
   }

   /* public void setProjectionEntry(Long phaseId, ProjectPhaseStats data) {
   projection.put(phaseId, data);
   }*/
   public long getProjectId() {
      return projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public double getRemaining() {
      return remaining;
   }

   public void setRemaining(double remaining) {
      this.remaining = remaining;
   }

   public double getMandayRate() {
      return mandayRate;
   }

   public void setMandayRate(double mandayRate) {
      this.mandayRate = mandayRate;
   }
}

package com.radoslavhusar.tapas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TIME_ALLOCATION")
public class TimeAllocation implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @Column
   private long timeAllocationId;

   public long getTimeAllocationId() {
      return timeAllocationId;
   }

   public void setTimeAllocationId(long timeAllocationId) {
      this.timeAllocationId = timeAllocationId;
   }
}
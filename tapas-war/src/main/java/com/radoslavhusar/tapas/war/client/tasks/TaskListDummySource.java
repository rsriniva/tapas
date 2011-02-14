/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.radoslavhusar.tapas.war.client.tasks;

import com.radoslavhusar.tapas.ejb.entity.Task;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author <a href="mailto:rhusar@redhat.com">Radoslav Husar</a>
 */
public class TaskListDummySource {

   public static List<Task> fetch() {

      List set = new LinkedList<Task>();

      int i = 30;
      set.add(new Task(1, "Implement somehting", 1));
      set.add(new Task(2, "Do somthing else", 4));
      set.add(new Task(4, "Ignore erros", 16));
      set.add(new Task(4 + i++, "Ignore erros", 16));
      set.add(new Task(4 + i++, "Ignore erros", 16));
      set.add(new Task(4 + i++, "Ignore erros", 16));
      set.add(new Task(4 + i++, "Ignore erros", 16));

      return set;
   }

   private TaskListDummySource() {
   }
}

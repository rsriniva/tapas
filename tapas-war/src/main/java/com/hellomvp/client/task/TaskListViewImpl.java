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
package com.hellomvp.client.task;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.radoslavhusar.tapas.ejb.entity.Task;

/**
 *
 * @author <a href="mailto:rhusar@redhat.com">Radoslav Husar</a>
 */
public class TaskListViewImpl extends ResizeComposite implements TaskListView {

   private Presenter presenter;
   private static Binder binder = GWT.create(Binder.class);

   interface Binder extends UiBinder<Widget, TaskListViewImpl> {
   }
   @UiField
   DockLayoutPanel panell;
   @UiField
   FlexTable header;
   @UiField
   FlexTable table;

   @SuppressWarnings("LeakingThisInConstructor")
   public TaskListViewImpl() {
      initWidget(binder.createAndBindUi(this));

      header.setText(0, 0, "Task ID");
      header.setText(0, 1, "Text");
      header.setText(0, 2, "Some Integer");

      int i = 0;
      for (Task t : TaskListDummySource.fetch()) {
         //table.insertRow(0);
         table.setText(i, 0, "" + t.getTaskId());
         table.setText(i, 1, t.getSummary());
         table.setText(i, 2, String.valueOf(t.getSomeinteger()));
         i++;
      }
   }

   @Override
   public void setPresenter(Presenter presenter) {
      this.presenter = presenter;
   }
}
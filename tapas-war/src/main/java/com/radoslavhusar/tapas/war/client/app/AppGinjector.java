package com.radoslavhusar.tapas.war.client.app;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.radoslavhusar.tapas.war.client.ui.MenuActivity;
import com.radoslavhusar.tapas.war.client.ui.MenuViewImpl;
import com.radoslavhusar.tapas.war.client.ui.StatusViewImpl;
import com.radoslavhusar.tapas.war.client.tasks.TasksActivity;
import com.radoslavhusar.tapas.war.client.tasks.TasksViewImpl;
import com.radoslavhusar.tapas.war.client.task.edit.TaskEditViewImpl;
import com.radoslavhusar.tapas.war.shared.services.MyResourceServiceAsync;

@GinModules(GinClientModule.class)
public interface AppGinjector extends Ginjector {

   EventBus getEventBus();

   PlaceControllerGin getPlaceController();

//   MyResourceService getMyResourceService();
//   MyResourceServiceAsync getMyResourceServiceAsync();
   MenuActivity getMenuActivity();

   MenuViewImpl getMenuView();

   StatusViewImpl getStatusView();

   TasksActivity getTaskListActivity();

   TasksViewImpl getTaskListView();

//   TaskEditActivity getTaskEditActivity();
   TaskEditViewImpl getTaskEditView();

   MyResourceServiceAsync getMyResourceService();
}

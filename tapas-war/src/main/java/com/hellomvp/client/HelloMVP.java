package com.hellomvp.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.hellomvp.client.mvp.AppActivityMapper;
import com.hellomvp.client.mvp.AppPlaceHistoryMapper;
import com.hellomvp.client.task.TaskListPlace;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HelloMVP implements EntryPoint {

   private Place defaultPlace = new TaskListPlace(); // new HelloPlace("World!"); //new DefaultPlace(); //
   //private SimplePanel appWidget = new ScrollPanel();
   private SimplePanel appWidget = new SimplePanel();
//   private ComplexPanel appWidget = new DockLayoutPanel(Unit.EM);

   /**
    * This is the entry point method.
    */
   @Override
   public void onModuleLoad() {
      // Create ClientFactory using deferred binding so we can replace with different
      // impls in gwt.xml
      ClientFactory clientFactory = new ClientFactoryImpl(); //GWT.create(ClientFactory.class);
      EventBus eventBus = clientFactory.getEventBus();
      PlaceController placeController = clientFactory.getPlaceController();

      // Start ActivityManager for the main widget with our ActivityMapper
      ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
      ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
//      appWidget.setHeight("1000px");
//      appWidget.setWidth("1000px");
      activityManager.setDisplay(appWidget);

      // Start PlaceHistoryHandler with our PlaceHistoryMapper
      AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
      PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
      historyHandler.register(placeController, eventBus, defaultPlace);

      Window.enableScrolling(false);
      Window.setMargin("0px");

//      RootPanel.get().add(appWidget);
      appWidget.setHeight("100%");
      appWidget.setWidth("100%");
//appWidget.removeStyleName(null)
//      appWidget.setTitle("THIS IS IT");
      RootLayoutPanel.get().add(appWidget);

      // Goes to place represented on URL or default place
      historyHandler.handleCurrentHistory();
   }
}
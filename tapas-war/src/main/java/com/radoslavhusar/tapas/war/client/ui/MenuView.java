package com.radoslavhusar.tapas.war.client.ui;

import com.google.gwt.user.client.ui.IsWidget;

public interface MenuView extends IsWidget {

   void setPresenter(Presenter presenter);

   public interface Presenter {

      void doAbout();
   }
}

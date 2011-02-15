package com.radoslavhusar.tapas.war.client.app;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;
import com.radoslavhusar.tapas.war.client.tasks.TaskListPlace;
import com.radoslavhusar.tapas.war.client.task.edit.TaskEditPlace;

/**
 * PlaceHistoryMapper interface is used to attach all places which the
 * PlaceHistoryHandler should be aware of. This is done via the @WithTokenizers
 * annotation or by extending PlaceHistoryMapperWithFactory and creating a
 * separate TokenizerFactory.
 */
@WithTokenizers({
   TaskListPlace.Tokenizer.class,
   TaskEditPlace.Tokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
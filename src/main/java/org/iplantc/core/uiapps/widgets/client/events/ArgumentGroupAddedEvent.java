package org.iplantc.core.uiapps.widgets.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupAddedEvent.ArgumentGroupAddedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm.ArgumentGroupEditor;

public class ArgumentGroupAddedEvent extends GwtEvent<ArgumentGroupAddedEventHandler> {

    public interface ArgumentGroupAddedEventHandler extends EventHandler {
        void onArgumentGroupAdded(ArgumentGroupAddedEvent event);
    }

    public static interface HasArgumentGroupAddedEventHandlers {
        HandlerRegistration addArgumentGroupAddedEventHandler(ArgumentGroupAddedEventHandler handler);
    }

    public static final GwtEvent.Type<ArgumentGroupAddedEventHandler> TYPE = new GwtEvent.Type<ArgumentGroupAddedEvent.ArgumentGroupAddedEventHandler>();
    private final ArgumentGroupEditor argGrpEditor;
    private final ArgumentGroup argumentGroup;

    public ArgumentGroupAddedEvent(ArgumentGroup argumentGroup, ArgumentGroupEditor argGrpEditor) {
        this.argumentGroup = argumentGroup;
        this.argGrpEditor = argGrpEditor;
    }

    public ArgumentGroup getArgumentGroup() {
        return argumentGroup;
    }

    public ArgumentGroupEditor getArgumentGroupEditor() {
        return argGrpEditor;
    }

    @Override
    public GwtEvent.Type<ArgumentGroupAddedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ArgumentGroupAddedEventHandler handler) {
        handler.onArgumentGroupAdded(this);
    }
}

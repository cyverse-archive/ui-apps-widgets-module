package org.iplantc.core.uiapps.widgets.client.dnd;

import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

/**
 * This is a simple {@link DragSource} class which automatically sets a designated
 * <code>ArgumentType</code> to the Drag start event's data.
 * 
 * @author jstroot
 * 
 */
public class AppArgumentTypeDragSource extends DragSource {

    private final ArgumentType argumentType;

    public AppArgumentTypeDragSource(Widget widget, ArgumentType argumentType) {
        super(widget);
        this.argumentType = argumentType;
    }

    @Override
    protected void onDragStart(DndDragStartEvent event) {
        event.setData(argumentType);
    }
}

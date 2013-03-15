package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

/**
 * This class implements <code>HasWidgets</code> in order to support manual validation of view via
 * {@link FormPanelHelper#isValid(HasWidgets)}
 * 
 * @author jstroot
 * 
 */
class ArgumentListEditor extends Composite implements HasWidgets, IsEditor<ListEditor<Argument, ArgumentEditor>> {

    private class PropertyListEditorSource extends EditorSource<ArgumentEditor> {
        private static final int DEF_ARGUMENT_MARGIN = 10;
        private final VerticalLayoutContainer con;
        private final EventBus eventBus;

        public PropertyListEditorSource(VerticalLayoutContainer con, EventBus eventBus) {
            this.con = con;
            this.eventBus = eventBus;
        }

        @Override
        public ArgumentEditor create(int index) {
            ArgumentEditor subEditor = new ArgumentEditor(eventBus);
            con.add(subEditor, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));
            return subEditor;
        }
        
        @Override
        public void dispose(ArgumentEditor subEditor) {
            subEditor.removeFromParent();
        }
        
        @Override
        public void setIndex(ArgumentEditor editor, int index) {
            con.insert(editor, index, new VerticalLayoutData(1, -1, new Margins(DEF_ARGUMENT_MARGIN)));
        }
    }

    private final VerticalLayoutContainer argumentsContainer;

    private final ListEditor<Argument, ArgumentEditor> editor;

    public ArgumentListEditor(final EventBus eventBus) {
        argumentsContainer = new VerticalLayoutContainer();
        initWidget(argumentsContainer);
        argumentsContainer.setAdjustForScroll(true);
        argumentsContainer.setScrollMode(ScrollMode.AUTOY);
        editor = ListEditor.of(new PropertyListEditorSource(argumentsContainer, eventBus));
    }

    @Override
    public ListEditor<Argument, ArgumentEditor> asEditor() {
        return editor;
    }

    @Override
    public void add(Widget w) {
        argumentsContainer.add(w);
    }

    @Override
    public void clear() {
        argumentsContainer.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return argumentsContainer.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return argumentsContainer.remove(w);
    }

}

package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

/**
 * @author jstroot
 * 
 */
class ArgumentListEditor extends Composite implements IsEditor<ListEditor<Argument, ArgumentEditor>> {

    private class PropertyListEditorSource extends EditorSource<ArgumentEditor> {
        private static final int DEF_ARGUMENT_MARGIN = 10;
        private final VerticalLayoutContainer con;
        private final EventBus eventBus;
        private final AppTemplateWizardPresenter presenter;

        public PropertyListEditorSource(final VerticalLayoutContainer con, final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
            this.con = con;
            this.eventBus = eventBus;
            this.presenter = presenter;
        }

        @Override
        public ArgumentEditor create(int index) {
            ArgumentEditor subEditor = new ArgumentEditor(eventBus, presenter);
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

    public ArgumentListEditor(final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
        argumentsContainer = new VerticalLayoutContainer();
        initWidget(argumentsContainer);
        argumentsContainer.setAdjustForScroll(true);
        argumentsContainer.setScrollMode(ScrollMode.AUTOY);
        editor = ListEditor.of(new PropertyListEditorSource(argumentsContainer, eventBus, presenter));
    }

    @Override
    public ListEditor<Argument, ArgumentEditor> asEditor() {
        return editor;
    }

}

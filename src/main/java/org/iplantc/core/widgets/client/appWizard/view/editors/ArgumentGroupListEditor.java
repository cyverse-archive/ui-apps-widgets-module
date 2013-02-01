package org.iplantc.core.widgets.client.appWizard.view.editors;

import org.iplantc.core.widgets.client.appWizard.models.ArgumentGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;

/**
 * @author jstroot
 * 
 */
public class ArgumentGroupListEditor extends Composite implements IsEditor<ListEditor<ArgumentGroup, ArgumentGroupEditor>>{

    interface GroupListEditorUiBinder extends UiBinder<AccordionLayoutContainer, ArgumentGroupListEditor> {
    }

    private static GroupListEditorUiBinder BINDER = GWT.create(GroupListEditorUiBinder.class);

    private class ArgumentGroupEditorSource extends EditorSource<ArgumentGroupEditor> {

        private final AccordionLayoutContainer con;

        public ArgumentGroupEditorSource(AccordionLayoutContainer con) {
            this.con = con;
        }

        @Override
        public ArgumentGroupEditor create(int index) {
            ArgumentGroupEditor subEditor = new ArgumentGroupEditor();
            con.insert(subEditor, index);
            if (index == 0) {
                // Ensure that the first container is expanded automatically
                con.setActiveWidget(subEditor.asWidget());
            }
            return subEditor;
        }
        
        @Override
        public void dispose(ArgumentGroupEditor subEditor){
            subEditor.asWidget().removeFromParent();
        }
        
        @Override
        public void setIndex(ArgumentGroupEditor editor, int index){
            con.insert(editor, index);
        }

    }
    
    @Ignore
    @UiField
    AccordionLayoutContainer groupsContainer;

    private final ListEditor<ArgumentGroup, ArgumentGroupEditor> editor;

    public ArgumentGroupListEditor() {
        initWidget(BINDER.createAndBindUi(this));
        editor = ListEditor.of(new ArgumentGroupEditorSource(groupsContainer));
    }

    @Override
    public ListEditor<ArgumentGroup, ArgumentGroupEditor> asEditor() {
        return editor;
    }
}

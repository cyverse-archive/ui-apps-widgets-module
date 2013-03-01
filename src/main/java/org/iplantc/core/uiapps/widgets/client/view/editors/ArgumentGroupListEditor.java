package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

/**
 * An editor class for displaying the argument group list in an {@link AppTemplate#getArgumentGroups()}.
 * This class implements <code>HasWidgets</code> in order to support manual validation of view via
 * {@link FormPanelHelper#isValid(HasWidgets)}
 * 
 * @author jstroot
 * 
 */
public class ArgumentGroupListEditor extends Composite implements IsEditor<ListEditor<ArgumentGroup, ArgumentGroupEditor>>, HasWidgets {

    interface GroupListEditorUiBinder extends UiBinder<Widget, ArgumentGroupListEditor> {}

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

    @Override
    public void add(Widget w) {
        groupsContainer.add(w);
    }

    @Override
    public void clear() {
        groupsContainer.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return groupsContainer.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return groupsContainer.remove(w);
    }
}

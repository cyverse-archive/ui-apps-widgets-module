package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface AppWizardView extends IsWidget, Editor<AppTemplate> {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter{
    
        /**
         * KLUDGE JDS If the server JSON structure can be corrected, this can go away.
         * 
         * @param json
         */
        void setAppTemplateFromJsonString(String json);
    
        AppTemplate getAppTemplate();
    }

    /**
     * @return the <code>EditorDriver</code> associated with this editor. It is expected that
     *         implementing classes will initialize this driver at construction time.
     */
    SimpleBeanEditorDriver<AppTemplate, ? extends AppWizardView> getEditorDriver();

    void setPresenter(AppWizardView.Presenter presenter);
}

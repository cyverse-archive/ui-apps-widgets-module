package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;


public interface AppWizardView extends Editor<AppTemplate>, IsWidget{
    
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter{

        void setAppTemplateFromJsonString(String json);

        AppTemplate getAppTemplate();
        
    }

}

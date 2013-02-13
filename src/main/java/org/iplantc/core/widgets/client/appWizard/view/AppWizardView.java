package org.iplantc.core.widgets.client.appWizard.view;

import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * <h1>Requirements</h1>
 * <ul>
 * <li>Need to be able bring view up with designated values (e.g. values from a previous run)</li>
 * <li></li>
 * <li></li>
 * </ul>
 * <h2>Properties</h2>
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * <h2>Data Model</h2>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * @author jstroot
 * 
 */
public interface AppWizardView extends IsWidget, Editor<AppTemplate> {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter{
    
        /**
         * KLUDGE JDS If the server JSON structure can be corrected, this can go away.
         * 
         * @param json
         */
        void setAppTemplateFromLegacyJson(final Splittable legacyJson);
    
        AppTemplate getAppTemplate();

        void goLegacy(final HasOneWidget container, final Splittable legacyJson);

        void go(final HasOneWidget container, final AppTemplate appTemplate);
    }

    /**
     * @return the <code>EditorDriver</code> associated with this editor. It is expected that
     *         implementing classes will initialize this driver at construction time.
     */
    SimpleBeanEditorDriver<AppTemplate, ? extends AppWizardView> getEditorDriver();

    void setPresenter(AppWizardView.Presenter presenter);
}

package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import java.util.List;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.resources.client.uiapps.widgets.ArgumentListEditorCss;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.AppTemplatePropertyEditor;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.widget.core.client.button.IconButton;

/**
 * TODO JDS Move {@link ArgumentListEditorCss} up here.
 * 
 * @author jstroot
 * 
 */
public interface AppTemplateWizardAppearance {

    interface AppTemplateWizardTemplates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<span style='color: red;'>*&nbsp</span>")
        SafeHtml fieldLabelRequired();

        @SafeHtmlTemplates.Template("<span style='color: red;'>{0}</span>")
        SafeHtml redText(String text);

        @SafeHtmlTemplates.Template("<span style='color: red;float: right;'>{0}</span>")
        SafeHtml redTextFloatRight(String text);

        @SafeHtmlTemplates.Template("<span qtip='{1}'>{0}</span>")
        SafeHtml fieldLabel(SafeHtml name, String textToolTip);

        @SafeHtmlTemplates.Template("<p style='text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'>{0}</p>")
        SafeHtml contentPanelHeader(SafeHtml label);

        @SafeHtmlTemplates.Template("<p style='text-overflow: ellipsis;overflow: hidden;white-space: nowrap;'><span style='color: red;'>*&nbsp</span>{0}</p>")
        SafeHtml contentPanelHeaderRequired(SafeHtml label);

        @SafeHtmlTemplates.Template("{0}<img style='float: right;' src='{1}' qtip='{2}'></img>")
        SafeHtml fieldLabelImgFloatRight(SafeHtml label, SafeUri img, String toolTip);

        @SafeHtmlTemplates.Template("{0}&nbsp;<img src='{1}' qtip='{2}'></img>")
        SafeHtml fieldLabelImg(SafeHtml label, SafeUri img, String toolTip);
    }

    interface Style extends CssResource {
        /**
         * FIXME JDS This style should be applied in the
         * 
         * @return
         */
        // String appHeader();

        String appHeaderSelect();

        String argument();

        String argumentSelect();

        String deleteHover();

        String delete();

        String deleteBtn();

        String emptyGroupBgText();

        String grab();

    }

    interface Resources extends IplantResources {
        @Source("AppTemplateWizard.css")
        Style css();
    }

    /**
     * @return 400
     */
    int getAutoExpandOnHoverDelay();

    int getAutoScrollDelay();

    int getAutoScrollRegionHeight();

    int getAutoScrollRepeatDelay();

    /**
     * @return 200
     */
    int getDefaultArgListHeight();

    /**
     * IconButton argDeleteBtn = new IconButton(new IconConfig(res.argumentListEditorCss().delete(),
     * res.argumentListEditorCss().deleteHover()));
     * 
     * @return
     */
    IconButton getArgListDeleteButton();

    /**
     * headerErrorIcon = Document.get().createImageElement();
     * 
     * headerErrorIcon.setSrc(IplantResources.RESOURCES.exclamation().getSafeUri().asString());
     * 
     * @return returns a freshly constructed Error ImageElement.
     */
    ImageElement getErrorIconImg();

    /**
     * 
     * @param errors list of <code>EditorError</code>s whose messages will be used in the returned
     *            <code>ImageElement</code>'s "qtip" attribute.
     * @return an <code>ImageElement</code> with a "qtip" attribute populated with the messages of the
     *         given errors.
     */
    ImageElement getErrorIconImgWithErrQTip(List<EditorError> errors);

    AppTemplateWizardTemplates getTemplates();

    Style getStyle();

    /**
     * @return default height for the tree selection widget.
     */
    int getDefaultTreeSelectionHeight();

    SafeHtml createArgumentLabel(Argument model);

    SafeHtml createContextualHelpLabel(String label, String toolTip);

    SafeHtml createContextualHelpLabelNoFloat(String label, String toolTip);

    AppsWidgetsPropertyPanelLabels getPropertyPanelLabels();

    AppsWidgetsContextualHelpMessages getContextHelpMessages();

    /**
     * @param label
     * @return a formatted label for the ArgumentGroup and AppTemplate ContentPanel headers.
     */
    SafeHtml createContentPanelHeaderLabel(SafeHtml label, boolean required);

    /**
     * @return the text used to indicate that an app template has no tool during edit mode.
     */
    SafeHtml createEmptyToolText();

    /**
     * @return the character limit which is applied to the <code>AppTemplate</code> <i>name</i> field in
     *         the {@link AppTemplatePropertyEditor}.
     */
    int getAppNameCharLimit();
}

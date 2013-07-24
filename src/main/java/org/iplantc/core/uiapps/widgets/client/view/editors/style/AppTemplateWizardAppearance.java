package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.resources.client.uiapps.widgets.ArgumentListEditorCss;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.button.IconButton;

/**
 * TODO JDS Move {@link ArgumentListEditorCss} up here.
 * 
 * @author jstroot
 * 
 */
public interface AppTemplateWizardAppearance {

    interface AppTemplateWizardTemplates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("<span style=\"color: red;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();

        @SafeHtmlTemplates.Template("<span style=\"color: red;\">{0}</span>")
        SafeHtml redText(String text);

        @SafeHtmlTemplates.Template("<span qtip=\"{1}\">{0}</span>")
        SafeHtml fieldLabel(SafeHtml name, String textToolTip);
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

    }

    interface Resources extends IplantResources {
        @Source("AppTemplateWizard.css")
        Style css();
    }

    interface EmptyMessages {

        /**
         * @return the text to be displayed inside an empty selection item.
         */
        String emptyListSelectionText();

        /**
         * 
         * @return "[NO TOOL SELECTED]"
         */
        String emptyToolText();

    }

    interface DefaultLabelMessages {

        /**
         * @param grpNum
         * @return "Group {0}"
         */
        String defaultGroupLabel(int grpNum);
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

    AppTemplateWizardTemplates getTemplates();

    AppTemplateWizardDisplayStrings getMessages();

    // ContentPanelHoverHeaderSelectionAppearance getContentPanelEditingAppearance();

    Style getStyle();

    /**
     * @return 200
     */
    int getDefaultTreeSelectionHeight();
}

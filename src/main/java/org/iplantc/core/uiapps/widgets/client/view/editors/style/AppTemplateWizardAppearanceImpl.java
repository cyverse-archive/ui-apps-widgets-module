package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.util.IPlantSimpleHtmlSanitizer;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

/**
 * FIXME JDS Extract hardcoded values to a Constants interface.
 * 
 * @author jstroot
 * 
 */
public class AppTemplateWizardAppearanceImpl implements AppTemplateWizardAppearance {

    private final Resources res;
    private final AppTemplateWizardTemplates templates;
    private final AppsWidgetsPropertyPanelLabels labels;
    private final AppsWidgetsContextualHelpMessages help;

    public AppTemplateWizardAppearanceImpl() {
        res = GWT.create(Resources.class);
        templates = GWT.create(AppTemplateWizardTemplates.class);
        labels = I18N.APPS_LABELS;
        help = I18N.APPS_HELP;
    }

    @Override
    public int getAutoExpandOnHoverDelay() {
        return 500;
    }

    @Override
    public int getAutoScrollDelay() {
        return 200;
    }

    @Override
    public int getAutoScrollRegionHeight() {
        return 5;
    }

    @Override
    public int getAutoScrollRepeatDelay() {
        return 50;
    }

    @Override
    public int getDefaultArgListHeight() {
        return 200;
    }

    @Override
    public IconButton getArgListDeleteButton() {
        IconButton argDeleteBtn = new IconButton(new IconConfig(res.css().delete(), res.css().deleteHover()));
        argDeleteBtn.addStyleName(res.css().deleteBtn());
        return argDeleteBtn;
    }

    @Override
    public ImageElement getErrorIconImg() {
        ImageElement errIconImg = Document.get().createImageElement();
        errIconImg.setSrc(res.exclamation().getSafeUri().asString());
        errIconImg.getStyle().setFloat(Float.LEFT);
        return errIconImg;
    }

    @Override
    public ImageElement getErrorIconImgWithErrQTip(List<EditorError> errors) {
        ImageElement errIconImg = getErrorIconImg();
        String errorString = "";
        for (EditorError err : errors) {
            if (err instanceof HasLabel) {
                errorString += ((HasLabel)err).getLabel() + ": ";
            }
            errorString += err.getMessage();
            if (errors.indexOf(err) != errors.size() - 1) {
                errorString += "<br>";
            }
        }
        errIconImg.setAttribute("qtip", errorString);
        return errIconImg;
    }

    @Override
    public AppTemplateWizardTemplates getTemplates() {
        return templates;
    }

    @Override
    public Style getStyle() {
        res.css().ensureInjected();
        return res.css();
    }

    @Override
    public int getDefaultTreeSelectionHeight() {
        return 200;
    }

    @Override
    public SafeHtml createArgumentLabel(Argument model) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        if (model.getRequired()) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired());
        }
        // JDS Remove the trailing colon. The FieldLabels will apply it automatically.
        SafeHtml label = SafeHtmlUtils.fromString(model.getLabel().replaceFirst(":$", ""));
        if (model.getType().equals(ArgumentType.Info)) {
            labelText.append(IPlantSimpleHtmlSanitizer.sanitizeHtml(model.getLabel()));
        } else if (Strings.isNullOrEmpty(model.getDescription()) || ((model.getId() != null) && model.getId().equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID))) {
            labelText.append(label);
        } else {
            labelText.append(templates.fieldLabelImgFloatRight(label, res.info().getSafeUri(), model.getDescription()));
        }
        return labelText.toSafeHtml();
    }

    @Override
    public SafeHtml createContextualHelpLabel(String labelToolTipText, String propertyToolTip) {
        return templates.fieldLabelImgFloatRight(SafeHtmlUtils.fromString(labelToolTipText), res.help().getSafeUri(), propertyToolTip);
    }

    @Override
    public SafeHtml createContextualHelpLabelNoFloat(String label, String toolTip) {
        return templates.fieldLabelImg(SafeHtmlUtils.fromString(label), res.help().getSafeUri(), toolTip);
    }

    @Override
    public AppsWidgetsPropertyPanelLabels getPropertyPanelLabels() {
        return labels;
    }

    @Override
    public AppsWidgetsContextualHelpMessages getContextHelpMessages() {
        return help;
    }

    @Override
    public SafeHtml createContentPanelHeaderLabel(SafeHtml label, boolean required) {
        if (required) {
            return templates.contentPanelHeaderRequired(label);
        }
        return templates.contentPanelHeader(label);
    }

    @Override
    public int getAppNameCharLimit() {
        return 255;
    }

}

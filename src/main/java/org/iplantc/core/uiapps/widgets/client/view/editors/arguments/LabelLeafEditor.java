package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.safehtml.client.HasSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm.ArgumentEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance.AppTemplateWizardTemplates;
import org.iplantc.core.uiapps.widgets.client.view.util.IPlantSimpleHtmlSanitizer;

public class LabelLeafEditor<T> implements LeafValueEditor<T> {
    protected final ArgumentEditor argEditor;

    private final HasSafeHtml hasHtml;
    private T lleModel;

    public LabelLeafEditor(HasSafeHtml hasHtml, ArgumentEditor argEditor) {
        this.hasHtml = hasHtml;
        this.argEditor = argEditor;
    }

    @Override
    public T getValue() {
        return lleModel;
    }

    @Override
    public void setValue(T value) {
        this.lleModel = value;
        SafeHtml createArgumentLabel = createArgumentLabel(argEditor);
        hasHtml.setHTML(createArgumentLabel);
    }

    private SafeHtml createArgumentLabel(ArgumentEditor argEditor) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        AppTemplateWizardTemplates templates = AppTemplateWizardAppearance.INSTANCE.getTemplates();
        Boolean isRequired = argEditor.requiredEditor().getValue();
        if ((isRequired != null) && isRequired) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired());
        }
        // JDS Remove the trailing colon. The FieldLabels will apply it automatically.
        String label = Strings.nullToEmpty(argEditor.labelEditor().getValue());
        SafeHtml safeHtmlLabel = SafeHtmlUtils.fromString(label.replaceFirst(":$", ""));
        ArgumentType argumentType = argEditor.typeEditor().getValue();
        if ((argumentType != null) && argumentType.equals(ArgumentType.Info)) {
            labelText.append(IPlantSimpleHtmlSanitizer.sanitizeHtml(label));
        } else {
            String id = argEditor.idEditor().getValue();
            String description = argEditor.descriptionEditor().getValue();

            if (Strings.isNullOrEmpty(description) || ((id != null) && id.equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID))) {
                labelText.append(safeHtmlLabel);
            } else {
                labelText.append(templates.fieldLabelImgFloatRight(safeHtmlLabel, IplantResources.RESOURCES.info().getSafeUri(), description));
            }
        }
        return labelText.toSafeHtml();
    }

}
package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.I18N;
import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uidiskresource.client.models.DiskResource;

import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;

/**
 * Abstract class for single select DiskResource fields.
 * 
 * TODO JDS All Diskresource selectors (incl multi) need to have a "file_info_type". This will be passed
 * to the DiskResource presenter, which will filter outputs.
 * 
 * @author jstroot
 * 
 */
public abstract class AppWizardDiskResourceSelector<R extends DiskResource> extends Component implements IsField<HasId>, ValueAwareEditor<HasId> {

    interface FileFolderSelectorStyle extends CssResource {
        String buttonWrap();

        String wrap();
    }

    interface Resources extends ClientBundle {
        @Source("FileFolderSelector.css")
        FileFolderSelectorStyle style();
    }

    public interface FileUploadTemplate extends XTemplates {
        @XTemplate("<div class='{style.wrap}'></div>")
        SafeHtml render(FileFolderSelectorStyle style);
    }

    private final TextButton button;
    private final TextField input = new TextField();

    private final Resources res = GWT.create(Resources.class);
    private final FileUploadTemplate template = GWT.create(FileUploadTemplate.class);
    private final int buttonOffset = 3;
    private EditorDelegate<HasId> editorDelegate;
    private List<EditorError> errors = Lists.newArrayList();
    private R selectedResource;

    protected AppWizardDiskResourceSelector() {
        res.style().ensureInjected();
        
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.append(template.render(res.style()));
        setElement(XDOM.create(builder.toSafeHtml()));

        input.setReadOnly(true);
        getElement().appendChild(input.getElement());

        sinkEvents(Event.ONCHANGE | Event.ONCLICK | Event.MOUSEEVENTS);

        button = new TextButton(I18N.DISPLAY.browse());
        button.getElement().addClassName(res.style().buttonWrap());
        getElement().appendChild(button.getElement());
        button.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onBrowseSelected();
            }
        });
    }

    protected void setSelectedResource(R selectedResource) {
        this.selectedResource = selectedResource;
        input.setValue(selectedResource.getId());
    }

    protected R getSelectedResource() {
        return selectedResource;
    }

    @Override
    public void setValue(HasId value) {
        if (value == null)
            return;
        input.setValue(value.getId());
        findDiskResource(value);
    }

    /**
     * Convenience method which creates a HasId object from a given string id.
     * 
     * @param id
     */
    public void setValueFromStringId(String id) {
        setValue(CommonModelUtils.createHasIdFromString(id));
    }

    /**
     * Retrieves the resource with the given Id, otherwise returns null.
     * 
     * @param value the Id to be used.
     * @return the resource, if it exists. Null otherwise.
     */
    private void findDiskResource(HasId value) {

    }

    @Override
    public HasId getValue() {
        return selectedResource;
    }

    protected abstract void onBrowseSelected();

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        // Forward the event
        button.onBrowserEvent(event);
    }

    @Override
    protected void doAttachChildren() {
        super.doAttachChildren();
        ComponentHelper.doAttach(input);
        ComponentHelper.doAttach(button);
    }

    @Override
    protected void doDetachChildren() {
        super.doDetachChildren();
        ComponentHelper.doDetach(input);
        ComponentHelper.doDetach(button);
    }

    @Override
    protected XElement getFocusEl() {
        return input.getElement();
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        input.setWidth(width - button.getOffsetWidth() - buttonOffset);
    }

    @Override
    public void clear() {
        input.clear();
    }

    @Override
    public void clearInvalid() {
        input.clearInvalid();
        errors.clear();
    }

    @Override
    public void reset() {
        input.reset();
    }

    @Override
    public boolean isValid(boolean preventMark) {
        // If the input field is not valid, make a call to validate in order to 
        // propagate errors from the input field to the editor delegate.
        if (!input.isValid(preventMark)) {
            validate(preventMark);
        }
        return input.isValid(preventMark);
    }

    @Override
    public boolean validate(boolean preventMark) {
        errors = Lists.newArrayList();
        for (Validator<String> v : input.getValidators()) {
            List<EditorError> errs = v.validate(input, input.getCurrentValue());
            if (errs != null) {
                errors.addAll(errs);
            }
        }
        return input.validate(preventMark);
    }

    @Override
    public void flush() {
        // Validate on flush.
        validate(false);
        input.flush();
        // Transfer any errors we have to the EditorDelegate<Splittable>
        for (EditorError e : errors) {
            editorDelegate.recordError(e.getMessage(), e.getValue(), input);
        }
    }

    @Override
    public void onPropertyChange(String... paths) {
    }

    @Override
    public void setDelegate(EditorDelegate<HasId> delegate) {
        editorDelegate = delegate;
    }

}

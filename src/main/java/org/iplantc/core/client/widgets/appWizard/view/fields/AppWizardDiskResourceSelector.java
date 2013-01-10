package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.view.fields.converters.SplittableStringListToStringConverter;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;

import com.google.common.base.Joiner;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasText;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * XXX JDS May want to turn this into a selector of either files or folders
 * TODO JDS All Diskresource selectors (incl multi) need to have a "file_info_type". This will be passed
 * to the DiskResource presenter, which will filter outputs.
 * 
 * @author jstroot
 * 
 */
public abstract class AppWizardDiskResourceSelector extends Component implements TemplatePropertyField, LeafValueEditor<Splittable> {

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

    private final SplittableStringListToStringConverter cnvt;

    private final Resources res = GWT.create(Resources.class);
    private final FileUploadTemplate template = GWT.create(FileUploadTemplate.class);
    private final boolean fileSelector;
    private final int buttonOffset = 3;

    protected AppWizardDiskResourceSelector(boolean fileSelector) {
        this.fileSelector = fileSelector;
        res.style().ensureInjected();
        
        cnvt = new SplittableStringListToStringConverter();
        
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

    @Override
    public void initialize(TemplateProperty property) {/* Do Nothing */}

    private void onBrowseSelected() {
        if (fileSelector) {
            FileSelectDialog fileSD = FileSelectDialog.singleSelect();
            fileSD.addHideHandler(new DialogHideHandler(fileSD, input));
            fileSD.show();
        } else {
            FolderSelectDialog folderSD = new FolderSelectDialog();
            folderSD.addHideHandler(new DialogHideHandler(folderSD, input));
            folderSD.show();
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        // Forward the event
        button.onBrowserEvent(event);
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return input.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return input.addValidHandler(handler);
    }

    @Override
    public void setValue(Splittable value) {
        input.setValue(cnvt.convertModelValue(value));
    }

    @Override
    public Splittable getValue() {
        return cnvt.convertFieldValue(input.getValue());
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

    private final class DialogHideHandler implements HideHandler {
        private final TakesValue<List<String>> takesValue;
        private final HasText tf;
    
        private DialogHideHandler(TakesValue<List<String>> dlg, HasText tf) {
            this.takesValue = dlg;
            this.tf = tf;
        }
    
        @Override
        public void onHide(HideEvent event) {
            if(takesValue.getValue() == null)
                return;
            tf.setText(Joiner.on(",").join(takesValue.getValue()));
        }
    }

    @Override
    public void setToolTip(ToolTipConfig toolTip) {
        input.setToolTipConfig(toolTip);
    }

}

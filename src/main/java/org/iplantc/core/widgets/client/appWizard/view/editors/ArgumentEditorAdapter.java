package org.iplantc.core.widgets.client.appWizard.view.editors;

import java.util.List;

import org.iplantc.core.widgets.client.appWizard.models.Argument;
import org.iplantc.core.widgets.client.appWizard.models.ArgumentValidator;
import org.iplantc.core.widgets.client.appWizard.util.AppWizardFieldFactory;
import org.iplantc.core.widgets.client.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.widgets.client.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.widgets.client.appWizard.view.fields.ArgumentField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class ArgumentEditorAdapter extends Composite implements CompositeEditor<Argument, Splittable, ArgumentField>, ValueAwareEditor<Argument> {

    interface ArgumentEditorAdapterUiBinder extends UiBinder<FieldLabel, ArgumentEditorAdapter> {}

    private static ArgumentEditorAdapterUiBinder BINDER = GWT.create(ArgumentEditorAdapterUiBinder.class);
    private CompositeEditor.EditorChain<Splittable, ArgumentField> chain;

    @Ignore
    @UiField
    FieldLabel propertyLabel;
    
    private ArgumentField subEditor = null;

    public ArgumentEditorAdapter() {
        initWidget(BINDER.createAndBindUi(this));
        propertyLabel.addDomHandler(new MouseOverHandler() {

            @Override
            public void onMouseOver(MouseOverEvent event) {
                propertyLabel.setBorders(true);

            }
        }, MouseOverEvent.getType());

        propertyLabel.addDomHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                propertyLabel.setBorders(false);
            }
        }, MouseOutEvent.getType());

        propertyLabel.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                GWT.log("Click on " + propertyLabel.getText());

            }
        }, ClickEvent.getType());
    }
    

    @Override
    public void setValue(Argument value) {
        // attach it to the chain. Attach the formvalue  
        subEditor = AppWizardFieldFactory.createPropertyField(value);
        subEditor.initialize(value);
        List<ArgumentValidator> validators = value.getValidators();
        if (validators != null) {
            GWT.log("Property: " + value.getLabel() + "; " + value.getType());
            for (ArgumentValidator v : validators) {
                GWT.log("\tValidator: " + v.getType());
            }
        }
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);
        propertyLabel.setWidget(subEditor);
        if (subEditor instanceof AppWizardCheckbox) {
            propertyLabel.setHTML("");
            propertyLabel.setLabelSeparator("");
            ((AppWizardCheckbox)subEditor).setBoxLabel(fieldLabelText.asString());
        }
        
        Splittable formValue = value.getValue();
        chain.attach(formValue, subEditor);
        // Can explicitly add the detail panel to the editor chain here.

    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {}


    @Override
    public void flush() {}


    @Override
    public void onPropertyChange(String... paths) {}


    @Override
    public ArgumentField createEditorForTraversal() {
        return new AppWizardTextField();
    }


    @Override
    public String getPathElement(ArgumentField subEditor) {
        return "";
    }


    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Splittable, ArgumentField> chain) {
        this.chain = chain;
    }

}

package org.iplantc.core.uiapps.widgets.client.view.fields;

import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.CheckBox;
import com.sencha.gxt.widget.core.client.form.AdapterField;

public class CheckBoxAdapter extends AdapterField<Boolean> implements ValueAwareEditor<Boolean>, HasValueChangeHandlers<Boolean> {

    private final CheckBox cb;
    private boolean negated = false;

    public CheckBoxAdapter() {
        this(new CheckBox());
    }

    protected CheckBoxAdapter(CheckBox cb) {
        super(cb);
        this.cb = cb;
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    @Override
    public void setValue(Boolean value) {
        if (value == null) {
            cb.setValue(Boolean.FALSE);
        } else {
            boolean toSet = negated != value;
            cb.setValue(toSet);
        }
    }

    @Override
    public Boolean getValue() {
        boolean toGet = negated != cb.getValue();
        return toGet;
    }

    public void setText(String text) {
        cb.setText(text);
    }

    public CheckBox getCheckBox() {
        return cb;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return cb.addValueChangeHandler(handler);
    }

    public void setHTML(SafeHtml html) {
        cb.setHTML(html);
    }

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}

package org.iplantc.core.uiapps.widgets.client.view.fields;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.CheckBox;
import com.sencha.gxt.widget.core.client.form.AdapterField;

public class CheckBoxAdapter extends AdapterField<Boolean> implements HasValueChangeHandlers<Boolean> {

    private final CheckBox cb;

    public CheckBoxAdapter() {
        this(new CheckBox());
    }

    protected CheckBoxAdapter(CheckBox cb) {
        super(cb);
        this.cb = cb;
    }

    @Override
    public void setValue(Boolean value) {
        cb.setValue(value);
    }

    @Override
    public Boolean getValue() {
        return cb.getValue();
    }

    public void setText(String text) {
        cb.setText(text);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
        return cb.addValueChangeHandler(handler);
    }

    public void setHTML(SafeHtml html) {
        cb.setHTML(html);
    }

}

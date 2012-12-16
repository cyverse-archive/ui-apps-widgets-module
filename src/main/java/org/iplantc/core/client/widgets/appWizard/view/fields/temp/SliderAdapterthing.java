package org.iplantc.core.client.widgets.appWizard.view.fields.temp;

import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;

public class SliderAdapterthing extends ConverterEditorAdapter<Splittable, Integer, Slider> implements TemplatePropertyEditorBase {

    private static MySliderCell sliderCell = new MySliderCell();
    private final Slider slider;

    public SliderAdapterthing(Converter<Splittable, Integer> converter) {
        super(new Slider(sliderCell), converter);

        this.slider = createEditorForTraversal();
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {return null;}

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {return null;}

    @Override
    public Widget asWidget() {
        return slider;
    }

}

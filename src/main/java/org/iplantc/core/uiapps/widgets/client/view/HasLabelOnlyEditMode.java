package org.iplantc.core.uiapps.widgets.client.view;

import com.google.gwt.editor.client.Editor.Ignore;

public interface HasLabelOnlyEditMode {

    @Ignore
    boolean isLabelOnlyEditMode();

    @Ignore
    void setLabelOnlyEditMode(boolean labelOnlyEditMode);

}

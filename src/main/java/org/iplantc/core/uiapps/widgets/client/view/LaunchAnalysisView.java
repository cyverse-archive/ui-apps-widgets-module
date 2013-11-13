package org.iplantc.core.uiapps.widgets.client.view;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.user.client.ui.IsWidget;

import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;

import java.util.List;

public interface LaunchAnalysisView extends IsWidget, Editor<JobExecution> {

    void edit(JobExecution je);

    JobExecution flushJobExecution();

    List<EditorError> getErrors();

    boolean hasErrors();

}

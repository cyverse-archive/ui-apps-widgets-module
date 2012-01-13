package org.iplantc.core.client.widgets.strategies;

import java.util.List;

public interface IWizardValidationBroadcastStrategy {
    void broadcast(List<String> errors);
}

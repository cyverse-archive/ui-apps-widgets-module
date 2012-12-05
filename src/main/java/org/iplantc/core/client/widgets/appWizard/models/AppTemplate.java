package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AppTemplate extends HasId, HasLabel, HasName {
    
    @PropertyName("groups")
    List<TemplateGroup> getGroups();
    
    @PropertyName("groups")
    void setGroups(List<TemplateGroup> groups);
    
    
    /* Example JSON for 
    {
        "id": "aa54b4fd9b56545db978fff4398c5ce81",
        "name": "ExtractFirstLinesFromaFile",
        "label": "ExtractFirstLinesFromaFile",
        "type": "TextManipulation",
        "groups": [
            {
                "id": "B2C65499-0332-4A6D-BC64-CBCA3A517C65",
                "name": "Selectdata: ",
                "label": "Selectinputdata",
                "properties": [
                    {
                        "id": "step_1_6FF31B1C-3DAB-499C-8521-69227C52CE10",
                        "isVisible": true,
                        "description": "",
                        "validator": {
                            "name": "",
                            "label": "",
                            "required": true
                        },
                        "name": "",
                        "label": "InputFile",
                        "type": "FileInput"
                    }
                ],
                "type": "step"
            },
            {
                "id": "g9a362e539d074a4da644c51156ef3b18",
                "name": "group1",
                "label": "Options",
                "properties": [
                    {
                        "id": "step_1_LastLines",
                        "isVisible": true,
                        "description": "ExtractfirstNlinesfromtheinputfile.",
                        "validator": {
                            "id": "va8743812dbd14712b636dd37d70d61c1",
                            "name": "",
                            "label": "",
                            "required": true,
                            "rules": [
                                {
                                    "IntAbove": [
                                        0
                                    ]
                                }
                            ]
                        },
                        "name": "",
                        "value": "1",
                        "label": "ExtractfirstNlines",
                        "type": "Number"
                    }
                ],
                "type": "step"
            }
        ]
    }
    */

}

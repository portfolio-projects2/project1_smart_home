package afgangsProjekt.automation.database2;

import afgangsProjekt.automation.systemEnums.AutomationStatus;
import afgangsProjekt.automation.systemModels.Automation;

public class DatabaseAutomation extends Automation {
    public DatabaseAutomation(int id, String automationTitle, AutomationStatus automationStatus, String automationTimeRange) {
        super(id, automationTitle, automationStatus, automationTimeRange);
    }
}

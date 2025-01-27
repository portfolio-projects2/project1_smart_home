package afgangsProjekt.automation.systemModels;

import afgangsProjekt.automation.systemEnums.AutomationStatus;

import java.io.Serializable;

public class Automation implements Serializable {
    private int Id;
    private String automationTitle;
    private AutomationStatus automationStatus;
    private String automationTimeRange;


    public Automation(int id, String automationTitle,AutomationStatus automationStatus, String automationTimeRange) {
        Id = id;
        this.automationTitle = automationTitle;

        this.automationStatus = automationStatus;
        this.automationTimeRange = automationTimeRange;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setAutomationStatus(AutomationStatus automationStatus) {
        this.automationStatus = automationStatus;
    }

    public void setAutomationTimeRange(String automationTimeRange) {
        this.automationTimeRange = automationTimeRange;
    }

    public String getAutomationTimeRange() {
        return automationTimeRange;
    }

    public int getId() {
        return Id;
    }


    public String getAutomationTitle() {
        return automationTitle;
    }



    public AutomationStatus getAutomationStatus() {
        return automationStatus;
    }



    public void setAutomationTitle(String automationTitle) {
        this.automationTitle = automationTitle;
    }






}

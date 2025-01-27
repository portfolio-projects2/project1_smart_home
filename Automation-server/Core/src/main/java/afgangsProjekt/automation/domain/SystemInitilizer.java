package afgangsProjekt.automation.domain;

import afgangsProjekt.automation.Environment;
import afgangsProjekt.automation.presentation.FlowTracker;
import afgangsProjekt.automation.services.IPersistence;
import afgangsProjekt.automation.systemEnums.EnvironmentStatus;
import afgangsProjekt.automation.systemRigestry.AutomationRegistry;
import afgangsProjekt.automation.systemRigestry.DevicesRegistry;
import afgangsProjekt.automation.systemRigestry.ParametersRegistry;
import afgangsProjekt.automation.systemRigestry.SensorsRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@DependsOn(value = {
        "Environment"
})
@Component("SystemInitilizer")
public class SystemInitilizer {

    DevicesRegistry devicesRegistry;
    AutomationRegistry automationRegistry;
    ParametersRegistry parametersRegistry;
    SensorsRegistry sensorsRegistry;
    ApplicationContext applicationContext;
    Environment environment;
    @Autowired
    public SystemInitilizer(@Qualifier("DevicesRegistry") DevicesRegistry _devicesRegistry,
                            AutomationRegistry _automationRegistry,
                            ParametersRegistry _parametersRegistry,
                            SensorsRegistry _sensorsRegistry,
                            ApplicationContext _applicationContext,
                            Environment _environment
    ) {

        devicesRegistry = _devicesRegistry;
        automationRegistry = _automationRegistry;
        parametersRegistry = _parametersRegistry;
        sensorsRegistry = _sensorsRegistry;
        applicationContext = _applicationContext;
        environment = _environment;
        initialize();
    }

    private void initialize() {
        if (environment.getEnvironmentStatus().equals(EnvironmentStatus.development) || environment.getEnvironmentStatus().equals(EnvironmentStatus.production)) {

            applicationContext.getBeansOfType(IPersistence.class).forEach((k, v) -> {

                if (v.isActiveDatabase()) {
                    String databaseName = v.getDatabaseName();
                    FlowTracker.trackFlow("Initializing system entities from database :" + databaseName);
                    try {
                        devicesRegistry.setAllDevices(databaseName, v.getAllDevices());
                        sensorsRegistry.setAllSensors(databaseName, v.getAllSensors());
                        automationRegistry.setAllAutomations(databaseName, v.getAllAutomations());
                        parametersRegistry.setAllParameters(databaseName, v.getAllParameters());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

}

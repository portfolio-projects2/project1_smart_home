package afgangsProjekt.automation.domain;

import afgangsProjekt.automation.Environment;
import afgangsProjekt.automation.presentation.FlowTracker;
import afgangsProjekt.automation.systemEnums.EnvironmentStatus;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@DependsOn(value = {"Environment"})
@Component("HazelcastClientBean")
public class HazelCastClientManagement {


HazelcastInstance hazelcastClient;
Environment environment;
FlowTracker flowTracker;

@Autowired
public HazelCastClientManagement(Environment _environment) {
    environment=_environment;


if (!environment.getEnvironmentStatus().equals(EnvironmentStatus.testing)) {

createHazelcastClient();
}}


    public HazelcastInstance getHazelcastClient() {
        return hazelcastClient;
    }


    private void createHazelcastClient() {
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.setClusterName("GreenSmartHome");
            clientConfig.getConnectionStrategyConfig().getConnectionRetryConfig()
                    .setClusterConnectTimeoutMillis(Integer.MAX_VALUE);

            ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();

            networkConfig

                    //Docker
                   // .addAddress(System.getenv("cluster"))
                    .setSmartRouting(true)
                    .addOutboundPortDefinition("34700-43112")
                    .setRedoOperation(true)
                    .setConnectionTimeout(50000);


            hazelcastClient = HazelcastClient.newHazelcastClient(clientConfig);
            FlowTracker.trackFlow("Hazelcast Client is added");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    }





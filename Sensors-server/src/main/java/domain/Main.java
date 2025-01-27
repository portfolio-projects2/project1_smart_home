package domain;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;

public class Main {

    public static void main(String[] args) throws Exception {
        //Create HazelCast member
        String clusterName= "GreenSmartHome";
        int port = 5701;

        HazelcastManagement.createHazelcastMember(clusterName,port);
        String brokerTCPIP = System.getenv("broker") != null ? System.getenv("broker") : "tcp://eu1.cloud.thethings.network:1883";
        String applicationId = System.getenv("appId") !=null ? System.getenv("appId"):"sensorsapp@ttn";
        String accessKey = System.getenv("accessKey") != null ?  System.getenv("accessKey"): "NNSXS.XLA4GF5VPTJDGLOOSWIFPFYKD4AI6RILEIDEEVQ.5M55Q4BMSZQCED4MYMRA3HKXUCKJMEJWKGOP6754ZX2WWQFKKILA" ;
        //connect to TTN
        SensorsMqttClient sensorsMqttClient = new SensorsMqttClient(brokerTCPIP, applicationId, accessKey);
        sensorsMqttClient.onMessage((String devId, DataMessage data) -> {
            System.out.println(devId);
            HazelcastManagement.putDataInMap(devId,data.toString());
        });
        sensorsMqttClient.onError((Throwable _error) -> System.err.println("error: " + _error.getMessage()));
// Start the client
        sensorsMqttClient.start();

    }


}

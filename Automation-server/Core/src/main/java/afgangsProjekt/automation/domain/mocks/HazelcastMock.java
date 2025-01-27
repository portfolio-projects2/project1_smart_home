package afgangsProjekt.automation.domain.mocks;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastMock {

   static HazelcastInstance hazelcastInstance;

public static void createMapForSensor(String devId){

    hazelcastInstance.getMap(devId);
}




    public static void initialize(String clusterName) {
        Config config = new Config();
        config.setClusterName(clusterName).getNetworkConfig()
                //local
                // .setPublicAddress(host)
                .setPortCount(20).setPort(5701)
                .setPortAutoIncrement(true)
                .getJoin();

        hazelcastInstance = Hazelcast.newHazelcastInstance(config);


    }

    public static void puInMap(String mapName, String testKey, String value) {
        hazelcastInstance.getMap(mapName).put(testKey,value);
    }

    public static IMap<Object, Object> getMap(String mapName) {
      return   hazelcastInstance.getMap(mapName);
    }
}

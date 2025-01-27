package domain;

import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastManagement {

    private static HazelcastInstance hazelcastInstance;

   public static void createHazelcastMember(String clusterName, int port){
       ManagementCenterConfig managementCenterConfig= new ManagementCenterConfig();

       Config config = new Config();
       config.setClusterName(clusterName).getNetworkConfig()
               //Docker
              // .setPublicAddress(System.getenv("cluster"))

               .setPortCount(20).setPort(port)
               .setPortAutoIncrement(true)
               .getJoin();
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        HazelcastObserver observer = new HazelcastObserver();
        hazelcastInstance.addDistributedObjectListener(observer);
        hazelcastInstance.getLifecycleService().addLifecycleListener(observer);
        hazelcastInstance.getCluster().addMembershipListener(observer);


   }

    public static void putDataInMap(String devId, String data) {
        hazelcastInstance.getMap(devId).put(devId,data.toString());
    }
}

/*
package afgangsProject.devices.domain.integrationTest;

        import afgangsProject.devices.domain.AutomationHandler;
        import afgangsProject.devices.domain.HazelCastClientManagement;
        import afgangsProject.devices.domain.SensorObserver;
        import afgangsProject.devices.domain.mocks.HazelcastMock;
        import afgangsProject.devices.systemEnums.SensorStatus;
        import afgangsProject.devices.systemGenerics.Sensor;
        import afgangsProject.devices.systemRigestry.SensorsRegistry;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.junit.jupiter.api.extension.ExtendWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.test.context.junit.jupiter.SpringExtension;

        import java.util.ArrayList;
        import java.util.Arrays;

        import static java.lang.Thread.sleep;
        import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class HazelCastClusterIntegration {

    @Autowired
    SensorsRegistry sensorsRegistry;
    @Autowired
    AutomationHandler automationHandler;
    @Autowired
    HazelCastClientManagement hazelCastClientsManagement;
    @Autowired
    SensorObserver sensorObserver;


    @BeforeEach
    void initializeHazelCastMock() {
        // Simulate creating a hazelCast cluster remotely with one active member with name kitchen and one map with name kitchen¤1
        HazelcastMock.initialize("testCluster");
    }

    @Test
    void test_Integration_with_Hazelcast_cluster() {

        HazelcastMock.createMapForSensor( "testDevId");
        // create map in the cluster from the remote member
    //The following raw assert that the remotely created clusterMap the same as the fetched map at the client side
        assertEquals(HazelcastMock.getMap("kitchen¤1").getName(), hazelCastClientsManagement.getHazelcastClient().getMap() tchen").getMap("kitchen¤1").getName());

    }


    // This method test a hazelCast-client-map's observer object if it updates a sensor
    // When a cluster map with sensorId's reference is updated the observer bean must catch the new value
    // After that the system go to the sensors registry and update the sensor status to active and its LiveValue to the value from the map
    @Test
    void test_creating_observer_on_cluster_map() throws InterruptedException {
        //make a new sensor object and add it to the registry
        Sensor sensor = new Sensor(1, "testSensor", "00", SensorStatus.inactive);
        sensorsRegistry.addSensor(sensor);
        // Simulate creating a hazelCast cluster remotely with one active member with name kitchen and one map with name kitchen¤1
        HazelcastMock.createMapForSensor("kitchen", 1);
        // After the socket client receives membersInfo from socket input stream
        membersSocketUpdate.activeMembersInfo.put("kitchen", new ArrayList<Integer>(Arrays.asList(1)));
        // addClients method will automatically get this map from memberSocketUpdate bean and creat clients for each remote-created hazelcast member
        hazelCastClientsManagement.addClients();
        // create map in the cluster from the remote member
        HazelcastMock.createMapForSensor("kitchen", 1);
        // add observer oject on each map in the cluster
        sensorObserver.addObserver();
        // add liveData the map with sensorId reference  1
        HazelcastMock.puInMap("kitchen¤1", "key", "15");
        // update the map with new value
        HazelcastMock.puInMap("kitchen¤1", "key", "22");
        sleep(500);
        // assert sensorStatus has changed to active
        assertEquals(SensorStatus.activ, sensor.getSensorStatus());
        // assert sensorLiveValue is updated
        assertEquals("22", sensor.getLiveValue());

    }


}

*/

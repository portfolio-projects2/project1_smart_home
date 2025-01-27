package afgangsProjekt.automation.domain;

import afgangsProjekt.automation.presentation.FlowTracker;
import afgangsProjekt.automation.systemEnums.SensorStatus;
import afgangsProjekt.automation.systemRigestry.SensorsRegistry;
import afgangsProjekt.automation.systemModels.Sensor;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.core.*;
import com.hazelcast.map.listener.EntryUpdatedListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sensorObserverBean")
public class SensorObserver implements DistributedObjectListener, MembershipListener, EntryUpdatedListener<String, String>, LifecycleListener {

   private SensorsRegistry sensorsRegistry;
   private HazelCastClientManagement hazCastClient;
@Autowired
    public SensorObserver(SensorsRegistry _sensorsRegistry, HazelCastClientManagement _hazCastClient) throws InterruptedException {
        sensorsRegistry=_sensorsRegistry;
        hazCastClient=_hazCastClient;
    if(hazCastClient.getHazelcastClient()!=null){

        addObservers();
    }

    }

    private void addObservers() {
    try {

        hazCastClient.getHazelcastClient().getCluster().addMembershipListener(this);
        hazCastClient.getHazelcastClient().addDistributedObjectListener(this);
        hazCastClient.getHazelcastClient().getLifecycleService().addLifecycleListener(this);
        FlowTracker.trackFlow("Observers added");
    }catch (Exception e){
        FlowTracker.trackFlow("Observers failed");
    }
    }


    @Override
    public void entryUpdated(EntryEvent<String, String> entryEvent) {
        String[] clusterMap = entryEvent.getName().split("¤",2);
        System.out.println("Seen");
           Sensor sensor = sensorsRegistry.getSensor(clusterMap[0]);

        if (!sensor.getSensorStatus().equals(SensorStatus.activ)){
               sensor.setSensorStatus(SensorStatus.activ);
           }

           sensor.setLiveValue(entryEvent.getValue());
        FlowTracker.trackFlow("ClusterMap updated for sensor"+ sensor);

    }


    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        FlowTracker.trackFlow(membershipEvent.getMember().toString() +"member is added");
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        FlowTracker.trackFlow(membershipEvent.getMember().toString() +"member is removed");
    }

    @Override
    public void distributedObjectCreated(DistributedObjectEvent event) {
        FlowTracker.trackFlow("A new map added to the cluster"+event);
        hazCastClient.getHazelcastClient().getMap(event.getObjectName().toString()).addEntryListener(this,true);

    }

    @Override
    public void distributedObjectDestroyed(DistributedObjectEvent event) {
    FlowTracker.trackFlow("map removed from the cluster"+event);
    }

    @Override
    public void stateChanged(LifecycleEvent event) {
        FlowTracker.trackFlow("LifeCycle"+event);

        if (event.getState() == LifecycleEvent.LifecycleState.CLIENT_CONNECTED) {
            // Block the client until the cluster has been re-formed and all members have joined
            hazCastClient.getHazelcastClient().addDistributedObjectListener(this);
    }
}}


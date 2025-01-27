package domain;

import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;

public class HazelcastObserver implements LifecycleListener, MembershipListener, DistributedObjectListener {
    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        System.out.println(membershipEvent);
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        System.out.println(membershipEvent);
    }

    @Override
    public void stateChanged(LifecycleEvent event) {
        System.out.println(event);
    }

    @Override
    public void distributedObjectCreated(DistributedObjectEvent event) {
        System.out.println(event);
    }

    @Override
    public void distributedObjectDestroyed(DistributedObjectEvent event) {
        System.out.println(event);
    }
}

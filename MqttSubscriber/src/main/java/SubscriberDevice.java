import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SubscriberDevice {
    static String broker = "tcp://broker.emqx.io:1883";
    static String username = "emqx";
    static String password = "public";
    static String clientid2 = "subscribe_client";
    static MqttClient client2;
    static MqttClient pubClient;

    static {
        try {

            client2 = new MqttClient(broker, clientid2, new MemoryPersistence());
            pubClient = new MqttClient(broker,"pubgr",new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) throws MqttException {

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        client2.connect(options);
        pubClient.connect(options);
        client2.subscribe("GreenHome/#");
        client2.setCallback(new MqttCallback() {

            public void connectionLost(Throwable cause) {
                System.out.println("connectionLost: " + cause.getMessage());
            }

            public void messageArrived(String topic, MqttMessage message) throws MqttException {
                System.out.println("topic: " + topic);
                System.out.println("Qos: " + message.getQos());
                System.out.println("message content: " + new String(message.getPayload()));
                String[] messageArray = topic.split("/",4);
                System.out.println(Arrays.toString(messageArray));
                switch (messageArray[1]){
                    case "start":
                        switch (messageArray[2]){
                            case "manuallyStart":
                                Device.setDeviceStatus(Integer.parseInt(messageArray[3]),"manuallyStarted");
                                break;
                            case "active":
                                Device.setDeviceStatus(Integer.parseInt(messageArray[3]),"active");
                                break;
                        }
                        break;
                    case "stop":
                        switch (messageArray[2]){
                            case "manuallyStop":
                                Device.setDeviceStatus(Integer.parseInt(messageArray[3]),"manuallyStopped");
                                break;
                            case "disconnect":
                                Device.setDeviceStatus(Integer.parseInt(messageArray[3]),"disconnected");
                                break;
                        }
                        break;
                    case "status":
                        String stat="device"+String.valueOf(messageArray[2])+" is not connected yet";
                        try {
                            stat = Device.getDeviceStatus(Integer.parseInt(messageArray[2]));
                        }catch (Exception e){
                            System.out.println("not connected");

                        }

                       MqttMessage mqttMessage= new MqttMessage(stat.getBytes(StandardCharsets.UTF_8));
                        System.out.println(stat);
                        pubClient.publish("GSH/status/"+stat,mqttMessage);
                        System.out.println(pubClient.isConnected());
                        break;

                }
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("deliveryComplete---------" + token.isComplete());
            }

        });


    }




}

package domain;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.Subscribable;
import org.thethingsnetwork.data.common.TriConsumer;
import org.thethingsnetwork.data.common.events.*;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.DownlinkMessage;
import org.thethingsnetwork.data.common.messages.RawMessage;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SensorsMqttClient extends org.thethingsnetwork.data.common.AbstractClient {
    private final String broker;
    private final String appId;
    private MqttClientPersistence persistence;
    private final MqttConnectOptions connOpts;
    private final ExecutorService executor;
    private final Map<Class, List<EventHandler>> handlers;
    private MqttClient mqttClient;
    public SensorsMqttClient(String _broker, String _appId, String _appAccessKey) throws URISyntaxException {
        this(_broker, _appId, _appAccessKey, (MqttConnectOptions)null);
    }

    public SensorsMqttClient(String _broker, String _appId, String _appAccessKey, MqttConnectOptions _connOpts) throws URISyntaxException {
        this.persistence = new MemoryPersistence();
        this.executor = Executors.newCachedThreadPool();
        this.handlers = new HashMap();
        this.broker = this.validateBroker(_broker);
        this.appId = _appId;
        if (_connOpts != null) {
            this.connOpts = _connOpts;
        } else {
            this.connOpts = new MqttConnectOptions();
        }

        this.connOpts.setUserName(_appId);
        this.connOpts.setPassword(_appAccessKey.toCharArray());
    }

    private String validateBroker(String _source) throws URISyntaxException {
        URI tempBroker = new URI(_source.contains(".") ? _source : _source + ".thethings.network");
        if ("tcp".equals(tempBroker.getScheme())) {
            return tempBroker.getPort() == -1 ? tempBroker.toString() + ":1883" : tempBroker.toString();
        } else if ("ssl".equals(tempBroker.getScheme())) {
            return tempBroker.getPort() == -1 ? tempBroker.toString() + ":8883" : tempBroker.toString();
        } else {
            return tempBroker.getPort() != -1 ? "tcp://" + tempBroker.toString() : "tcp://" + tempBroker.toString() + ":1883";
        }
    }


    public SensorsMqttClient start() throws MqttException, Exception {

        if (this.mqttClient != null) {
            throw new RuntimeException("Already connected");
        } else {
            this.mqttClient = new MqttClient(this.broker, MqttClient.generateClientId(), this.persistence);
            this.mqttClient.connect(this.connOpts);System.out.println("start");
            this.mqttClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    SensorsMqttClient.this.mqttClient = null;
                    if (SensorsMqttClient.this.handlers.containsKey(ErrorHandler.class)) {
                        ((List) SensorsMqttClient.this.handlers.get(ErrorHandler.class)).stream().forEach((handler) -> {
                            SensorsMqttClient.this.executor.submit(() -> {
                                ((ErrorHandler)handler).safelyHandle(cause);
                            });
                        });
                    }

                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String[] tokens = topic.split("\\/");
                    System.out.println("topic"+topic);
                    String deviceId= tokens[3];
                    if (SensorsMqttClient.this.handlers.containsKey(UplinkHandler.class)) {
                    System.out.println("trueContainUplink");
                    ((List) SensorsMqttClient.this.handlers.get(UplinkHandler.class)).stream().forEach((handler) -> {
                    SensorsMqttClient.this.executor.submit(() -> {
                    try {
                        UplinkHandler uh = (UplinkHandler) handler;
                        System.out.println("uplinkhandlerDeviceId" + uh.getDevId());
                        String message2 = message.toString();
                        JSONObject obj = new JSONObject(message2);
                        JSONObject uplink_message = obj.getJSONObject("uplink_message");
                        String frm_payload= uplink_message.getString("frm_payload");



                        byte[] decodedBytes = Base64.getDecoder().decode(frm_payload);
                        String messageString = new String(decodedBytes, "UTF-8");

                        DataMessage dataMessage = new DataMessage() {
                        @Override
                        public String toString() {
                        return messageString;
                         }
                        };
                        uh.handle(deviceId, dataMessage);}
                    catch (Exception var6) {

                    if (SensorsMqttClient.this.handlers.containsKey(ErrorHandler.class)) {
                    ((List) SensorsMqttClient.this.handlers.get(ErrorHandler.class)).stream().forEach((handler1) -> {
                    SensorsMqttClient.this.executor.submit(() -> {
                    ((ErrorHandler) handler1).safelyHandle(var6);
                                                        });
                                                    });
                                                }
                                            }

                                        });
                                    });
                                }

                        }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            Iterator var1 = this.handlers.values().iterator();

            while(var1.hasNext()) {
                List<EventHandler> ehl = (List)var1.next();
                Iterator var3 = ehl.iterator();

                while(var3.hasNext()) {
                    EventHandler eh = (EventHandler)var3.next();
                    eh.subscribe(new Subscribable() {
                        private static final String WILDCARD_WORD = "+";
                        private static final String WILDCARD_PATH = "#";

                        public void subscribe(String[] _key) throws Exception {
                            StringJoiner sj = new StringJoiner("/");
                            String[] var3 = _key;
                            int var4 = _key.length;

                            for(int var5 = 0; var5 < var4; ++var5) {
                                String key = var3[var5];
                                sj.add(key);
                            }

                            SensorsMqttClient.this.mqttClient.subscribe("#");
                        }

                        public String getWordWildcard() {
                            return "+";
                        }

                        public String getPathWildcard() {
                            return "#";
                        }
                    });
                }
            }

            if (this.handlers.containsKey(ConnectHandler.class)) {
                ((List)this.handlers.get(ConnectHandler.class)).stream().forEach((handler) -> {
                    this.executor.submit(() -> {
                        try {
                            ((ConnectHandler)handler).handle(() -> {
                                return this.mqttClient;
                            });
                        } catch (Exception var3) {
                            if (this.handlers.containsKey(ErrorHandler.class)) {
                                ((List)this.handlers.get(ErrorHandler.class)).stream().forEach((handler1) -> {
                                    this.executor.submit(() -> {
                                        ((ErrorHandler)handler1).safelyHandle(var3);
                                    });
                                });
                            }
                        }

                    });
                });
            }

            return this;
        }
    }


    public SensorsMqttClient end() throws MqttException, InterruptedException {
        if (this.mqttClient == null) {
            throw new RuntimeException("Not connected");
        } else {
            return this.end(5000L);
        }
    }

    public SensorsMqttClient end(long _timeout) throws MqttException, InterruptedException {
        if (this.mqttClient == null) {
            throw new RuntimeException("Not connected");
        } else {
            this.executor.awaitTermination(_timeout, TimeUnit.MILLISECONDS);
            this.mqttClient.disconnect(_timeout);
            if (!this.mqttClient.isConnected()) {
                this.mqttClient = null;
            }

            return this;
        }
    }

    public SensorsMqttClient endNow() throws MqttException {
        if (this.mqttClient == null) {
            throw new RuntimeException("Not connected");
        } else {
            this.mqttClient.disconnectForcibly(0L, 0L);
            this.mqttClient = null;
            return this;
        }
    }

    public void send(String _devId, DownlinkMessage _payload) throws Exception {
        this.mqttClient.publish(this.appId + "/devices/" + _devId + "/down", MAPPER.writeValueAsBytes(_payload), 0, false);
    }

    public SensorsMqttClient onConnected(final Consumer<Connection> _handler) {
        if (this.mqttClient != null) {
            throw new RuntimeException("Already connected");
        } else {
            if (!this.handlers.containsKey(ConnectHandler.class)) {
                this.handlers.put(ConnectHandler.class, new LinkedList());
            }

            ((List)this.handlers.get(ConnectHandler.class)).add(new ConnectHandler() {
                public void handle(Connection _client) {
                    _handler.accept(_client);
                }
            });
            return this;
        }
    }

    public SensorsMqttClient onError(final Consumer<Throwable> _handler) {
        if (this.mqttClient != null) {
            throw new RuntimeException("Already connected");
        } else {
            if (!this.handlers.containsKey(ErrorHandler.class)) {
                this.handlers.put(ErrorHandler.class, new LinkedList());
            }

            ((List)this.handlers.get(ErrorHandler.class)).add(new ErrorHandler() {
                public void handle(Throwable _error) {
                    _handler.accept(_error);
                }
            });
            return this;
        }
    }

    public SensorsMqttClient onMessage(final String _devId, final String _field, final BiConsumer<String, DataMessage> _handler) {
        if (this.mqttClient != null) {
            throw new RuntimeException("Already connected");
        } else {
            if (!this.handlers.containsKey(UplinkHandler.class)) {
                this.handlers.put(UplinkHandler.class, new LinkedList());
            }

            ((List)this.handlers.get(UplinkHandler.class)).add(new UplinkHandler() {
                public void handle(String _devIdx, DataMessage _data) {
                    _handler.accept(_devIdx, _data);
                }

                public String getDevId() {
                    return _devId;
                }

                public String getField() {
                    return _field;
                }
            });
            return this;
        }
    }

    public SensorsMqttClient onMessage(String _devId, BiConsumer<String, DataMessage> _handler) {
        return this.onMessage(_devId, (String)null, _handler);
    }

    public SensorsMqttClient onMessage(BiConsumer<String, DataMessage> _handler) {
        return this.onMessage((String)null, (String)null, _handler);
    }

    public SensorsMqttClient onActivation(final String _devId, final BiConsumer<String, ActivationMessage> _handler) {
        if (this.mqttClient != null) {
            throw new RuntimeException("Already connected");
        } else {
            if (!this.handlers.containsKey(ActivationHandler.class)) {
                this.handlers.put(ActivationHandler.class, new LinkedList());
            }

            ((List)this.handlers.get(ActivationHandler.class)).add(new ActivationHandler() {
                public void handle(String _devIdx, ActivationMessage _data) {
                    _handler.accept(_devIdx, _data);
                }

                public String getDevId() {
                    return _devId;
                }
            });
            return this;
        }
    }

    public SensorsMqttClient onActivation(BiConsumer<String, ActivationMessage> _handler) {
        return this.onActivation((String)null, _handler);
    }

    public SensorsMqttClient onDevice(final String _devId, final String _event, final TriConsumer<String, String, RawMessage> _handler) {
        if (this.mqttClient != null) {
            throw new RuntimeException("Already connected");
        } else {
            if (!this.handlers.containsKey(AbstractEventHandler.class)) {
                this.handlers.put(AbstractEventHandler.class, new LinkedList());
            }

            ((List)this.handlers.get(AbstractEventHandler.class)).add(new AbstractEventHandler() {
                public void handle(String _devIdx, String _eventx, RawMessage _data) {
                    _handler.accept(_devIdx, _eventx, _data);
                }

                public String getDevId() {
                    return _devId;
                }

                public String getEvent() {
                    return _event;
                }
            });
            return this;
        }
    }

    public SensorsMqttClient onDevice(String _devId, TriConsumer<String, String, RawMessage> _handler) {
        return this.onDevice(_devId, (String)null, _handler);
    }

    public SensorsMqttClient onDevice(TriConsumer<String, String, RawMessage> _handler) {
        return this.onDevice((String)null, (String)null, _handler);
    }
}

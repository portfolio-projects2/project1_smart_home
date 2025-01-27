package afgangsProjekt.automation;

import afgangsProjekt.automation.systemEnums.DeviceStatus;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.OutputStream;
import java.util.Enumeration;

public class ArduinoConfiguration implements SerialPortEventListener {
    SerialPort serialPort;
    public static DeviceStatus status;
    private static final String[] PORT_NAMES = {

            "COM3" // Windows
    };


    private static final int TIME_OUT = 2000;

    private static final int DATA_RATE = 9600;



    public ArduinoConfiguration() {
        status = DeviceStatus.initial;
        System.out.println("fromArduinoTest off");

    }

    public void initialize() {
        CommPortIdentifier portId = getCommPortIdentifier();
        if (portId == null) {
            System.out.println("Could not find COM port.");
            return;
        }
        System.out.println("helloFromInitialize");
        try {
            System.out.println(portId);
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);
            System.out.println(serialPort);
            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            System.out.println("serialPort" + serialPort);
            // open the streams
            // input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            OutputStream output = serialPort.getOutputStream();
            System.out.println("statusFromInitialize" + status);
            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

            if (status.equals(DeviceStatus.activ)||status.equals(DeviceStatus.manuallyStartted)) {
                System.out.println("On");
                output.write(1);
            } else if (status.equals(DeviceStatus.inactive)||status.equals(DeviceStatus.manuallyStopped)) {
                System.out.println("off");
                output.write(0);
            }

            output.close();
            serialPort.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private static CommPortIdentifier getCommPortIdentifier() {
        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        // System.out.println(portEnum);
        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                //  System.out.println(portName);
                //  System.out.println(currPortId.getName());
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;

                }
            }
        }
        return portId;
    }


    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {

        // Ignore all the other eventTypes, but you should consider the other ones.
    }

public boolean portConnected(){
    CommPortIdentifier portId = getCommPortIdentifier();
    return portId != null;
}
    public  void setStatus(DeviceStatus status) {
        ArduinoConfiguration.status = status;
    }



}
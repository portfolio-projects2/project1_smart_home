package afgangsProjekt.automation.systemModels;

import afgangsProjekt.automation.systemEnums.SensorStatus;

import java.io.Serializable;
import java.util.Objects;

public class Sensor implements Serializable {
    private int id;
    private String sensorName;
    private String liveValue;
    private SensorStatus sensorStatus;
    private String dev_Id;

    public Sensor(int _id, String _sensorName, String _liveValue, SensorStatus _sensorStatus,String _dev_Id) {
        id = _id;
        sensorName = _sensorName;
        liveValue = _liveValue;
        sensorStatus = _sensorStatus;
        dev_Id=_dev_Id;
    }

    public int getId() {
        return id;
    }


    public String getSensorName() {
        return sensorName;
    }

    public void setDev_Id(String devId) {
        this.dev_Id = devId;
    }

    public String getLiveValue() {
        return liveValue;
    }

    public SensorStatus getSensorStatus() {
        return sensorStatus;
    }

    public void setId(int _id) {
        id = _id;
    }


    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public void setLiveValue(String liveValue) {
        this.liveValue = liveValue;
    }

    public void setSensorStatus(SensorStatus sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public String getdev_Id() {
    return dev_Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sensor sensor = (Sensor) o;
        return Objects.equals(dev_Id, sensor.dev_Id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", sensorName='" + sensorName + '\'' +
                ", liveValue='" + liveValue + '\'' +
                ", sensorStatus=" + sensorStatus +
                ", dev_Id='" + dev_Id + '\'' +
                '}';
    }
}

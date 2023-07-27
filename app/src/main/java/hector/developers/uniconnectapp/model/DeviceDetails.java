package hector.developers.uniconnectapp.model;

public class DeviceDetails {
    private String meter_type;
    private String device_number;

    public DeviceDetails(String meter_type, String device_number) {
        this.meter_type = meter_type;
        this.device_number = device_number;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getDevice_number() {
        return device_number;
    }

    public void setDevice_number(String device_number) {
        this.device_number = device_number;
    }
}

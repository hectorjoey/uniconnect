package hector.developers.uniconnectapp.model;

import java.io.Serializable;
import java.util.Map;

public class Power implements Serializable {
    private String id;
    private String desc;
    private String name;
    private String sector;
    private int amount;
    private String imgUrl;
    private String category;
    private String fee_type;
    private String operator;

    private DeviceDetails device_details;
    private Map<String, Object> meta_data;


    public Power() {
    }


    public Power(String id, String desc, String name, String sector, int amount, String imgUrl,
                 String category, String fee_type, String operator, DeviceDetails device_details, Map<String, Object> meta_data) {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.sector = sector;
        this.amount = amount;
        this.imgUrl = imgUrl;
        this.category = category;
        this.fee_type = fee_type;
        this.operator = operator;
        this.device_details = device_details;
        this.meta_data = meta_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public DeviceDetails getDevice_details() {
        return device_details;
    }

    public void setDevice_details(DeviceDetails device_details) {
        this.device_details = device_details;
    }

    public Map<String, Object> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(Map<String, Object> meta_data) {
        this.meta_data = meta_data;
    }
}
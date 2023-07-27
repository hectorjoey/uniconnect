package hector.developers.uniconnectapp.model;

import java.io.Serializable;

public class MobileData implements Serializable {

    private String category;
    private String desc;
    private String fee_type;
    private String id;
    private String name;
    private String operator;
    private String amount;
    private String data_expiry;
    private String data_value;
    private String imgUrl;


    public MobileData() {
    }

    public MobileData(String category, String desc, String fee_type, String id, String name, String operator, String amount, String data_expiry, String data_value, String imgUrl) {
        this.category = category;
        this.desc = desc;
        this.fee_type = fee_type;
        this.id = id;
        this.name = name;
        this.operator = operator;
        this.amount = amount;
        this.data_expiry = data_expiry;
        this.data_value = data_value;
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getData_expiry() {
        return data_expiry;
    }

    public void setData_expiry(String data_expiry) {
        this.data_expiry = data_expiry;
    }

    public String getData_value() {
        return data_value;
    }

    public void setData_value(String data_value) {
        this.data_value = data_value;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
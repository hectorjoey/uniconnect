package hector.developers.uniconnectapp.model;

import java.io.Serializable;

public class Airtime implements Serializable {
    private String category;
    private String desc;
    private String fee_type;
    private String id;
    private String name;
    private String operator;
    private String amount;
    private String maximum_fee;
    private String minimum_fee;
    private String imgUrl;


    public Airtime() {
    }

    public Airtime(String category, String desc, String fee_type, String id, String name, String operator, String amount, String maximum_fee, String minimum_fee, String imgUrl) {
        this.category = category;
        this.desc = desc;
        this.fee_type = fee_type;
        this.id = id;
        this.name = name;
        this.operator = operator;
        this.amount = amount;
        this.maximum_fee = maximum_fee;
        this.minimum_fee = minimum_fee;
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

    public String getMaximum_fee() {
        return maximum_fee;
    }

    public void setMaximum_fee(String maximum_fee) {
        this.maximum_fee = maximum_fee;
    }

    public String getMinimum_fee() {
        return minimum_fee;
    }

    public void setMinimum_fee(String minimum_fee) {
        this.minimum_fee = minimum_fee;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

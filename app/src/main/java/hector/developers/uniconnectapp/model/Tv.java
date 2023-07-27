package hector.developers.uniconnectapp.model;

import java.io.Serializable;

public class Tv  implements Serializable {
    private String category;
    private String desc;
    private String fee_type;
    private String id;
    private String name;
    private String operator;
    private String amount;

    private String imgUrl;


    public Tv() {
    }

    public Tv(String category, String desc, String fee_type, String id, String name, String operator, String amount, String imgUrl) {
        this.category = category;
        this.desc = desc;
        this.fee_type = fee_type;
        this.id = id;
        this.name = name;
        this.operator = operator;
        this.amount = amount;
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


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

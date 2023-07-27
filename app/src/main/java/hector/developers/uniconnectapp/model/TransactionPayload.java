package hector.developers.uniconnectapp.model;

import java.util.Map;

public class TransactionPayload {
    private String amount;
    private String product_id;
    private String operator_id;
    private String beneficiary_msisdn;
    private DeviceDetails device_details;
    private Map<String, Object> meta_data;

    public TransactionPayload(int amount, String product_id, String operator_id, String beneficiary_msisdn, DeviceDetails device_details, Map<String, Object> meta_data) {
        this.amount = String.valueOf(amount);
        this.product_id = product_id;
        this.operator_id = operator_id;
        this.beneficiary_msisdn = beneficiary_msisdn;
        this.device_details = device_details;
        this.meta_data = meta_data;
    }



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getBeneficiary_msisdn() {
        return beneficiary_msisdn;
    }

    public void setBeneficiary_msisdn(String beneficiary_msisdn) {
        this.beneficiary_msisdn = beneficiary_msisdn;
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
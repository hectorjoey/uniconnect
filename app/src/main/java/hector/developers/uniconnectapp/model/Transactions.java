package hector.developers.uniconnectapp.model;

public class Transactions {
    private long id;
    private String created_at;
    private String status;
    private String amount;
    private String reference;
    private String operator_id;
    private String product_id;
    private String bill_type;
    private String operator_name;

    private  Long userId;

    public Transactions() {
    }

    public Transactions(long id, String created_at, String status, String amount, String reference, String operator_id, String product_id, String bill_type, String operator_name, Long userId) {
        this.id = id;
        this.created_at = created_at;
        this.status = status;
        this.amount = amount;
        this.reference = reference;
        this.operator_id = operator_id;
        this.product_id = product_id;
        this.bill_type = bill_type;
        this.operator_name = operator_name;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

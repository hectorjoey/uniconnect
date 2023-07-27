package hector.developers.uniconnectapp.model;

import com.google.gson.annotations.SerializedName;

public class TransactionResponse {

        @SerializedName("status")
        private String status;

        @SerializedName("message")
        private String message;

        // Add other fields as needed

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    // Add setters if needed

}

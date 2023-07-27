package hector.developers.uniconnectapp.model;

import com.google.gson.annotations.SerializedName;

public class ResponseBody {


    @SerializedName("error_message")
    private String errorMessage;

    // Add any other relevant fields you need to extract from the error response

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

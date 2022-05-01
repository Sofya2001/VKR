package ru.chirkova.vkr_asych.ModelMessageAnswer;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"errorCause", "errorMessage"})
public class ErrorMessage {
    private String errorCause;
    private String errorMessage;

    public String getErrorCode() {
        return errorCause;
    }

    public void setErrorCode(String errorCode) {
        this.errorCause = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessage(String errorCode, String errorMessage) {
        this.errorCause = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorCode='" + errorCause + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

package ru.chirkova.vkr_asych.ModelMessageRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"method", "params"})
    public class MessageBody {
        String method;
        String[] params;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String[] getParams() {
            return params;
        }

        public void setParams(String[] params) {
            this.params = params;
        }

    public MessageBody(String method, String[] params) {
        this.method = method;
        this.params = params;
    }



    @Override
    public String toString() {
        return "MessageBody{" +
                "method='" + method + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}

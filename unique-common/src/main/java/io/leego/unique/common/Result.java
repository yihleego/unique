package io.leego.unique.common;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -6923820166518806231L;
    /** Data */
    private T data;
    /** Success */
    private Boolean success;
    /** Message */
    private String message;
    /** Code */
    private Integer code;

    public Result() {
    }

    public Result(T data, Boolean success, String message, Integer code) {
        this.data = data;
        this.success = success;
        this.message = message;
        this.code = code;
    }

    protected Result(Boolean success) {
        this.success = success;
    }

    public static <T> Result<T> buildSuccess(Integer code, String message, T data) {
        return new Result<>(data, true, message, code);
    }

    public static <T> Result<T> buildSuccess(Integer code, T data) {
        return new Result<>(data, true, null, code);
    }

    public static <T> Result<T> buildSuccess(String message, T data) {
        return new Result<>(data, true, message, null);
    }

    public static <T> Result<T> buildSuccess(String message) {
        return new Result<>(null, true, message, null);
    }

    public static <T> Result<T> buildSuccess(T data) {
        return new Result<>(data, true, null, null);
    }

    public static <T> Result<T> buildSuccess() {
        return new Result<>(null, true, null, null);
    }

    public static <T> Result<T> buildFailure(Integer code, String message, T data) {
        return new Result<>(data, false, message, code);
    }

    public static <T> Result<T> buildFailure(Integer code, String message) {
        return new Result<>(null, false, message, code);
    }

    public static <T> Result<T> buildFailure(String message, T data) {
        return new Result<>(data, false, message, null);
    }

    public static <T> Result<T> buildFailure(String message) {
        return new Result<>(null, false, message, null);
    }

    public static <T> Result<T> buildFailure() {
        return new Result<>(null, false, null, null);
    }

    public static boolean isSuccessful(Result result) {
        return result != null && result.getSuccess() != null && result.getSuccess();
    }

    public static boolean isSuccessfulWithData(Result result) {
        return isSuccessful(result) && result.getData() != null;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String toJSONString() {
        StringBuilder sb = new StringBuilder("{");
        if (data != null) {
            sb.append("\"data\":\"").append(data.toString()).append("\",");
        }
        if (success != null) {
            sb.append("\"success\":").append(success).append(",");
        }
        if (message != null) {
            sb.append("\"message\":\"").append(message).append("\",");
        }
        if (code != null) {
            sb.append("\"code\":").append(code).append(",");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.append("}").toString();
    }

    @Override
    public String toString() {
        return "Result{" + "data=" + data +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }

    public static class Builder<T> {
        private T data;
        private Boolean success;
        private String message;
        private Integer code;

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> success(Boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> code(Integer code) {
            this.code = code;
            return this;
        }

        public Result<T> build() {
            return new Result<>(data, success, message, code);
        }

        public Result<T> buildSuccess() {
            return new Result<>(data, true, message, code);
        }

        public Result<T> buildFailure() {
            return new Result<>(data, false, message, code);
        }

    }

}
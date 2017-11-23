package pl.sggw.support.webservice.dto;

/**
 * Created by Kamil on 2017-11-21.
 */
public class Response<T> {
    private String errorCode;
    private String errorDesc;
    private T body;

    public Response(String errorCode, String errorDesc, T body) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.body = body;
    }

    public String getErrorCode() {
            return errorCode;
        }

    public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

    public String getErrorDesc() {
            return errorDesc;
        }

    public void setErrorDesc(String errorDesc) {
            this.errorDesc = errorDesc;
        }

    public T getBody() {
            return body;
        }

    public void setBody(T body) {
            this.body = body;
        }
}

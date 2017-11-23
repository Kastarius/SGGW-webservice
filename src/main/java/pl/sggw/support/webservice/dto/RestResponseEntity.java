package pl.sggw.support.webservice.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * Created by Kamil on 2017-11-19.
 */
public class RestResponseEntity<T> extends ResponseEntity<Response<T>> {

    public static final String SUCCESS_CODE = "0";
    public static final String SUCCESS_DESC = "Success";

    public RestResponseEntity(HttpStatus status) {
        super(status);
    }

    public RestResponseEntity(String errorCode, String errorDesc, T body, HttpStatus status) {
        super(new Response<>(errorCode,errorDesc,body), status);
    }

    public RestResponseEntity(String errorCode, String errorDesc, HttpStatus status) {
        super(new Response<>(errorCode,errorDesc,null), status);
    }

    public RestResponseEntity(T body, HttpStatus status) {
        super(new Response<>(SUCCESS_CODE,SUCCESS_DESC,body), status);
    }

    public RestResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public RestResponseEntity(String errorCode, String errorDesc, T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(new Response<>(errorCode,errorDesc,null), headers, status);
    }

    @JsonIgnore
    @Override
    public HttpStatus getStatusCode() {
        return super.getStatusCode();
    }

    @JsonIgnore
    @Override
    public int getStatusCodeValue() {
        return super.getStatusCodeValue();
    }

    @JsonIgnore
    @Override
    public HttpHeaders getHeaders() {
        return super.getHeaders();
    }

    @JsonUnwrapped
    @Override
    public Response<T> getBody() {
        return super.getBody();
    }
}

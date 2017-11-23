package pl.sggw.support.webservice.controller.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import pl.sggw.support.webservice.dto.RestResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Kamil on 2017-11-19.
 */
public class ResponseHelper {

    private ResponseHelper() {
    }

    public static <T> void createResponse(HttpServletResponse httpResponse,int responseStatus,String errorCode, String errorDesc, T body) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(new RestResponseEntity<T>(errorCode,errorDesc,body, HttpStatus.valueOf(responseStatus)));

        httpResponse.setStatus(responseStatus);
        httpResponse.getWriter().write(message);
    }
}

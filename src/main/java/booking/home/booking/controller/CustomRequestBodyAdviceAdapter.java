package booking.home.booking.controller;

import booking.home.booking.service.LoggingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.http.HttpInputMessage;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;


@ControllerAdvice
public class CustomRequestBodyAdviceAdapter extends RequestBodyAdviceAdapter {

   @Autowired
   LoggingService loggingService;

   @Autowired
   HttpServletRequest httpServletRequest;

   @Override
   public boolean supports(MethodParameter methodParameter, Type type,Class<? extends HttpMessageConverter<?>> aClass) {
       return true;
   }

   @Override
   public Object afterBodyRead(Object body, HttpInputMessage inputMessage,MethodParameter parameter, Type targetType,Class<? extends HttpMessageConverter<?>> converterType) {
       loggingService.logRequest(httpServletRequest, body);

       return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
   }
   
//   @Override
//   public Object beforeBodyRead(Object o,MethodParameter methodParameter,MediaType mediaType,Class<? extends HttpMessageConverter<?>> aClass,ServerHttpRequest serverHttpRequest,ServerHttpResponse serverHttpResponse) {
//
//       if (serverHttpRequest instanceof ServletServerHttpRequest &&
//               serverHttpResponse instanceof ServletServerHttpResponse) {
//           loggingService.logResponse(
//                   ((ServletServerHttpRequest) serverHttpRequest).getServletRequest(),
//                   ((ServletServerHttpResponse) serverHttpResponse).getServletResponse(), o);
//       }
//
//       return o;
//   }


}

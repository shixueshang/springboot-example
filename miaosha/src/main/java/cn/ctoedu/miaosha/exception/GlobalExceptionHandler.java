package cn.ctoedu.miaosha.exception;

import com.lhd.common.util.AjaxJson;
import com.lhd.common.util.CodeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = RuntimeException.class)
    public AjaxJson defaultErrorHandler(HttpServletRequest request, RuntimeException e) throws Exception {
        logger.error("接口 {} 异常 ", request.getRequestURI(), e);
        AjaxJson result = new AjaxJson();
        result.setMessage("运行错误");
        return result;
    }

    @ExceptionHandler(BadRequestException.class)
    public AjaxJson handleInvalidRequestError(BadRequestException ex) {
        return ex.getResult();
    }

    @ExceptionHandler(FailedRequestException.class)
    public AjaxJson handleInvalidRequestError(FailedRequestException ex) {
        return ex.getResult();
    }

    /**
     * 处理传参校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxJson handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        AjaxJson result = new AjaxJson(CodeMessage.REQUEST_ILLEGAL.getCode());
        String message = "";
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            message += error.getDefaultMessage() + ",";
        }
        result.setMessage(message.substring(0, message.length() - 1));
        return result;
    }


}
package cn.ctoedu.miaosha.exception;

import com.lhd.common.util.AjaxJson;
import com.lhd.common.util.CodeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by xiaomi on 2018/05/09.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The request you send to Server was bad.")
public class BadRequestException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private AjaxJson result;

    public BadRequestException(String message) {
        super(message);
        this.result = new AjaxJson(CodeMessage.REQUEST_ILLEGAL.getCode());
        this.result.setMessage(message);
    }


    public AjaxJson getResult() {
        return result;
    }

    public void setResult(AjaxJson result) {
        this.result = result;
    }
}

package com.lhd.common.util;

import lombok.Data;

/**
 * Created by xiaomi on 2018/06/15.
 */
@Data
public class AjaxJson {

    private int code;
    private String message = "";
    private Object data;

    public AjaxJson() {
        super();
    }

    public AjaxJson(int code) {
        this.code = code;
    }

    public AjaxJson(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AjaxJson(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public AjaxJson(Object data) {
        this.data = data;
    }

    private AjaxJson(CodeMessage codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.message = codeMsg.getMsg();
        }
    }

    /**
     *  成功时候的调用
     * */
    public static AjaxJson success(Object data){
        return new AjaxJson(data);
    }

    public static AjaxJson success(CodeMessage codeMsg){
        return new AjaxJson(codeMsg);
    }

    /**
     *  失败时候的调用
     * */
    public static AjaxJson error(CodeMessage codeMsg){
        return new AjaxJson(codeMsg);
    }

}

package com.lhd.security.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by lihongde on 2018/05/08.
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected Integer page;
    protected Integer size;



    public BaseController() {
        super();
    }

    @ModelAttribute
    protected void initRequestResponseSession(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        String page_str = request.getParameter("page");
        String size_str = request.getParameter("n");

    }


}

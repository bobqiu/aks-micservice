package com.aks.cloud.gateway.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: bobqiu
 * @create: 2019-07-29
 **/

@RestController
public class IndexController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
        return "Error handling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}

package com.blackbean.api.controller;

import com.blackbean.api.Services.CustomerNextBestConversationPreComputationService;
import com.blackbean.api.Services.EntityCreationService;
import com.blackbean.api.Services.EntityCreationServiceImpl;
import com.blackbean.api.entity.FileEntity;
import com.blackbean.api.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class MainController {

    UserEntity userEntity;
    FileEntity fileEntity;
    EntityCreationService entityCreationService = new EntityCreationServiceImpl();

    @RequestMapping("/test")
    @ResponseBody
    public String hello(){
        return "testing";
    }

    @RequestMapping("/hi")
    public ModelAndView hi(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("result","success!");
        return modelAndView;
    }

    @RequestMapping("/hi2")
    public ModelAndView hi2(){
        ModelAndView modelAndView = new ModelAndView("Login");
        modelAndView.addObject("result","Login!");
        return modelAndView;
    }

    @RequestMapping("/test1")
    public void test() {
        entityCreationService.createEntity("123");
    }

    @RequestMapping("/analysis/{file_path}")
    public void analysis(@PathVariable("file_path") String file_path) throws IOException, ExecutionException {
        CustomerNextBestConversationPreComputationService customerNextBestConversationPreComputationService = new CustomerNextBestConversationPreComputationService();
        customerNextBestConversationPreComputationService.basketAnalysis();
    }
}

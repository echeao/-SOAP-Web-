package com.example.soap.server.service;

import com.example.soap.server.model.UserRequest;
import com.example.soap.server.model.UserResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(targetNamespace = "http://service.server.soap.example.com/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface UserService {

    @WebMethod(operationName = "getUserInfo")
    @WebResult(name = "userResponse")
    UserResponse getUserInfo(
        @WebParam(name = "userRequest") UserRequest request
    );
}

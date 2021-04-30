package com.ing.fr.app.controllers;

import com.gautam.students.GetStudentDetailsRequest;
import com.gautam.students.GetStudentDetailsResponse;
import com.ing.fr.app.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class StudentEndpoint {

    @Autowired
    private StudentRepository studentRepository;

    @PayloadRoot(namespace = "http://gautam.com/students", localPart = "GetStudentDetailsRequest")
    @ResponsePayload
    public GetStudentDetailsResponse details(@RequestPayload GetStudentDetailsRequest request) {
        GetStudentDetailsResponse response = new GetStudentDetailsResponse();
        response.setStudentDetails(studentRepository.findDetails(String.valueOf(request.getId())));
        return response;
    }
}

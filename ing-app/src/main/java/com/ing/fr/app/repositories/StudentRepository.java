package com.ing.fr.app.repositories;

import com.gautam.students.StudentDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class StudentRepository {

    private static final Map<String, StudentDetails> studentDetailsMap = new HashMap<>();

    @PostConstruct
    public void initData() {
        // initialize countries map
        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setId(1);
        studentDetails.setName("Gautam");
        studentDetails.setPassportNumber("Xyz");
        studentDetailsMap.put("1", studentDetails );
    }

    public StudentDetails findDetails(String id) {
        return studentDetailsMap.get(id);
    }
}

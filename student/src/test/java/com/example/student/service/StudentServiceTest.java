package com.example.student.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.student.dto.StudentDTO;
import com.example.student.entity.Student;
import com.example.student.entity.constant.Grade;

@Disabled
@SpringBootTest
public class StudentServiceTest {
    
    @Autowired
    private StudentService studentService;

    @Test
    public void testInsert(){
        StudentDTO dto = StudentDTO.builder()
        .name("홍길동")
        .gender("M")
        .addr("종로")
        .grade(Grade.FRESHMAN)
        .build();

        System.out.println(studentService.insert(dto));
    }

    @Test
    public void testRead(){

        System.out.println(studentService.read(1L));
    }

    @Test
    public void testReadAll(){
        List<StudentDTO> list = studentService.readAll();
        for (StudentDTO studentDTO : list) {
            System.out.println(studentDTO);
        }
    }

    @Test
    public void testUpdate(){
        StudentDTO dto = StudentDTO.builder()
        .id(21L)
        .name("홍길동")
        .gender("M")
        .addr("종로")
        .grade(Grade.FRESHMAN)
        .build();
        studentService.update(dto);
    }

    @Test
    public void testDelete(){
        studentService.delete(null);
    }
}

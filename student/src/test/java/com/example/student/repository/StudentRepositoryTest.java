package com.example.student.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.student.entity.Student;
import com.example.student.entity.constant.Grade;

@Disabled
@SpringBootTest
public class StudentRepositoryTest {
    
    @Autowired
    private StudentRepository studentRepository;


    @Test
    public void deleteTest(){ // test는 무조건 void 로 리턴, 
        // Student student = studentRepository.findById(1L).get();
        // studentRepository.delete(null);
        studentRepository.deleteById(1L);
    }

    @Test
    public void readTest(){ // test는 무조건 void 로 리턴, 
        Student student = studentRepository.findById(2L).get(); // 2번이 확실하게 있어서 Optional이 아니어도 확인할 수 있는 방법이다.
        System.out.println(student);
    }

    @Test
    public void readTest2(){
        // 전체 학생 조회
        List<Student> students = studentRepository.findAll();
        for (Student student : students) {
            System.out.println(student);
        }
    }


    @Test
    public void updateTest(){
        // Entity
        // update student set 수정컬럼

        Optional<Student> result = studentRepository.findById(1L);
        
        Student student = result.get();
        // student.changeName("성춘향");
        student.changeGrade(Grade.FRESHMAN);

        // insert(C), update(U) 작업 시 호출
        studentRepository.save(student);

        }


    @Test
    public void insertTest(){

        for (int i = 1; i < 11; i++) {
            Student student = Student.builder()
            .name("성춘향"+i)
            .addr("서울")
            .gender("F")
            .grade(Grade.SENIOR)
            .build();
            studentRepository.save(student);
            
        }

    // // insert(C), update(U) 작업 시 호출

    //     // delete from ~ 호출
        // studentRepository.delete(student);
        // studentRepository.deleteById(student);

        // select * from where id = 1;
        // studentRepository.findById(null)
        // select * from 
        // studentRepository.findAll();
    // }
    }
}

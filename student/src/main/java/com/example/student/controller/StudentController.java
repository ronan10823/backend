package com.example.student.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.student.dto.StudentDTO;
import com.example.student.service.StudentService;

// import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor // final을 해결하기 위해 추가
@RequestMapping("/student") // student로 모두 경로가 시작할거라고 설정해줌
@Log4j2
@Controller
public class StudentController {

    private final StudentService studentService; // 이걸 왜 하느 거지?

    @GetMapping("/register")
    public void getRegister() {
        log.info("등록화면 보여주기");
    }

    @PostMapping("/register")
    public String postRegister(StudentDTO dto) {
        log.info("등록 {}", dto);

        String name = studentService.insert(dto);

        return "redirect:/student/list";
    }

    @GetMapping({"/modify","/read"})
    public void getModify(@RequestParam Long id, Model model) { // id=3으로 넘어올 것이라, Long id로 해줘야 한다. 
        log.info("조회 {}", id);;
        StudentDTO dto = studentService.read(id); // <- list.html이 실행되면 이 메서드가 실행된다. read(id)는 StudentDTO로 리턴받기에 그거로 받아준다. 
        model.addAttribute("dto",dto); // 이름은 어떻게 지어도 상관없다. 화면단만 맞추면 된다. -> 무슨 의미?

    }
    
    @PostMapping("/modify")
    public String postModify(StudentDTO dto, RedirectAttributes rttr) { // post -> DTO, Redirect- 목적: read는 Long id가 필요하기에 같이 넣어줌
        log.info("수정 {}", dto);
        
        Long id = studentService.update(dto);

        // 상세 조회로 이동 목적: 수정한 내용이 제대로 수정되었는지 확인
        rttr.addAttribute("id", id);
        return "redirect:/student/read";
    }

    @PostMapping("/remove")
    public String postRemove(StudentDTO dto) {
        log.info("탈퇴 {}",dto);
        
        studentService.delete(dto.getId());

        return "redirect:/student/list";
    }
    
    @GetMapping("/list")
    public void getList(Model model) {
        log.info("전체 조회 ");
        List<StudentDTO> list = studentService.readAll(); // 왜 List를 쓰지?
        model.addAttribute("list",list); // 왜 이렇게 "" 뒤에 콤마 쓰고 하느 거야?
    }
    
    

}

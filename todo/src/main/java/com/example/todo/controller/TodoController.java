package com.example.todo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.TodoDTO;
import com.example.todo.service.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Response Todos", description = "Response Todo API")
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/todos")
@RestController
public class TodoController {

    private final TodoService todoService;

    // 전체 조회 http://localhost:8080/todos?completed= + GET

    // 완료 조회 http://localhost:8080/todos?completed=true + GET

    // 미완료 조회 http://localhost:8080/todos?completed=false + GET
    @CrossOrigin(origins = "http://localhost:5173")
    @Operation(summary = "todo 조회", description = "todo 전체 조회 API 완료 여부 포함 가능")
    @GetMapping("")
    public List<TodoDTO> getTodoList(@RequestParam(required = false) Boolean completed) {
        log.info("조회 {}", completed);
        List<TodoDTO> result = todoService.findCompletedTodos(completed);
        return result;
    }

    // 입력 http://localhost:8080/todos/add + POST
    @Operation(summary = "todo 입력", description = "todo 입력 API")
    @PostMapping("/add")
    public Long postAddTodo(@RequestBody TodoDTO dto) {
        log.info("입력 {}");
        Long id = todoService.create(dto);
        return id;
    }

    // 수정 http://localhost:8080/todos/1 + PUT
    @Operation(summary = "todo 수정", description = "todo 수정 API")
    @PutMapping("/{id}")
    public Long putTodo(
            @Parameter(description = "수정할 todo id값", example = "1", required = true) @PathVariable Long id,
            @RequestBody TodoDTO dto) {
        log.info("수정 {} {}", id, dto);
        dto.setId(id);

        return todoService.update(dto);
    }

    // 삭제 http://localhost:8080/todos/1 + DELETE
    @Operation(summary = "todo 삭제", description = "todo 삭제 API")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(
            @Parameter(description = "삭제할 id 값", example = "1") @PathVariable Long id) {
        log.info("수정 {}", id);
        todoService.delete(id);

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

}

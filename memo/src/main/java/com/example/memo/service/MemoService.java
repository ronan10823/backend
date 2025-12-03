package com.example.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.memo.dto.MemoDTO;
import com.example.memo.entity.Memo;
import com.example.memo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class MemoService {

    @Autowired
    private final MemoRepository memoRepository;
    @Autowired
    private final ModelMapper modelMapper;

    // 전체조회
    public List<MemoDTO> readAll(){
        List<Memo> memos = memoRepository.findAll();

        // Entity :  service => repository, repository => service
        // --DTO  :  service => controller, controller => service
        // memo 가 entity, 
        // 리턴하기 전, Memo entity => MemoDTO 로 변경 후 리턴
        // List<MemoDTO> list = new ArrayList<>();
        // for (Memo memo : memos) {
        //     // MemoDTO dto = MemoDTO.builder()
        //     // .id(memo.getId())
        //     // .text(memo.getText())
        //     // .createDate(memo.getCreateDate())
        //     // .updateDate(memo.getUpdateDate())
        //     // .build();
        //     MemoDTO dto = modelMapper.map(memo, MemoDTO.class); // MemmoDTO를 돌려준다. 

        //     list.add(dto);
        // }
        List<MemoDTO> list = memos.stream().map(memo -> modelMapper.map(memo, MemoDTO.class)).collect(Collectors.toList());
        return list;
    }

    // Select(조회) One
    public MemoDTO read(Long id){  // so, this is why we put Long id in parameter. 

        // Optional<Memo> result = memoRepository.findById(id); // you can't put constant variables in parameter. you have to make sure that you can take the parameter which users input. 

        // // First Method.
        // Memo memo = null;
        // if (result.isPresent()) {
        //     memo = result.get();
        // }

        // // Second Method
        // NoSuchElementException
        Memo memo = memoRepository.findById(id).orElseThrow(); // orElseThrow means, if it availavle, return the value. if it not exist, throws NoSuchElementException.  
        // entity => dto 변환 후 리턴
        // t
        return modelMapper.map(memo, MemoDTO.class);
    }
    
    // Modify one
    public Long modify(MemoDTO dto){
        //  1) Find the target to modify.
        //  if you want to modify, you have to get the target info(id).
        Memo memo = memoRepository.findById(dto.getId()).orElseThrow();

        //  2) Modify.
        memo.changeText(dto.getText());
        //  i don't know why she said that We get that MemoDTO as parameter. 
        // // memo = memoRepository.save(memo);
        // // return memo.getId();
        //  the memo can get the variable value of save.

        return memoRepository.save(memo).getId();
    }

    // Delete one
    public void remove(Long id){
        memoRepository.deleteById(id);
    }

    // Insert One
    public Long insert(MemoDTO dto){
        // dto -> entity
        Memo memo = modelMapper.map(dto, Memo.class);
        return memoRepository.save(memo).getId();
    }
}

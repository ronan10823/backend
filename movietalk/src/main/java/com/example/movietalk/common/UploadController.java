package com.example.movietalk.common;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.movietalk.movie.dto.MovieImageDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@Controller
@RequestMapping("/upload")
public class UploadController {

    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    @GetMapping("/upload")
    public void getUpload() {
        log.info("업로드 폼 요청");
    }

    @ResponseBody
    @PostMapping("/upload")
    public List<MovieImageDTO> postUpload(MultipartFile[] uploadFiles) {
        // 폴더에 저장
        // 1. 폴더 생성 메소드 호출
        String saveDirPath = makeDir();
        List<MovieImageDTO> upList = new ArrayList<>();

        for (MultipartFile file : uploadFiles) {
            log.info("OriginalFilename {}", file.getOriginalFilename());
            log.info("size {}", file.getSize());
            log.info("content type {}", file.getContentType());

            // uuid 생성
            String uuid = UUID.randomUUID().toString();
            // 파일명
            String oriName = file.getOriginalFilename();

            // 파일저장 정보를 화면단으로 전송하기 위한 객체
            upList.add(MovieImageDTO.builder().imgName(oriName).uuid(uuid).path(saveDirPath).build());

            try {
                // 저장
                // upload/2025/12/24/4ac87a98-9e6d-4522-afad-41dd3f95772b_test0.jpg
                String saveName = uploadPath + File.separator + saveDirPath + File.separator + uuid + "_" + oriName;
                File saveFile = new File(saveName);
                file.transferTo(saveFile);

                // 썸네일 저장
                String thumbSaveName = uploadPath + File.separator + saveDirPath + File.separator + "s_" + uuid + "_"
                        + oriName;
                File thumbFile = new File(thumbSaveName);
                Thumbnailator.createThumbnail(saveFile, thumbFile, 200, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return upList;
    }

    // 이미지 화면에 보여주기
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            // c:/upload/2025--
            File file = new File(uploadPath + File.separator + srcFileName);

            // if (!file.exists()) {
            //     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // }
            HttpHeaders headers = new HttpHeaders();
            // Files.probeContentType(file.toPath()) -=> png 등의 이미지 정보(image/png, image/jpeg
            // 등)를 브라우저에게 알려준다. 그래야 서버에서 준비할 수 있다.
            // header에 content-type 을 붙이지 않으면 브라우저가 임의로 그 타입을 지정하게 된다. ( mime type - mdn
            // 검색해서 확인)
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            // copyToByteArray(file) 이미지라서 byte 계열로 파일 정보 전송
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    // 이미지 파일 삭제
    @PostMapping("/remove")
    public ResponseEntity<String> removeFile(String fileName) {
        log.info("삭제할 fileName {}", fileName);
        ResponseEntity<String> result = null;

        try {
            // decoding을 해야한다. %2f와 같은 인코딩된 글자들을 /로 다시 바꿔줘야 한다.
            String srcFileName = URLDecoder.decode(fileName, "utf-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            // 원본 파일 삭제 (imageURL 원본 파일을 받았으니, file로 바로 삭제 가능)
            file.delete();

            // 썸네일 삭제 (썸네일 관련 파일 원본으로 직방으로 )
            File thumbFile = new File(file.getParent(), "s_" + file.getName());
            thumbFile.delete();

            result = new ResponseEntity<>("success", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    // 폴더 생성 메소드
    private String makeDir() {

        // new Date(); => SimpleDateFormat

        // 오늘날짜 구하기
        LocalDate today = LocalDate.now(); // 2025-12-24 => 2025/12/24
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 디렉토리 생성
        File file = new File(uploadPath, dateStr);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dateStr;
    }

}

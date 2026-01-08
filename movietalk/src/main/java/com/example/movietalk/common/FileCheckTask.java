package com.example.movietalk.common;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.movietalk.movie.dto.MovieImageDTO;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.repository.MovieImageRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class FileCheckTask {

    @Autowired
    private MovieImageRepository movieImageRepository;

    // 영화 이미지 업로드 경로
    @Value("${com.example.movietalk.upload.path}")
    private String uploadPath;

    // 전일자 폴더 추출
    private String getFolderYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        // LocalDate 타입 -> String 타입으로 변환
        String str = yesterday.toString(); // str "2026-01-05"
        // - (대시) -> 폴더 구분자로 변환
        return str.replace("-", File.separator);
    }

    @Scheduled(cron = "0 * * 2 * *")
    private void checkFile() {
        log.info("file check test");

        // 어제 날짜의 MovieImage 데이터베이스 가져오기 (DB의 path 경로 호출)
        String path = LocalDate.now()
                .minusDays(1)
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        List<MovieImage> oldImages = movieImageRepository.getOldFileImages(path);

        // 안전한 방법 : entity -> dto로 변환
        List<MovieImageDTO> movieImageDTOs = oldImages.stream().map(e -> {
            return MovieImageDTO.builder()
                    .inum(e.getInum())
                    .uuid(e.getUuid())
                    .imgName(e.getImgName())
                    .path(e.getPath())
                    .build();
        }).collect(Collectors.toList());

        // 데이터 베이스에서 가져온 파일 내용을 Paths 객체로 수집(파일 제거 시 + s_)
        // 파일 제거 시, 원본파일 + 썸네일(s_) 함께 제거
        List<Path> fileList = movieImageDTOs.stream()
                .map(dto -> Paths.get(uploadPath, dto.getPath(), dto.getUuid() + "_" + dto.getImgName()))
                .collect(Collectors.toList());

        movieImageDTOs.stream()
                .map(dto -> Paths.get(uploadPath, dto.getPath(), "s" + dto.getUuid() + "_" + dto.getImgName()))
                .forEach(p -> fileList.add(p));

        // 데이터 베이스 파일과 폼더 파일 비교하기
        // c:\\upload\\2025\\\01
        File tarFile = Paths.get(uploadPath, getFolderYesterday()).toFile();
        File[] removeFiles = tarFile.listFiles(f -> fileList.contains(f.toPath()) == false);

        if (removeFiles != null) {
            for (File file : removeFiles) {
                log.warn(file.getAbsolutePath());
                file.delete();
            }
        }
    }
}

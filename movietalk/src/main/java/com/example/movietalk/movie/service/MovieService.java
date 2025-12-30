package com.example.movietalk.movie.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.movie.dto.MovieDTO;
import com.example.movietalk.movie.dto.MovieImageDTO;
import com.example.movietalk.movie.dto.PageRequestDTO;
import com.example.movietalk.movie.dto.PageResultDTO;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.repository.MovieImageRepository;
import com.example.movietalk.movie.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@Service
@Log4j2
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;

    // 영화 삭제
    public void deleteRow(Long mno) {
        // 영화 이미지 제거
        Movie movie = movieRepository.findById(mno).get();
        movieImageRepository.deleteByMovie(movie);
        // 영화 삭제
        movieRepository.delete(movie);

    }

    // 영화 수정
    public Long updateRow(MovieDTO dto) {

        // 영화 타이틀(제목) 변경(수정)
        Movie movie = movieRepository.findById(dto.getMno()).get();
        movie.changeTitle(dto.getTitle());
        // movieRepository.save(movie); 이거를 안 함 왜? dirty checking을 하기 때문, 변경 사항을 자동으로
        // 감지해서 저장되기 때문

        // 영화 이미지 제거 (MovieImage에서 해당 mno의 이미지 모두 삭제)
        movieImageRepository.deleteByMovie(movie);

        // 영화 이미지 수정
        movie = dtoToEntity(dto);
        movie.getMovieImages().forEach(img -> movieImageRepository.save(img));

        return movie.getMno();
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public MovieDTO getRow(Long mno) {
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        // Movie 첫 번째 배열의 첫 번째 Movie만 가져오기
        Movie movie = (Movie) result.get(0)[0];

        List<MovieImage> movieImages = result.stream().map(en -> (MovieImage) en[1]).collect(Collectors.toList());

        // review 수 / 평점 첫 번째 배열의 첫 번째 값들 가져오기
        Long reviewCnt = (Long) result.get(0)[2];
        Double avg = (Double) result.get(0)[3];

        return entityToDto(movie, movieImages, reviewCnt, avg);
    }

    // N대 1의 관계일 때
    // public Long register(MovieDTO dto) {
    // Map<String, Object> entityMap = dtoToEntity((dto));

    // // 영화 정보 저장
    // Movie movie = (Movie) entityMap.get("movie");
    // movieRepository.save(movie);

    // // 영화 이미지 저장
    // List<MovieImage> imgList = (List<MovieImage>) entityMap.get("imgList");
    // imgList.forEach(img -> {
    // movieImageRepository.save(img);
    // });
    // return movie.getMno();
    // }

    // // 첫 번째 방법으로 한다면?
    // private Map<String, Object> dtoToEntity(MovieDTO dto) {

    // Map<String, Object> entityMap = new HashMap<>();

    // Movie movie = Movie.builder()
    // .mno(dto.getMno())
    // .title(dto.getTitle())
    // .build();
    // entityMap.put("movie", movie);

    // // List<MovieImageDTO> => List<MovieImage>
    // List<MovieImageDTO> imageDTOs = dto.getMovieImages();
    // if (imageDTOs != null && imageDTOs.size() > 0) {
    // List<MovieImage> movieImages = imageDTOs.stream().map(movieImage -> {
    // return MovieImage.builder()
    // .inum(movieImage.getInum())
    // .imgName(movieImage.getImgName())
    // .uuid(movieImage.getUuid())
    // .path(movieImage.getPath())
    // .movie(movie)
    // .build();
    // }).collect(Collectors.toList());
    // entityMap.put("imgList", movieImages);
    // }
    // return entityMap;
    // }

    // 1:N의 관계 추가 설정 + cascade = create와 유사함
    public String register(MovieDTO dto) {

        Movie movie = dtoToEntity(dto);
        return movieRepository.save(movie).getTitle();
    }

    private Movie dtoToEntity(MovieDTO dto) {

        Movie movie = Movie.builder()
                .mno(dto.getMno())
                .title(dto.getTitle())
                .build();

        // List<MovieImageDTO> => List<MovieImage>
        List<MovieImageDTO> imageDTOs = dto.getMovieImages();
        if (imageDTOs != null && imageDTOs.size() > 0) {
            imageDTOs.stream().forEach(movieImage -> {
                MovieImage image = MovieImage.builder()
                        .inum(movieImage.getInum())
                        .imgName(movieImage.getImgName())
                        .uuid(movieImage.getUuid())
                        .path(movieImage.getPath())
                        .movie(movie)
                        .build();
                movie.addImage(image);
            });
        }

        return movie;
    }

    // 전체조회
    @Transactional(readOnly = true)
    public PageResultDTO<MovieDTO> getMovieList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("mno").descending());
        // [Movie(mno=291, title=Movie Title 91), MovieImage(inum=887,
        // uuid=4ac87a98-9e6d-4522-afad-41dd3f95772b, path=null, imgName=test0.jpg,
        // ord=0), 0, 0.0]
        Page<Object[]> result = movieRepository.getListPage(pageable);

        // entity <=> dto : ModelMapper (dto, entity 동일한 모양일 때)

        // List<MovieDTO> dtoList = new ArrayList<>();
        // result.forEach(obj -> {
        // MovieDTO dto = entityToDto((Movie) obj[0], List.of((MovieImage) obj[1]),
        // (Long) obj[2], (Double) obj[3]);
        // dtoList.add(dto);
        // });

        Function<Object[], MovieDTO> function = (obj -> entityToDto((Movie) obj[0], List.of((MovieImage) obj[1]),
                (Long) obj[2], (Double) obj[3]));

        List<MovieDTO> dtoList = result.stream().map(function).collect(Collectors.toList());
        Long totalCount = result.getTotalElements();

        return PageResultDTO.<MovieDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    private MovieDTO entityToDto(Movie movie, List<MovieImage> mImages, Long reviewCnt, Double avg) {
        MovieDTO movieDTO = MovieDTO.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .avg(avg)
                .reviewCnt(reviewCnt)
                .createDate(movie.getCreateDate())
                .build();

        // List<MovieImage> => List<MovieImageDTO>
        List<MovieImageDTO> imageDTOs = mImages.stream().map(movieImage -> {
            return MovieImageDTO.builder()
                    .inum(movieImage.getInum())
                    .imgName(movieImage.getImgName())
                    .uuid(movieImage.getUuid())
                    .path(movieImage.getPath())
                    .build();
        }).collect(Collectors.toList());

        movieDTO.setMovieImages(imageDTOs);

        return movieDTO;
    }

}

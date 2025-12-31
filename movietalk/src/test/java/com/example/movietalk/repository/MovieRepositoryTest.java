package com.example.movietalk.repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.movietalk.member.entity.Member;
import com.example.movietalk.member.entity.constant.Role;
import com.example.movietalk.member.repository.MemberRepository;
import com.example.movietalk.movie.entity.Movie;
import com.example.movietalk.movie.entity.MovieImage;
import com.example.movietalk.movie.entity.Review;
import com.example.movietalk.movie.repository.MovieImageRepository;
import com.example.movietalk.movie.repository.MovieRepository;
import com.example.movietalk.movie.repository.ReviewRepository;

@Disabled
@SpringBootTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Commit
    @Transactional
    @Test
    public void deleteByMovieTest() {
        Movie movie = movieRepository.findById(118L).get();
        // 영화 이미지 삭제
        movieImageRepository.deleteByMovie(movie);
        // 영화
        movieRepository.delete(movie);
    }

    // @Transactional(readOnly = true)
    @Test
    public void getMovieReviewTest() {
        List<Review> result = reviewRepository.findByMovie(Movie.builder().mno(88L).build());

        result.forEach(r -> {
            System.out.println(r);
            // 리뷰 정보
            System.out.print(r.getRno() + "/t");
            System.out.print(r.getGrade() + "/t");
            System.out.print(r.getText() + "/t");
            // 리뷰작성자 조회
            System.out.print(r.getMember().getEmail() + "/t");
            System.out.print(r.getMember().getMid() + "/t");
            System.out.print(r.getMember().getNickname() + "/t");

            // 영화 정보 조회
            System.out.println(r.getMovie().getMno());
        });
    }

    @Test
    public void getMovieWithAllTest() {
        // [Movie(mno=100, title=Movie Title 100), MovieImage(inum=304,
        // uuid=8613366f-9c14-4fd7-a0ed-a7b560544263, path=null, imgName=test0.jpg,
        // ord=0), 5, 2.8]
        // [Movie(mno=100, title=Movie Title 100), MovieImage(inum=305,
        // uuid=b6d9aac2-3240-4188-a66b-c71e09be39eb, path=null, imgName=test1.jpg,
        // ord=1), 5, 2.8]
        // [Movie(mno=100, title=Movie Title 100), MovieImage(inum=306,
        // uuid=d0ee0470-4d97-42d0-9275-fd1020b3e80f, path=null, imgName=test2.jpg,
        // ord=2), 5, 2.8]
        // [Movie(mno=100, title=Movie Title 100), MovieImage(inum=307,
        // uuid=d6ee1601-3f3a-45d9-88cf-77dbdcff8c47, path=null, imgName=test3.jpg,
        // ord=3), 5, 2.8]

        List<Object[]> result = movieRepository.getMovieWithAll(100L);

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    // 조회
    // mno, 영화이미지중첫번째이미지, 영화제목, 리뷰수, 리뷰평균점수, 영화등록일
    @Test
    public void movieListTest() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void memberInsertTest() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .nickname("user" + i)
                    .password(passwordEncoder.encode("1111"))
                    .role(Role.MEMBER)
                    .build();
            memberRepository.save(member);
        });
    }

    @Test
    public void reviewInsertTest() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            // 영화번호 임의 추출
            long mno = (int) (Math.random() * 100) + 1;

            // 리뷰 사용자 임의 추출
            long mid = (int) (Math.random() * 10) + 1;
            Review review = Review.builder()
                    .member(Member.builder().mid(mid).build())
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int) (Math.random() * 5) + 1)
                    .text("review...." + i)
                    .build();

            reviewRepository.save(review);
        });
    }

    @Test
    public void insertTest() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder()
                    .title("Movie Title " + i)
                    .build();

            movieRepository.save(movie);

            // 임의의 이미지 삽입
            int count = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < count; j++) {
                MovieImage movieImage = MovieImage.builder()
                        // java.util.UUID
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .ord(j)
                        .imgName("test" + j + ".jpg")
                        .build();
                movieImageRepository.save(movieImage);
            }
        });

    }

}

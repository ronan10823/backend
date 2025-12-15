package com.example.board.post.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.board.member.entity.QMember;
import com.example.board.post.entity.Board;
import com.example.board.post.entity.QBoard;
import com.example.board.reply.entity.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Object[]> list(String type, String keyword, Pageable pageable) {
        log.info("board + reply + member join");

        // Q 클래스
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board)
                .leftJoin(member).on(board.writer.eq(member))
                .leftJoin(reply).on(reply.board.eq(board));

        // 데이터 베이스 Tuple == 레코드 == 하나의 행
        JPQLQuery<Tuple> tuple = query.select(board, member, reply.count());

        // where 절이 들어가줘야 페이지 나누기 절이 들어간다.
        // where 절 작성
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.bno.gt(0)); // where board.bno > 0 // bno = pk = index of itself. = the reaon of write the
                                      // gt(0)

        // type = "t" or type = "c" or type = "w" or type = "tc" or type = "tcw"
        // tc or tec => t, c, w
        // query statement
        if (type != null) {
            String[] typeArr = type.split(""); // "", 이렇게 하면 개별로 하나씩 잘라준다.

            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;

                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;

                    case "w":
                        // conditionBuilder.or(board.writer.email.contains(keyword));
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                }
            }

            builder.and(conditionBuilder);
        }

        tuple.where(builder);

        // order by
        Sort sort = pageable.getSort();

        // sort 기준이 여러 개 있을 수 있다.
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder<>(Board.class, "board");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });

        // sort 기준이 하나만 존재
        // tuple.orderBy(board.bno.desc());
        tuple.groupBy(board);

        // page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        log.info("==================================");
        log.info(query);
        log.info("==================================");

        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount(); // 전체 개수

        // List<Tuple> => List<Object[]> 변경
        List<Object[]> list = result.stream().map(t -> t.toArray()).collect(Collectors.toList());

        // 리턴값 List<Object[]> list, count, pageable
        return new PageImpl<>(list, pageable, count);
    }

}

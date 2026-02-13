package com.example.novels.novel.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNovel is a Querydsl query type for Novel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNovel extends EntityPathBase<Novel> {

    private static final long serialVersionUID = -1381964887L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNovel novel = new QNovel("novel");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath author = createString("author");

    public final BooleanPath available = createBoolean("available");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final QGenre genre;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> publishedDate = createDate("publishedDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QNovel(String variable) {
        this(Novel.class, forVariable(variable), INITS);
    }

    public QNovel(Path<? extends Novel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNovel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNovel(PathMetadata metadata, PathInits inits) {
        this(Novel.class, metadata, inits);
    }

    public QNovel(Class<? extends Novel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.genre = inits.isInitialized("genre") ? new QGenre(forProperty("genre")) : null;
    }

}


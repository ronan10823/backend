package com.example.novels.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 839106103L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final BooleanPath fromSocial = createBoolean("fromSocial");

    public final StringPath nickname = createString("nickname");

    public final StringPath pw = createString("pw");

    public final SetPath<com.example.novels.member.entity.constant.MemberRole, EnumPath<com.example.novels.member.entity.constant.MemberRole>> roles = this.<com.example.novels.member.entity.constant.MemberRole, EnumPath<com.example.novels.member.entity.constant.MemberRole>>createSet("roles", com.example.novels.member.entity.constant.MemberRole.class, EnumPath.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}


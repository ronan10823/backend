package com.example.ai.service;

import java.util.Map;

import org.springframework.ai.chat.prompt.PromptTemplate;

public class PromptFactory {

    private static final String TEMPLATE = """
            당신은 최고의 카피라이터 입니다.
            제품 메타 데이터를 참고해서 1~2줄짜리 광고 문구 5개 작성해줘

            [제품 메타 데이터]
            - 제품명 : {name}
            - 브랜드명 : {brand}
            - 브랜드 핵심 가치 : {value}
            - 제품특징 : {strength}
            - 톤앤매너 : {tone}
            - 필수 포함 키워드 : {keyword}
            """;

    public static String render(String name,
            String brand,
            String strength,
            String tone,
            String keyword,
            String value) {

        var pt = new PromptTemplate(TEMPLATE);

        return pt.render(Map.of("name", name,
                "brand", brand,
                "value", value,
                "strength", strength,
                "tone", tone,
                "keyword", keyword));

    }
}

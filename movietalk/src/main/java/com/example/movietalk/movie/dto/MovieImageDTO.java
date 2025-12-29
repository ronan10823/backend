package com.example.movietalk.movie.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieImageDTO {
    private Long inum;

    private String uuid;

    private String path;

    private String imgName;

    public String getThumbnailURL() {
        String thumbFullPath = "";

        // java.net.URL~~
        try {
            // 2025/12/24/s_4ac87a98-9e6d-4522-afad-41dd3f95772b_test0.jpg
            thumbFullPath = URLEncoder.encode(path + "/s_" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return thumbFullPath;
    }

    public String getImageURL() {
        String fullPath = "";

        // java.net.URL~~
        try {
            // 2025/12/24/4ac87a98-9e6d-4522-afad-41dd3f95772b_test0.jpg
            fullPath = URLEncoder.encode(path + "/" + uuid + "_" + imgName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return fullPath;
    }
}

package com.example.rus.exercise2_2.utils;

import com.example.rus.exercise2_2.db.NewsEntity;
import com.example.rus.exercise2_2.network.dto.Multimedium;
import com.example.rus.exercise2_2.network.dto.Result;

import java.util.ArrayList;

public class FromNetToDbConverter {

    public static Result fromDatabase(NewsEntity newsEntity) {
        Result result = new Result();
        result.subsection = newsEntity.subsection;
        result.title = newsEntity.title;
        result._abstract = newsEntity.previewText;
        result.url = newsEntity.url;
        result.publishedDate = newsEntity.publishedDate;
        Multimedium multimedium = new Multimedium();
        multimedium.url = newsEntity.multimediaUrl;
        result.multimedia = new ArrayList<>();
        result.multimedia.add(multimedium);
        return result;
    }

    public static NewsEntity toDatabase(Result result) {
        NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(result.title+result.url);
        if (result.subsection.isEmpty()){
            newsEntity.setSubsection("");
        } else {
            newsEntity.setSubsection(result.subsection);
        }
        newsEntity.setTitle(result.title);
        newsEntity.setPreviewText(result._abstract);
        newsEntity.setUrl(result.url);
        newsEntity.setPublishedDate(result.publishedDate);
        if (result.multimedia.size()>0) {
            newsEntity.setMultimediaUrl(result.multimedia.get(1).url);
        } else {
            newsEntity.setMultimediaUrl("");
        }
        return newsEntity;
    }
}

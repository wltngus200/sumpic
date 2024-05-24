package com.green.webclienttest.picsum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PicSumGetRes {
    private String id;
    private String author;

    private String downloadUrl;

    @JsonProperty(value="downloadUrl")
    public String getDownloadUrl(){
        return downloadUrl;
    }

    @JsonProperty(value="download_url")
    public void SetDownloadUrl(String downloadUrl){
        this.downloadUrl=downloadUrl;
    }
    //똑같은 구조를 만들고 어레이리스트 만들기
}

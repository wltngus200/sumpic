package com.green.webclienttest.picsum;

import com.green.webclienttest.picsum.model.PicSumGetReq;
import com.green.webclienttest.picsum.model.PicSumGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/picsum")
public class PicSumController {
    private final PicSumService service;

    @GetMapping
    public List<PicSumGetRes> getPicSum(@ParameterObject @ModelAttribute PicSumGetReq p){
        return service.getPicSum(p);
    }
    //포스트 맨은 차이 X 파라미터 오브젝트는 스웨거에 영향
}

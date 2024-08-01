package com.plog.backend.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forever")
public class CheckController {
    @GetMapping
    public String test() {
        StringBuilder sb = new StringBuilder();
        sb.append("우리팀에 하늘같은 인프라를 담당하여 주어 감사합니다 ^^7\n");
        sb.append("처음 하는 거라 힘들텐데 아주 잘 해주어 항상 감사하군요\n");
        sb.append("피해가 되지 않게 저도 열심히 한 번 해보겠습니다 !!!!!!!!!!!!!!!!!! \n");
        sb.append("우리팀 파이팅 힘내세요 파이팅~~!!!!!!!!!!!!!");
        return sb.toString();
    }
}

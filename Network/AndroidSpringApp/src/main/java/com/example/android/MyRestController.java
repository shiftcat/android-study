package com.example.android;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class MyRestController {


    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }


    /**
     * Get Request
     *
     * @param param1
     * @param param2
     * @return
     */
    @GetMapping("/service1")
    public ResponseEntity<ResponseVO> service1(
            @RequestParam(value = "param1", required = false) String param1,
            @RequestParam(value = "param2", required = false) String param2
    ) {
        ResponseVO res = new ResponseVO();
        res.setData1(param1 + param2);
        res.setData2(200);
        res.setData3(9.18);
        return ResponseEntity.ok(res);
    }


    /**
     * JSON REST
     *
     * @param requestVO
     * @return
     */
    @PostMapping("/service2")
    public ResponseEntity<ResponseVO> service2(@RequestBody RequestVO requestVO) {
        ResponseVO res = new ResponseVO();
        res.setData1(requestVO.getParam1() + requestVO.getParam2());
        res.setData2(100);
        res.setData3(3.14);
        return ResponseEntity.ok(res);
    }


    /**
     * 폼 데이터 처리
     *
     * @param vo
     * @return
     */
    @PostMapping("/postsvc")
    public ResponseEntity<String> postService(@ModelAttribute RequestVO vo) {
        return ResponseEntity.ok(vo.getParam1() + vo.getParam2());
    }


}

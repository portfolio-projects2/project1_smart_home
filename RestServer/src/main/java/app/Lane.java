package app;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Lane {

    @RequestMapping("/test")
    void test(@RequestBody String body){
        System.out.println(body);
    }

}

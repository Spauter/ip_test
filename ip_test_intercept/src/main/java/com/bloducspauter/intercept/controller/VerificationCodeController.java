package com.bloducspauter.intercept.controller;



import com.bloducspauter.base.utils.CreateVerificationCode;
import com.bloducspauter.base.utils.CreateVerificationCodeImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * @author 32306
 */
@RestController
@Slf4j
@RequestMapping("intercept")
public class VerificationCodeController {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("/verify")
    public void verifyCode(HttpServletResponse resp) throws IOException {
        String decode = CreateVerificationCode.getSecurityCode();
        //Session交给了redis去管理
//      HttpSession session = req.getSession();
        redisTemplate.opsForValue().set("verifyCode",decode);
        log.info("verifyCode:"+decode);
        //设置返回的内容
        resp.setContentType("img/jpeg");
        //浏览器不缓存响应内容--验证码图片，点一次就要刷新一次，所以不能有缓存出现
        resp.setHeader("Pragma","No-cache");
        resp.setHeader("Cache-Control","no-cache");
        //设置验证码失效时间
        resp.setDateHeader("Expires",0);
        //以字节流发过去，交给img的src属性去解析即可
        ImageIO.write(new CreateVerificationCodeImage(decode).createImage(),"JPEG",resp.getOutputStream());
    }

    @RequestMapping("getCode")
    public String getCode(HttpServletResponse resp, HttpSession session) throws IOException {
        Object code= redisTemplate.opsForValue().get("verifyCode");
        return code==null?"oldfu":code.toString();
    }
}

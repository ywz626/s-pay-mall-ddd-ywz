package com.ywz.domain.auth.service;

import com.google.common.cache.Cache;
import com.ywz.domain.auth.adapter.port.ILoginPort;
import com.ywz.types.common.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 于汶泽
 */
@Service
@Slf4j
public class WeixinLoginService implements ILoginService {

    @Resource
    private Cache<String, String> openidToken;
    @Resource
    private ILoginPort loginPort;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String createQrCodeTicket() throws Exception {
        return loginPort.createQrCodeTicket();
    }

    @Override
    public String checkLogin(String ticket) {
        return stringRedisTemplate.opsForValue().get(RedisConstants.OPEN_ID_TICKET + ticket);
    }

    @Override
    public void saveLoginState(String ticket, String openid, HttpServletRequest request) throws IOException {
        stringRedisTemplate.opsForValue().set(RedisConstants.OPEN_ID_TICKET + ticket, openid);
//        openidToken.put(ticket, openid);
//        //发送模板消息
        loginPort.sendLoginTemplate(openid);
    }
}

package com.ywz.domain.auth.service;

import com.google.common.cache.Cache;
import com.ywz.domain.auth.adapter.port.ILoginPort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 于汶泽
 */
@Service
public class WeixinLoginService implements ILoginService {

    @Resource
    private Cache<String, String> openidToken;
    @Resource
    private ILoginPort loginPort;

    @Override
    public String createQrCodeTicket() throws Exception {
        return loginPort.createQrCodeTicket();
    }

    @Override
    public String checkLogin(String ticket) {
        return openidToken.getIfPresent(ticket);
    }

    @Override
    public void saveLoginState(String ticket, String openid, HttpServletRequest request) throws IOException {
        openidToken.put(ticket, openid);
        //发送模板消息
        loginPort.sendLoginTemplate(openid);
    }
}

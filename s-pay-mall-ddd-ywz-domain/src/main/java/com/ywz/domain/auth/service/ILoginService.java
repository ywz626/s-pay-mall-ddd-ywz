package com.ywz.domain.auth.service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 于汶泽
 */
public interface ILoginService {
    String createQrCodeTicket() throws Exception;

    String checkLogin(String ticket);

    void saveLoginState(String ticket, String openid, HttpServletRequest request) throws IOException;
}

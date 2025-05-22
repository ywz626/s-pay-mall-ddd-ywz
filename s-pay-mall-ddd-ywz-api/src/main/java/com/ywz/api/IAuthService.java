package com.ywz.api;

import com.ywz.api.response.Response;

/**
 * @author 于汶泽
 */
public interface IAuthService {

    Response<String> weixinQrCodeTicket();

    Response<String> checkLogin(String ticket);
}

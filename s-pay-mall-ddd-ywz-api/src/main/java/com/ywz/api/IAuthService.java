package com.ywz.api;

import com.ywz.api.response.Response;

/**
 * @author 于汶泽
 */
public interface IAuthService {

    public Response<String> weixinQrCodeTicket();

    public Response<String> checkLogin(String ticket);
}

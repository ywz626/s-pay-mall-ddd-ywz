package com.ywz.domain.auth.adapter.port;

import java.io.IOException;

/**
 * @author 于汶泽
 */
public interface ILoginPort {
    String createQrCodeTicket() throws Exception;

    void sendLoginTemplate(String openid) throws IOException;
}

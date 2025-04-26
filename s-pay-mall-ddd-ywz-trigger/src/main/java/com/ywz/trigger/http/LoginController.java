package com.ywz.trigger.http;

import com.ywz.api.IAuthService;
import com.ywz.api.response.Response;
import com.ywz.domain.auth.service.ILoginService;
import com.ywz.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 于汶泽
 */
@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/login/")
public class LoginController implements IAuthService {

    @Resource
    private ILoginService loginService;

    @Override
    @GetMapping("weixin_qrcode_ticket")
    public Response<String> weixinQrCodeTicket() {
        try{
            String qrCodeTicket = loginService.createQrCodeTicket();
            assert qrCodeTicket != null;
            log.info("微信二维码ticket：{}", qrCodeTicket);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(qrCodeTicket)
                    .build();
        }catch (Exception e){
            log.error("微信二维码ticket生成失败：{}", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    @GetMapping("check_login")
    public Response<String> checkLogin(@RequestParam String ticket) {
        try{
            String checkLogin = loginService.checkLogin(ticket);
            log.info("扫码登陆结果:{},{}", ticket, checkLogin);
            if(checkLogin != null){
                return Response.<String>builder()
                        .code(Constants.ResponseCode.SUCCESS.getCode())
                        .info(Constants.ResponseCode.SUCCESS.getInfo())
                        .data(checkLogin)
                        .build();
            }
            else {
                return Response.<String>builder()
                        .code(Constants.ResponseCode.UN_ERROR.getCode())
                        .info(Constants.ResponseCode.UN_ERROR.getInfo())
                        .build();
            }
        }catch (Exception e){
            log.info("扫码登陆失败：{}", e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}

package com.ywz.infrastructure.adapter.port;

import com.google.common.cache.Cache;
import com.ywz.domain.auth.adapter.port.ILoginPort;
import com.ywz.infrastructure.gateway.IWeixinApiService;
import com.ywz.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import com.ywz.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import com.ywz.infrastructure.gateway.dto.WeixinTemplateMessageDTO;
import com.ywz.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import com.ywz.types.common.RedisConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 于汶泽
 */
@Service
@Slf4j
public class LoginPort implements ILoginPort {

    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template_id}")
    private String templateId;

    @Resource
    private Cache<String, String> weixinAccessToken;
    @Resource
    private IWeixinApiService weixinApiService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public LoginPort() throws IOException {
    }

    @Override
    public String createQrCodeTicket() throws Exception {
//        String tocken = weixinAccessToken.getIfPresent(appid);
        String tocken = stringRedisTemplate.opsForValue().get(RedisConstants.WEIXIN_ACCESS_TOKEN + appid);
        if (tocken == null) {
            Call<WeixinTokenResponseDTO> call = weixinApiService.getAccessToken("client_credential", appid, appSecret);
            WeixinTokenResponseDTO weixinTokenRes = call.execute().body();
            assert weixinTokenRes != null;
            tocken = weixinTokenRes.getAccess_token();
            log.info("tocken:{}", tocken);
//            weixinAccessToken.put(appid, tocken);
            stringRedisTemplate.opsForValue().set(RedisConstants.WEIXIN_ACCESS_TOKEN + appid, tocken);
        }


        WeixinQrCodeRequestDTO weixinQrCodeReq = WeixinQrCodeRequestDTO.builder()
                .expire_seconds(2592000)
                .action_name(WeixinQrCodeRequestDTO.ActionNameTypeVO.QR_SCENE.getCode())
                .action_info(WeixinQrCodeRequestDTO.ActionInfo.builder()
                        .scene(WeixinQrCodeRequestDTO.ActionInfo.Scene.builder()
                                .scene_id(100601)
                                .build())
                        .build())
                .build();
        Call<WeixinQrCodeResponseDTO> call = weixinApiService.createQrCode(tocken, weixinQrCodeReq);
        WeixinQrCodeResponseDTO weixinQrCodeRes = call.execute().body();
        assert null!=weixinQrCodeRes;
        return weixinQrCodeRes.getTicket();
    }

    @Override
    public void sendLoginTemplate(String openid) throws IOException {
// 1. 获取 accessToken 【实际业务场景，按需处理下异常】
        String accessToken = stringRedisTemplate.opsForValue().get(RedisConstants.WEIXIN_ACCESS_TOKEN + appid);
        if (null == accessToken){
            Call<WeixinTokenResponseDTO> call = weixinApiService.getAccessToken("client_credential", appid, appSecret);
            WeixinTokenResponseDTO weixinTokenResponseDTO = call.execute().body();
            assert weixinTokenResponseDTO != null;
            accessToken = weixinTokenResponseDTO.getAccess_token();
            stringRedisTemplate.opsForValue().set(RedisConstants.WEIXIN_ACCESS_TOKEN + appid, accessToken);
        }

        // 2. 发送模板消息
        Map<String, Map<String, String>> data = new HashMap<>();
        WeixinTemplateMessageDTO.put(data, WeixinTemplateMessageDTO.TemplateKey.USER, openid);

        WeixinTemplateMessageDTO templateMessageDTO = new WeixinTemplateMessageDTO(openid, templateId);
        templateMessageDTO.setUrl("https://gaga.plus");
        templateMessageDTO.setData(data);

        Call<Void> call = weixinApiService.sendMessage(accessToken, templateMessageDTO);
        call.execute();
    }
}

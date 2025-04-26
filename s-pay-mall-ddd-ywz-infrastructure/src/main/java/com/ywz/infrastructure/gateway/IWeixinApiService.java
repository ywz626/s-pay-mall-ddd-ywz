package com.ywz.infrastructure.gateway;


import com.ywz.infrastructure.gateway.dto.WeixinQrCodeRequestDTO;
import com.ywz.infrastructure.gateway.dto.WeixinQrCodeResponseDTO;
import com.ywz.infrastructure.gateway.dto.WeixinTemplateMessageDTO;
import com.ywz.infrastructure.gateway.dto.WeixinTokenResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author 于汶泽
 */
public interface IWeixinApiService {
    /**
     * 获取AccessToken
     *
     * @param grant_type 授权类型
     * @param appid      小程序ID
     * @param secret     小程序密钥
     * @return WeixinTokenRes
     */
    @GET("cgi-bin/token")
    Call<WeixinTokenResponseDTO> getAccessToken(@Query("grant_type") String grant_type,
                                                @Query("appid") String appid,
                                                @Query("secret") String secret);

    /**
     * @param accessToken
     * @param weixinQrCodeRequestDTO
     * @return
     */
    @POST("cgi-bin/qrcode/create")
    Call<WeixinQrCodeResponseDTO> createQrCode(@Query("access_token") String accessToken, @Body WeixinQrCodeRequestDTO weixinQrCodeRequestDTO);

    /**
     * Send template message
     *
     * @param accessToken 授权token
     * @param weixinTemplateMessageDTO 模板消息请求参数
     * @return Void
     */
    @POST("cgi-bin/message/template/send")
    Call<Void> sendMessage(@Query("access_token") String accessToken, @Body WeixinTemplateMessageDTO weixinTemplateMessageDTO);
}

package cn.iocoder.yudao.module.system.service.oauth2;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.oauth2.vo.token.OAuth2AccessTokenPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;

import java.util.List;

public interface OAuth2TokenService {

    OAuth2AccessTokenDO createAccessToken(Long userId, Integer userType, String clientId, List<String> scopes);

    OAuth2AccessTokenDO refreshAccessToken(String refreshToken);

    OAuth2AccessTokenDO getAccessToken(String accessToken);

    OAuth2AccessTokenDO checkAccessToken(String accessToken);

    OAuth2AccessTokenDO removeAccessToken(String accessToken);

    void removeAccessToken(Long userId, Integer userType);

    PageResult<OAuth2AccessTokenDO> getAccessTokenPage(OAuth2AccessTokenPageReqVO reqVO);

}

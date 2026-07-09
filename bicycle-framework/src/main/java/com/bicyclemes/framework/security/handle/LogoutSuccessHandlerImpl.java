package com.bicyclemes.framework.security.handle;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson2.JSON;
import com.bicyclemes.common.constant.Constants;
import com.bicyclemes.common.core.domain.AjaxResult;
import com.bicyclemes.common.core.domain.model.LoginUser;
import com.bicyclemes.common.utils.MessageUtils;
import com.bicyclemes.common.utils.ServletUtils;
import com.bicyclemes.common.utils.StringUtils;
import com.bicyclemes.framework.manager.AsyncManager;
import com.bicyclemes.framework.manager.factory.AsyncFactory;
import com.bicyclemes.framework.web.service.TokenService;

/**
 * 自定义退出处理类 返回成功
 * 
 * @author BicycleMES
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     * 
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success(MessageUtils.message("user.logout.success"))));
    }
}

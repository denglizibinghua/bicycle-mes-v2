package com.bicyclemes.mes.domain;

/**
 * AI 对话请求
 *
 * @author BicycleMES
 */
public class AiChatRequest
{
    /** 用户输入 */
    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
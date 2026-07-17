package com.bicyclemes.mes.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bicyclemes.common.core.domain.AjaxResult;
import com.bicyclemes.mes.domain.AiChatRequest;
import com.bicyclemes.mes.service.impl.MesAiTools;

/**
 * MES AI 对话接口
 *
 * @author BicycleMES
 */
@RestController
@RequestMapping("/api/ai")
public class MesAiController
{
    @Autowired
    private ChatClient chatClient;

    @Autowired
    private MesAiTools mesAiTools;

    private static final String SYSTEM_PROMPT = """
        你是自行车MES系统的AI助手。你的任务包括：
        1. 当用户想打开某个页面时，调用 navigateToPage 工具获取路由
        2. 当用户想查询数据时，调用 queryWorkOrderStats 或 queryProductionReports 工具
        3. 当用户想创建工单时，必须先调用 listMaterials 和 listProductionLines 获取物料和产线的ID-名称对照表，
           从中匹配用户提到的名称找到对应ID（模糊匹配，比如用户说"车架"就找material_name包含"车架"的），
           然后调用 createWorkOrder 工具。日期如果用户没提供，默认用今天+7天

        在回复时：
        - 如果调用了 navigateToPage，回复中包含"已为您导航到[页面名称]"，系统会自动跳转
        - 如果调用了查询工具，用清晰的中文总结数据
        - 如果创建了工单，回复工单号和后续操作建议
        - 如果用户问题超出你的能力范围，如实告知
        """;

    /**
     * AI 对话
     */
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody AiChatRequest request)
    {
        try
        {
            String reply = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(request.getMessage())
                .tools(mesAiTools)
                .call()
                .content();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("reply", reply);

            // 检测回复中是否包含导航意图
            for (Map.Entry<String, String> entry : MesAiTools.PAGE_ROUTES.entrySet())
            {
                if (reply.contains(entry.getKey()))
                {
                    result.put("action", "navigate");
                    Map<String, String> navData = new LinkedHashMap<>();
                    navData.put("route", entry.getValue());
                    navData.put("label", entry.getKey());
                    result.put("data", navData);
                    break;
                }
            }

            return AjaxResult.success(result);
        }
        catch (Exception e)
        {
            return AjaxResult.error("AI服务异常：" + e.getMessage());
        }
    }
}
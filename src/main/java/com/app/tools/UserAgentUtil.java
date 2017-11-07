package com.app.tools;

/**
 * 本类实现了UASparser的单例，该实例可通过分析user-agent信息判断当前Http请求的客户端浏览器类型
 *
 * @author hduser
 */
public class UserAgentUtil {

    /**
     * 是否移动设备
     *
     * @param agent
     * @return
     */
    public static String CheckAgent(String agent) {
        String flag = "PC";

        String[] keywords = {"Android", "iPhone", "iPod", "iPad",
                "Windows Phone", "MQQBrowser"};

        // 排除 Windows 桌面系统
        if (!agent.contains("Windows NT")
                || (agent.contains("Windows NT") && agent
                .contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!agent.contains("Windows NT") && !agent.contains("Macintosh")) {
                for (String item : keywords) {
                    if (agent.contains(item)) {
                        flag = "MOBILE";
                        break;
                    }
                }
            }
        }
        return flag;
    }
}
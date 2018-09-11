package cn.qblank.wxdto;

import lombok.Data;

/**
 * @author evan_qb
 * @date 2018/8/13 9:56
 * 微信自定义菜单测试
 */
@Data
public class Menu {
    /** 一级菜单*/
    private String button;
    /** 二级菜单*/
    private String sub_button;
    /** 菜单的响应动作类型，view表示网页类型，click表示点击类型，miniprogram表示小程序类型*/
    private String type;
    /** 菜单标题，不超过16个字节，子菜单不超过60个字节*/
    private String name;
    /** 菜单KEY值，用于消息接口推送，不超过128字节*/
    private String key;
    /** 网页 链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url。*/
    private String url;

    private String media_id;

    private String appid;

    private String pagepath;
}

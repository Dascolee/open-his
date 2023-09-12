package com.dasco.openhis.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 构造菜单返回给前台的vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeVo {
    private String id;
    //菜单表里面的url
    private String serPath;

    private boolean show = true; //是否显示
}

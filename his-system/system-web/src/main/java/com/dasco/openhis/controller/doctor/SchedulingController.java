package com.dasco.openhis.controller.doctor;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.domain.Scheduling;
import com.dasco.openhis.domain.User;
import com.dasco.openhis.dto.SchedulingDto;
import com.dasco.openhis.dto.SchedulingFormDto;
import com.dasco.openhis.dto.SchedulingQueryDto;
import com.dasco.openhis.service.SchedulingService;
import com.dasco.openhis.service.UserService;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("doctor/scheduling")
public class SchedulingController {

    @Reference
    private SchedulingService schedulingService;

    @Autowired
    private UserService userService;

    /**
     * 查询参与排班的医生信息
     * @param deptId
     * @return
     */
    @GetMapping("queryUsersNeedScheduling")
    public AjaxResult queryUsersNeedScheduling(Long deptId){
        List<User> users = userService.queryUsersNeedScheduling(null,deptId);
        return AjaxResult.success(users);
    }

    /**
     * 查询当前用户的排班信息
     * @param schedulingQueryDto
     * @return
     */
    @GetMapping("queryMyScheduling")
    public AjaxResult queryMyScheduling(SchedulingQueryDto schedulingQueryDto){
        List<User> users = userService.queryUsersNeedScheduling(ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserId(),schedulingQueryDto.getDeptId());
        return getSchedulingAjaxResult(schedulingQueryDto,users);
    }

    /**
     * 查询其它医生的排班信息
     * @param schedulingQueryDto
     * @return
     */
    @GetMapping("queryScheduling")
    public AjaxResult queryScheduling(SchedulingQueryDto schedulingQueryDto){
        List<User> users = userService.queryUsersNeedScheduling(schedulingQueryDto.getUserId(),schedulingQueryDto.getDeptId());
        return getSchedulingAjaxResult(schedulingQueryDto,users);
    }

    /**
     * 保存排班数据
     * @param schedulingFormDto
     * @return
     */
    @PostMapping("saveScheduling")
    public AjaxResult saveScheduling(@RequestBody SchedulingFormDto schedulingFormDto){
        schedulingFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(schedulingService.saveScheduling(schedulingFormDto));
    }

    /**
     * 构造排班数据表格
     * @param schedulingQueryDto
     * @param users
     * @return
     */
    private AjaxResult getSchedulingAjaxResult(SchedulingQueryDto schedulingQueryDto, List<User> users) {
        //取得当前时间
        DateTime date = DateUtil.date();
        if(StringUtils.isNoneBlank(schedulingQueryDto.getQueryDate())){
            date = DateUtil.parse(schedulingQueryDto.getQueryDate(), "yyyy-MM-dd");
            //根据页面传来的日期进行判断是周几
            int i = DateUtil.dayOfWeek(date); //1-周日 2-周一
            if(i == 1){
                date = DateUtil.offsetDay(date, 1);     //下一周周一的日期
            }else{
                date = DateUtil.offsetDay(date, -1);    //上一周的周日日期
            }
        }
        //计算一周的开始日期和结束日期
        DateTime beginTime = DateUtil.beginOfWeek(date);
        DateTime endTime = DateUtil.endOfWeek(date);
        //设置开始日期和结束日期到SchedulingQueryDto
        schedulingQueryDto.setBeginDate(DateUtil.format(beginTime,"yyyy-MM-dd"));
        schedulingQueryDto.setEndDate(DateUtil.format(endTime,"yyyy-MM-dd"));
        //根据开始日期和结束日期查询his_scheduling的值班数据
        List<Scheduling> list = schedulingService.queryScheduling(schedulingQueryDto);
        ArrayList<SchedulingDto> schedulingDtos = new ArrayList<>();
        //根据用户来循环
        for (User user : users) {
            SchedulingDto schedulingDto1 = new SchedulingDto(user.getUserId(), user.getDeptId(), "1", initMap(beginTime));
            SchedulingDto schedulingDto2 = new SchedulingDto(user.getUserId(), user.getDeptId(), "2", initMap(beginTime));
            SchedulingDto schedulingDto3 = new SchedulingDto(user.getUserId(), user.getDeptId(), "3", initMap(beginTime));
            //一个用户都有这三条数据
            schedulingDtos.add(schedulingDto1);
            schedulingDtos.add(schedulingDto2);
            schedulingDtos.add(schedulingDto3);
            for (Scheduling scheduling : list) {
                Long userId = scheduling.getUserId();   //获取表里用户id
                String subsectionType = scheduling.getSubsectionType(); //值班的时间段 早中晚
                String schedulingDay = scheduling.getSchedulingDay();   //值班日期
                if(user.getUserId().equals(userId)){
                    switch (subsectionType){
                        case "1":
                            Map<String, String> record1 = schedulingDto1.getRecord();
                            record1.put(schedulingDay,scheduling.getSchedulingType());
                            break;
                        case "2":
                            Map<String, String> record2 = schedulingDto2.getRecord();
                            record2.put(schedulingDay,scheduling.getSchedulingType());
                            break;
                        case "3":
                            Map<String, String> record3 = schedulingDto3.getRecord();
                            record3.put(schedulingDay,scheduling.getSchedulingType());
                            break;
                    }
                }
            }
            //把map转成数组数据放到SchedulingType
            schedulingDto1.setSchedulingType(schedulingDto1.getRecord().values());
            schedulingDto2.setSchedulingType(schedulingDto2.getRecord().values());
            schedulingDto3.setSchedulingType(schedulingDto3.getRecord().values());
        }
        //组装返回的对象
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("tableData",schedulingDtos);

        HashMap<String, Object> schedulingData = new HashMap<>();
        schedulingData.put("startTimeThisWeek",schedulingQueryDto.getBeginDate());
        schedulingData.put("endTimeThisWeek",schedulingQueryDto.getEndDate());
        resMap.put("schedulingData",schedulingData);
        resMap.put("labelNames",initLabel(beginTime));
        return AjaxResult.success(resMap);
    }

    /**
     * 初始化labelNames
     * @param beginTime
     * @return
     */
    private Object initLabel(DateTime beginTime) {
        String[] labelNames = new String[7];
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            labelNames[i] = DateUtil.format(d,"yyyy-MM-dd") + formatterWeek(i);
        }
        return labelNames;
    }

    public static void main(String[] args) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse("2022-02-07");
            String[] labelNames = new String[7];
            for (int i = 0; i < 7; i++) {
                DateTime d = DateUtil.offsetDay(date1, i);
                labelNames[i] = DateUtil.format(d,"yyyy-MM-dd");
            }
            for (int i = 0; i < labelNames.length; i++) {
                System.out.println(labelNames[i]);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 翻译周
     *
     * @param i
     * @return
     */
    private String formatterWeek(int i) {
        switch (i) {
            case 0:
                return "(周一)";
            case 1:
                return "(周二)";
            case 2:
                return "(周三)";
            case 3:
                return "(周四)";
            case 4:
                return "(周五)";
            case 5:
                return "(周六)";
            default:
                return "(周日)";
        }
    }

    /**
     * 初始化值班记录
     * @param beginTime
     * @return
     */
    private Map<String, String> initMap(DateTime beginTime) {
        Map<String,String> map = new TreeMap<>();
        for (int i = 0; i < 7; i++) {
            DateTime d = DateUtil.offsetDay(beginTime, i);
            String key = DateUtil.format(d, "yyyy-MM-dd");
            map.put(key,"");
        }
        return map;
    }


}

package com.dasco.openhis.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.Scheduling;
import com.dasco.openhis.dto.SchedulingFormDto;
import com.dasco.openhis.dto.SchedulingQueryDto;
import com.dasco.openhis.mapper.SchedulingMapper;
import com.dasco.openhis.service.SchedulingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
* @author a
* @description 针对表【his_scheduling(排班信息表)】的数据库操作Service实现
*/
@Service
public class SchedulingServiceImpl extends ServiceImpl<SchedulingMapper, Scheduling> implements SchedulingService{

    @Autowired
    private SchedulingMapper schedulingMapper;

    @Override
    public List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto) {
        QueryWrapper<Scheduling> wrapper = new QueryWrapper<>();
        wrapper.eq(schedulingQueryDto.getUserId() != null, Scheduling.COL_USER_ID,
                schedulingQueryDto.getUserId());
        wrapper.eq(schedulingQueryDto.getDeptId() != null, Scheduling.COL_DEPT_ID,
                schedulingQueryDto.getDeptId());
        wrapper.ge(StringUtils.isNotBlank(schedulingQueryDto.getBeginDate()), Scheduling.COL_SCHEDULING_DAY,
                schedulingQueryDto.getBeginDate());
        wrapper.le(StringUtils.isNotBlank(schedulingQueryDto.getEndDate()), Scheduling.COL_SCHEDULING_DAY,
                schedulingQueryDto.getEndDate());
        return schedulingMapper.selectList(wrapper);
    }

    @Override
    public int saveScheduling(SchedulingFormDto schedulingFormDto) {
        if(schedulingFormDto.getData() != null && schedulingFormDto.getData().size() > 0){
            DateTime dateTime = DateUtil.parse(schedulingFormDto.getBeginDate(), "yyyy-MM-dd");
            //得到DateTime所在的周的周一
            DateTime beginDate = DateUtil.beginOfWeek(dateTime);
            DateTime endDate = DateUtil.endOfWeek(dateTime);
            String start = DateUtil.format(beginDate, "yyyy-MM-dd");
            String end = DateUtil.format(endDate, "yyyy-MM-dd");
            //获取用户名和部门
            SchedulingFormDto.SchedulingData schedulingData = schedulingFormDto.getData().get(0);
            Long userId = schedulingData.getUserId();
            Long deptId = schedulingData.getDeptId();
            if(userId != null){
                //先删除该用户所在周的排班数据
                QueryWrapper<Scheduling> wrapper = new QueryWrapper<>();
                wrapper.eq(Scheduling.COL_USER_ID,userId);
                wrapper.eq(Scheduling.COL_DEPT_ID,deptId);
                wrapper.between(Scheduling.COL_SCHEDULING_DAY,start,end);
                schedulingMapper.delete(wrapper);
                //再进行保存
                List<String> schedulingDays = initScheduling(beginDate);
                for (SchedulingFormDto.SchedulingData datum : schedulingFormDto.getData()) {
                    Scheduling scheduling = null;
                    int i = 0;
                    for (String s : datum.getSchedulingType()) {
                        if(StringUtils.isNotBlank(s)){
                            scheduling = new Scheduling(userId,deptId,schedulingDays.get(i),
                                    datum.getSubsectionType(),s,DateUtil.date(),schedulingFormDto.getSimpleUser().getUserName());
                            //保存数据
                            schedulingMapper.insert(scheduling);
                        }
                        i++;
                    }
                }
                return 1;
            }else{
                return 0;
            }
        }
        return 0;
    }

    /**
     * 根据条件查询有号的部门id
     * @param deptId
     * @param schedulingDay
     * @param schedulingType
     * @param subsectionType
     * @return
     */
    @Override
    public List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType) {
        return schedulingMapper.queryHasSchedulingDeptIds(deptId,schedulingDay,schedulingType,subsectionType);
    }

    /**
     * 初始化一周的日期到list中
     * @param start
     * @return
     */
    private List<String> initScheduling(DateTime start) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            DateTime dateTime = DateUtil.offsetDay(start, i);
            list.add(DateUtil.format(dateTime,"yyyy-MM-dd"));
        }
        return list;
    }
}





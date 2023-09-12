package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.Notice;
import com.dasco.openhis.dto.NoticeDto;
import com.dasco.openhis.mapper.NoticeMapper;
import com.dasco.openhis.service.NoticeService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* @author a
* @description 针对表【sys_notice(通知公告表)】的数据库操作Service实现
*/
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public DataGridView listNoticeForPage(NoticeDto noticeDto) {
        Page<Notice> page = new Page<>(noticeDto.getPageNum(), noticeDto.getPageSize());
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(noticeDto.getNoticeTitle()),Notice.COL_NOTICE_TITLE,
                noticeDto.getNoticeTitle());
        wrapper.like(StringUtils.isNotBlank(noticeDto.getCreateBy()),Notice.COL_CREATE_BY,
                noticeDto.getCreateBy());
        wrapper.eq(StringUtils.isNotBlank(noticeDto.getStatus()),Notice.COL_STATUS,
                noticeDto.getStatus());
        wrapper.eq(StringUtils.isNotBlank(noticeDto.getNoticeType()),Notice.COL_NOTICE_TYPE,
                noticeDto.getNoticeType());
        wrapper.orderByDesc(Notice.COL_CREATE_TIME);
        noticeMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        BeanUtil.copyProperties(noticeDto,notice);
        notice.setCreateBy(noticeDto.getSimpleUser().getUserName());
        notice.setCreateTime(DateUtil.date());
        return noticeMapper.insert(notice);
    }

    @Override
    public int updateNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        BeanUtil.copyProperties(noticeDto,notice);
        notice.setUpdateBy(noticeDto.getSimpleUser().getUserName());
        return noticeMapper.updateById(notice);
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        List<Long> ids = Arrays.asList(noticeIds);
        if(ids != null && ids.size() > 0){
            return noticeMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public Notice getOne(Long noticeId) {
        return this.noticeMapper.selectById(noticeId);
    }
}





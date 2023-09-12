package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Notice;
import com.dasco.openhis.dto.NoticeDto;
import com.dasco.openhis.vo.DataGridView;

/**
* @author a
* @description 针对表【sys_notice(通知公告表)】的数据库操作Service
*/
public interface NoticeService extends IService<Notice> {

    /**
     * 分页查询
     *
     * @param noticeDto
     * @return
     */
    DataGridView listNoticeForPage(NoticeDto noticeDto);

    /**
     * 添加
     *
     * @param noticeDto
     * @return
     */
    int addNotice(NoticeDto noticeDto);

    /**
     * 修改
     *
     * @param noticeDto
     * @return
     */
    int updateNotice(NoticeDto noticeDto);

    /**
     * 根据ID删除
     *
     * @param noticeIds
     * @return
     */
    int deleteNoticeByIds(Long[] noticeIds);

    /**
     * 根据ID查询
     *
     * @param noticeId
     * @return
     */
    Notice getOne(Long noticeId);
}

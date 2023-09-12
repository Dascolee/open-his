package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.DictType;
import com.dasco.openhis.dto.DictTypeDto;
import com.dasco.openhis.vo.DataGridView;

/**
* @author li118
* @description 针对表【sys_dict_type(字典类型表)】的数据库操作Service
* @createDate 2023-07-21 21:47:51
*/
public interface DictTypeService extends IService<DictType> {

    DataGridView listForPage(DictTypeDto dictTypeDto);

    /**
     * 查询所有字典类型
     *
     * @return
     */
    DataGridView listAll();

    /**
     * 检查字典类型是否存在
     *
     * @param dictType
     * @return
     */
    Boolean checkDictTypeUnique(Long dictId, String dictType);

    /**
     * 插入新的字典类型
     *
     * @param dictTypeDto
     * @return
     */
    int insert(DictTypeDto dictTypeDto);

    /**
     * 修改的字典类型
     *
     * @param dictTypeDto
     * @return
     */
    int update(DictTypeDto dictTypeDto);

    /**
     * 根据ID删除字典类型
     *
     * @param dictIds
     * @return
     */
    int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 根据ID查询一个字典类型
     *
     * @param dictId
     * @return
     */
    DictType selectDictTypeById(Long dictId);

    void dictCacheAsync();
}

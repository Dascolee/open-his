package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.DictData;
import com.dasco.openhis.dto.DictDataDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author li118
* @description 针对表【sys_dict_data(字典数据表)】的数据库操作Service
* @createDate 2023-07-21 21:47:51
*/
public interface DictDataService extends IService<DictData> {
    DataGridView listForPage(DictDataDto dictDataDto);

    DictData selectDictDataById(Long dictId);

    int insert(DictDataDto dictDataDto);

    int update(DictDataDto dictDataDto);

    int deleteDictData(Long[] dictIds);

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType
     * @return
     */
    List<DictData> selectDictDataByDictType(String dictType);

}

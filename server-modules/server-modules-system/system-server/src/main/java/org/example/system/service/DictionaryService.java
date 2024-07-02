package org.example.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.system.entity.query.DictionaryQueryPage;
import org.example.system.entity.vo.DictionaryVO;

/**
 * @author lihui
 * @since 2023/4/8
 */
public interface DictionaryService {
    /**
     * 新增字典
     *
     * @param dictionaryVO
     * @return
     */
    Boolean addDictionary(DictionaryVO dictionaryVO);

    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    Boolean deleteDictionary(String id);

    /**
     * 更新字典
     *
     * @param dictionaryVO
     * @return
     */
    Boolean updateDictionary(DictionaryVO dictionaryVO);

    /**
     * 查询字典列表
     *
     * @param queryPage
     * @return
     */
    Page<DictionaryVO> getDictionaryList(DictionaryQueryPage queryPage);
}
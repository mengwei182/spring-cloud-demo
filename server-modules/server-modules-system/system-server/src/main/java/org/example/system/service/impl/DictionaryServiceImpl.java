package org.example.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.core.domain.BaseEntity;
import org.example.common.core.exception.SystemException;
import org.example.common.core.result.CommonServerResult;
import org.example.common.core.result.SystemServerResult;
import org.example.common.core.util.CommonUtils;
import org.example.common.core.util.PageUtils;
import org.example.system.entity.Dictionary;
import org.example.system.entity.vo.DictionaryVO;
import org.example.system.mapper.DictionaryMapper;
import org.example.system.query.DictionaryQueryPage;
import org.example.system.service.DictionaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lihui
 * @since 2023/4/8
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Resource
    private DictionaryMapper dictionaryMapper;

    /**
     * 新增字典
     *
     * @param dictionaryVO
     * @return
     */
    @Override
    public Boolean addDictionary(DictionaryVO dictionaryVO) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryVO, dictionary);
        dictionary.setId(CommonUtils.uuid());
        String parentId = BaseEntity.TOP_PARENT_ID;
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        // 有父级id
        if (!StrUtil.isEmpty(dictionaryVO.getParentId())) {
            Dictionary parentDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getId, dictionaryVO.getParentId()));
            if (parentDictionary == null) {
                throw new SystemException(SystemServerResult.PARENT_NOT_EXIST);
            }
            parentId = dictionaryVO.getParentId();
            dictionary.setIdChain(parentDictionary.getIdChain() + "," + parentDictionary.getId());
        }
        // 无父级id
        if (StrUtil.isEmpty(dictionaryVO.getParentId())) {
            dictionary.setParentId(BaseEntity.TOP_PARENT_ID);
            dictionary.setIdChain(BaseEntity.TOP_PARENT_ID);
        }
        Dictionary resultDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getParentId, parentId).eq(Dictionary::getName, dictionaryVO.getName()));
        if (resultDictionary != null) {
            throw new SystemException(SystemServerResult.DICTIONARY_NAME_EXIST);
        }
        // 校验编码唯一性
        resultDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getCode, dictionaryVO.getCode()));
        if (resultDictionary != null) {
            throw new SystemException(SystemServerResult.DICTIONARY_CODE_EXIST);
        }
        dictionaryMapper.insert(dictionary);
        return true;
    }

    /**
     * 删除字典
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteDictionary(String id) {
        Dictionary dictionary = dictionaryMapper.selectById(id);
        if (dictionary == null) {
            throw new SystemException(CommonServerResult.OBJECT_NOT_EXIST);
        }
        List<Dictionary> dictionaries = dictionaryMapper.selectList(new LambdaQueryWrapper<Dictionary>().eq(Dictionary::getParentId, id));
        if (!CollectionUtil.isEmpty(dictionaries)) {
            throw new SystemException(SystemServerResult.DICTIONARY_CHILD_EXIST);
        }
        dictionaryMapper.deleteById(id);
        return true;
    }

    /**
     * 更新字典
     *
     * @param dictionaryVO
     * @return
     */
    @Override
    public Boolean updateDictionary(DictionaryVO dictionaryVO) {
        String parentId = BaseEntity.TOP_PARENT_ID;
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        Dictionary resultDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getParentId, parentId).eq(Dictionary::getName, dictionaryVO.getName()));
        if (resultDictionary != null) {
            throw new SystemException(SystemServerResult.DICTIONARY_NAME_EXIST);
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(dictionaryVO.getId());
        dictionary.setName(dictionaryVO.getName());
        dictionaryMapper.updateById(dictionary);
        return true;
    }

    /**
     * 查询字典列表
     *
     * @param queryPage
     * @return
     */
    @Override
    public Page<DictionaryVO> getDictionaryList(DictionaryQueryPage queryPage) {
        Page<Dictionary> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<Dictionary> dictionaries = dictionaryMapper.getDictionaries(page, queryPage);
        page.setRecords(dictionaries);
        return PageUtils.wrap(page, DictionaryVO.class);
    }
}
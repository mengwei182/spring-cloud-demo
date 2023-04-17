package org.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.entity.base.BaseEntity;
import org.example.common.entity.system.Dictionary;
import org.example.common.entity.system.vo.DictionaryVo;
import org.example.common.error.CommonErrorResult;
import org.example.common.error.SystemServerErrorResult;
import org.example.common.error.exception.CommonException;
import org.example.common.util.CommonUtils;
import org.example.common.util.PageUtils;
import org.example.system.api.DictionaryQueryPage;
import org.example.system.mapper.DictionaryMapper;
import org.example.system.service.DictionaryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
     * @param dictionaryVo
     * @return
     */
    @Override
    public Boolean addDictionary(DictionaryVo dictionaryVo) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryVo, dictionary);
        dictionary.setId(CommonUtils.uuid());
        String parentId = BaseEntity.TOP_PARENT_ID;
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        // 有父级id
        if (StringUtils.hasLength(dictionaryVo.getParentId())) {
            Dictionary parentDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getId, dictionaryVo.getParentId()));
            if (parentDictionary == null) {
                throw new CommonException(SystemServerErrorResult.PARENT_NOT_EXIST);
            }
            parentId = dictionaryVo.getParentId();
            dictionary.setIdChain(parentDictionary.getIdChain() + "," + parentDictionary.getId());
        }
        // 无父级id
        if (!StringUtils.hasLength(dictionaryVo.getParentId())) {
            dictionary.setParentId(BaseEntity.TOP_PARENT_ID);
            dictionary.setIdChain(BaseEntity.TOP_PARENT_ID);
        }
        Dictionary resultDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getParentId, parentId).eq(Dictionary::getName, dictionaryVo.getName()));
        if (resultDictionary != null) {
            throw new CommonException(SystemServerErrorResult.DICTIONARY_NAME_EXIST);
        }
        // 校验编码唯一性
        resultDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getCode, dictionaryVo.getCode()));
        if (resultDictionary != null) {
            throw new CommonException(SystemServerErrorResult.DICTIONARY_CODE_EXIST);
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
            throw new CommonException(CommonErrorResult.OBJECT_NOT_EXIST);
        }
        List<Dictionary> dictionaries = dictionaryMapper.selectList(new LambdaQueryWrapper<Dictionary>().eq(Dictionary::getParentId, id));
        if (!CollectionUtils.isEmpty(dictionaries)) {
            throw new CommonException(SystemServerErrorResult.DICTIONARY_CHILD_EXIST);
        }
        dictionaryMapper.deleteById(id);
        return true;
    }

    /**
     * 更新字典
     *
     * @param dictionaryVo
     * @return
     */
    @Override
    public Boolean updateDictionary(DictionaryVo dictionaryVo) {
        String parentId = BaseEntity.TOP_PARENT_ID;
        LambdaQueryWrapper<Dictionary> queryWrapper = new LambdaQueryWrapper<>();
        Dictionary resultDictionary = dictionaryMapper.selectOne(queryWrapper.eq(Dictionary::getParentId, parentId).eq(Dictionary::getName, dictionaryVo.getName()));
        if (resultDictionary != null) {
            throw new CommonException(SystemServerErrorResult.DICTIONARY_NAME_EXIST);
        }
        Dictionary dictionary = new Dictionary();
        dictionary.setId(dictionaryVo.getId());
        dictionary.setName(dictionaryVo.getName());
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
    public Page<DictionaryVo> getDictionaryList(DictionaryQueryPage queryPage) {
        Page<Dictionary> page = new Page<>(queryPage.getPageNumber(), queryPage.getPageSize());
        List<Dictionary> dictionaries = dictionaryMapper.getDictionaries(page, queryPage);
        page.setRecords(dictionaries);
        return PageUtils.wrap(page, DictionaryVo.class);
    }
}
package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.constant.ErrorAttribute;
import com.epam.esm.dao.constant.Symbol;
import com.epam.esm.dto.Tag;
import com.epam.esm.exception.InvalidFieldException;
import com.epam.esm.exception.ResourceDuplicateException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.esm.validator.TagValidator.isNameValid;

@Service
public class TagServiceImpl implements TagService<Tag> {
    private final TagDao<Tag> dao;

    @Autowired
    public TagServiceImpl(TagDao<Tag> dao) {
        this.dao = dao;
    }

    @Override
    public long insert(Tag tag) {
        if (!isNameValid(tag.getName())) {
            throw new InvalidFieldException(ErrorAttribute.TAG_ERROR_CODE, ErrorAttribute.INVALID_TAG_ID_ERROR,
                    tag.getName());
        }
        if (dao.findByName(tag.getName()).isPresent()) {
            throw new ResourceDuplicateException(ErrorAttribute.TAG_ERROR_CODE, ErrorAttribute.TAG_DUPLICATE_ERROR,
                    tag.getName());
        }
        return dao.insert(tag);
    }

    @Override
    public Tag findById(String id) {
        try {
            return dao.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException(
                    ErrorAttribute.TAG_ERROR_CODE, ErrorAttribute.RESOURCE_NOT_FOUND_ERROR, id));
        } catch (NumberFormatException e) {
            throw new InvalidFieldException(ErrorAttribute.TAG_ERROR_CODE, ErrorAttribute.INVALID_TAG_ID_ERROR, id);
        }
    }

    @Override
    public List<Tag> findAll(int page, int elements) {
        List<Tag> tags = dao.findAll(page, elements);
        if (CollectionUtils.isEmpty(tags)) {
            throw new ResourceNotFoundException(ErrorAttribute.TAG_ERROR_CODE, ErrorAttribute.RESOURCE_NOT_FOUND_ERROR,
                    page + Symbol.COMMA + Symbol.SPACE_SYMBOL + elements);
        }
        return tags;
    }

    @Override
    public boolean delete(String id) {
        try {
            if (!dao.delete(Long.parseLong(id))) {
                throw new ResourceNotFoundException(ErrorAttribute.TAG_ERROR_CODE,
                        ErrorAttribute.RESOURCE_NOT_FOUND_ERROR, id);
            }
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidFieldException(ErrorAttribute.TAG_ERROR_CODE, ErrorAttribute.INVALID_TAG_ID_ERROR, id);
        }
    }
}

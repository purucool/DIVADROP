package com.SBProject.DivaDrop.Service;

import com.SBProject.DivaDrop.Modal.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    public Boolean existCategory(String category);
    public Category saveCategory(Category c);
    public List<Category> getAllCategory();
    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategory();

    public Page<Category> getAllCategoryPaging(Integer pageNo,Integer pageSize);
}

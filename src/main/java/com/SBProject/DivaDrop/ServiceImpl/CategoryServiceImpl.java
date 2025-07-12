package com.SBProject.DivaDrop.ServiceImpl;

import com.SBProject.DivaDrop.Modal.Category;
import com.SBProject.DivaDrop.Repository.CategoryRepository;
import com.SBProject.DivaDrop.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository cr;

//    Constructor Injection
    @Autowired
    CategoryServiceImpl(CategoryRepository cr){
        this.cr=cr;
    }

    @Override
    public Boolean existCategory(String category) {
        return cr.existByName(category);
    }

    @Override
    public Category saveCategory(Category c) {
        return cr.save(c);
    }

    @Override
    public List<Category> getAllCategory() {
        return cr.findAll();
    }

    @Override
    public Boolean deleteCategory(int id) {
        Optional<Category> optionalCategory=cr.findById(id);
        Category category = optionalCategory.orElse(null);
        if(category!=null){
            cr.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        Optional<Category> optionalCategory=cr.findById(id);
        Category category = optionalCategory.orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> categories=cr.findAllActiveCategory();
        return categories;
    }

    @Override
    public Page<Category> getAllCategoryPaging(Integer pageNo,Integer pageSize) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        return cr.findAll(pageable);
    }

//    public static class ProductServiceImpl {
//    }
}

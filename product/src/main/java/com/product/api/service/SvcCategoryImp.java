package com.product.api.service;

import com.product.api.entity.Category;
import com.product.api.repository.RepoCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SvcCategoryImp implements SvcCategory{

    @Autowired
    RepoCategory repo;

    @Override
    public List<Category> getCategories() {
        return repo.findByStatus(1);
    }

    @Override
    public Category getCategory(Integer category_id) {
        return repo.findByCategoryId(category_id);
    }

    @Override
    public String createCategory(Category category) {
        Category categoryS = (Category) repo.findByCategory(category.getCategory());
        if (categoryS != null) {
            if (categoryS.getStatus() == 0) {
                repo.activateCategory(categoryS.getCategory_id());
                return "categort has been activated";
            } else {
                return "category already exists";
            }
        }
        repo.createCategory(category.getCategory());
        return "category created";
    }

    @Override
    public String updateCategory(Integer category_id, Category category) {
        Category categoryS = (Category) repo.findByCategoryId(category_id);
        if (categoryS == null) {
            return "category does not exist";
        } else {
            if (categoryS.getStatus() == 0) {
                return "category is not active";
            } else {
                categoryS = (Category) repo.findByCategory(category.getCategory());
                if (categoryS != null)
                    return "category already exists";
                repo.updateCategory(category_id,category.getCategory());
                return "category update";
            }
        }
    }

    @Override
    public String deleteCategory(Integer category_id) {
        Category categoryS = (Category) repo.findByCategoryId(category_id);
        if (categoryS == null)
            return "category does not exist";
        else {
            repo.deleteById(category_id);
            return "category removed";
        }
    }
}

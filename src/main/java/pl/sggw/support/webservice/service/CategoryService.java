package pl.sggw.support.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.CategoryDAO;
import pl.sggw.support.webservice.dto.Category;
import pl.sggw.support.webservice.model.CategoryModel;
import pl.sggw.support.webservice.populator.CategoryPopulator;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryPopulator populator;

    @Autowired
    private CategoryDAO dao;

    public List<Category> getAll() {
        return dao.getAll().stream().map(this::convert).collect(Collectors.toList());
    }

    public Category save(Category category) {
        CategoryModel model = new CategoryModel();
        populator.reversePopulate(category, model);
        dao.save(model);
        return category;
    }

    private Category convert(CategoryModel model){
        Category category = new Category();
        if(Objects.nonNull(model))populator.populate(model, category);
        return category;
    }

    public Category saveOrUpdate(Category category) {
        CategoryModel categoryModel = new CategoryModel();
        populator.reversePopulate(category, categoryModel);
        save(category);
        populator.populate(categoryModel, category);
        return category;
    }

    public void remove(long id) {
        dao.remove(dao.getCategoryByID(id));
    }
}

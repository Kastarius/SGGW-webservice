package pl.sggw.support.webservice.populator;

import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.dto.Category;
import pl.sggw.support.webservice.model.CategoryModel;

@Component
public class CategoryPopulator extends AbstractPopulator<CategoryModel, Category> {

    @Override
    public void populate(CategoryModel source, Category target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }

    @Override
    public void reversePopulate(Category source, CategoryModel target) {
        target.setId(source.getId());
        target.setName(source.getName());
    }
}

package com.product.api.controller;

import com.product.api.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CtrlCategory {

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        //Creación de categorías internas
        Category cat1 = new Category();
        cat1.setCategory_id(1);
        cat1.setCategory("Abarrotes");
        cat1.setStatus(1);

        Category cat2 = new Category();
        cat2.setCategory_id(2);
        cat2.setCategory("Electrónica");
        cat2.setStatus(1);

        List categorias = new ArrayList();
        categorias.add(cat1);
        categorias.add(cat2);
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<Category> getCategory(@PathVariable int category_id) {
        //Creación de categorías internas
        Category cat1 = new Category();
        cat1.setCategory_id(1);
        cat1.setCategory("Abarrotes");
        cat1.setStatus(1);

        return new ResponseEntity<>(cat1, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category, BindingResult bindingResult) {
        String msj = "";
        String m = category.getCategory();
        if (m.equals("Abarrotes") || m.equals("Electrónica")){
            msj = "category already exists";
            return new ResponseEntity<>(msj, HttpStatus.BAD_REQUEST);
        }
        msj = "category created";

        return new ResponseEntity<>(msj, HttpStatus.OK);
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<String> updateCategory(@PathVariable int category_id, @Valid @RequestBody Category category, BindingResult bindingResult) {
        String msj = "";
        String m = category.getCategory();
        if (m.equals("Abarrotes") || m.equals("Electrónica")){
            msj = "category updated";
            return new ResponseEntity<>(msj, HttpStatus.OK);
        }
        msj = "category does not exist";

        return new ResponseEntity<>(msj, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int category_id) {
        String msj = "";
        if (category_id == 1 || category_id == 2){
            msj = "category removed";
            return new ResponseEntity<>(msj, HttpStatus.OK);
        }
        msj = "category does not exist";

        return new ResponseEntity<>(msj, HttpStatus.BAD_REQUEST);
    }
}

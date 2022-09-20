package com.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class CtrlProduct {

    @GetMapping("/category")
    public ArrayList<Object> categorias() {
        ArrayList<Object> lista = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("category_id", 13);
        map.put("category", "Uvas");
        lista.add(map.clone());
        map.clear();
        map.put("category_id", 14);
        map.put("category", "Electrónicos");
        lista.add(map.clone());
        map.put("category_id", 15);
        map.put("category", "Abarrotes");
        lista.add(map.clone());
        map.clear();
        map.put("category_id", 16);
        map.put("category", "Línea Blanca");
        lista.add(map.clone());
        map.clear();
        map.put("category_id", 1);
        map.put("category", "Cocina");
        lista.add(map.clone());
        map.clear();
        return lista;
    }
}

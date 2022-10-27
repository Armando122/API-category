package com.product.api.service;

import com.product.api.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.product.api.dto.ApiResponse;
import com.product.api.entity.Product;
import com.product.api.repository.RepoCategory;
import com.product.api.repository.RepoProduct;
import com.product.exception.ApiException;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class SvcProductImp implements SvcProduct {

	@Autowired
	RepoProduct repo;
	
	@Autowired
	RepoCategory repoCategory;

	@Override
	public Product getProduct(String gtin) {
		Product product = repo.findByStatusAndGtin(gtin);
		if (product != null) {
			product.setCategory(repoCategory.findByCategoryId(product.getCategory_id()));
			return product;
		}else
			throw new ApiException(HttpStatus.NOT_FOUND, "product does not exist");
	}

	/*
	 * 4. Implementar el método createProduct considerando las siguientes validaciones:
  		1. validar que la categoría del nuevo producto exista
  		2. el código GTIN y el nombre del producto son únicos
  		3. si al intentar realizar un nuevo registro ya existe un producto con el mismo GTIN pero tiene estatus 0, 
  		   entonces se debe cambiar el estatus del producto existente a 1 y actualizar sus datos con los del nuevo registro
	 */
	@Override
	public ApiResponse createProduct(Product in) {
		Product product = (Product) repo.findByGtin(in.getGtin());
		Category categoryS = (Category) repoCategory.findByCategoryId(in.getCategory_id());

		//Se revisa que la categoría exista
		if (categoryS == null)
			throw new ApiException(HttpStatus.NOT_FOUND, "Category not found");

		//Se revisa que el gtin del producto está en la base de datos
		if (product != null) {
			//Se verifica el status del producto, si es 0 se activa, en otro caso se lanza una excepción.
			if (product.getStatus() == 0) {
				repo.activateProduct(product.getProduct_id());
				return new ApiResponse("product activated");
			} else {
				throw new ApiException(HttpStatus.BAD_REQUEST, "Product gtin already exists");
			}
		}

		//Se busca el producto pro nombre en caso de que no se encuentre por gtin.
		product = (Product) repo.findByProduct(in.getProduct());
		//Se revisa que el nombre no exista
		if (product != null){
			throw new ApiException(HttpStatus.BAD_REQUEST, "product name already exists");
		}

		//En otro caso no se encontró el producto, entonces se crea.
		repo.createProduct(in.getGtin(),in.getProduct(),in.getDescription(),in.getPrice(),in.getStock(),in.getCategory_id());
		return new ApiResponse("product created");
	}

	@Override
	public ApiResponse updateProduct(Product in, Integer id) {
		Integer updated = 0;
		try {
			updated = repo.updateProduct(id, in.getGtin(), in.getProduct(), in.getDescription(), in.getPrice(), in.getStock(), in.getCategory_id());
		}catch (DataIntegrityViolationException e) {
			if (e.getLocalizedMessage().contains("gtin"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product gtin already exist");
			if (e.getLocalizedMessage().contains("product"))
				throw new ApiException(HttpStatus.BAD_REQUEST, "product name already exist");
			if (e.contains(SQLIntegrityConstraintViolationException.class))
				throw new ApiException(HttpStatus.BAD_REQUEST, "category not found");
		}
		if(updated == 0)
			throw new ApiException(HttpStatus.BAD_REQUEST, "product cannot be updated");
		else
			return new ApiResponse("product updated");
	}

	@Override
	public ApiResponse deleteProduct(Integer id) {
		if (repo.deleteProduct(id) > 0)
			return new ApiResponse("product removed");
		else
			throw new ApiException(HttpStatus.BAD_REQUEST, "product cannot be deleted");
	}

	@Override
	public ApiResponse updateProductStock(String gtin, Integer stock) {
		Product product = getProduct(gtin);
		if(stock > product.getStock())
			throw new ApiException(HttpStatus.BAD_REQUEST, "stock to update is invalid");
		
		repo.updateProductStock(gtin, product.getStock() - stock);
		return new ApiResponse("product stock updated");
	}
}

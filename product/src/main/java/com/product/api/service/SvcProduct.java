package com.product.api.service;

import com.product.api.dto.ApiResponse;
import com.product.api.dto.ProductResponse;
import com.product.api.entity.Product;

import java.util.List;

public interface SvcProduct {

	public Product getProduct(String gtin);
	public List<ProductResponse> getProducts(Integer catId);
	public ApiResponse createProduct(Product in);
	public ApiResponse updateProduct(Product in, Integer id);
	public ApiResponse updateProductStock(String gtin, Integer stock);
	public ApiResponse deleteProduct(Integer id);

}

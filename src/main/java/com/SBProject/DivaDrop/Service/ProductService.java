package com.SBProject.DivaDrop.Service;

import com.SBProject.DivaDrop.Modal.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    public Product addProduct(Product product);
    public Boolean existProduct(String pname);

    public List<Product> getAllProduct();

    Boolean deleteCategory(int id);

    Product getProductById(int id);

    List<Product> getAllProductByCategory(String Category);
    List<Product> searchProduct(String ch);

    Page<Product> getAllProductByCategoryPaging(Integer pageNo,Integer pageSize);
    Page<Product> getAllActiveProductPaging(Integer pageNo,Integer pageSize,String category);

    Page<Product> getAllActiveProductPaging2(Integer pageNo, Integer pageSize);

    Page<Product> searchProductPaging(Integer pageNo, Integer pageSize, String str);

//    Page<Product> findByCategory(String category,Pageable pageable);

}

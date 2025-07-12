package com.SBProject.DivaDrop.ServiceImpl;

import com.SBProject.DivaDrop.Modal.Category;
import com.SBProject.DivaDrop.Modal.Product;
import com.SBProject.DivaDrop.Repository.ProductRepository;
import com.SBProject.DivaDrop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    private ProductRepository pr;

    @Autowired
    public ProductServiceImpl(ProductRepository pr){
        this.pr=pr;
    }

    @Override
    public Product addProduct(Product product) {
        return pr.save(product);
    }

    @Override
    public Boolean existProduct(String pname) {
        return pr.existByName(pname);
    }

    @Override
    public List<Product> getAllProduct() {
        return pr.findAll();
    }

    @Override
    public Boolean deleteCategory(int id) {
        Optional<Product> optionalProduct=pr.findById(id);
        Product product= optionalProduct.orElse(null);
        if(product!=null){
            pr.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(int id) {
        Optional<Product> optionalProduct=pr.findById(id);
        Product product=optionalProduct.orElse(null);
        return product;
    }

    @Override
    public List<Product> getAllProductByCategory(String Category) {

        return pr.findByCategory(Category);
    }

    @Override
    public List<Product> searchProduct(String ch) {
        return pr.searchByProductOrCategory(ch);
    }

    @Override
    public Page<Product> getAllProductByCategoryPaging(Integer pageNo, Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);
        Page<Product> productPage=pr.findByIsActiveTrue(pageable);
        return productPage;
    }

    @Override
    public Page<Product> getAllActiveProductPaging(Integer pageNo, Integer pageSize, String category) {
        Pageable pageable= PageRequest.of(pageNo,pageSize);

        Page<Product> productPage=null;
                pr.findByIsActiveTrue(pageable);

        if(ObjectUtils.isEmpty(category)){
            productPage=pr.findByIsActiveTrue(pageable);
        }
        else{
            productPage=pr.findByCategory(category,pageable);
        }
        return productPage;
    }

    @Override
    public Page<Product> getAllActiveProductPaging2(Integer pageNo, Integer pageSize) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        return pr.findByIsActiveTrue(pageable);

    }

    @Override
    public Page<Product> searchProductPaging(Integer pageNo, Integer pageSize, String str) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        return pr.searchByProductOrCategoryPagging(str,pageable);
    }


}

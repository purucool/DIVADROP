package com.SBProject.DivaDrop.ServiceImpl;

import com.SBProject.DivaDrop.CommonServices.CommonServices;
import com.SBProject.DivaDrop.CommonServices.CommonServicesImpl;
import com.SBProject.DivaDrop.Modal.Cart;
import com.SBProject.DivaDrop.Modal.Product;
import com.SBProject.DivaDrop.Modal.User;
import com.SBProject.DivaDrop.Repository.CartRepository;
import com.SBProject.DivaDrop.Repository.ProductRepository;
import com.SBProject.DivaDrop.Repository.UserRepository;
import com.SBProject.DivaDrop.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository kr;

    @Autowired
    private UserRepository ur;

    @Autowired
    private ProductRepository pr;

    @Autowired
    private CommonServices cs;

    @Autowired
    CommonServices commonServices;

    @Override
    public Cart saveCart(Integer pid, Integer uid) {
        User user=ur.findById(uid).get();
        Product product=pr.findById(pid).get();
        Cart cartsts=kr.findByProductIdAndUserId(pid,uid);
        Cart cart=new Cart();
        if(cartsts==null){
            cart=new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(cs.discountedPrice(product.getPrice(), product.getDiscount()));
        }
        else{
           cart=cartsts;
           Integer qty=cart.getQuantity()+1;
           Double disc_price=cs.discountedPrice(cart.getProduct().getPrice(), cart.getProduct().getDiscount());
           cart.setQuantity(qty);
           cart.setTotalPrice(qty*disc_price);
        }
        Cart saveCart=kr.save(cart);
        return saveCart;
    }

    @Override
    public List<Cart> getCartByUser(Integer uid) {
        List<Cart> cartList=kr.findByUserId(uid);
        Double totalOrderPrice=0.0;
        List<Cart> updatedCart=new ArrayList<>();
        for(Cart c:cartList){
            Double discountedprice=commonServices.discountedPrice(c.getProduct().getPrice(),c.getProduct().getDiscount());
            Integer qty=c.getQuantity();
            Double totalprice=discountedprice*qty;
            c.setTotalPrice(totalprice);
            totalOrderPrice+=totalprice;
            c.setTotalOrderAmount(totalOrderPrice);
            updatedCart.add(c);
        }


        return updatedCart;
    }

    @Override
    public Integer getCountCart(Integer uid) {
        return kr.countByUserId(uid);

    }

    @Override
    public Boolean updateQuantity(String sign, Integer cid) {
        Cart cart = kr.findByCartId(cid);
        Integer updatedQty;
        if(sign.equals("dec")){
            updatedQty=(cart.getQuantity()-1);
            if(updatedQty<=0){
//                delete cart
                kr.deleteById(cid);
                return true;
            }
        }
        else{
            updatedQty=(cart.getQuantity()+1);
        }
        cart.setQuantity(updatedQty);
        Cart updatedCart=kr.save(cart);
        if(updatedCart==null)return false;
        return true;
    }
}

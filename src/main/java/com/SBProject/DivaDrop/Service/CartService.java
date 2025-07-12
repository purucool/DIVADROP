package com.SBProject.DivaDrop.Service;

import com.SBProject.DivaDrop.Modal.Cart;

import java.util.List;



public interface CartService
{
    public Cart saveCart(Integer pid,Integer uid);
    public List<Cart> getCartByUser(Integer uid);

    public Integer getCountCart(Integer uid);

    Boolean updateQuantity(String sign, Integer cid);
}

package com.SBProject.DivaDrop.Service;

import com.SBProject.DivaDrop.Modal.OrderRequest;
import com.SBProject.DivaDrop.Modal.ProductOrder;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ProductOrderService {
    public void saveOrder(Integer uid, OrderRequest or) throws MessagingException, UnsupportedEncodingException;
    public List<ProductOrder> getOrdersByUser(Integer uid);

    public ProductOrder updateStatus(Integer id,String st);
    public List<ProductOrder> getAllOrders();
    List<ProductOrder> searchProductByOrderId(String oid);

    Page<ProductOrder> searchProductByOrderIdPagging(Integer pageNo, Integer pageSize, String str);

    Page<ProductOrder> getAllOrdersPaging(Integer pageNo, Integer pageSize);
}

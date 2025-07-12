package com.SBProject.DivaDrop.ServiceImpl;

import com.SBProject.DivaDrop.CommonServices.CommonServices;
import com.SBProject.DivaDrop.CommonServices.CommonUtil;
import com.SBProject.DivaDrop.CommonServices.OrderStatus;
import com.SBProject.DivaDrop.Modal.*;
import com.SBProject.DivaDrop.Repository.CartRepository;
import com.SBProject.DivaDrop.Repository.ProductOrderRepository;
import com.SBProject.DivaDrop.Service.ProductOrderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("productOrderService")
public class ProductOrderServiceImpl implements ProductOrderService{
    @Autowired
    private ProductOrderRepository por;

    @Autowired
    private CartRepository kr;

    @Autowired
    private CommonServices commonServices;
    @Autowired
    private CommonUtil commonUtil;
    @Override
    public void saveOrder(Integer uid, OrderRequest or) throws MessagingException, UnsupportedEncodingException {
        List<Cart> carts =kr.findByUserId(uid);
        for(Cart c:carts){
        ProductOrder order=new ProductOrder();
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderDate(LocalDate.now());
        order.setProduct(c.getProduct());
        order.setPrice(commonServices.discountedPrice(c.getProduct().getPrice(),c.getProduct().getDiscount()));
        order.setQuantity(c.getQuantity());
        order.setUser(c.getUser());
        order.setStatus(OrderStatus.IN_PROGRSS.getName());
        order.setPaymentType(or.getPaymentType());
            OrderAddress address=new OrderAddress();
            address.setFirstName(or.getFirstName());
            address.setLastName(or.getLastName());
            address.setEmail(or.getEmail());
            address.setMoblieNo(or.getMoblieNo());
            address.setAddress(or.getAddress());
            address.setCity(or.getCity());
            address.setState(or.getState());
            address.setPincode(or.getPincode());
        order.setOrderAddress(address);
        ProductOrder productOrder=por.save(order);
        commonUtil.sendmailforproductorder(productOrder,7);
        }

    }

    @Override
    public List<ProductOrder> getOrdersByUser(Integer uid) {
        List<ProductOrder> orders =por.findByUserId(uid);
        return orders;
    }

    @Override
    public ProductOrder updateStatus(Integer id, String st) {
        Optional<ProductOrder> optionalProductOrder=por.findById(id);
        if(optionalProductOrder.isPresent()){
            ProductOrder orders=optionalProductOrder.get();
            orders.setStatus(st);
            por.save(orders);
            return orders;
        }
        return  null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return por.findAll();
    }

    @Override
    public List<ProductOrder> searchProductByOrderId(String oid) {
        return por.searchByOrderId(oid);
    }

    @Override
    public Page<ProductOrder> searchProductByOrderIdPagging(Integer pageNo, Integer pageSize, String str) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        return por.searchByOrderIdPaging(str,pageable);
    }

    @Override
    public Page<ProductOrder> getAllOrdersPaging(Integer pageNo, Integer pageSize) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        return por.getAllOrdersPaging(pageable);
    }
}

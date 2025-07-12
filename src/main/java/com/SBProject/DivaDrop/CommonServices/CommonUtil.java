package com.SBProject.DivaDrop.CommonServices;

import com.SBProject.DivaDrop.Modal.ProductOrder;
import jakarta.mail.MessagingException;
//import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.UnsupportedEncodingException;

@Component
public class CommonUtil {


    @Autowired
    private JavaMailSender mailSender;
    public Boolean sendMail(String url,String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom("purucoolbab123@gmail.com","DivaDrop");
        helper.setTo(email);
        String content="<h3>DIVADROP => !!♕ Drop into Style, Rise in Confidence ♕!!</h3>"+"<p>You have requested to reset your password.</p>"+"<p>Click the link to Reset Password </p>"+"<p><a href=\""+url+"\">Reset Password</a></p>";
        helper.setSubject("DivaDrop : ResetPassword");
        helper.setText(content,true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        String url=request.getRequestURL().toString();
        url=url.replace(request.getServletPath(),"");
        return url;
    }
    String msg=null;
//    29
    public Boolean sendmailforproductorder(ProductOrder po, Integer statuscode) throws MessagingException, UnsupportedEncodingException {
        msg="<h3>DIVADROP => !!♕ Drop into Style, Rise in Confidence ♕!!</h3>"+
                "<p>Thank you [[name]] your order : <b>[[status]]</b> </p>"+
                "<p><b><mark> Product Details:</mark></b></p>"+
                "<p><mark style=\"background-color:pink;\">Product Name</mark>:[[productName]] </p>"+
                "<p><mark style=\"background-color:pink;\">Category</mark>: [[category]]</p>"+
                "<p><mark style=\"background-color:pink;\">Quantity</mark>:[[quantity]] </p>"+
                "<p><mark style=\"background-color:pink;\">Price</mark>: [[price]]</p>"+
                "<p><mark style=\"background-color:pink;\">Payment Type</mark>: [[paymentType]]</p>";

        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        helper.setFrom("purucoolbab123@gmail.com","DivaDrop");
        helper.setTo(po.getOrderAddress().getEmail());

        OrderStatus[] values=OrderStatus.values();
        for(OrderStatus status:values){
            if(status.getId()==statuscode){
                msg=msg.replace("[[status]]",status.getName());
            }
        }
        msg=msg.replace("[[productName]]",po.getProduct().getProductName());
        msg=msg.replace("[[name]]",po.getOrderAddress().getFirstName()+" "+po.getOrderAddress().getLastName());
        msg=msg.replace("[[category]]",po.getProduct().getCategory());
        msg=msg.replace("[[quantity]]",Integer.toString(po.getQuantity()));
        msg=msg.replace("[[price]]",Double.toString(po.getPrice()));
        msg=msg.replace("[[paymentType]]",po.getPaymentType());
        helper.setSubject("DivaDrop : Product Order Status");
        helper.setText(msg,true);
        mailSender.send(message);
        return true;
    }
}

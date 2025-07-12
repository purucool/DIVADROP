package com.SBProject.DivaDrop.Controller;
//http://localhost:8080/DivaDrop/
import com.SBProject.DivaDrop.CommonServices.CommonUtil;
import com.SBProject.DivaDrop.Modal.Category;
import com.SBProject.DivaDrop.Modal.Product;
import com.SBProject.DivaDrop.Modal.User;
import com.SBProject.DivaDrop.Service.CartService;
import com.SBProject.DivaDrop.Service.CategoryService;
import com.SBProject.DivaDrop.Service.ProductService;
import com.SBProject.DivaDrop.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.ObjectUtils;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/DivaDrop")
public class HomeController {

    private CategoryService cs;
    private ProductService ps;

    private UserService us;
    private CartService ks;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public HomeController(@Qualifier("categoryService") CategoryService cs,@Qualifier("productService") ProductService ps,@Qualifier("userService") UserService us ,@Qualifier("cartService") CartService ks){
        this.cs=cs;
        this.ps=ps;
        this.us=us;
        this.ks=ks;
    }
    @Autowired
    private CommonUtil commonUtil;

    @GetMapping("/")
    public String loadIndexPage(Model m){

        List<Category> categories=cs.getAllActiveCategory().stream()
                .sorted((c1,c2)->c2.getId() - c1.getId())
                .limit(12).toList();
        List<Product> products=ps.getAllProduct().stream()
                .sorted((p1,p2)->p2.getId() - p1.getId())
                .limit(6).toList();
        m.addAttribute("products",products);
        m.addAttribute("categories",categories);
        return "index";
    }


    @GetMapping("/signin")
    public String loadLoginPage(){
        return "login";
    }


    @GetMapping("/register")
    public String loadRegisterPage(Model m){
        m.addAttribute("user",new User());
        return "register";
    }


    @GetMapping("/products")
    public String loadProductsPage(Model m, @RequestParam(value="category", required = false) String category,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "8")Integer pageSize){

        if(category!=null && category.isEmpty()==false){
            m.addAttribute("paramValue",category);
        }
        else{
           m.addAttribute("paramValue","All");
        }
//        System.out.println(products);

        Page<Product> page=ps.getAllActiveProductPaging(pageNo,pageSize,category);
        List<Product> products=page.getContent();
        m.addAttribute("products",products);
        m.addAttribute("productSize",products.size());

        m.addAttribute("pageNo",page.getNumber());
        m.addAttribute("pageSize",pageSize);
        m.addAttribute("TotalElements",page.getTotalElements());
        m.addAttribute("TotalPages",page.getTotalPages());
        m.addAttribute("isFirst",page.isFirst());
        m.addAttribute("isLast",page.isLast());
        m.addAttribute("categories",cs.getAllActiveCategory());
        return "product";
    }


    @GetMapping("/products_details/{id}")
    public String loadProductsDetailPage(Model m, @PathVariable("id") int id){
        m.addAttribute("product",ps.getProductById(id));
        return "product_details";
    }

//    when home controller is called Model Attribute function is called automatically
    @ModelAttribute
    public void getUserDetails(Principal p,Model m){
        if(p!=null){
            String email=p.getName();
            User user=us.getUserByEmail(email);
            Integer cart_count=ks.getCountCart(user.getId());
            m.addAttribute("cartCount",cart_count );
            m.addAttribute("loginuser",user);
        }

//        get active categories
        List<Category> categories=cs.getAllActiveCategory();
        m.addAttribute("navcategories",categories);


    }
//    forgot password
    @GetMapping("/forgot_pwd")
    public String loadForgotPswdPage(){
        return "forget_pwd";
    }

    @PostMapping("/forgot_pwd")
    public String processingForgetPassword(@RequestParam("email") String email, HttpSession session, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        User user=us.getUserByEmail(email);
        if(ObjectUtils.isEmpty(user)){
            session.setAttribute("errorMsg","Email Not Registered");
        }
        else{
            String reset_token=UUID.randomUUID().toString();
            us.updateUserResetToken(email,reset_token);

//            generate URL localhost:8080/reset_pwd?token=qwertyuiopasdfghjkl
            String url=CommonUtil.generateUrl(request)+"/DivaDrop/reset_pwd?token="+reset_token;

            Boolean res=commonUtil.sendMail(url,email);
            if(res){
                session.setAttribute("successMsg","Mail send to the registered email for Reset Password");
            }
            else{
                session.setAttribute("errorMsg","Server error !! Mail Not sent");
            }
        }
        return "redirect:/DivaDrop/forgot_pwd";
    }



    //    reset password
    @GetMapping("/reset_pwd")
    public String loadResetPswdPage(@RequestParam("token") String token,HttpSession session,Model m){
        User user=us.getUserByToken(token);
        if(user==null){
           session.setAttribute("errorMsg","Invalid Link or Link has been Expired");
           return "message";
        }
        m.addAttribute("token",token);
        return "reset_pwd";
    }
    @PostMapping("/reset_pwd")
    public String processResetPassword(@RequestParam("token") String token,@RequestParam("password")String password,@RequestParam("cnf_password")String cnf_password, HttpSession session,Model m){
        User user=us.getUserByToken(token);

        if(user==null){
            session.setAttribute("errorMsg","Invalid Link or Link has been Expired");
            return "message";
        }

        else{
            if(password.equals(cnf_password)){
            user.setPassword(passwordEncoder.encode(password));
            user.setConfirmPassword(cnf_password);
            user.setReset_token(null);
            User updateduser=us.UpdateUserPassword(user);
            if(updateduser!=null){
                session.setAttribute("successMsg","Password Updated Successfully");
            }
            else{
                session.setAttribute("errorMsg","Server Error");

            }
            }
            else{
                session.setAttribute("errorMsg","Please Enter Same Password in Both the field");

            }
        }
        return "message";
    }

    @GetMapping("/search")
    public String proccessSearch(@RequestParam("ch") String str,Model m,HttpSession session,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "8")Integer pageSize){
        List<Product> products=null;
        Page<Product> page=null;
        if (str != null && str.length() > 0) {
            page = ps.searchProductPaging(pageNo,pageSize,str);
//            List<Product> products = ps.searchProduct(str);
            products=page.getContent();
            m.addAttribute("productSize",products.size());
            m.addAttribute("pageNo",page.getNumber());
            m.addAttribute("pageSize",pageSize);
            m.addAttribute("TotalElements",page.getTotalElements());
            m.addAttribute("TotalPages",page.getTotalPages());
            m.addAttribute("isFirst",page.isFirst());
            m.addAttribute("isLast",page.isLast());
            if (ObjectUtils.isEmpty(products)) {
                session.setAttribute("errorMsg", "No Product!!");
                page=ps.getAllActiveProductPaging2(pageNo,pageSize);
                products=page.getContent();
                m.addAttribute("productSize",products.size());
                m.addAttribute("pageNo",page.getNumber());
                m.addAttribute("pageSize",pageSize);
                m.addAttribute("TotalElements",page.getTotalElements());
                m.addAttribute("TotalPages",page.getTotalPages());
                m.addAttribute("isFirst",page.isFirst());
                m.addAttribute("isLast",page.isLast());
                m.addAttribute("products",products);
            } else {
                m.addAttribute("products", products);
            }
        }
        else{
//            session.setAttribute("errorMsg", "Product Name Can't Be Empty!!");
            page=ps.getAllActiveProductPaging2(pageNo,pageSize);
            products=page.getContent();
            m.addAttribute("productSize",products.size());
            m.addAttribute("pageNo",page.getNumber());
            m.addAttribute("pageSize",pageSize);
            m.addAttribute("TotalElements",page.getTotalElements());
            m.addAttribute("TotalPages",page.getTotalPages());
            m.addAttribute("isFirst",page.isFirst());
            m.addAttribute("isLast",page.isLast());
            m.addAttribute("products",products);
        }

        m.addAttribute("categories", cs.getAllActiveCategory());

        return "product";
    }

}

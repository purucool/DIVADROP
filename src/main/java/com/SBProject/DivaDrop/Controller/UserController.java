package com.SBProject.DivaDrop.Controller;
//ACESS KEY-AKIA5ERJF45X2VYRXOAI
// SECRET KEY-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

import com.SBProject.DivaDrop.CommonServices.CommonUtil;
import com.SBProject.DivaDrop.CommonServices.OrderStatus;
import com.SBProject.DivaDrop.Modal.*;
import com.SBProject.DivaDrop.Service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/DivaDrop/user")
public class UserController {

    private CategoryService cs;
    private UserService us;

    private CartService ks;

    private ProductOrderService pos;
    @Autowired
    private CommonUtil commonUtil;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(@Qualifier("userService") UserService us, @Qualifier("categoryService") CategoryService cs, @Qualifier("cartService") CartService ks, @Qualifier("productOrderService") ProductOrderService pos) {
        this.us = us;
        this.cs = cs;
        this.ks = ks;
        this.pos = pos;
    }

    @GetMapping("/")
    public String loadUserHomePage() {
        return "user/home";
    }

    @PostMapping("/save_user")
    public String loadSaveUSer(@ModelAttribute("user") User user, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imgName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        user.setImgName(imgName);
//        user.setImgName(commonUtil.getImageUrl(file, BucketType.Profile.getId()));

        Boolean result = us.existUser(user.getEmailAddress());
        if (result == true) {
            session.setAttribute("errorMsg", "!! Already Registered !!");
        } else {
            if (user.getPassword().equals(user.getConfirmPassword()) == false) {
                session.setAttribute("errorMsg", "~Both Input Password are not Same.~");
            } else {
                User saveUser = us.addUser(user);
                if (saveUser == null || saveUser.getUserName().trim().length() == 0) {
                    session.setAttribute("errorMsg", "~User NOT able to Registered due Internal server error.~");

                } else {
//                store image into user_img folder
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "" + "user_img" + File.separator + imgName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                    fileService.updloadFileS3(file,BucketType.Profile.getId());

                    session.setAttribute("successMsg", "Registration Successfully.");

                }
            }
        }

        return "redirect:/DivaDrop/register";
    }

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = us.getUserByEmail(email);
            m.addAttribute("loginuser", user);
            Integer cart_count = ks.getCountCart(user.getId());
            m.addAttribute("cartCount", cart_count);
        }
        //        get active categories
        List<Category> categories = cs.getAllActiveCategory();
        m.addAttribute("navcategories", categories);

    }

    @GetMapping("/addcart")
    public String processAddtoCart(@RequestParam("pid") Integer pid, @RequestParam("uid") Integer uid, HttpSession session) {
        Cart savecart = ks.saveCart(pid, uid);
        if (ObjectUtils.isEmpty(savecart)) {
            session.setAttribute("errorMsg", "Product add to cart failed");
        } else {
            session.setAttribute("successMsg", "Product added to cart");
        }
        return "redirect:/DivaDrop/products_details/" + pid;
    }

    @GetMapping("/cart")
    public String loadCartPage(Principal p, Model m) {

        User log_user = getLoggedInUserDetails(p);
        List<Cart> cartList = ks.getCartByUser(log_user.getId());
        m.addAttribute("carts", cartList);
        if (cartList.size() > 0) {
            m.addAttribute("totalOrderAmount", Math.round(cartList.get(cartList.size() - 1).getTotalOrderAmount() * 100) / 100.0);
        } else {
            m.addAttribute("totalOrderAmount", 0);
        }
        return "user/cart";
    }

    private User getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        User user = us.getUserByEmail(email);
        return user;
    }

    @GetMapping("/updateQty")
    public String cartQuantity(@RequestParam("cid") Integer cid, @RequestParam("sign") String sign) {
        Boolean flag = ks.updateQuantity(sign, cid);
        return "redirect:/DivaDrop/user/cart";
    }

    @GetMapping("/order")
    public String loadOrderPage(Model m, Principal p) {
        m.addAttribute("order_request", new OrderRequest());
//        showing total price

        User log_user = getLoggedInUserDetails(p);
        List<Cart> cartList = ks.getCartByUser(log_user.getId());
        m.addAttribute("carts", cartList);
        Double totalPrice = 0.0;
        if (cartList.size() > 0) {
            totalPrice += Math.round(cartList.get(cartList.size() - 1).getTotalOrderAmount() * 100) / 100.0 + 40 + 18;
            m.addAttribute("orderAmount", Math.round(cartList.get(cartList.size() - 1).getTotalOrderAmount() * 100) / 100.0);
            m.addAttribute("totalOrderAmount", totalPrice);
        } else {
            m.addAttribute("orderAmount", 0);
            m.addAttribute("totalOrderAmount", totalPrice);
        }
        return "user/order";
    }

    @GetMapping("Ordersuccess")
    public String loadOrderSuccessPage() {
        return "user/Ordersucess";
    }

    @PostMapping("/save_order")
    public String saveOrder(@ModelAttribute("order_request") OrderRequest or, Principal p) throws MessagingException, UnsupportedEncodingException {
        User user = getLoggedInUserDetails(p);
        pos.saveOrder(user.getId(), or);
        return "redirect:/DivaDrop/user/Ordersuccess";
    }

    @GetMapping("/user_orders")
    public String loadMyOrderPage(Model m, Principal p) {
        User user = getLoggedInUserDetails(p);
        List<ProductOrder> orders = pos.getOrdersByUser(user.getId());
        m.addAttribute("myorders", orders);
        return "user/MyOrder";
    }

    @GetMapping("/update-status")
    public String updateOrderSatus(HttpSession session, @RequestParam("id") Integer id, @RequestParam("st") Integer st) throws MessagingException, UnsupportedEncodingException {
        OrderStatus[] values = OrderStatus.values();
        String Status = null;
        for (OrderStatus os : values) {
            if (os.getId() == st) {
                Status = os.getName();
            }
        }
        ProductOrder update = pos.updateStatus(id, Status);
//send mail
        commonUtil.sendmailforproductorder(update, st);
        if (update != null) {
            session.setAttribute("successMsg", "ORDER CANCELED");
        } else {
            session.setAttribute("errorMsg", "Server Issue");

        }
        return "redirect:/DivaDrop/user/user_orders";
    }

    @GetMapping("/profile")
    public String loadUserProfilePage(Model m, Principal p) {
        User user = getLoggedInUserDetails(p);
        m.addAttribute("profile", user);
        return "user/profile";
    }

    @PostMapping("/update_profile")
    public String updateProfile(@ModelAttribute("profile") User user, @RequestParam("file") MultipartFile img, HttpSession session) {
        User update = us.UpdateUserProfile(user, img);
        if (ObjectUtils.isEmpty(update)) {
            session.setAttribute("errorMsg", "Profile Not Updated");
        } else {
            session.setAttribute("successMsg", "Profile Updated Successfully");
        }
        return "redirect:/DivaDrop/user/profile";
    }

    @PostMapping("/change_password")
    public String updatePassword(HttpSession session, @RequestParam("new_password") String new_pswd, @RequestParam("confirm_password") String cnf_pswd, Principal p, @RequestParam("current_password") String cur_pswd) {
        User user = getLoggedInUserDetails(p);
        boolean matches = passwordEncoder.matches(cur_pswd, user.getPassword());
        if (matches) {
            if (new_pswd.equals(cnf_pswd)) {
                String encodePaswword = passwordEncoder.encode(new_pswd);
                user.setPassword(encodePaswword);
                user.setConfirmPassword(new_pswd);
                User update = us.addUser(user);
                if(ObjectUtils.isEmpty(update)){
                session.setAttribute("errorMsg", "Server Error");
                }
                else{
                session.setAttribute("successMsg", "Password Updated Successfully");
                }
            }
            else{
            session.setAttribute("errorMsg", "New Password Different from Confirm Password");
            }
        } else {
            session.setAttribute("errorMsg", "Current Password is Wrong");
        }
        return "redirect:/DivaDrop/user/profile";
    }


}


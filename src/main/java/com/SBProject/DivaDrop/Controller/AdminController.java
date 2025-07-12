package com.SBProject.DivaDrop.Controller;

import com.SBProject.DivaDrop.CommonServices.CommonUtil;
import com.SBProject.DivaDrop.CommonServices.OrderStatus;
import com.SBProject.DivaDrop.Modal.Category;
import com.SBProject.DivaDrop.Modal.Product;
import com.SBProject.DivaDrop.Modal.ProductOrder;
import com.SBProject.DivaDrop.Modal.User;
import com.SBProject.DivaDrop.Service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
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
@RequestMapping("/DivaDrop/admin")
public class AdminController {


    private CategoryService cs;
    private ProductService ps;

    private UserService us;

    private CartService ks;
    private ProductOrderService pos;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    CommonUtil commonUtil;

    @Autowired
    AdminController(@Qualifier("categoryService") CategoryService cs, @Qualifier("productService") ProductService ps, @Qualifier("userService") UserService us, @Qualifier("cartService") CartService ks, @Qualifier("productOrderService") ProductOrderService pos) {
        this.cs = cs;
        this.ps = ps;
        this.us = us;
        this.ks = ks;
        this.pos = pos;
    }



    @GetMapping("/dashboard")
    public String loadAdminDashboard() {
        return "admin/index";
    }


    @GetMapping("/add_category")
    public String loadAddCategoryPage(Model m,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "5")Integer pageSize) {
//        m.addAttribute("categories", cs.getAllCategory());
        Page<Category> page =cs.getAllCategoryPaging(pageNo,pageSize);
        List<Category> categories=page.getContent();
        m.addAttribute("categories",categories);

        m.addAttribute("pageNo",page.getNumber());
        m.addAttribute("pageSize",pageSize);
        m.addAttribute("TotalElements",page.getTotalElements());
        m.addAttribute("TotalPages",page.getTotalPages());
        m.addAttribute("isFirst",page.isFirst());
        m.addAttribute("isLast",page.isLast());

        m.addAttribute("category", new Category());
        return "admin/add_category";
    }


    @PostMapping("/save_category")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imgName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";

        category.setImgName(imgName);
//        category.setImgName(commonUtil.getImageUrl(file, BucketType.Category.getId()));
        Boolean result = cs.existCategory(category.getCategoryName());
        if (result == true) {
            session.setAttribute("errorMsg", "Category Already Exist");
        } else {
            Category saveCat = cs.saveCategory(category);
            if (saveCat == null || saveCat.getCategoryName().trim().length() == 0) {
                session.setAttribute("errorMsg", "Category NOT Added due Internal server error.");

            } else {
////                store image into category_img folder
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "" + "category_img" + File.separator + imgName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

//                fileService.updloadFileS3(file,BucketType.Category.getId());
                session.setAttribute("successMsg", "Added Successfully.");
            }
        }
        return "redirect:/DivaDrop/admin/add_category";
    }


    @GetMapping("/delete_category/{id}")
    public String loadDeleteCategory(@PathVariable("id") int id, HttpSession session) {
        Boolean res = cs.deleteCategory(id);
        if (res == true) {
            session.setAttribute("successMsg", "Category Deleted successfully");
        } else {
            session.setAttribute("errorMsg", "Category Not Deleted");
        }
        return "redirect:/DivaDrop/admin/add_category";
    }


    @GetMapping("/edit_category/{id}")
    public String loadEditCategory(Model m, @PathVariable("id") int id) {
        Category category = cs.getCategoryById(id);
        m.addAttribute("category", category);
        return "admin/edit_category";
    }

    @PostMapping("/update_category")
    public String loadUpdateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        Category old = cs.getCategoryById(category.getId());
        if (old != null) {
            old.setCategoryName(category.getCategoryName());
            old.setIsActive(category.getIsActive());
            String imgName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
            old.setImgName(imgName);
//
            Category update = cs.saveCategory(old);
            if (update != null) {
                if (!file.isEmpty()) {
//                    store image into category_img folder
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "" + "category_img" + File.separator + imgName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                    fileService.updloadFileS3(file,BucketType.Category.getId());
                }
                session.setAttribute("successMsg", "Category Updated Successfully");
            } else {
                session.setAttribute("errorMsg", "Category Not Updated");
            }
        } else {
            session.setAttribute("errorMsg", "Category Not Found");

        }
        return "redirect:/DivaDrop/admin/edit_category/" + category.getId();

    }


    @GetMapping("/add_product")
    public String loadAddProductPage(Model m) {
//        fetched categories
        m.addAttribute("categories", cs.getAllCategory());
        m.addAttribute("product", new Product());
        return "admin/add_product";
    }


    @PostMapping("/save_product")
    public String loadSaveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imgName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        product.setImgName(imgName);

//        product.setImgName(commonUtil.getImageUrl(file, BucketType.Product.getId()));

        Boolean result = ps.existProduct(product.getProductName());
        if (result == true) {
            session.setAttribute("errorMsg", "Product Already Exist");
        } else {
            Product saveProd = ps.addProduct(product);
            if (saveProd == null || saveProd.getProductName().trim().length() == 0) {
                session.setAttribute("errorMsg", "Product NOT Added due Internal server error.");

            } else {
//                store image into product_img folder
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "" + "product_img" + File.separator + imgName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                fileService.updloadFileS3(file,BucketType.Product.getId());
                session.setAttribute("successMsg", "Added Successfully.");
            }
        }
        return "redirect:/DivaDrop/admin/add_product";
    }

    @GetMapping("/view_product")
    public String loadViewProduct(Model m,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "5")Integer pageSize) {
        Page<Product> page = ps.getAllActiveProductPaging2(pageNo,pageSize);

//        m.addAttribute("products", products);
        List<Product> products=page.getContent();
        m.addAttribute("products",products);
        m.addAttribute("productSize",products.size());

        m.addAttribute("pageNo",page.getNumber());
        m.addAttribute("pageSize",pageSize);
        m.addAttribute("TotalElements",page.getTotalElements());
        m.addAttribute("TotalPages",page.getTotalPages());
        m.addAttribute("isFirst",page.isFirst());
        m.addAttribute("isLast",page.isLast());

        return "admin/view_product";
    }

    @GetMapping("/edit_product/{id}")
    public String loadEditProduct(@PathVariable("id") int id, Model m) {
        Product product = ps.getProductById(id);
        m.addAttribute("categories", cs.getAllCategory());
        m.addAttribute("product", product);
        return "admin/edit_product";
    }

    @PostMapping("/update_product")
    public String loadUpdateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {

        Product old = ps.getProductById(product.getId());
        if (old != null) {
            old.setProductName(product.getProductName());
            old.setCategory(product.getCategory());
            old.setDiscount(product.getDiscount());
            old.setDescription(product.getDescription());
            old.setPrice(product.getPrice());
            old.setQuantity(product.getQuantity());
            old.setIsActive(product.getIsActive());

            String imgName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";

            old.setImgName(imgName);

            Product update = ps.addProduct(old);

            if (update != null) {
                if (!file.isEmpty()) {
//                    store image into product_img folder
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "" + "product_img" + File.separator + imgName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                    fileService.updloadFileS3(file,BucketType.Product.getId());

                }
                session.setAttribute("successMsg", "Product Updated Successfully");
            } else {
                session.setAttribute("errorMsg", "Product Not Updated");
            }
        } else {
            session.setAttribute("errorMsg", "Product Not Found");

        }
        return "redirect:/DivaDrop/admin/edit_product/" + product.getId();

    }


    @GetMapping("/delete_product/{id}")
    public String loadDeleteProduct(@PathVariable("id") int id, HttpSession session) {
        Boolean res = ps.deleteCategory(id);
        if (res == true) {
            session.setAttribute("successMsg", "Product Deleted Successfully");
        } else {
            session.setAttribute("errorMsg", "Category Not Deleted ");
        }
        return "redirect:/DivaDrop/admin/view_product";
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

//    @GetMapping("/view_user")
//    public String loadViewUsers(Model m) {
//        List<User> user = us.getAllUsersByRole("ROLE_USER");
//        m.addAttribute("users", user);
//        return "admin/view_users";
//    }

    @GetMapping("/view_user")
    public String loadViewUsers(@RequestParam(defaultValue = "ROLE_USER") String role, Model m,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "5")Integer pageSize) {
        Page<User> page = us.getAllUsersByRolePagging(pageNo,pageSize,role);
        List<User> users=page.getContent();
        m.addAttribute("pageNo",page.getNumber());
        m.addAttribute("pageSize",pageSize);
        m.addAttribute("TotalElements",page.getTotalElements());
        m.addAttribute("TotalPages",page.getTotalPages());
        m.addAttribute("isFirst",page.isFirst());
        m.addAttribute("isLast",page.isLast());

        m.addAttribute("users", users);
        m.addAttribute("selectedRole", role);
        return "admin/view_users";
    }

    @GetMapping("/update_sts")
    public String updateUserStatus(@RequestParam("status") Boolean status, @RequestParam("id") Integer id, HttpSession session) {
        Boolean res = us.updateUserStatus(id, status);
        if (res == true) {
            session.setAttribute("successMsg", "User's Status Updated");
        } else {
            session.setAttribute("successMsg", "!! Server Down !!");
        }
        return "redirect:/DivaDrop/admin/view_user";
    }

    @GetMapping("/orders")
    public String loadAllOrdersPage(Model m,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "5")Integer pageSize) {
        Page<ProductOrder> page=pos.getAllOrdersPaging(pageNo,pageSize);
        List<ProductOrder> orders=page.getContent();
        m.addAttribute("allorders",orders);
//        m.addAttribute("search",false);
        m.addAttribute("pageNo",page.getNumber());
        m.addAttribute("pageSize",pageSize);
        m.addAttribute("TotalElements",page.getTotalElements());
        m.addAttribute("TotalPages",page.getTotalPages());
        m.addAttribute("isFirst",page.isFirst());
        m.addAttribute("isLast",page.isLast());

        return "admin/allOrders";
    }

    @PostMapping("/update-status")
    public String updateOrderStatus(HttpSession session, @RequestParam("id") Integer id, @RequestParam("st") Integer st) throws MessagingException, UnsupportedEncodingException {
        OrderStatus[] values = OrderStatus.values();
        String Status = null;
        for (OrderStatus os : values) {
            if (os.getId() == st) {
                Status = os.getName();
            }
        }
        ProductOrder update = pos.updateStatus(id, Status);
        commonUtil.sendmailforproductorder(update, st);
        if (update != null) {
            session.setAttribute("successMsg", "STATUS UPDATED");
        } else {
            session.setAttribute("errorMsg", "Server Issue");
        }
        return "redirect:/DivaDrop/admin/orders";
    }

    @GetMapping("/search_product")
    public String proccessProductSearch(@RequestParam("ch") String str, Model m, HttpSession session,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "5")Integer pageSize) {
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
        return "admin/view_product";
    }

    @GetMapping("/search_order")
    public String proccessOrderSearch(@RequestParam("ch") String str, Model m, HttpSession session,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name="pageSize",defaultValue = "5")Integer pageSize) {

        Page<ProductOrder> page=null;
        List<ProductOrder> orders=null;
        if (str != null && str.length() > 0) {
            page = pos.searchProductByOrderIdPagging(pageNo,pageSize,str);
            orders=page.getContent();
            m.addAttribute("pageNo",page.getNumber());
            m.addAttribute("pageSize",pageSize);
            m.addAttribute("TotalElements",page.getTotalElements());
            m.addAttribute("TotalPages",page.getTotalPages());
            m.addAttribute("isFirst",page.isFirst());
            m.addAttribute("isLast",page.isLast());

            if (ObjectUtils.isEmpty(orders)) {
                session.setAttribute("errorMsg", "Incorrect OrderId !!");
                page=pos.getAllOrdersPaging(pageNo,pageSize);
                orders=page.getContent();
                m.addAttribute("pageNo",page.getNumber());
                m.addAttribute("pageSize",pageSize);
                m.addAttribute("TotalElements",page.getTotalElements());
                m.addAttribute("TotalPages",page.getTotalPages());
                m.addAttribute("isFirst",page.isFirst());
                m.addAttribute("isLast",page.isLast());
                m.addAttribute("allorders",orders);
            } else {
                m.addAttribute("allorders", orders);
            }
        }
        else{
            page=pos.getAllOrdersPaging(pageNo,pageSize);
            orders=page.getContent();
            m.addAttribute("pageNo",page.getNumber());
            m.addAttribute("pageSize",pageSize);
            m.addAttribute("TotalElements",page.getTotalElements());
            m.addAttribute("TotalPages",page.getTotalPages());
            m.addAttribute("isFirst",page.isFirst());
            m.addAttribute("isLast",page.isLast());

//            session.setAttribute("errorMsg", "Order Id Can't Be Empty !!");
            m.addAttribute("allorders", orders);

        }
//        m.addAttribute("search",true);
        return "admin/allOrders";
    }

    @GetMapping("/register")
    public String loadAddAdminPage(Model m){
        m.addAttribute("user",new User());
        return "admin/add_admin";
    }
    @PostMapping("/save_admin")
    public String loadSaveUser(@ModelAttribute("user") User user, @RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        String imgName = !file.isEmpty() ? file.getOriginalFilename() : "default.jpg";
        user.setImgName(imgName);
//        user.setImgName(commonUtil.getImageUrl(file, BucketType.Category.getId()));

        Boolean result = us.existUser(user.getEmailAddress());
        if (result == true) {
            session.setAttribute("errorMsg", "!! Already Registered !!");
        } else {
            if (user.getPassword().equals(user.getConfirmPassword()) == false) {
                session.setAttribute("errorMsg", "~Both Input Password are not Same.~");
            } else {
                User saveUser = us.addAdmin(user);
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

        return "redirect:/DivaDrop/admin/register";
    }
    private User getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        User user = us.getUserByEmail(email);
        return user;
    }

    @GetMapping("/profile")
    public String loadUserProfilePage(Model m, Principal p) {
        User user = getLoggedInUserDetails(p);
        m.addAttribute("profile", user);
        return "admin/Adminprofile";
    }

    @PostMapping("/update_profile")
    public String updateProfile(@ModelAttribute("profile") User user, @RequestParam("file") MultipartFile img, HttpSession session) {
        User update = us.UpdateUserProfile(user, img);
        if (ObjectUtils.isEmpty(update)) {
            session.setAttribute("errorMsg", "Profile Not Updated");
        } else {
            session.setAttribute("successMsg", "Profile Updated Successfully");
        }
        return "redirect:/DivaDrop/admin/profile";
    }
    @PostMapping("/change_password")
    public String updatePassword(HttpSession session,
                                 @RequestParam("new_password") String newPassword,
                                 @RequestParam("confirm_password") String confirmPassword,
                                 @RequestParam("current_password") String currentPassword,
                                 Principal principal) {

        User user = getLoggedInUserDetails(principal);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            session.setAttribute("errorMsg", "Current password is incorrect");
            return "redirect:/DivaDrop/admin/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("errorMsg", "New password and confirm password do not match");
            return "redirect:/DivaDrop/admin/profile";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setConfirmPassword(newPassword); // optional: store confirm password
        User updatedUser = us.addUser(user);  // should be save/update method

        if (ObjectUtils.isEmpty(updatedUser)) {
            session.setAttribute("errorMsg", "Failed to update password due to server error");
        } else {
            session.setAttribute("successMsg", "Password updated successfully");
        }

        return "redirect:/DivaDrop/admin/profile";
    }

}

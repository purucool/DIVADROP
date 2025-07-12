package com.SBProject.DivaDrop.CommonServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.http.HttpRequest;

@Service
public class CommonServicesImpl implements CommonServices{


    @Override
    public void removeSessionMssg() {
        HttpServletRequest request= ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        HttpSession session=request.getSession();
        session.removeAttribute("successMsg");
        session.removeAttribute("errorMsg");
    }

    @Override
    public Double discountedPrice(Double price,int discount) {
        if(discount>=100 || discount<=0)return price;
        return new Double((price*(100-discount))/100);
    }
}

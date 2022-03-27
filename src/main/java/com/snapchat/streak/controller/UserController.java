package com.snapchat.streak.controller;

import com.snapchat.streak.entity.Friend;
import com.snapchat.streak.entity.User;
import com.snapchat.streak.services.FriendService;
import com.snapchat.streak.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;

    public UserController(){
    }

    @GetMapping("/")
    public String start(){
        return "redirect:signUp";
    }
    @GetMapping("/signUp")
    public String signUp(HttpSession httpSession){
        if(httpSession.getAttribute("username")!=null && !httpSession.getId().isEmpty()){
            return "redirect:home";
        }
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute User user,HttpSession httpSession) {
        if(httpSession.getAttribute("username")!=null && !httpSession.getId().isEmpty()){
            return "redirect:home";
        }
        if(this.userService.signUp(user)){
            return "redirect:signIn";
        }
        else{
            return "signUp";
        }
    }

    @GetMapping("/signIn")
    public String signIn(HttpSession httpSession){
        if(httpSession.getAttribute("username")!=null && !httpSession.getId().isEmpty()){
            return "redirect:home";
        }
        return "signIn";
    }

    @GetMapping("/home")
    public ModelAndView home(HttpSession session){
        ModelAndView modelAndView=new ModelAndView();

        if(!session.getId().isEmpty() && session.getAttribute("username") != null){
            String username=session.getAttribute("username").toString();
            List<User> users=userService.fetchUnFriendUsers(username);
            List<String> sendByStreakList=userService.fetchSendByStreakList(username);
            List<Friend> friendList=friendService.userFriends(username);
            if(sendByStreakList.isEmpty()){
                modelAndView.addObject("noStreaks","You have no streaks.");
            }else{
                modelAndView.addObject("sendByStreakList",sendByStreakList);
            }
            if(users.isEmpty()){
                modelAndView.addObject("noFriends","There are no friends to add.");
            }
            modelAndView.addObject("userFriends",friendList);
            modelAndView.addObject("luminogicsTeam",users);
            modelAndView.setViewName("home");
        }
        else{
            modelAndView.setViewName("signIn");
        }
        return modelAndView;
    }

    @PostMapping("/signIn")
    public String signIn(@ModelAttribute User user , HttpSession httpSession) {
        if(httpSession.getAttribute("username")!=null && !httpSession.getId().isEmpty()){
            return "redirect:home";
        }
        if(this.userService.signIn(user)){
            httpSession.setAttribute("username",user.getUsername());
            return "redirect:home";
        }
        else{
            return "signIn";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        if(!httpSession.getId().isEmpty() && httpSession.getAttribute("username") != null){
            httpSession.invalidate();
        }
        return "redirect:signIn";
    }

}

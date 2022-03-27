package com.snapchat.streak.controller;

import com.snapchat.streak.entity.Streak;
import com.snapchat.streak.entity.StreakCount;
import com.snapchat.streak.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/addFriend")
    public String addFriend(@RequestParam(required = true) String friend, HttpSession session) {
        if (!session.getId().isEmpty() && session.getAttribute("username") != null) {
            String name = session.getAttribute("username").toString();
            friendService.addFriend(friend, name);
            friendService.addFriend(name, friend);
            return "redirect:home";
        } else {
            return "signIn";
        }

    }

    @GetMapping("/streakPage")
    public ModelAndView streakPage(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        if (!session.getId().isEmpty() && session.getAttribute("username") != null) {
            String name = session.getAttribute("username").toString();
            List<StreakCount> streakCountList=friendService.getFriendsAndStreakCount(name);
            modelAndView.addObject("friendsAndStreakCount",streakCountList);
            modelAndView.setViewName("streakPage");
        } else {
            modelAndView.setViewName("signIn");
        }
        return modelAndView;
    }

    @PostMapping("/sendStreak")
    public String sendStreak(@RequestParam(required = false) List<String> myFriends, @RequestParam String streak, HttpSession httpSession) {
        if(myFriends==null){
            return "redirect:streakPage";
        }
        String name = httpSession.getAttribute("username").toString();
        friendService.sendStreak(streak, myFriends, name);
        return "redirect:home";
    }

    @GetMapping("/getStreaks")
    public ModelAndView getStreaks(@RequestParam String friend, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String name = httpSession.getAttribute("username").toString();
        httpSession.setAttribute("streakFriendName", friend);
        friend = httpSession.getAttribute("streakFriendName").toString();
        List<Streak> streakList = friendService.getStreaks(name, friend);
        if (streakList != null) {
            modelAndView.addObject("streakList", streakList);
            modelAndView.setViewName("streakList");
        } else {
            modelAndView.setViewName("home");
        }
        return modelAndView;
    }

    @GetMapping("/openStreak")
    public ModelAndView openStreak(@RequestParam int order, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String name = httpSession.getAttribute("username").toString();
        String friend = httpSession.getAttribute("streakFriendName").toString();
        String message = friendService.deleteStreakAndSendMessage(name, friend, order);
        if (message != null) {
            modelAndView.addObject("message", message);
            modelAndView.setViewName("showMessage");
        }
        return modelAndView;
    }

    @GetMapping("/checkStreakList")
    public ModelAndView checkStreakList(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String name = httpSession.getAttribute("username").toString();
        String friend = httpSession.getAttribute("streakFriendName").toString();
        List<Streak> streakList = friendService.getStreaks(name, friend);
        if (streakList.isEmpty()) {
            modelAndView.setViewName("redirect:home");
        } else {
            modelAndView.addObject("streakList", streakList);
            modelAndView.setViewName("streakList");
        }
        return modelAndView;
    }
}

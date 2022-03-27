package com.snapchat.streak.services;

import com.snapchat.streak.Dao.UserDao;
import com.snapchat.streak.entity.Friend;
import com.snapchat.streak.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;


    public UserServiceImplementation() {
    this.passwordEncoder=new BCryptPasswordEncoder();
    }

    @Override
    public boolean signUp(User user) {
            boolean response=true;
            try {
                if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
                    ArrayList<User> getUser = (ArrayList<User>)userDao.findByUsername(user.getUsername());
                    if (getUser.size() == 1) {
                        if (getUser.get(0).getUsername().equals(user.getUsername())) {
                            response=false;
                        }
                    } else {
                        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
                        user.setPassword(encodedPassword);
                        userDao.save(user);
                        response=true;
                    }
                }
            } catch (Exception e) {
                throw new NullPointerException();
            }
            return response;
    }

    @Override
    public boolean signIn(User user) {
        boolean response=false;
        try {
            List<User> getUser = userDao.findByUsername(user.getUsername());
            if (getUser.size() == 1) {
                if (getUser.get(0).getUsername().equals(user.getUsername()) && this.passwordEncoder.matches(user.getPassword(), getUser.get(0).getPassword())) {
                    response=true;
                }
            }
        } catch (NullPointerException n) {
            n.getMessage();
        }
        return response;
    }

    @Override
    public List<User> fetchUnFriendUsers(String username) {
        ArrayList<User> users=(ArrayList<User>) userDao.findAll();
        List<User> loginUser=userDao.findByUsername(username);
        List<Friend> friendList=loginUser.get(0).getFriendsList();
        users.removeIf(user -> user.getUsername().equals(loginUser.get(0).getUsername()));
        if(friendList!=null){
            for(Friend friend:friendList){
                users.removeIf(user -> user.getUsername().equals(friend.getName()));
            }
        }
        return users;
    }

    @Override
    public List<String> fetchSendByStreakList(String username) {
        List<User> findUser=userDao.findByUsername(username);
        User user=findUser.get(0);
        List<Friend> friendList=user.getFriendsList() !=null ?user.getFriendsList():new ArrayList<>();
        ArrayList<String> names=user.getSendByStreakList() !=null ?(ArrayList<String>) user.getSendByStreakList(): new ArrayList<>();
        int count=0;
        if(friendList.size()!=0){
            for(Friend friend: friendList){
                if(names.size()!=0){
                    if(friend.getName().equals(names.get(count))){
                        if(friend.getStreak()!=null&&friend.getStreak().isEmpty()){
                            names.remove(friend.getName());
                            userDao.save(user);
                        }
                    }
                }
                count++;
            }
        }
        return names;
    }
}

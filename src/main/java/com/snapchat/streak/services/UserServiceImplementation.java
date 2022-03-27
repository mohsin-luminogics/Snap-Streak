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
import java.util.stream.Collectors;

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
        try {
            List<User> loginUser=userDao.findByUsername(username);
            List<Friend> friendList=loginUser.get(0).getFriendsList();
            users.removeIf(user -> user.getUsername().equals(loginUser.get(0).getUsername()));
            if(friendList!=null){
                for(Friend friend:friendList){
                    users.removeIf(user -> user.getUsername().equals(friend.getName()));
                }
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return users;
    }

    @Override
    public List<String> fetchSendByStreakList(String username) {
        List<User> findUser=userDao.findByUsername(username);
        User user=findUser.get(0);
        List<Friend> friendList=user.getFriendsList() !=null ?user.getFriendsList():new ArrayList<>();
        ArrayList<String> names=user.getSendByStreakList() !=null ?(ArrayList<String>) user.getSendByStreakList(): new ArrayList<>();
        try {
            List<String> removeList=new ArrayList<>();
            if(friendList.size()!=0){
                if(names.size()>0){
                    for(String name:names){
                        List<Friend> friendList1=friendList.stream().filter(friend -> friend.getName().equals(name))
                                .collect(Collectors.toList());
                        if(friendList1.get(0).getStreak()!=null&&friendList1.get(0).getStreak().isEmpty()){
                            removeList.add(friendList1.get(0).getName());
                        }
                    }
                    if(removeList.size()>0){
                        names.removeAll(removeList);
                    }
                }
            }
        } catch (Exception n) {
            n.printStackTrace();
        }

        return names;
    }
}

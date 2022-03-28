package com.snapchat.streak.services;

import com.snapchat.streak.Dao.CommonDataDao;
import com.snapchat.streak.Dao.UserDao;
import com.snapchat.streak.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FriendServiceImplementation implements FriendService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CommonDataDao commonDataDao;
    private Friend friend;
    private Streak streak;
    private StreakCount streakCount;
    private boolean firstTime=false;
    private int firstDay=0;
    private int secondDay=0;
    private String date="";

    public FriendServiceImplementation() {
        this.friend = new Friend();
        this.streak = new Streak();
        this.streakCount=new StreakCount();
    }

    @Override
    public boolean addFriend(String friendName, String username) {
        List<User> user = userDao.findByUsername(username);
        try {
            if (!user.isEmpty()) {
                friend.setName(friendName);
                User temp = user.get(0);
                List<Friend> friendList = temp.getFriendsList();
                if (friendList == null) {
                    friendList = new ArrayList<>();
                    friendList.add(friend);
                } else {
                    friendList.add(friend);
                }
                temp.setFriendsList(friendList);
                userDao.save(temp);
                return true;
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Friend> userFriends(String username) {
        List<User> user = userDao.findByUsername(username);
        List<Friend> temp = null;
        try {
            if (!user.isEmpty()) {
                temp = user.get(0).getFriendsList();
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return temp;
    }

    @Override
    public List<StreakCount> getFriendsAndStreakCount(String username) {
        List<StreakCount> friendAndStreakCount=null;
        try {
            List<User> user = userDao.findByUsername(username);
            List<Friend> friendList=user.get(0).getFriendsList() != null ? user.get(0).getFriendsList():new ArrayList<>();
            if(friendList!=null && friendList.size()!=0){
                for (Friend friend:friendList) {
                    CommonData commonData=commonDataDao.findByUniqueId(friend.getUniqueId());
                    if(commonData==null){
                        commonData=new CommonData();
                    }
                    if(friendAndStreakCount==null){
                        friendAndStreakCount=new ArrayList<>();
                    }
                    streakCount.setName(friend.getName());
                    streakCount.setCount(commonData.getStreakCount());
                    friendAndStreakCount.add(streakCount);
                    streakCount=new StreakCount();
                }
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return friendAndStreakCount;
    }

    @Override
    public boolean sendStreak(String streakMsg, List<String> myFriends, String username) {
        try {
            Random rand = new Random(LocalDateTime.now().getSecond());
            int randomNumber = rand.nextInt();
            List<User> activeUser = userDao.findByUsername(username);
            for (String friend : myFriends) {
                Friend activeUserFriend = getActiveUserFriend(activeUser, friend);
                User tempActiveUser = setUniqueIdOfActiveUser(activeUser, activeUserFriend, randomNumber);
                User temp = setSendByStreakList(friend, tempActiveUser);
                List<Friend> friendList = temp.getFriendsList() != null ? temp.getFriendsList() : new ArrayList<>();
                Friend activeUserInFriendList = getActiveUserInFriendList(friendList, tempActiveUser);
                setActiveUserInFriendListUniqueId(activeUserInFriendList,randomNumber);
                setStreakList(activeUserInFriendList,streakMsg);
                CommonData commonData = commonDataDao.findByUniqueId(activeUserInFriendList.getUniqueId());
                streakTimeImplementationAndChecking(commonData,activeUserInFriendList,randomNumber,activeUserFriend);
                userDao.save(tempActiveUser);
                userDao.save(temp);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Streak> getStreaks(String username, String friend) {
        try {
            List<User> user = userDao.findByUsername(username);
            List<Friend> friendList=user.get(0).getFriendsList();
            if(friendList!=null){
                List<Friend> temp=friendList.stream()
                        .filter(friend1 -> friend1.getName().equals(friend))
                        .collect(Collectors.toList());
                return temp.get(0).getStreak();
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteStreakAndSendMessage(String username, String friend, int order) {
        String message=null;
        try {
            List<User> user = userDao.findByUsername(username);
            User activeuser=user.get(0);
            List<Friend> friendList=activeuser.getFriendsList();
            List<Streak> streakList=null;
            if(friendList!=null){
                List<Friend> temp=friendList.stream()
                        .filter(friend1 -> friend1.getName().equals(friend))
                        .collect(Collectors.toList());
                streakList=temp.get(0).getStreak();
            }
            if(streakList!=null && streakList.size()!=0){
                List<Streak> streak=streakList.stream().filter(streak1 -> streak1.getOrder()==order)
                        .collect(Collectors.toList());
                message=streak.get(0).getText();
                streakList.removeIf(streak1 -> streak1.getOrder()==order);
                userDao.save(activeuser);
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return message;
    }

    private User setUniqueIdOfActiveUser(List<User> activeUser, Friend activeUserFriend, int randomNumber) {
        User tempActiveUser = activeUser.get(0);
        try {
            int activeUserUniqueId = activeUserFriend.getUniqueId();
            if (activeUserUniqueId == 0) {
                activeUserFriend.setUniqueId(randomNumber);
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return tempActiveUser;
    }

    private Friend getActiveUserFriend(List<User> activeUser, String friend) {
        Friend activeUserFriend = null;
        try {
            User tempActiveUser = activeUser.get(0);
            List<Friend> activeUserFriendList = tempActiveUser.getFriendsList();
            if (activeUserFriendList != null) {
                List<Friend> friendList1 = activeUserFriendList.stream()
                        .filter(friend1 -> friend1.getName().equals(friend))
                        .collect(Collectors.toList());
                activeUserFriend = friendList1.get(0);
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return activeUserFriend;
    }

    private User setSendByStreakList(String friend, User tempActiveUser) {
        List<User> user = userDao.findByUsername(friend);
        User temp = user.get(0);
        try {
            List<String> sendByStreakList = temp.getSendByStreakList();
            if (sendByStreakList == null) {
                sendByStreakList = new ArrayList<>();
                sendByStreakList.add(tempActiveUser.getUsername());
                temp.setSendByStreakList(sendByStreakList);
            } else if (!sendByStreakList.contains(tempActiveUser.getUsername())) {
                sendByStreakList.add(tempActiveUser.getUsername());
                temp.setSendByStreakList(sendByStreakList);
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return temp;
    }

    private Friend getActiveUserInFriendList(List<Friend> friendList, User tempActiveUser) {
        Friend activeUserInFriendList = null;
        try {
            if (friendList != null) {
                List<Friend> friendList1 = friendList.stream()
                        .filter(friend1 -> friend1.getName().equals(tempActiveUser.getUsername()))
                        .collect(Collectors.toList());
                activeUserInFriendList = friendList1.get(0);
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
        return activeUserInFriendList;
    }
    private void setActiveUserInFriendListUniqueId(Friend activeUserInFriendList,int randomNumber){
        try {
            int checkUniqueId = activeUserInFriendList.getUniqueId();
            if (checkUniqueId == 0) {
                activeUserInFriendList.setUniqueId(randomNumber);
            }
        } catch (Exception n) {
            n.printStackTrace();
        }
    }

    private void setStreakList(Friend activeUserInFriendList,String streakMsg){
        try {
            List<Streak> streakList = activeUserInFriendList.getStreak();
            if (streakList == null || streakList.size()==0) {
                streakList = new ArrayList<>();
                streak.setOrder(1);
                streak.setText(streakMsg);
            } else {
                streak.setText(streakMsg);
                streak.setOrder(streakList.get(streakList.size() - 1).getOrder() + 1);
            }
            streakList.add(streak);
            activeUserInFriendList.setStreak(streakList);
        } catch (Exception n) {
            n.printStackTrace();
        }

    }

    private void streakTimeImplementationAndChecking(CommonData commonData,Friend activeUserInFriendList,int randomNumber,Friend activeUserFriend){
        try {
            LocalDateTime currentTime = LocalDateTime.now();
            if (!activeUserInFriendList.isSendStreak()) {
                activeUserInFriendList.setStartPresentTime(LocalDateTime.now().toString());
                activeUserInFriendList.setEndPresentTime(LocalDateTime.now().plusDays(1).toString());
                activeUserInFriendList.setNextStartTime(LocalDateTime.now().plusDays(2).toString());
                activeUserInFriendList.setSendStreak(true);
                if (commonData == null) {
                    commonData = new CommonData();
                    commonData.setUniqueId(randomNumber);
                    commonData.setStreakCount(0);
                    commonDataDao.save(commonData);
                }
            }
                if (commonData != null) {
                    if(activeUserFriend.getStartPresentTime()!=null){
                    if(!firstTime && activeUserInFriendList.getStartPresentTime().compareTo(
                            activeUserFriend.getEndPresentTime())>0){
                        activeUserFriend.setSendStreak(false);
                        activeUserFriend.setSendCount(0);
                    }
                    }
                    if (currentTime.toString().compareTo(activeUserInFriendList.getStartPresentTime()) >= 0&&
                            currentTime.toString().compareTo(activeUserInFriendList.getEndPresentTime()) <= 0) {
                        if(currentTime.toString().split("T")[0].compareTo(date)==0){
                            activeUserInFriendList.setSendCount(1);
                        }else{
                            date=activeUserInFriendList.getStartPresentTime().split("T")[0];
                            activeUserInFriendList.setSendCount(1);
                            activeUserFriend.setSendCount(0);
                        }
                        if(activeUserFriend.getSendCount()==1 && activeUserInFriendList.getSendCount()==1){
                            if(commonData.getStreakCount()==0 && !firstTime && firstDay==0){
                                commonData.setStreakCount(1);
                                firstTime=true;
                                firstDay=1;
                                secondDay=0;
                            }
                            if(commonData.getStreakCount()==0 && firstTime && firstDay==0){
                                commonData.setStreakCount(commonData.getStreakCount() + 1);
                                activeUserInFriendList.setStartPresentTime(LocalDateTime.now().toString());
                                activeUserInFriendList.setEndPresentTime(LocalDateTime.now().plusDays(1).toString());
                                activeUserInFriendList.setNextStartTime(LocalDateTime.now().plusDays(2).toString());
                                firstDay=1;
                                secondDay=0;
                            }
                        }

                    } else if (currentTime.toString().compareTo(activeUserInFriendList.getEndPresentTime()) > 0 &&
                            currentTime.toString().compareTo(activeUserInFriendList.getNextStartTime()) <= 0 ) {
                        if(currentTime.toString().split("T")[0].compareTo(date)==0){
                            activeUserInFriendList.setSendCount(1);
                        }else{
                            date=activeUserInFriendList.getEndPresentTime().split("T")[0];
                            activeUserFriend.setSendCount(0);
                            activeUserInFriendList.setSendCount(1);
                        }
                        if(activeUserFriend.getSendCount()==1 && activeUserInFriendList.getSendCount()==1){
                            if(commonData.getStreakCount()>0 && secondDay==0){
                                commonData.setStreakCount(commonData.getStreakCount() + 1);
                                activeUserInFriendList.setStartPresentTime(LocalDateTime.now().toString());
                                activeUserInFriendList.setEndPresentTime(LocalDateTime.now().plusDays(1).toString());
                                activeUserInFriendList.setNextStartTime(LocalDateTime.now().plusDays(2).toString());
                                secondDay=1;
                                firstDay=0;
                            }
                        }
                    } else if (currentTime.toString().compareTo(activeUserInFriendList.getNextStartTime()) > 0) {
                        activeUserInFriendList.setStartPresentTime(LocalDateTime.now().toString());
                        activeUserInFriendList.setEndPresentTime(LocalDateTime.now().plusDays(1).toString());
                        activeUserInFriendList.setNextStartTime(LocalDateTime.now().plusDays(2).toString());
                        commonData.setStreakCount(0);
                        firstDay=0;
                        firstTime=false;
                        secondDay=0;
                        activeUserFriend.setSendCount(0);
                        activeUserInFriendList.setSendCount(1);
                        date=currentTime.toString().split("T")[0];
                        activeUserFriend.setSendStreak(false);
                    }
                    commonDataDao.save(commonData);
                }
        } catch (Exception n) {
            n.printStackTrace();
        }
    }
}

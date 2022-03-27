<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Snap Streak</title>
</head>
<style>
    .container{
        width: 50%;
        height: 90vh;
        float:left;
    }
</style>
<body>
<div align="right">
    <a class="logout btn btn-dark" href="/logout">Log Out</a>
</div>
<div align="middle">
    <a class="btn btn-success" href="/home">Snap Streak</a>
</div>
<div class="container">
    <c:if test="${!empty userFriends}">
        <form action="streakPage" method="get">
            <button type="submit" class="btn btn-success">Send Streak</button>
        </form>
    </c:if>

    <h2 class="abc">Friends who send you streaks.</h2>
    <c:if test="${!empty sendByStreakList}">
        <h2>${noStreaks}</h2>
    </c:if>
    <form action="getStreaks">
        <c:forEach var="name" items="${sendByStreakList}">
            <h5>
                <input type="submit" class="btn btn-success ml-4" name="friend" value="${name}"> has send you streak
            </h5>
        </c:forEach>
    </form>
</div>
<div class="container" align="right">
    <h2>You can add friends from here</h2>
    <c:if test="${empty luminogicsTeam}">
        <h2>${noFriends}</h2>
    </c:if>
    <form action="addFriend">
        <c:forEach var="name" items="${luminogicsTeam}">
            <h5>
                <input type="submit" class="btn btn-success ml-4" name="friend" value="${name.getUsername()}">
            </h5>
        </c:forEach>
    </form>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</body>
</html>
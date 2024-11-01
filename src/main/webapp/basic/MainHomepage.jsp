<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Medical Booking</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-image: url('background.jpg'); /* Change to your background image URL */
            background-size: cover;
            background-position: center;
            color: #fff;
        }
        header {
            background: rgba(0, 0, 0, 0.5);
            padding: 20px;
            text-align: center;
        }
        h1 {
            margin: 0;
            font-size: 2.5em;
        }
        nav {
            margin-top: 20px;
        }
        nav a {
            margin: 0 15px;
            text-decoration: none;
            color: #ffffff;
            font-weight: bold;
            padding: 10px 20px;
            border: 2px solid #fff;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
        nav a:hover {
            background-color: rgba(255, 255, 255, 0.3);
        }
        .content {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 80vh;
            text-align: center;
        }
        .btn {
            margin-top: 20px;
            padding: 15px 30px;
            background-color: #4CAF50;
            border: none;
            border-radius: 5px;
            color: white;
            font-size: 1.2em;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <header>
        <h1>Welcome to Online Medical Booking</h1>
        <nav>
            <a class="nav-link" href="<c:url value='/services' />">Services</a>
            <a class="nav-link" href="<c:url value='/login' />">Login</a>
           <a href="<c:url value='/register' />">Register</a>
            <a href="about.jsp">About Us</a>
        </nav>
    </header>
    <div class="content">
        <h2>Your Health, Our Priority</h2>
        <p>Book your appointments online with ease and comfort.</p>
        <a class="nav-link" href="<c:url value='/appointment' />">Appointments</a>
    </div>
</body>
</html>

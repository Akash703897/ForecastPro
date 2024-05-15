<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Weather Information</title>
    <link rel="stylesheet" href="style.css">
    
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>ToDay's Forecast</h2>
        </div>

        <div class="weather-body">
            <img src="${weatherImageUrl}" alt="Weather Image" class="weather-img">

            <div class="weather-box">
                <p class="temperature">${temperature}<sup>°C</sup></p>
                <p class="description">${weatherCondition}</p>
            </div>

            <div class="weather-details">
                <div class="humidity">
                    <i class="fa-sharp fa-solid fa-droplet"></i>
                    <div class="text">
                        <span>${humidity}%</span>
                        <p>Humidity</p>
                    </div>
                </div>

                <div class="wind">
                    <i class="fa-solid fa-wind"></i>
                    <div class="text">
                        <span>${windSpeed} m/s</span>
                        <p>Wind Speed</p>
                    </div>
                </div>
            </div>

            <p class="date">Date: ${date}</p>
        </div>

        <script src="script.js"></script>
         
</body>
</html>
    
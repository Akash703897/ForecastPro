package com.java;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

//@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Servlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String apiKey = "79171fe8df10648dbeab8236fb8c0ffb";
        String city = request.getParameter("city");

        if (city == null || city.isEmpty()) {
            // Handle the case where the city parameter is missing or empty
            response.getWriter().println("Please provide a valid city");
            return;
        }

        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            // Api Integration
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check if the connection was successful
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     InputStreamReader reader = new InputStreamReader(inputStream);
                     Scanner scanner = new Scanner(reader)) {

                    // Want to store in string
                    StringBuilder responseContent = new StringBuilder();

                    while (scanner.hasNext()) {
                        responseContent.append(scanner.nextLine());
                    }

                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);

                    // Date & Time
                 // Extract the necessary data from the JSON response
                    long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
                    String date = new Date(dateTimestamp).toString();

                    double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
                    int temperatureCelsius = (int) (temperatureKelvin - 273.15);

                    int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();

                    double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();

                    String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

                    // Set the data as request attributes
                    request.setAttribute("date", date);
                    request.setAttribute("temperature", temperatureCelsius);
                    request.setAttribute("humidity", humidity);
                    request.setAttribute("windSpeed", windSpeed);
                    request.setAttribute("weatherCondition", weatherCondition);

                    
                    String weatherImageUrl = getWeatherImageUrl(weatherCondition);
                    request.setAttribute("weatherImageUrl", weatherImageUrl);

                    //Forward the request to the index.jsp page for rendering
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                }
            }
            
               else {
                // Handle the case where the API request was not successful
                response.getWriter().println("Failed to retrieve weather data. HTTP Error: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private String getWeatherImageUrl(String weatherCondition) {
		switch (weatherCondition.toLowerCase()) {
		case "clouds":
			return "https://tse3.mm.bing.net/th?id=OIP.qrCK5s3Esd5m0wtHMVobIQHaFV&pid=Api&P=0&h=180";
		case "clear":
			return "https://tse3.mm.bing.net/th?id=OIP.OSotHMLHvrImX-jJnXSgAwHaHZ&pid=Api&P=0&h=180";
		case "rain":
			return "https://tse1.mm.bing.net/th?id=OIP.cJBRyBJmrATjnEv4qCFIuAHaHa&pid=Api&P=0&h=180";
		case "mist":
			return "https://tse4.mm.bing.net/th?id=OIP.vrJ1ZXmX0mqjni6JJ6jwbAHaHa&pid=Api&P=0&h=180";
		case "snow":
			return "https://tse2.mm.bing.net/th?id=OIP.jDo1UM49Q_52GZviSEG2VgHaF2&pid=Api&P=0&h=180";
		default:
			// Provide a default image or handle other conditions
			return "default_image_url";
		}
	}

}

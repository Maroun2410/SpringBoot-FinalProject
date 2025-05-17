// File: src/main/java/com/example/finalprojectmarountheresa/client/RecipeClient.java
package com.example.finalprojectmarountheresa.Client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeClient {

    private static final String BASE_URL = "http://localhost:8080/recipes";
    private static final Scanner scanner = new Scanner(System.in);
    private static String base64Credentials = "";
    private static boolean isAdmin = false;

    public static void main(String[] args) throws Exception {
        loginPrompt();

        while (true) {
            System.out.println("\n--- Recipe Client Menu ---");
            int index = 1;
            if (isAdmin) System.out.println((index++) + ". Add Recipe");
            System.out.println((index++) + ". View All Recipes");
            System.out.println((index++) + ". View Recipe by ID");
            if (isAdmin) System.out.println((index++) + ". Update Recipe");
            if (isAdmin) System.out.println((index++) + ". Delete Recipe");
            System.out.println((index++) + ". View Paginated Recipes");
            System.out.println((index++) + ". Search Recipes (by Category, Ingredient, Title, or MaxTime)");
            System.out.println((index) + ". Exit");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> { if (isAdmin) addRecipe(); else getAllRecipes(); }
                case "2" -> { if (isAdmin) getAllRecipes(); else getRecipeById(); }
                case "3" -> { if (isAdmin) getRecipeById(); else viewPaginatedRecipes(); }
                case "4" -> { if (isAdmin) updateRecipe(); else searchRecipes(); }
                case "5" -> { if (isAdmin) deleteRecipe(); else System.out.println("Exiting..."); return; }
                case "6" -> { if (isAdmin) viewPaginatedRecipes(); else invalid(); }
                case "7" -> { if (isAdmin) searchRecipes(); else invalid(); }
                case "8" -> { if (isAdmin) { System.out.println("Exiting..."); return; } else invalid(); }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void loginPrompt() throws Exception {
        while (true) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            base64Credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

            HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Basic " + base64Credentials);
            con.setRequestProperty("Content-Type", "application/json");

            if (con.getResponseCode() == 200) {
                isAdmin = username.equals("admin");
                System.out.println("\n✅ Login successful as: " + (isAdmin ? "ADMIN" : "USER"));
                return;
            } else {
                System.out.println("\n❌ Login failed. Try again.");
            }
        }
    }

    private static void invalid() {
        System.out.println("\n❌ Access Denied: Admins only.");
    }

    private static void addRecipe() throws Exception {
        String json = buildRecipeJson();
        sendRequest(BASE_URL, "POST", json);
    }

    private static void getAllRecipes() throws Exception {
        sendRequest(BASE_URL, "GET", null);
    }

    private static void getRecipeById() throws Exception {
        System.out.print("Enter recipe ID: ");
        String id = scanner.nextLine();
        sendRequest(BASE_URL + "/" + id, "GET", null);
    }

    private static void updateRecipe() throws Exception {
        System.out.print("Enter recipe ID to update: ");
        String id = scanner.nextLine();
        String json = buildRecipeJson();
        sendRequest(BASE_URL + "/" + id, "PUT", json);
    }

    private static void deleteRecipe() throws Exception {
        System.out.print("Enter recipe ID to delete: ");
        String id = scanner.nextLine();
        sendRequest(BASE_URL + "/" + id, "DELETE", null);
    }

    private static void viewPaginatedRecipes() throws Exception {
        System.out.print("Page number: ");
        int page = Integer.parseInt(scanner.nextLine());

        System.out.print("Page size: ");
        int size = Integer.parseInt(scanner.nextLine());

        System.out.print("Sort field (e.g., title): ");
        String sort = scanner.nextLine();

        System.out.print("Sort direction (asc/desc): ");
        String direction = scanner.nextLine();

        String url = BASE_URL + "/page?page=" + page + "&size=" + size + "&sort=" + sort + "&direction=" + direction;
        sendRequest(url, "GET", null);
    }

    private static void searchRecipes() throws Exception {
        System.out.print("Category (leave blank if none): ");
        String category = scanner.nextLine().trim();

        System.out.print("Ingredient (leave blank if none): ");
        String ingredient = scanner.nextLine().trim();

        System.out.print("Title (leave blank if none): ");
        String title = scanner.nextLine().trim();

        System.out.print("Max Cooking Time (leave blank if none): ");
        String maxTime = scanner.nextLine().trim();

        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/search?");
        if (!category.isEmpty()) urlBuilder.append("category=").append(category).append("&");
        else if (!ingredient.isEmpty()) urlBuilder.append("ingredient=").append(ingredient).append("&");
        else if (!title.isEmpty()) urlBuilder.append("title=").append(title).append("&");
        else if (!maxTime.isEmpty()) urlBuilder.append("maxTime=").append(maxTime).append("&");

        String url = urlBuilder.toString();
        if (url.endsWith("&") || url.endsWith("?")) {
            url = url.substring(0, url.length() - 1);
        }

        sendRequest(url, "GET", null);
    }

    private static String buildRecipeJson() {
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Ingredients (comma-separated): ");
        String[] ingredients = scanner.nextLine().split(",");

        System.out.print("Instructions: ");
        String instructions = scanner.nextLine();

        System.out.print("Cooking Time (minutes): ");
        int cookingTime = Integer.parseInt(scanner.nextLine());

        System.out.print("Category: ");
        String category = scanner.nextLine();

        return String.format("""
            {
                \"title\": \"%s\",
                \"ingredients\": [%s],
                \"instructions\": \"%s\",
                \"cookingTime\": %d,
                \"category\": \"%s\"
            }
        """,
                title,
                String.join(",", wrapWithQuotes(ingredients)),
                instructions,
                cookingTime,
                category
        );
    }

    private static String[] wrapWithQuotes(String[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = "\"" + values[i].trim() + "\"";
        }
        return result;
    }

    private static void sendRequest(String targetUrl, String method, String body) throws Exception {
        System.out.println("Sending request to: " + targetUrl);

        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Authorization", "Basic " + base64Credentials);
        con.setRequestProperty("Content-Type", "application/json");

        if (body != null && !body.isEmpty()) {
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                os.write(body.getBytes());
                os.flush();
            }
        }

        int responseCode = con.getResponseCode();
        System.out.println("HTTP Status: " + responseCode);

        InputStream stream = (responseCode >= 200 && responseCode < 300)
                ? con.getInputStream()
                : con.getErrorStream();

        if (stream == null) {
            System.out.println("No content returned from server.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            String responseBody = response.toString().trim();

            if (responseBody.startsWith("{")) {
                JSONObject json = new JSONObject(responseBody);
                if (json.has("content")) {
                    JSONArray content = json.getJSONArray("content");
                    if (content.isEmpty()) {
                        System.out.println("No recipes found.");
                    }
                    for (int i = 0; i < content.length(); i++) {
                        System.out.println(content.getJSONObject(i).toString(2));
                    }
                } else {
                    System.out.println(json.toString(2));
                }
            } else if (responseBody.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(responseBody);
                if (jsonArray.isEmpty()) {
                    System.out.println("No recipes found.");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    System.out.println(jsonArray.getJSONObject(i).toString(2));
                }
            } else {
                System.out.println(responseBody);
            }
        }
    }
}

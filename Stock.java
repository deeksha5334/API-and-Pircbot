import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class Stock {
    public static final String ALPHA_VANTAGE_API_KEY = "G80LDVNA9KJQMIUZ";
    public static final String ALPHA_VANTAGE_BASE_URL = "https://www.alphavantage.co/query";
    private static final String EXCHANGE_RATE_API_KEY = "66bbe3895a8ece0cb430aca9";

    private static HttpURLConnection fetchAPIResponse(String url_string) {
        try {
            URL url = new URL(url_string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return conn;
        } catch (IOException e) {
            //Prints out the error to the console
            e.printStackTrace();
        }

        System.out.println("Could not connect");
        return null; //Could not connect
    }

    private static String readAPIresponse(HttpURLConnection conn) {
        try {
            StringBuilder resultJson = new StringBuilder();
            Scanner scn = new Scanner(conn.getInputStream());

            while (scn.hasNext()) {
                resultJson.append(scn.nextLine());
            }

            scn.close();
            return resultJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONArray searchStock(String symbol) {
        //Defining the function parameter for the API request
        String function = "SYMBOL_SEARCH";

        //Construct the URL with necessary parameters
        //Alpha Vantage offers a base URL and
        String params = String.format("function=%s&keywords=%s&apikey=%s", function, symbol, ALPHA_VANTAGE_API_KEY);
        String URL = ALPHA_VANTAGE_BASE_URL + "?" + params;
        try {
            //Create a URL Object from the URL String
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(URL);
            //Reference - HTTP URL Codes
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext()) {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();

                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                //Returning the JSON Object
                return (JSONArray) responseObject.get("bestMatches");
            } else {
                System.out.println("Could not connect");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void displayStock(String symbol) throws ParseException {
        JSONArray stocks = searchStock(symbol);
        System.out.println(stocks);

        //Printing error message if data not found
        if (stocks == null || stocks.isEmpty()) {
            System.out.println("No stocks found");
            return;
        }

        for (int i = 0; i < stocks.size(); i++)
        {
            JSONObject stock = (JSONObject) stocks.get(i);
            System.out.println("-".repeat(80));
            System.out.println("STOCK INFORMATION");
            System.out.println("-".repeat(80));
            System.out.println("Symbol: " + stock.get("1. symbol"));
            System.out.println("Name: " + stock.get("2. name"));
            System.out.println("Type: " + stock.get("3. type"));
            System.out.println("Region: " + stock.get("4. region"));
            System.out.println("Currency: " + stock.get("8. currency"));
            System.out.println("-".repeat(80));
        }
    }

    private static JSONObject getStockQuote(String symbol) {
        String function = "GLOBAL_QUOTE";
        String params = String.format("function=%s&symbol=%s&apikey=%s", function, symbol, ALPHA_VANTAGE_API_KEY);
        String URL = ALPHA_VANTAGE_BASE_URL + "?" + params;
        try {
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(URL);
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext()) {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();
                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                return (JSONObject) responseObject.get("Global Quote");
            } else {
                System.out.println("Could not connect");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void displayQuote(String symbol) throws ParseException {
        JSONObject quote = getStockQuote(symbol);

        //Printing error message if data not found
        if (quote == null) {
            System.out.println("No stocks found");
            return;
        }

            System.out.println("-".repeat(80));
            System.out.println("STOCK QUOTE");
            System.out.println("-".repeat(80));
            System.out.println("Stock Name: " + quote.get("01. symbol"));
            System.out.println("Open: " + quote.get("02. open"));
            System.out.println("High: " + quote.get("03. high"));
            System.out.println("Low: " + quote.get("04. low"));
            System.out.println("Price: " + quote.get("05. price"));
            System.out.println("Volume: " + quote.get("06. volume"));
            System.out.println("Latest: " + quote.get("07. latest trading day"));
            System.out.println("Previous Close: " + quote.get("08. previous close"));
            System.out.println("Change: " + quote.get("09. change"));
            System.out.println("Change Percent: " + quote.get("10. change percent"));
            System.out.println("-".repeat(80));


    }

    private static JSONObject getStockHistory(String symbol) {
        String function = "TIME_SERIES_DAILY";
        String params = String.format("function=%s&symbol=%s&apikey=%s", function, symbol, ALPHA_VANTAGE_API_KEY);
        String url = ALPHA_VANTAGE_BASE_URL + "?" + params;
        try {
            //Create a URL Object from the URL String
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(url);
            //Reference - HTTP URL Codes
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext()) {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();

                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                return (JSONObject) responseObject.get("Time Series (Daily)");
            } else {
                System.out.println("Could not connect");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void displayHistory(String symbol) {
        JSONObject history = getStockHistory(symbol);

        // Printing error message if data not found
        if (history == null) {
            System.out.println("No historical data found");
            return;
        }

        // Printing the table header
        System.out.printf("%-12s%-12s%-12s%-12s%-12s%-12s%n", "Date", "Open", "High", "Low", "Close", "Volume");

        // Iterating through the JSON object and printing each entry
        for (Object key : history.keySet()) {
            String date = (String) key;
            JSONObject dailyData = (JSONObject) history.get(date);

            String open = (String) dailyData.get("1. open");
            String high = (String) dailyData.get("2. high");
            String low = (String) dailyData.get("3. low");
            String close = (String) dailyData.get("4. close");
            String volume = (String) dailyData.get("5. volume");

            System.out.printf("%-12s%-12s%-12s%-12s%-12s%-12s%n", date, open, high, low, close, volume);
        }
    }

    private static double currencyConversion(double amount, String fromCurrency, String toCurrency) {
        String URL = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s", EXCHANGE_RATE_API_KEY, fromCurrency, toCurrency);
        try {
            //Create a URL Object from the URL String
            HttpURLConnection conn = (HttpURLConnection) fetchAPIResponse(URL);
            //Reference - HTTP URL Codes
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                Scanner scn = new Scanner(in);
                StringBuilder jsonResult = new StringBuilder();

                while (scn.hasNext()) {
                    jsonResult.append(scn.nextLine());
                }

                scn.close();

                JSONParser parser = new JSONParser();
                JSONObject responseObject = (JSONObject) parser.parse(jsonResult.toString());

                double rate = (double) responseObject.get("conversion_rate");
                System.out.println("Current Conversion rate: " + rate);
                return amount * rate;
            } else {
                System.out.println("Could not connect");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

//    public static String evaluateStock(String symbol) {
//        JSONArray timeSeries = getStockHistory(symbol);
//        if (timeSeries.isEmpty()) {
//            return "Failed to fetch data for " + symbol + ".";
//        }
//
//        int recentDaysCount = 5;
//        double[] recentCloses = new double[recentDaysCount];
//        int index = 0;
//        for (String date : timeSeries.set()) {
//            if (index >= recentDaysCount) break;
//            recentCloses[index] = (JSONObject) timeSeries.get(date).getDouble("4. close");
//            index++;
//        }
//
//        double averageClose = 0;
//        for (double close : recentCloses) {
//            averageClose += close;
//        }
//        averageClose /= recentDaysCount;
//        double latestClose = recentCloses[recentDaysCount - 1];
//        double threshold = 1.05;
//        String message;
//        if (latestClose > averageClose * threshold) {
//            message = symbol + " looks like a good investment based on recent performance.";
//        } else {
//            message = symbol + " might not be a good investment based on recent performance.";
//        }
//
//        System.out.println("This is an evaluation of the stock: " + symbol);
//        System.out.println("Recent closing prices: " + arrayToString(recentCloses));
//        System.out.println("Average close price: " + round(averageClose, 2));
//        System.out.println("Latest close price: " + round(latestClose, 2));
//        return message;
//    }

    public static void main(String[] args) throws ParseException {
        Scanner scn = new Scanner(System.in);

        while (true) {
            System.out.println("=".repeat(80));
            System.out.println("STOCK MARKET ANALYSER");
            System.out.println("=".repeat(80));
            System.out.println("Services: ");
            System.out.println("1. Search Stocks");
            System.out.println("2. Get Stock Quote");
            System.out.println("3. Get Stock History (Last 100 Days)");
            System.out.println("4. Evaluate a stock (display recent stock data and also evaluate)");
            System.out.println("5. Convert currency");
            System.out.println("6. Exit");

            System.out.print("Choose an option: ");
            String userChoice = scn.nextLine();

            switch (userChoice) {
                case "1":
                    System.out.print("Enter a symbol or keywords to search for stocks: ");
                    String symbol = scn.nextLine();
                    displayStock(symbol);
                    break;
                case "2":
                    System.out.print("Enter a stock symbol to get quote: ");
                    symbol = scn.nextLine();
                    displayQuote(symbol);
                    break;
                case "3":
                    System.out.print("Enter a stock symbol to get history: ");
                    symbol = scn.nextLine();
                    displayHistory(symbol);
                    break;
//                case "4":
//                    System.out.print("Enter stock symbol: ");
//                    symbol = scn.nextLine();
//                    System.out.println(evaluateStock(symbol));
//                    break;
                case "5":
                    System.out.print("Enter amount: ");
                    double amount = Double.parseDouble(scn.nextLine());
                    System.out.print("From currency (e.g., USD): ");
                    String fromCurrency = scn.nextLine().toUpperCase();
                    System.out.print("To currency (e.g., EUR): ");
                    String toCurrency = scn.nextLine().toUpperCase();
                    System.out.println(amount + " " + fromCurrency + " = " + currencyConversion(amount, fromCurrency, toCurrency) + " " + toCurrency);
                    break;
                case "6":
                    System.out.println("Thank you for using the Stock Market Analyser.");
                    scn.close();
                    return;
                default:
                    System.out.println("This choice is invalid. Please try again.");
            }
        }
    }
}

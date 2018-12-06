package no.semester.oppgave.weather;
/* class that fetches the weather-data from openweather.com */
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FetchWeatherData extends AsyncTask<String, Void, ArrayList<Weather>> {
    private ArrayList<Weather> weathers;


    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        return doFetchWeatherData(params[0]);
    }

    private ArrayList<Weather> doFetchWeatherData(String dataUrl) {
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            url = new URL(dataUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            forecastJsonStr = buffer.toString();
            Log.e("Json1", forecastJsonStr);

            weathers = new ArrayList<>();
            Weather weather = new Weather();

            JSONObject weatherObject = new JSONObject(forecastJsonStr);

            weather.setName(weatherObject.getString("name"));

            JSONArray weatherArray = weatherObject.getJSONArray("weather");
            for (int i = 0; i < 1; i++) {
                JSONObject each = (JSONObject) weatherArray.get(i);
                weather.setDescription(each.getString("description"));
            }

            JSONObject windObject = weatherObject.getJSONObject("wind");
            weather.setWindSpeed(windObject.getDouble("speed"));
            // openweather sometimes removes wind degree from their API, this then becomes null
            weather.setWindDirection(windDirection(windObject.getInt("deg")));


            JSONObject mainObject = weatherObject.getJSONObject("main");
            weather.setTemp(calvinConverter(mainObject.getDouble("temp")));
            weather.setPressure(mainObject.getDouble("pressure"));
            weather.setHumidity(mainObject.getInt("humidity"));

            weathers.add(weather);


        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return weathers;
    }

    //openweather removes wind degree from their API sometimes, which bugges this
    private String windDirection(int degree) {
        String direction = "";

        if (degree >= 26 && degree <= 65) {
            direction = "South-West";
        } else if (degree >= 66 && degree <= 115) {
            direction = "West";
        } else if (degree >= 116 && degree <= 155) {
            direction = "North-West";
        } else if (degree >= 156 && degree <= 205) {
            direction = "North" ;
        } else if (degree >= 206 && degree <= 245) {
            direction = "North-East";
        } else if (degree >= 246 && degree <= 295) {
            direction = "East";
        } else if (degree >= 296 && degree <= 335) {
            direction = "South-East";
        } else if (degree >= 336) {
            direction = "South";
        }
        return direction;
    }

    private double calvinConverter(double calvin) {
        return Math.round(calvin - 273.15);
    }

}

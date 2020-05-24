package mesh.plugin.cbr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.stream.Collectors;

public final class cbr {

    public static Double getKeyRate() {
        Calendar now = Calendar.getInstance();
        int weekDay = now.get(Calendar.DAY_OF_WEEK);
        if(weekDay == Calendar.SUNDAY)
            now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) - 2);
        else if(weekDay == Calendar.SATURDAY)
            now.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) - 1);
        String date = new SimpleDateFormat("dd.MM.yyyy").format(now.getTime());
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL("https://www.cbr.ru/hd_base/KeyRate/?UniDbQuery.Posted=True&UniDbQuery.From=" + date + "&UniDbQuery.To=" + date).openConnection());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String data = br.lines().collect(Collectors.joining("\n"));
            data = data.substring(data.indexOf("<td>" + date + "</td>") + 4);
            data = data.substring(data.indexOf("<td>") + 4);
            data = data.substring(0, data.indexOf("<")).replace(",", ".");
            return Double.parseDouble(data) / 100.0;
        } catch(Exception ex) {
            ex.printStackTrace();//DEBUG
            return 0.0;
        }
    }
}

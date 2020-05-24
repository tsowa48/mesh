package mesh.plugin.fedstat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public final class fedstat {
    public static Double getLivingWage(int year) {
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL("https://www.fedstat.ru/indicator/dataGrid.do?id=30957").openConnection());
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Origin", "https://www.fedstat.ru");
            connection.setRequestProperty("Sec-Fetch-Site", "same-origin");
            connection.setRequestProperty("Sec-Fetch-Mode", "cors");
            connection.setRequestProperty("Sec-Fetch-Dest", "empty");
            connection.setRequestProperty("Referer", "https://www.fedstat.ru/indicator/30957");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9,ru;q=0.8");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            pw.write("lineObjectIds=0&lineObjectIds=30611&lineObjectIds=57831&columnObjectIds=3&columnObjectIds=33560&columnObjectIds=58812&selectedFilterIds=0_30957&selectedFilterIds=3_"
                    + year
                    + "&selectedFilterIds=30611_950351&selectedFilterIds=33560_1540222&selectedFilterIds=33560_1540224&selectedFilterIds=33560_1540226&selectedFilterIds=33560_1540227&selectedFilterIds=33560_1540273&selectedFilterIds=57831_1688487&selectedFilterIds=58812_1750834");
            pw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String data = br.lines().collect(Collectors.joining("\n"));
            String[] dim = data.split(",");
            dim[3] = dim[3].substring(dim[3].indexOf(": \"") + 3).replace("\"", "");
            dim[4] = dim[4].substring(dim[4].indexOf(": \"") + 3).replace("\"", "");
            dim[5] = dim[5].substring(dim[5].indexOf(": \"") + 3).replace("\"", "");
            dim[6] = dim[6].substring(dim[6].indexOf(": \"") + 3).replace("\"", "")
                    .replace("}", "")
                    .replace("]", "");
            return (Integer.parseInt(dim[3]) + Integer.parseInt(dim[4]) + Integer.parseInt(dim[5]) + Integer.parseInt(dim[6])) / 4.0;
        } catch(Exception ex) {
            ex.printStackTrace();//DEBUG
            return 0.0;
        }
    }
}

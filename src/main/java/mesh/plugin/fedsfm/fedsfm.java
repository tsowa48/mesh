package mesh.plugin.fedsfm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class fedsfm {
    public static Double get(String first_name, String last_name, String patronymic, String birth) {
        try {
            HttpURLConnection connection = (HttpURLConnection)(new URL("http://www.fedsfm.ru/documents/terrorists-catalog-portal-act").openConnection());
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "android");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Long isTerorrist = br.lines().filter(line ->
                    line.contains(first_name.toUpperCase())
                    && line.contains(last_name.toUpperCase())
                    && line.contains(patronymic.toUpperCase())
                    && line.contains(birth.toUpperCase())
            ).count();
            return (isTerorrist == 0L ? 1.0 : 0.0);
        } catch(Exception ex) {
            ex.printStackTrace();//DEBUG
            return 0.0;
        }
    }
}

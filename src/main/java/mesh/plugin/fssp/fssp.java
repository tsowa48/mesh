package mesh.plugin.fssp;

import javafx.util.Pair;
import json.json;
import mesh.plugin.proxy.ProxyList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author tsowa
 */
public final class fssp {
  private static final String udid = "582ada580ac286df";
  private static final Integer version = 41;
  private static final Pattern rDuty = Pattern.compile("(([0-9]{1,},[0-9]{1,2})|([0-9]{1,})) (руб|\\\\u0440\\\\u0443\\\\u0431)\\.");
  private static Integer queryCount = 0;
  private static Proxy proxy;
  
   /***
   * 
   * @param first_name
   * @param last_name
   * @param patronymic
   * @param birth
   * @return fullDuty
   */
  public static Double get(String first_name, String last_name, String patronymic, String birth) {
    return get(first_name, last_name, patronymic, birth, Region.ALL);
  }
  
  /***
   * 
   * @param first_name
   * @param last_name
   * @param patronymic
   * @param birth
   * @param region
   * @return fullDuty
   */
  public static Double get(String first_name, String last_name, String patronymic, String birth, Region region) {
    if(queryCount == 0) {
      Pair<String, Integer> newProxy = ProxyList.next();
      proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(newProxy.getKey(), newProxy.getValue()));
    } else if(queryCount == 9) {
      queryCount = 0;
    }
    try {
      HttpURLConnection connection = (HttpURLConnection)(new URL("https://api.fssprus.ru/api/v2/search?type=form&first_name="
              + URLEncoder.encode(first_name, "UTF-8") + "&last_name=" + URLEncoder.encode(last_name, "UTF-8") + "&patronymic=" + URLEncoder.encode(patronymic, "UTF-8")
              + "&date=" + birth + "&region_id=" + region.getId() + "&udid="
              + udid + "&ver=" + version.toString()).openConnection(proxy));
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "android");
      //connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
      //connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9,ru;q=0.8");
      //connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String data = br.lines().collect(Collectors.joining("\n"));
      json J = new json(data);
      ErrorCode error_code = ErrorCode.values()[J.<Integer>get("error_code")];
System.err.println("[FSSP]: error_code=" + J.<Integer>get("error_code"));//DEBUG
      switch(error_code) {
        case OK:
          ++queryCount;
          json[] jx = J.<json>get("data").<json[]>get("list");
          return Arrays.stream(jx)
                  .map(it -> it.<String>get("subject"))
                  .mapToDouble(it -> {
                    Matcher m = rDuty.matcher(it);
                    Integer groups = m.groupCount();
                    Double partDuty = 0.0;
                    while(m.find()) {
                      String x = m.group()
                        .replaceAll(" руб.", "")
                        .replaceAll(" \\\\u0440\\\\u0443\\\\u0431.", "")
                        .replace(",", ".");
                      partDuty += Double.parseDouble(x);
                    }
                    return partDuty;
                  })
                  .sum();
        case CAPTCHA:
          throw new Exception("[FSSP]: need captcha at query #" + queryCount);
        default:
          return -0.0;
      }
    } catch (Exception ex) {
      ex.printStackTrace();//DEBUG
      queryCount = 0;
      return -0.0;
    }
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mesh.plugin.fssp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import json.json;

/**
 *
 * @author tsowa
 */
public class fssp {
  private static final String udid = "582ada580ac286df";
  private static final Integer version = 41;
  
  /***
   * 
   * @param first_name
   * @param last_name
   * @param patronymic
   * @param birth
   * @param region
   * @return Solvency
   */
  public static Double get(String first_name, String last_name, String patronymic, String birth, Region region) {
    try {
      HttpURLConnection connection = (HttpURLConnection)(new URL("https://api.fssprus.ru/api/v2/search?type=form&first_name="
              + URLEncoder.encode(first_name, "UTF-8") + "&last_name=" + URLEncoder.encode(last_name, "UTF-8") + "&patronymic=" + URLEncoder.encode(patronymic, "UTF-8")
              + "&date=" + birth + "&region_id=" + region.getId() + "&udid="
              + udid + "&ver=" + version.toString()).openConnection());
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", "android");
      //connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
      //connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9,ru;q=0.8");
      //connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String data = br.lines().collect(Collectors.joining("\n"));
System.err.println(data);
      json J = new json(data);
      ErrorCode error_code = ErrorCode.values()[J.<Integer>get("error_code")];
System.err.println("\n\ncode=" + error_code + "\n\n");
      switch(error_code) {
        case OK:
          json[] jx = J.<json>get("data").<json[]>get("list");;
                //[0].<String>get("subject");
          Double fullDuty = 0.0;
          Pattern rDuty = Pattern.compile("(([0-9]{1,},[0-9]{1,2})|([0-9]{1,})) (руб|\\u0440\\u0443\\u0431).");
  System.out.println("\n\n");
          Arrays.stream(jx).map(it -> it.<String>get("subject")).forEach(it -> {
            Matcher m = rDuty.matcher(it);
            Integer groups = m.groupCount();
  System.err.println("groups=" + groups);
            //for(int i = 0; i < groups; ++i) {
              //String x = m.group(i).replaceAll(" руб.", "").replaceAll(" \\u0440\\u0443\\u0431.", "");
//System.out.println("x=" + x);
            //}
          });
          return fullDuty;
        case CAPTCHA:
          //TODO: change proxy
          //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 8080));
          //conn = new URL(urlString).openConnection(proxy);
          return -0.0;
        default:
          return -0.0;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return -0.0;
    }
  }
}

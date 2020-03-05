package mesh.plugin.proxy;

import java.util.ArrayList;
import javafx.util.Pair;

/**
 *
 * @author tsowa
 */
public class ProxyList {
  //http://free-proxy.cz/ru/proxylist/country/all/socks/speed/all
  private static Integer index = 1;
  private static final java.util.List<Pair<String, Integer>> proxies = new ArrayList<>();
  static {
    proxies.add(new Pair<>("198.199.120.102", 1080));
    proxies.add(new Pair<>("198.27.75.152", 1080));
    proxies.add(new Pair<>("67.205.146.29", 1080));
    proxies.add(new Pair<>("159.203.166.41", 1080));
    proxies.add(new Pair<>("159.203.91.6", 1080));
    proxies.add(new Pair<>("165.227.215.62", 1080));
    proxies.add(new Pair<>("149.56.1.48", 8181));
    proxies.add(new Pair<>("165.227.215.71", 1080));
    proxies.add(new Pair<>("104.237.252.52", 54321));
    proxies.add(new Pair<>("162.223.89.69", 1080));
    proxies.add(new Pair<>("162.243.108.161", 1080));
    proxies.add(new Pair<>("67.205.132.241", 1080));
    proxies.add(new Pair<>("68.188.63.149", 54395));
    proxies.add(new Pair<>("206.71.228.193", 8841));
    proxies.add(new Pair<>("138.197.145.103", 1080));
    proxies.add(new Pair<>("192.241.245.207", 1080));
    proxies.add(new Pair<>("138.197.157.45", 1080));
    proxies.add(new Pair<>("67.205.149.230", 1080));
    proxies.add(new Pair<>("162.243.108.129", 1080));
    proxies.add(new Pair<>("67.205.174.209", 1080));
    proxies.add(new Pair<>("192.157.22.80", 61255));
    proxies.add(new Pair<>("71.122.164.51", 54321));
    proxies.add(new Pair<>("207.144.111.230", 64312));
    proxies.add(new Pair<>("12.109.102.86", 64312));
  };
  
  public static Pair<String, Integer> next() {
    Pair<String, Integer> proxy = proxies.get(index - 1);
    if(proxies.size() > index)
      ++index;
    else
      index = 1;
    return proxy;
  }
}

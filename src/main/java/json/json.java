package json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author tsowa
 */
public class json extends Object {
  private static class Entry<V> {
    private String key;
    private V value;
    
    public Entry(V value) {
      key = "";
      this.value = value;
    }
    
    @Override
    public String toString() {
      return ((key == null || key.isEmpty()) ? "" : "\"" + key + "\":") + valueToString(value);
    }
    
    private static String valueToString(Object value) {
      if(value == null)
        return "null";
      else if(value instanceof String)
        return "\"" + value + "\"";
      else if(value instanceof Boolean)
        return (Boolean)value ? "true" : "false";
      else if(value instanceof Object[]) {
        Object[] arrValue = ((Object[])value);
        return "[" + Arrays.stream(arrValue).map(it -> valueToString(it)).collect(Collectors.joining(",")) + "]";
      } else
        return value.toString();
    }
  }
  
  private List<Entry> entry;
  private String _in;
  
  public json(Object obj) {
    if(obj instanceof String) {
      _in = (String)obj;
      entry = stringToJson();
    } else if(obj instanceof json) {
      entry = ((json)obj).entry;
    } else {
      entry = parseClass(obj);
    }
  }
  
  private json() { }
  
  private json(List<Entry> e) {
    entry = e;
  }
  
  public <O> O get(String key) {
    Object value = entry.stream().filter(it -> it.key.equals(key)).findFirst().get().value;
    return (O)value;
  }
  
  @Override
  public String toString() {
    return "{" + entry.stream().map(it -> it.toString()).collect(Collectors.joining(",")) + "}";
  }
  
  private List<Entry> stringToJson() {
    List<Entry> ret = new ArrayList<>();
    _in = _in.substring(1).trim();
    while(!(_in.startsWith("}") || _in.length() < 1)) {
      int startKeyIndex = _in.indexOf("\"") + 1;
      int endKeyIndex = _in.indexOf("\"", startKeyIndex);
      final String key = _in.substring(startKeyIndex, endKeyIndex);
      _in = _in.substring(endKeyIndex + 1).trim();//remove "
      _in = _in.substring(1).trim();//remove :
      Entry e = new Entry(parseValue());
      e.key = key;
      ret.add(e);
    }
    if(_in.length() > 0)
      _in = _in.substring(1).trim();
    if(_in.startsWith(","))
      _in = _in.substring(1).trim();
    return ret;
  }
  
  private Object parseValue() {
    Object ret;
    if(_in.startsWith("\"")) {// String
      _in = _in.substring(1);//remove "
      String subPart = _in.substring(0, _in.indexOf("\""));
      _in = _in.substring(_in.indexOf("\"") + 2).trim();
      ret = subPart;
    } else if(_in.startsWith("[")) {
      _in = _in.substring(1).trim();//remove [
      List<Object> subRet = new ArrayList<>();
      while(!(_in.startsWith("}") || _in.startsWith(",") || _in.startsWith("]"))) {
        subRet.add(parseValue());
      }
      _in = _in.substring(1).trim();
      if(!subRet.isEmpty() && subRet.get(0) instanceof json)
        ret = subRet.stream().map(it -> (json)it).toArray(json[]::new);
      else
        ret = subRet.toArray();
    } else if(_in.startsWith("{")) {
      json J = new json();
      J.entry = stringToJson();
      ret = J;
    } else if(_in.startsWith("null")) {
      ret = null;
      _in = _in.substring(4);
    } else if(_in.startsWith("true") || _in.startsWith("false")) {
      ret = _in.startsWith("true");
      _in = _in.substring(_in.startsWith("true") ? 4 : 5);
    } else {
      int pos1 = _in.indexOf(",");
      int pos2 = _in.indexOf("]");
      int pos3 = _in.indexOf("}");
      pos1 = pos1 > 0 ? pos1 : Integer.MAX_VALUE;
      pos2 = pos2 > 0 ? pos2 : Integer.MAX_VALUE;
      pos3 = pos3 > 0 ? pos3 : Integer.MAX_VALUE;
      String subPart = _in.substring(0, Math.min(pos1, Math.min(pos3, pos2))).trim();
      _in = _in.substring(subPart.length()).trim();
      if(_in.startsWith(","))
        _in = _in.substring(1).trim();
      if(subPart.contains("."))
        ret = Double.valueOf(subPart);
      else
        ret = Integer.valueOf(subPart);
    }
    return ret;
  }
  
  public <T> T toClass(Class<T> clazz) {
    return toClass(clazz, entry);
  }
  
  private static <T> T toClass(Class<T> clazz, List<Entry> e) {
    try {
      T instance = clazz.newInstance();
      Field[] fields = instance.getClass().getDeclaredFields();
      for(int i = 0; i < fields.length; ++i) {
        Field field = fields[i];
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        String key = field.getName();
        Class cls = field.getType();
        Entry subEntry = e.stream().filter(it -> it.key.equals(key)).findFirst().orElse(null);
        Object value = subEntry == null ? null : subEntry.value;
        if(value instanceof Object[] || value instanceof Set) {
          Set<Object> arrValue = new HashSet<>();
          Object[] tmpValue = (value instanceof Set) ? ((Set<Object>)value).toArray() : (Object[])value;
          if(tmpValue.length == 0)
            value = null;
          else {
            Class subClass = cls;
            Type subType = field.getGenericType();
            if(subType instanceof ParameterizedType)
              subClass = (Class)((ParameterizedType)subType).getActualTypeArguments()[0];
//System.err.println("\njson:tmpValue[0]=" + tmpValue[0].getClass().getName());//DEBUG
            for(int index = 0; index < tmpValue.length; ++index) {
              Object subValue = toClass(subClass, ((json)tmpValue[index]).entry);//tmpValue[0].getClass(), (List<Entry>)
              arrValue.add(subValue);
            }
            value = (subType instanceof ParameterizedType) ? arrValue : arrValue.toArray();
          }
        } else if(cls.isEnum()) {
          value = cls.getEnumConstants()[(Integer)value];
        } else if(cls == Date.class) {
          value = new SimpleDateFormat("dd.MM.yyyy").parse((String)value);
        } else if(!isSimpleClass(value))
          value = toClass(cls, (List<Entry>)value);
        field.set(instance, value);
        field.setAccessible(isAccessible);
      }
      return instance;
    } catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  private List<Entry> parseClass(Object obj) {//TODO: fix
    List<Entry> ret = new ArrayList<>();
    try {
      Class userClass = obj.getClass();
      Field[] fields = userClass.getDeclaredFields();
      for(int i = 0; i < fields.length; ++i) {
        Field field = fields[i];
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        String key = field.getName();
        if(key.startsWith("this$"))
          continue;
        Object value = field.get(obj);
        if(field.getType().isEnum()) {
          value = Arrays.asList(field.getType().getEnumConstants()).indexOf(value);
        } else if(value instanceof Date)
          value = new SimpleDateFormat("dd.MM.yyyy").format((Date)value);
        else if(value instanceof Object[] || value instanceof Set)
          value = parseArrayClass(value);
        else if(!isSimpleClass(value))
          value = new json(value);
        field.setAccessible(isAccessible);
        Entry e = new Entry(value);
        e.key = key;
        ret.add(e);
      }
    } catch(IllegalAccessException iae) {
      iae.printStackTrace();
    }
    return ret;
  }
  
  private Object[] parseArrayClass(Object obj) {
    if(obj instanceof Set)
      obj = ((Set)obj).toArray();
    if(isSimpleArray(obj))
      return (Object[])obj;
    Object[] objArray = (Object[])obj;
    ArrayList<Object> subValue = new ArrayList<>();
    for(int i = 0; i < objArray.length; ++i) {
      List<Entry> jsonEntry = parseClass(objArray[i]);
      subValue.add(new json(jsonEntry));
    }
    return subValue.toArray();
  }
  
  private static boolean isSimpleClass(Object obj) {
    return obj instanceof String ||
           obj instanceof Integer ||
           obj instanceof Long ||
           obj instanceof Boolean ||
           obj instanceof Double ||
           obj == null;
  }
  
  private static boolean isSimpleArray(Object obj) {
    return obj instanceof String[] ||
           obj instanceof Integer[] ||
           obj instanceof Long[] ||
           obj instanceof Boolean[] ||
           obj instanceof Double[];
  }
}

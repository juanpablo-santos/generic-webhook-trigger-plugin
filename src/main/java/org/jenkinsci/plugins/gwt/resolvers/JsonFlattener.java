package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonFlattener {
  public JsonFlattener() {}

  @SuppressWarnings("unchecked")
  public Map<String, String> flattenJson(String key, String regexFilter, Object resolved) {
    Map<String, String> resolvedVariables = newHashMap();
    if (resolved instanceof List) {
      int i = 0;
      for (Object o : (List<?>) resolved) {
        resolvedVariables.putAll(flattenJson(key + "_" + i, regexFilter, o));
        i++;
      }
    } else if (resolved instanceof Map) {
      for (Entry<String, Object> entry : ((Map<String, Object>) resolved).entrySet()) {
        resolvedVariables.putAll(
            flattenJson(key + "_" + entry.getKey(), regexFilter, entry.getValue()));
      }
    } else if (resolved != null) {
      String variableName = toVariableName(key);
      resolvedVariables.put(variableName, filter(resolved.toString(), regexFilter));
    }
    return resolvedVariables;
  }
}

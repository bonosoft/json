/*
 * MIT License
 *
 * Copyright (c) 2018 Bonosoft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package bonosoft.json;

import bonosoft.json.elements.JsonElement;
import bonosoft.json.elements.JsonElementType;
import bonosoft.json.helper.JsonItem;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static bonosoft.json.helper.JsonItem.isNotEmpty;

/**
 * by bn on 23/11/2018.
 */
public class Json extends JsonElement {
    public Map<String, JsonElement> elements = new Hashtable<>();
    public Map<String, List<JsonElement>> elementLists = new Hashtable<>();

    public static Json parse(String jsonString) {
        if (jsonString == null) {
            return null;
        }
        boolean inQuotes = false;
        StringBuilder builder = new StringBuilder();
        Queue<JsonElement> parts = new LinkedList<>();
        char[] chars = jsonString.toCharArray();
        for (int index = 0; index < chars.length; index++) {
            char ch = chars[index];
            if (inQuotes) {
                if (ch == '\\') {
                    index++;
                    char nch = chars[index];
                    switch (nch) {
                        case '"':
                            // builder.append("\\\"");
                            builder.append("\"");
                            break;

                        case '\\':
                            // builder.append("\\\\");
                            builder.append("\\");
                            break;

                        case '/':
                            //builder.append("\\/");
                            builder.append("/");
                            break;

                        case 'b':
                            builder.append("\b");
                            break;

                        case 'f':
                            builder.append("\f");
                            break;

                        case 'n':
                            builder.append("\n");
                            break;

                        case 'r':
                            builder.append("\r");
                            break;

                        case 't':
                            builder.append("\t");
                            break;

                        case 'u':
                            StringBuilder buf = new StringBuilder();
                            buf.append(chars[1 + index]).append(chars[2 + index]).append(chars[3 + index]).append(chars[4 + index]);
                            builder.append(String.valueOf(Character.toChars(Integer.parseInt(buf.toString(), 16))));
                            index += 4;
                            break;

                    }
                } else if (ch == '"') {
                    parts.offer(new JsonElement(JsonElementType.JsonKey, builder.toString(), true));
                    builder.setLength(0);
                    inQuotes = false;
                } else {
                    builder.append(ch);
                }
            } else {
                switch (ch) {
                    case '"':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        inQuotes = true;
                        break;

                    case ',':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        parts.offer(new JsonElement(JsonElementType.JsonSplit));
                        break;

                    case ':':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        parts.offer(new JsonElement(JsonElementType.JsonPair));
                        break;

                    case '{':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        parts.offer(new JsonElement(JsonElementType.JsonStart));
                        break;

                    case '}':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        parts.offer(new JsonElement(JsonElementType.JsonEnd));
                        break;

                    case '[':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        parts.offer(new JsonElement(JsonElementType.JsonArrayStart));
                        break;

                    case ']':
                        if (builder.toString().trim().length() > 0) {
                            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
                            builder.setLength(0);
                        }
                        parts.offer(new JsonElement(JsonElementType.JsonArrayEnd));
                        break;

                    default:
                        if (!Character.isWhitespace(ch)) {
                            if (parts.size() == 0) {
                                parts.offer(new JsonElement(JsonElementType.JsonStart));
                            }
                            builder.append(ch);
                        }
                        break;
                }
            }
        }
        if (builder.toString().trim().length() > 0) {
            parts.offer(new JsonElement(JsonElementType.JsonValue, builder.toString()));
        }

        Json result = new Json();
        JsonElement entry = parts.poll();
        if (entry != null) {
            if (entry.isStart()) {
                result.build(parts);
            } else if (entry.isArrayStart()) {
                result.elementLists.put("", buildArray(parts));
            }
        }
        return result;
    }

    private static List<JsonElement> buildArray(Queue<JsonElement> parts) {
        JsonElement entry;
        List<JsonElement> arrayElements = new ArrayList<>();
        do {
            if (parts.size() == 0) {break;}
            entry = parts.poll();

            if (entry.isStart()) {
                Json json = new Json();
                arrayElements.add(json);
                json.build(parts);
            } else if (entry.isKey() || entry.isValue()) {
                arrayElements.add(entry);
            } else if (entry.isArrayStart()) {
                Json json = new Json();
                arrayElements.add(json);
                json.elementLists.put("", buildArray(parts));
            } else {
                // isSplit or somethings worng
            }

        } while (!entry.isArrayEnd());
        return arrayElements;
    }

    private void build(Queue<JsonElement> parts) {
        while (parts.size() > 0) {
            JsonElement entry = parts.poll();
            if (entry.isEnd()) {
                return;
            } else if (entry.isKey()) {
                JsonElement pairKey = entry;

                if (parts.size() == 0) {
                    elements.put("", pairKey);
                    return;
                }
                entry = parts.poll();

                if (entry.isPair()) {
                    if (parts.size() == 0) {return;}
                    entry = parts.poll();

                    if (entry.isKey()) {
                        elements.put(pairKey.content, entry);
                    } else if (entry.isValue()) {
                        elements.put(pairKey.content, entry);
                    } else if (entry.isStart()) {
                        Json json = new Json();
                        elements.put(pairKey.content, json);
                        json.build(parts);
                    } else if (entry.isArrayStart()) {
                        Json json = new Json();
                        elements.put(pairKey.content, json);
                        json.elementLists.put(pairKey.content, buildArray(parts));
                    } else {
                        // somethings wrong
                    }
                } else {
                    if (parts.size() == 0) {
                        elements.put("", pairKey);
                        return;
                    }
                }
            } else if (entry.isValue()) {
                elements.put("", entry);
            } else if (entry.isArrayStart()) {
                Json json = new Json();
                elements.put("", json);
                json.elementLists.put("", buildArray(parts));
            } else if (entry.isSplit()) {
                // ok
            } else {
                // somethings wrong
            }
        }
    }

    public Json() {
        super(JsonElementType.Json);
    }

    public String get(String key) {
        return get(key, "");
    }
    public String get(String key, String defaultValue) {
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            String value = jsonElement.content;
            if (value!=null && value.equalsIgnoreCase("null")) return defaultValue;
            return value;
        }
        return defaultValue;
    }

    public Json getObject(String key) {
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            if (jsonElement instanceof Json) {
                return (Json) jsonElement;
            }
        }
        return null;
    }


    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            if (jsonElement.content != null) {
                String str = jsonElement.content;
                if (str==null) return defaultValue;
                if (str.endsWith("%")) {
                    str = str.substring(0, str.length() - 1);
                }
                if (str.equalsIgnoreCase("null")) return defaultValue;
                try {
                    return Integer.parseInt(str);
                } catch (Exception e) {
                    // return default
                }
            }
        }
        return defaultValue;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            if (jsonElement.content != null) {
                String str = jsonElement.content;
                return Boolean.parseBoolean(str) || str.equalsIgnoreCase("1");
            }
        }
        return defaultValue;
    }


    public String getDecoded(String key) {
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            return new String(Base64.getDecoder().decode((jsonElement.content)), StandardCharsets.UTF_8);
        }
        return "";
    }

    public String toJsonEscaped() {
        String str = toJson();
        StringBuilder builder = new StringBuilder();
        for (char ch : str.toCharArray()) {
            if (ch == '"') {
                builder.append('\\').append(ch);
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }


    public String toJson() {
        StringBuilder builder = new StringBuilder();

        boolean overAppend = false;
        String separator = "";

        if (elements.size() > 0) {
            builder.append(separator);
            builder.append("{");
            overAppend = true;
            String listSeparator = "";
            for (Map.Entry<String, JsonElement> entry : elements.entrySet()) {
                builder.append(listSeparator);
                if (entry.getKey().length() > 0) {
                    builder.append("\"").append(escape(entry.getKey())).append("\":").append(entry.getValue().toElementJson());
                } else {
                    builder.append(entry.getValue().toElementJson());
                }
                listSeparator = ",";
            }
            separator = ",";
        }

        for (Map.Entry<String, List<JsonElement>> entry : elementLists.entrySet()) {
            builder.append(separator);
            if (elementLists.size() > 1) {
                if (builder.length() == 0) {
                    builder.append("{");
                    overAppend = true;
                }
            }

            if (entry.getKey().length() > 0) {
                if (builder.length() == 0) {
                    builder.append("{");
                    overAppend = true;
                }

                builder.append("\"").append(escape(entry.getKey())).append("\":[");
                String listSeparator = "";
                for (JsonElement jsonElement : entry.getValue()) {
                    builder.append(listSeparator);
                    builder.append(jsonElement.toElementJson());
                    listSeparator = ",";
                }
                builder.append("]");
            } else {
                builder.append("[");
                String listSeparator = "";
                for (JsonElement jsonElement : entry.getValue()) {
                    builder.append(listSeparator);
                    builder.append(jsonElement.toElementJson());
                    listSeparator = ",";
                }
                builder.append("]");
            }

            separator = ",";
        }

        if (overAppend) {
            builder.append("}");
        }

        if (builder.length() == 0) {
            builder.append("{}");
        }

        return builder.toString();
    }

    public boolean hasElements() {
        return (elements.size() + elementLists.size()) > 0;
    }

    public Json addPair(String key, String value) {
        elements.put(key, new JsonElement(JsonElementType.JsonValue, value, true));
        return this;
    }

    public Json addPair(String key, int value) {
        elements.put(key, new JsonElement(JsonElementType.JsonValue, String.valueOf(value)));
        return this;
    }

    public Json addPair(String key, boolean value) {
        elements.put(key, new JsonElement(JsonElementType.JsonValue, value ? "true" : "false"));
        return this;
    }

    public Json addEncoded(String key, String value) {
        String encoded = Base64.getEncoder().encodeToString((value).getBytes(StandardCharsets.UTF_8));
        elements.put(key, new JsonElement(JsonElementType.JsonValue, encoded));
        return this;
    }

    public Json addList(String key, ArrayList<String> value) {
        ArrayList<JsonElement> list = new ArrayList<>();
        for (String val : value) {
            list.add(new JsonElement(JsonElementType.JsonValue, val, true));
        }
        elementLists.put(key, list);
        return this;
    }

    public Json addIntList(String key, ArrayList<Integer> value) {
        ArrayList<JsonElement> list = new ArrayList<>();
        for (Integer val : value) {
            list.add(new JsonElement(JsonElementType.JsonValue, String.valueOf(val)));
        }
        elementLists.put(key, list);
        return this;
    }

    public Json addJsonList(String key, ArrayList<Json> value) {
        ArrayList<JsonElement> list = new ArrayList<>();
        list.addAll(value);
        elementLists.put(key, list);
        return this;
    }

    public Json addList(String key) {
        return addObject(key);
    }

    public Json addObject(String key) {
        Json json = new Json();
        elements.put(key, json);
        return json;
    }

    public Json addListEntry(String val) {
        if (!elementLists.containsKey("")) {
            elementLists.put("", new ArrayList<>());
        }
        elementLists.get("").add(new JsonElement(JsonElementType.JsonValue, val, true));
        return this;
    }

    public Json addListEntry(int val) {
        if (!elementLists.containsKey("")) {
            elementLists.put("", new ArrayList<>());
        }
        elementLists.get("").add(new JsonElement(JsonElementType.JsonValue, String.valueOf(val)));
        return this;
    }

    public Json addListEntry(Json val) {
        if (!elementLists.containsKey("")) {
            elementLists.put("", new ArrayList<>());
        }
        elementLists.get("").add(val);
        return this;
    }

    public Json addListObject() {
        if (!elementLists.containsKey("")) {
            elementLists.put("", new ArrayList<>());
        }
        Json json = new Json();
        elementLists.get("").add(json);
        return json;
    }

    public ArrayList<Json> getObjectList() {
        return getObjectList("");
    }

    public ArrayList<Json> getObjectList(String key) {
        ArrayList<Json> result = new ArrayList<>();
        if (elements.containsKey(key)) {
            JsonElement jsonList = elements.get(key);
            if (jsonList.jsonType == JsonElementType.Json) {
                for (JsonElement jsonElement : ((Json)jsonList).elementLists.get(key)) {
                    if (jsonElement.jsonType == JsonElementType.Json) {
                        result.add((Json) jsonElement);
                    } else if (jsonElement.jsonType == JsonElementType.JsonKey || jsonElement.jsonType == JsonElementType.JsonValue) {
                        Json json = new Json();
                        json.elements.put("", jsonElement);
                        result.add(json);
                    }
                }
            }
        }
        if (elementLists.containsKey(key)) {
            for (JsonElement jsonElement : elementLists.get(key)) {
                if (jsonElement.jsonType == JsonElementType.Json) {
                    result.add((Json) jsonElement);
                }
            }
            return result;
        }
        if (key.trim().length()==0 && elementLists.size()==1) {
            for(List<JsonElement> lists : elementLists.values()) {
                for (JsonElement jsonElement : lists) {
                    if (jsonElement.jsonType == JsonElementType.Json) {
                        result.add((Json) jsonElement);
                    }
                }
            }
        }
        return result;
    }

    public boolean containsKey(String key) {
        return elements.containsKey(key);
    }

    public boolean containsListKey(String key) {
        if (elements.containsKey(key)) {
            JsonElement element = elements.get(key);
            if (element.jsonType == JsonElementType.Json) {
                Json elementJson = (Json) element;
                if (elementJson.elementLists.containsKey(key)) {
                    return true;
                }
            }
        }
        return elementLists.containsKey(key);
    }

    public ArrayList<String> getList(String key) {
        ArrayList<String> result = new ArrayList<>();
        List<JsonElement> listElements = null;
        if (elements.containsKey(key)) {
            JsonElement element = elements.get(key);
            if (element.jsonType == JsonElementType.Json) {
                Json elementJson = (Json) element;
                if (elementJson.elementLists.containsKey(key)) {
                    listElements = elementJson.elementLists.get(key);
                }
            }
        }
        if (elementLists.containsKey(key)) {
            listElements = elementLists.get(key);
        }
        if (listElements!=null) {
            for (JsonElement jsonElement : listElements) {
                if (jsonElement.isValue() || jsonElement.isKey()) {
                    result.add(jsonElement.content);
                }
            }
        }
        return result;
    }

    public static Json toJson(Hashtable<String, String> texts) {
        Json json = new Json();
        JsonItem jsonItem = new JsonItem("");
        for (Map.Entry<String, String> entry : texts.entrySet()) {
            jsonItem.add(entry.getKey(), entry.getValue());
        }
        jsonItem.publish(json);
        return json;
    }

    public Hashtable<String, String> toKeyValueList() {
        Hashtable<String, String> texts = new Hashtable<>();
        parseJsonToList("", this, texts);
        return texts;
    }

    private void parseJsonToList(String preKey, Json contentList, Hashtable<String, String> texts) {
        for (Map.Entry<String, JsonElement> entity : contentList.elements.entrySet()) {
            switch (entity.getValue().jsonType) {
                case JsonKey:
                case JsonValue:
                    texts.put(addKey(preKey, entity.getKey()), entity.getValue().content);
                    break;

                case Json:
                    parseJsonToList(addKey(preKey, entity.getKey()), (Json) entity.getValue(), texts);
                    break;
            }
        }
        for (Map.Entry<String, List<JsonElement>> entity : contentList.elementLists.entrySet()) {
            String key = addKey(preKey, entity.getKey());
            int listIndex = 1;
            for (JsonElement element : entity.getValue()) {
                switch (element.jsonType) {
                    case JsonKey:
                    case JsonValue:
                        texts.put(key + ":" + listIndex, element.content);
                        break;

                    case Json:
                        parseJsonToList(key + ":" + listIndex, (Json) element, texts);
                        break;
                }
                listIndex++;
            }
        }
    }

    private String addKey(String preKey, String subKey) {
        if (isNotEmpty(subKey)) {
            String key = preKey;
            if (isNotEmpty(subKey)) {
                if (isNotEmpty(key)) {
                    key += ".";
                }
                return key + subKey;
            }
            return key;
        } else {
            return preKey;
        }
    }


}

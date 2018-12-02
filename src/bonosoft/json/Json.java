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

import java.nio.charset.StandardCharsets;
import java.util.*;

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
                            builder.append("\\\"");
                            break;

                        case '\\':
                            builder.append("\\\\");
                            break;

                        case '/':
                            builder.append("\\/");
                            break;

                        case 'b':
                            builder.append("\\b");
                            break;

                        case 'f':
                            builder.append("\\f");
                            break;

                        case 'n':
                            builder.append("\\n");
                            break;

                        case 'r':
                            builder.append("\\r");
                            break;

                        case 't':
                            builder.append("\\t");
                            break;

                        case 'u':
                            builder.append("\\u").append(chars[1 + index]).append(chars[2 + index]).append(chars[3 + index]).append(chars[4 + index]);
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
            if (parts.size() == 0) { break; }
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
                    if (parts.size() == 0) { return; }
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
                        json.elementLists.put("", buildArray(parts));
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
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            return jsonElement.content;
        }
        return "";
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
        if (elements.containsKey(key)) {
            JsonElement jsonElement = elements.get(key);
            if (jsonElement.content != null) {
                String str = jsonElement.content;
                if (str.endsWith("%")) {
                    str = str.substring(0, str.length() - 1);
                }
                return Integer.parseInt(str);
            }
        }
        return 0;
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
                    builder.append("\"").append(entry.getKey()).append("\":").append(entry.getValue().toElementJson());
                } else {
                    builder.append(entry.getValue().toElementJson());
                }
                listSeparator = ",";
            }
            separator = ",";
        }

        for (Map.Entry<String, List<JsonElement>> entry : elementLists.entrySet()) {
            builder.append(separator);
            if (entry.getKey().length() > 0) {
                boolean append = false;
                if (builder.length() == 0) {
                    builder.append("{");
                    append = true;
                }

                builder.append("\"").append(entry.getKey()).append("\":[");
                String listSeparator = "";
                for (JsonElement jsonElement : entry.getValue()) {
                    builder.append(listSeparator);
                    builder.append(jsonElement.toElementJson());
                    listSeparator = ",";
                }
                builder.append("]");
                if (append) {
                    builder.append("}");
                }
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
        elements.put(key, new JsonElement(JsonElementType.JsonValue, value));
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
            list.add(new JsonElement(JsonElementType.JsonValue, val));
        }
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
        elementLists.get("").add(new JsonElement(JsonElementType.JsonValue, val));
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
        if (elementLists.containsKey(key)) {
            for (JsonElement jsonElement : elementLists.get(key)) {
                if (jsonElement.jsonType == JsonElementType.Json) {
                    result.add((Json) jsonElement);
                }
            }
        }
        return result;
    }

    public boolean containsKey(String key) {
        return elements.containsKey(key);
    }

    public boolean containsListKey(String key) {
        return elementLists.containsKey(key);
    }

    public ArrayList<String> getList(String key) {
        ArrayList<String> result = new ArrayList<>();
        if (elementLists.containsKey(key)) {
            for (JsonElement jsonElement : elementLists.get(key)) {
                if (jsonElement.isValue() || jsonElement.isKey()) {
                    result.add(jsonElement.content);
                }
            }
        }
        return result;
    }
}

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

package bonosoft.json.elements;

/**
 * by bn on 23/11/2018.
 */
public class JsonElement {
    public JsonElementType jsonType;
    public String content;
    public boolean forceString;


    public JsonElement(JsonElementType jsonType) {
        this.jsonType = jsonType;
        this.forceString = false;
    }

    public JsonElement(JsonElementType jsonType, String content) {
        this.jsonType = jsonType;
        this.content = content;
        this.forceString = false;
    }

    public JsonElement(JsonElementType jsonType, String content, boolean forceString) {
        this.jsonType = jsonType;
        this.content = content;
        this.forceString = forceString;
    }

    public boolean isStart() {
        return jsonType == JsonElementType.JsonStart;
    }

    public boolean isEnd() {
        return jsonType == JsonElementType.JsonEnd;
    }

    public boolean isKey() {
        return jsonType == JsonElementType.JsonKey;
    }

    public boolean isSplit() {
        return jsonType == JsonElementType.JsonSplit;
    }

    public boolean isPair() {
        return jsonType == JsonElementType.JsonPair;
    }

    public boolean isValue() {
        return jsonType == JsonElementType.JsonValue;
    }

    public boolean isArrayStart() {
        return jsonType == JsonElementType.JsonArrayStart;
    }

    public boolean isArrayEnd() {
        return jsonType == JsonElementType.JsonArrayEnd;
    }

    public String toJson() {
        return "{}";
    }

    public String toElementJson() {
        switch (jsonType) {
            case JsonKey:
            case JsonValue:
                if (content!=null) {
                    if (!forceString) {
                        if (isNative() || isNumber()) {
                            return content;
                        }
                    }
                    return "\"" + escape(content) + "\"";
                } else {
                    return "null";
                }

            case Json:
                return toJson();
        }
        return "";
    }

    public String escape(String content) {
        StringBuilder builder = new StringBuilder();
        for (char ch : content.toCharArray()) {
            if (ch == '"') {
                builder.append('\\').append(ch);
            } else if (ch == '\\') {
                    builder.append('\\').append(ch);
            // } else if (ch == '/') {
                // builder.append('\\').append(ch);
            } else if (ch == '\b') {
                builder.append('\\').append("b");
            } else if (ch == '\f') {
                builder.append('\\').append("f");
            } else if (ch == '\n') {
                builder.append('\\').append("n");
            } else if (ch == '\r') {
                builder.append('\\').append("r");
            } else if (ch == '\t') {
                builder.append('\\').append("t");
            } else if (ch == '\b') {
                builder.append('\\').append("b");
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    public boolean isNative() {
        return content.equalsIgnoreCase("true") || content.equalsIgnoreCase("false") || content.equalsIgnoreCase("null");
    }

    public boolean isNumber() {
        if (content!=null) {
            boolean hasDigit = false;
            for(char ch : content.toCharArray()) {
                if (Character.isDigit(ch)) {
                    hasDigit = true;
                    continue;
                }
                if (ch=='.' || ch=='-'||ch=='+'||ch=='e'||ch=='E') continue;
                return false;
            }
            return hasDigit;
        }
        return false;
    }
}

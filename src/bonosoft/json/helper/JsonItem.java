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

package bonosoft.json.helper;

import bonosoft.json.Json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class JsonItem {
    private String key = "";
    private String singleValue;
    private Hashtable<String, JsonItem> items = new Hashtable<>();
    private Hashtable<Integer, JsonItem> listItems = new Hashtable<>();
    private Hashtable<Integer, String> listValues = new Hashtable<>();
    private int listIndex = 10000;

    public JsonItem(String key) {
        this.key = key;
    }

    public void add(String keyStr, String val) {
        if (isEmpty(keyStr)) {
            addValue(val);
        } else {
            JsonKeyItem keyItem = getNextKeyItem(keyStr);
            if (key.equalsIgnoreCase(keyItem.key)) {
                if (keyItem.isList) {
                    if (isEmpty(keyItem.restOfKey)) {
                        listValues.put(keyItem.listIndex, val);
                    } else {
                        addToList(val, keyItem);
                    }
                } else {
                    addValue(val);
                }
            } else {
                if (keyItem.isList) {
                    if (isEmpty(keyItem.key)) {
                        addToList(val, keyItem);
                    } else {
                        if (!items.containsKey(keyItem.key)) {
                            items.put(keyItem.key, new JsonItem(keyItem.key));
                        }
                        JsonItem jsonItem = items.get(keyItem.key);
                        if (isEmpty(keyItem.restOfKey)) {
                            jsonItem.listValues.put(keyItem.listIndex, val);
                        } else {
                            jsonItem.addToList(val, keyItem);
                        }
                    }
                } else {
                    if (!items.containsKey(keyItem.key)) {
                        items.put(keyItem.key, new JsonItem(keyItem.key));
                    }
                    JsonItem jsonItem = items.get(keyItem.key);
                    jsonItem.add(keyItem.restOfKey, val);
                }
            }
        }
    }

    private void addToList(String val, JsonKeyItem keyItem) {
        if (!listItems.containsKey(keyItem.listIndex)) {
            listItems.put(keyItem.listIndex, new JsonItem(""));
        }
        JsonItem jsonItem = listItems.get(keyItem.listIndex);
        jsonItem.add(keyItem.restOfKey, val);
    }

    private void addValue(String val) {
        if (listValues.size() > 0) {
            listValues.put(listIndex++, val);
        } else if (isEmpty(singleValue)) {
            singleValue = val;
        } else {
            listValues.put(listIndex++, singleValue);
            listValues.put(listIndex++, val);
            singleValue = null;
        }
    }

    private JsonKeyItem getNextKeyItem(String key) {
        JsonKeyItem keyItem = new JsonKeyItem();
        int indexDot = key.indexOf('.');
        int indexList = key.indexOf(':');
        if (indexDot < 0 && indexList < 0) {
            keyItem.key = key;
        } else if (indexList < 0) {
            keyItem.key = key.substring(0, indexDot);
            keyItem.restOfKey = key.substring(1 + indexDot);
        } else if (indexDot < 0) {
            keyItem.key = key.substring(0, indexList);
            keyItem.listIndex = Integer.parseInt(key.substring(1 + indexList));
            keyItem.isList = true;
        } else {
            if (indexDot < indexList) {
                keyItem.key = key.substring(0, indexDot);
                keyItem.restOfKey = key.substring(1 + indexDot);
            } else {
                keyItem.key = key.substring(0, indexList);
                keyItem.listIndex = Integer.parseInt(key.substring(1 + indexList, indexDot));
                keyItem.restOfKey = key.substring(1 + indexDot);
                keyItem.isList = true;
            }
        }
        return keyItem;
    }

    public void publish(Json json) {
        if (isNotEmpty(singleValue)) {
            json.addPair(key, singleValue);
        }
        if (listValues.size() > 0) {
            ArrayList<String> strList = new ArrayList<>();
            ArrayList<Integer> keys = new ArrayList<>(listValues.keySet());
            Collections.sort(keys);
            for (int i : keys) {
                strList.add(listValues.get(i));
            }
            json.addList(key, strList);
        }
        if (items.size() > 0) {
            for (Map.Entry<String, JsonItem> entry : items.entrySet()) {
                if (entry.getValue().isPair()) {
                    json.addPair(entry.getKey(), entry.getValue().singleValue);
                } else if (entry.getValue().isNamedList()) {
                    entry.getValue().publish(json);
                } else {
                    Json subJson = json.addObject(entry.getKey());
                    entry.getValue().publish(subJson);
                }
            }
        }
        if (listItems.size() > 0) {
            Json subJson = json.addList(key);
            ArrayList<Integer> keys = new ArrayList<>(listItems.keySet());
            Collections.sort(keys);
            for (int i : keys) {
                listItems.get(i).publish(subJson.addListObject());
            }
        }
    }

    private boolean isNamedList() {
        return isEmpty(singleValue) && items.size()==0 && (listItems.size()>0 || listValues.size()>0);
    }

    private boolean isPair() {
        return isNotEmpty(singleValue) && items.size()==0 && listItems.size()==0 && listValues.size()==0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0 || str.trim().length() <= 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

}

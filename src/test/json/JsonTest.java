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

package json;


import bonosoft.json.Json;
import bonosoft.json.elements.JsonElement;
import bonosoft.json.elements.JsonElementType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * by bn on 23/11/2018.
 */
public class JsonTest {

    @Test
    public void testEmpty() throws Exception {
        Json json = Json.parse(null);
        Assertions.assertNull(json, "Json should be null");
    }

    @Test
    public void testEmpty2() throws Exception {
        Json json = Json.parse("");
        Assertions.assertEquals("{}", json.toJson());
    }

    @Test
    public void testEmpty3() throws Exception {
        Json json = Json.parse("{");
        Assertions.assertEquals("{}", json.toJson());
    }

    @Test
    public void testEmpty4() throws Exception {
        Json json = Json.parse("}");
        Assertions.assertEquals("{}", json.toJson());
    }

    @Test
    public void testEmpty5() throws Exception {
        Json json = Json.parse("[");
        Assertions.assertEquals("[]", json.toJson());
    }

    @Test
    public void testEmpty6() throws Exception {
        Json json = Json.parse("]");
        Assertions.assertEquals("{}", json.toJson());
    }


    @Test
    public void testBase1() throws Exception {
        Json json = Json.parse("{}");
        Assertions.assertEquals("{}", json.toJson());
    }

    @Test
    public void testBase2() throws Exception {
        Json json = Json.parse(" { } ");
        Assertions.assertEquals("{}", json.toJson());
    }

    @Test
    public void testBase3() throws Exception {
        Json json = Json.parse("[]");
        Assertions.assertEquals("[]", json.toJson());
    }

    @Test
    public void testBase4() throws Exception {
        Json json = Json.parse(" [ ] ");
        Assertions.assertEquals("[]", json.toJson());
    }

    @Test
    public void testBase5() throws Exception {
        Json json = Json.parse("A");
        Assertions.assertEquals("{\"A\"}", json.toJson());
    }

    @Test
    public void testSimle1() throws Exception {
        Json json = Json.parse("{A}");
        Assertions.assertEquals("{\"A\"}", json.toJson());
    }

    @Test
    public void testSimle2() throws Exception {
        Json json = Json.parse("{\"A\"}");
        Assertions.assertEquals("{\"A\"}", json.toJson());
    }

    @Test
    public void testSimle3() throws Exception {
        Json json = Json.parse("{'A'}");
        Assertions.assertEquals("{\"'A'\"}", json.toJson());
    }

    @Test
    public void testPair1() throws Exception {
        Json json = Json.parse("{\"A\":\"B\"}");
        Assertions.assertEquals("{\"A\":\"B\"}", json.toJson());
    }

    @Test
    public void testPair2() throws Exception {
        Json json = Json.parse(" { \"A\" : \"B\" } ");
        Assertions.assertEquals("{\"A\":\"B\"}", json.toJson());
    }

    @Test
    public void testPair3() throws Exception {
        Json json = Json.parse(" { \" A \" : \" B \" } ");
        Assertions.assertEquals("{\" A \":\" B \"}", json.toJson());
    }

    @Test
    public void testPair3a() throws Exception {
        Json json = Json.parse(" { \" A\\\"A\\\"A \" : \" B\\\"B\\\"B \" } ");
        Assertions.assertEquals("{\" A\\\"A\\\"A \":\" B\\\"B\\\"B \"}", json.toJson());
    }

    @Test
    public void testPair4() throws Exception {
        Json json = Json.parse("{\"A\\\"A\":\"B\\\\B\"}");
        Assertions.assertEquals("{\"A\\\"A\":\"B\\\\B\"}", json.toJson());
    }

    @Test
    public void testPair5() throws Exception {
        Json json = Json.parse("{\"A\\/A\":\"B\\bB\"}");
        Assertions.assertEquals("{\"A\\/A\":\"B\\bB\"}", json.toJson());
    }

    @Test
    public void testPair6() throws Exception {
        Json json = Json.parse("{\"A\\nA\":\"B\\rB\"}");
        Assertions.assertEquals("{\"A\\nA\":\"B\\rB\"}", json.toJson());
    }

    @Test
    public void testPair7() throws Exception {
        Json json = Json.parse("{\"A\\tA\":\"B\\u0A1bB\"}");
        Assertions.assertEquals("{\"A\\tA\":\"BਛB\"}", json.toJson());
    }

    @Test
    public void testPair8() throws Exception {
        Json json = Json.parse("{\"A\":\"B\\fB\"}");
        Assertions.assertEquals("{\"A\":\"B\\fB\"}", json.toJson());
    }


    @Test
    public void testDirect1() throws Exception {
        Json json = Json.parse("{\"A\": true }");
        Assertions.assertEquals("{\"A\":true}", json.toJson());
    }

    @Test
    public void testDirect2() throws Exception {
        Json json = Json.parse("{\"A\": \"true\" }");
        Assertions.assertEquals("{\"A\":\"true\"}", json.toJson());
    }

    @Test
    public void testDirect3() throws Exception {
        Json json = Json.parse("{\"A\": false }");
        Assertions.assertEquals("{\"A\":false}", json.toJson());
    }

    @Test
    public void testDirect4() throws Exception {
        Json json = Json.parse("{\"A\": null }");
        Assertions.assertEquals("{\"A\":null}", json.toJson());
    }

    @Test
    public void testNumber1() throws Exception {
        Json json = Json.parse("{\"A\": 123 }");
        Assertions.assertEquals("{\"A\":123}", json.toJson());
    }

    @Test
    public void testNumber2() throws Exception {
        Json json = Json.parse("{\"A\": 1.3 }");
        Assertions.assertEquals("{\"A\":1.3}", json.toJson());
    }

    @Test
    public void testNumber3() throws Exception {
        Json json = Json.parse("{\"A\": 0.3 }");
        Assertions.assertEquals("{\"A\":0.3}", json.toJson());
    }

    @Test
    public void testNumber4() throws Exception {
        Json json = Json.parse("{\"A\": -0.321 }");
        Assertions.assertEquals("{\"A\":-0.321}", json.toJson());
    }

    @Test
    public void testNumber5() throws Exception {
        Json json = Json.parse("{\"A\": 0.01E1 }");
        Assertions.assertEquals("{\"A\":0.01E1}", json.toJson());
    }

    @Test
    public void testNumber6() throws Exception {
        Json json = Json.parse("{\"A\": -0.01e-1 }");
        Assertions.assertEquals("{\"A\":-0.01e-1}", json.toJson());
    }

    @Test
    public void testNumber7() throws Exception {
        Json json = Json.parse("{\"A\": -0.01e+1 }");
        Assertions.assertEquals("{\"A\":-0.01e+1}", json.toJson());
    }

    @Test
    public void testArray1() throws Exception {
        Json json = Json.parse("[true]");
        Assertions.assertEquals("[true]", json.toJson());
    }

    @Test
    public void testArray2() throws Exception {
        Json json = Json.parse("[ false, true ]");
        Assertions.assertEquals("[false,true]", json.toJson());
    }

    @Test
    public void testArray3() throws Exception {
        Json json = Json.parse("[ false, null, true ]");
        Assertions.assertEquals("[false,null,true]", json.toJson());
    }

    @Test
    public void testArray4() throws Exception {
        Json json = Json.parse("[ false, 0, null, true ]");
        Assertions.assertEquals("[false,0,null,true]", json.toJson());
    }

    @Test
    public void testArray5() throws Exception {
        Json json = Json.parse("[ false, 0, null, -5.92,0.001E1, true ]");
        Assertions.assertEquals("[false,0,null,-5.92,0.001E1,true]", json.toJson());
    }

    @Test
    public void testArray6() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", null, -5.92,0.001E1, true ]");
        Assertions.assertEquals("[false,0,\"bo\",null,-5.92,0.001E1,true]", json.toJson());
    }

    @Test
    public void testArray7() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", {}, null, -5.92,0.001E1, true ]");
        Assertions.assertEquals("[false,0,\"bo\",{},null,-5.92,0.001E1,true]", json.toJson());
    }

    @Test
    public void testArray8() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", { \"A\" : \"B\" }, null, -5.92,0.001E1, true ]");
        Assertions.assertEquals("[false,0,\"bo\",{\"A\":\"B\"},null,-5.92,0.001E1,true]", json.toJson());
    }

    @Test
    public void testArray9() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", { \"A\" : \"B\" }, [], null, -5.92,0.001E1, true ]");
        Assertions.assertEquals("[false,0,\"bo\",{\"A\":\"B\"},[],null,-5.92,0.001E1,true]", json.toJson());
    }

    @Test
    public void testArray10() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", { \"A\" : \"B\" }, [ \"C\", false ], null, -5.92,0.001E1, true ]");
        Assertions.assertEquals("[false,0,\"bo\",{\"A\":\"B\"},[\"C\",false],null,-5.92,0.001E1,true]", json.toJson());
    }

    @Test
    public void testArray11() throws Exception {
        Json json = Json.parse("{ \"test\" : [ false, 0, \"bo\", { \"A\" : \"B\" }, [ \"C\", false ], null, -5.92,0.001E1, true ]}");
        Assertions.assertEquals("{\"test\":[false,0,\"bo\",{\"A\":\"B\"},[\"C\",false],null,-5.92,0.001E1,true]}", json.toJson());
    }

    @Test
    public void testLoop1() throws Exception {
        Json json = Json.parse("{ \"A\" : { \"B\" : { \"C\" : { \"D\" : { \"E\" : \"F\"}}}}}");
        Assertions.assertEquals("{\"A\":{\"B\":{\"C\":{\"D\":{\"E\":\"F\"}}}}}", json.toJson());
    }

    @Test
    public void testLoop2() throws Exception {
        Json json = Json.parse("[ \"A\" , [ \"B\" , [ \"C\" , [ \"D\" , [ \"E\" , \"F\"]]]]]");
        Assertions.assertEquals("[\"A\",[\"B\",[\"C\",[\"D\",[\"E\",\"F\"]]]]]", json.toJson());
    }

    @Test
    public void testLoop3() throws Exception {
        Json json = Json.parse("{ \"A\" : [\"B\", { \"C\" : [\"D\", { \"E\" : [\"F\", \"G\"]}]}]}");
        Assertions.assertEquals("{\"A\":[\"B\",{\"C\":[\"D\",{\"E\":[\"F\",\"G\"]}]}]}", json.toJson());
    }

    @Test
    public void testBuild1() throws Exception {
        Json json = new Json();
        Assertions.assertEquals("{}", json.toJson());
    }

    @Test
    public void testBuild2() throws Exception {
        Json json = new Json();
        json.addPair("A","B");
        Assertions.assertEquals("{\"A\":\"B\"}", json.toJson());
    }

    @Test
    public void testBuild3() throws Exception {
        Json json = new Json();
        json.addObject("C").addPair("A","B");
        Assertions.assertEquals("{\"C\":{\"A\":\"B\"}}", json.toJson());
    }

    @Test
    public void testBuild4() throws Exception {
        Json json = new Json();
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        json.addList("C", list);
        Assertions.assertEquals("{\"C\":[\"A\",\"B\"]}", json.toJson());
    }

    @Test
    public void testBuild5() throws Exception {
        Json json = new Json();
        Json list = json.addList("C");
        list.addListEntry("A");
        list.addListEntry("B");
        Assertions.assertEquals("{\"C\":[\"A\",\"B\"]}", json.toJson());
    }

    @Test
    public void testBuild6() throws Exception {
        Json json = new Json();
        json.addList("C").addListEntry("A").addListEntry("B");
        Assertions.assertEquals("{\"C\":[\"A\",\"B\"]}", json.toJson());
    }

    @Test
    public void testBuild7() throws Exception {
        Json json = new Json();
        Json jsonList = json.addList("D").addListEntry("E");
        jsonList.addListObject().addList("C").addListEntry("A").addListEntry("B");
        jsonList.addPair("F", "G");
        json.addPair("H","I");
        Assertions.assertEquals("{\"H\":\"I\",\"D\":{\"F\":\"G\",[\"E\",{\"C\":[\"A\",\"B\"]}]}}", json.toJson());
    }

    @Test
    public void testBuild8() throws Exception {
        Json json = new Json();
        json.addListEntry("A").addListEntry("B");
        Assertions.assertEquals("[\"A\",\"B\"]", json.toJson());
        Assertions.assertEquals(0, json.getObjectList().size());
    }

    @Test
    public void testBuild9() throws Exception {
        Json json = new Json();
        json.addListEntry("A").addListObject();
        Assertions.assertEquals("[\"A\",{}]", json.toJson());
        Assertions.assertEquals(1, json.getObjectList().size());
    }

    @Test
    public void testBuild10() throws Exception {
        Json json = new Json();
        json.addListObject();
        Assertions.assertEquals("[{}]", json.toJson());
    }



    @Test
    public void testEncode1() throws Exception {
        Json json = new Json();
        json.addEncoded("A", "{[ABC]}");
        Assertions.assertEquals("{\"A\":\"e1tBQkNdfQ==\"}", json.toJson());
        Assertions.assertEquals("e1tBQkNdfQ==", json.get("A"));
        Assertions.assertEquals("{[ABC]}", json.getDecoded("A"));
        Assertions.assertEquals("", json.getDecoded("B"));
    }

    @Test
    public void testEncode2() throws Exception {
        Json json = new Json();
        json.addPair("A", "{[ABC]}");
        Assertions.assertEquals("{\"A\":\"{[ABC]}\"}", json.toJson());

        Assertions.assertEquals("{\\\"A\\\":\\\"{[ABC]}\\\"}", json.toJsonEscaped());
    }

    @Test
    public void testMethods1() throws Exception {
        Json json = new Json();
        Assertions.assertFalse(json.hasElements(), "Should not have any");
        Assertions.assertFalse(json.containsKey("A"), "Should not have any");
        json.addPair("A","B");
        Assertions.assertTrue(json.hasElements(), "Should have any");
        Assertions.assertTrue(json.containsKey("A"), "Should have any");
        Assertions.assertEquals("", json.get("C"));
    }

    @Test
    public void testMethods2() throws Exception {
        Json json = new Json();
        Assertions.assertFalse(json.containsListKey(""), "Should not have any");
        json = Json.parse("[ false, null, true ]");
        Assertions.assertTrue(json.containsListKey(""), "Should have any");
        ArrayList<String> list = json.getList("");
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void testMethods3() throws Exception {
        Json json = Json.parse("{ \"A\" : 3 }");
        Assertions.assertEquals(3, json.getInt("A"));
    }

    @Test
    public void testMethods4() throws Exception {
        Json json = Json.parse("{ \"A\" : \"3%\" }");
        Assertions.assertEquals(3, json.getInt("A"));
        Assertions.assertEquals(0, json.getInt("B"));
    }

    @Test
    public void testError1() throws Exception {
        Json json = Json.parse("{ a \"b\" : \"c\" }");
        Assertions.assertEquals("{\"b\":\"c\",\"a\"}", json.toJson());
    }

    @Test
    public void testError2() throws Exception {
        Json json = Json.parse("{ \"b\" a : \"c\" }");
        Assertions.assertEquals("{\"c\"}", json.toJson());
    }

    @Test
    public void testError3() throws Exception {
        Json json = Json.parse("a { \"b\" : \"c\" }");
        Assertions.assertEquals("{\"b\":\"c\",\"a\"}", json.toJson());
    }

    @Test
    public void testError4() throws Exception {
        Json json = Json.parse("a [ \"b\" , \"c\" ]");
        Assertions.assertEquals("{[\"b\",\"c\"]}", json.toJson());
    }

    @Test
    public void testError5() throws Exception {
        Json json = Json.parse("{\"A\"");
        Assertions.assertEquals("{\"A\"}", json.toJson());
    }

    @Test
    public void testError6() throws Exception {
        Json json = Json.parse("{\"A\":{\"B\":\"C\"}}");
        Assertions.assertEquals("{\"A\":{\"B\":\"C\"}}", json.toJson());
    }

    @Test
    public void testError7() throws Exception {
        JsonElement jsonElement = new JsonElement(JsonElementType.JsonValue, null);
        Assertions.assertFalse(jsonElement.isNumber());
        Assertions.assertEquals("{}", jsonElement.toJson());
        Assertions.assertEquals("null", jsonElement.toElementJson());
        jsonElement.jsonType = JsonElementType.JsonSplit;
        Assertions.assertEquals("", jsonElement.toElementJson());
    }

    @Test
    public void testKeyValue1() {
        Json json = Json.parse("{\"A\":{\"B\":\"C\"}}");
        Hashtable<String, String> list = json.toKeyValueList();
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("A.B", list.keys().nextElement());
        Assertions.assertEquals("C", list.get("A.B"));
    }

    @Test
    public void testKeyValue2() {
        Hashtable<String, String> list = new Hashtable<>();
        list.put("A.B", "C");
        Json json = Json.toJson(list);
        Assertions.assertEquals("{\"A\":{\"B\":\"C\"}}", json.toJson());
    }

    @Test
    public void testKeyValue4() {
        Hashtable<String, String> list = new Hashtable<>();
        list.put("A.B:1", "C");
        list.put("A.B:2", "D");
        Json json = Json.toJson(list);
        Assertions.assertEquals("{\"A\":{\"B\":[\"C\",\"D\"]}}", json.toJson());
    }

    @Test
    public void testKeyValue5() {
        Hashtable<String, String> list = new Hashtable<>();
        list.put("A.B:1.C", "D");
        Json json = Json.toJson(list);
        Assertions.assertEquals("{\"A\":{\"B\":[{\"C\":\"D\"}]}}", json.toJson());
    }

    @Test
    public void testArray12() {
        ArrayList<String> l1 = new ArrayList<>();
        l1.add("A1"); l1.add("A2");
        ArrayList<String> l2 = new ArrayList<>();
        l2.add("B1"); l2.add("B2");
        Json json = new Json();
        json.addList("A", l1);
        json.addList("B", l2);

        Assertions.assertEquals("{\"A\":[\"A1\",\"A2\"],\"B\":[\"B1\",\"B2\"]}", json.toJson());
    }

    // Json json = Json.parse("{\"informed\":\"USR-0001 USR-0002\",\"controlType\":2,\"ishidden\":false,\"responsible\":\"USR-0001 USR-0002\",\"sortOrder\":818,\"requirements\":\"REQ-0166 REQ-0304 REQ-0305 REQ-0306 REQ-0307 REQ-0308 REQ-0309 REQ-0312 REQ-0313 REQ-0324 REQ-0325 REQ-1433 REQ-1434 REQ-1435\",\"tasks\":\"TSK-0075 TSK-0077 TSK-0078 TSK-0154 TSK-0171 TSK-0186\",\"accountable\":\"USR-0001 USR-0002\",\"templatename\":\"Third-Party Management\",\"type\":\"system\",\"templatedescription\":\"Mechanisms exist to facilitate the implementation of third-party management controls.\",\"links\":[],\"scfkey\":\"TPM-01\",\"tags\":[\"TAG-0004\"]}");

    @Test
    public void testParseList() {
        Json json = Json.parse("{\"informed\":\"USR-0001 USR-0002\",\"tags\":[\"TAG-0004\"]}");
        Assertions.assertEquals("USR-0001 USR-0002", json.get("informed"));

        ArrayList<String> strList = json.getList("tags");
        Assertions.assertEquals(1, strList.size());
        Assertions.assertEquals("TAG-0004", strList.get(0));

        ArrayList<Json> jsonList = json.getObjectList("tags");
        Assertions.assertEquals(1, jsonList.size());
        Assertions.assertEquals("{\"TAG-0004\"}", jsonList.get(0).toJson());

        Assertions.assertTrue(json.containsListKey("tags"));

    }

    @Test
    public void testNullValue() {
        Json json = new Json();
        json.addPair("test", null);

        Assertions.assertEquals("{\"test\":null}", json.toJson());

    }

}
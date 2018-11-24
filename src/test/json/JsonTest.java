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
import org.junit.Assert;

import java.util.ArrayList;

/**
 * by bn on 23/11/2018.
 */
public class JsonTest {

    @org.junit.Test
    public void testEmpty() throws Exception {
        Json json = Json.parse(null);
        Assert.assertNull("Json should be null", json);
    }

    @org.junit.Test
    public void testEmpty2() throws Exception {
        Json json = Json.parse("");
        Assert.assertEquals("{}", json.toJson());
    }

    @org.junit.Test
    public void testEmpty3() throws Exception {
        Json json = Json.parse("{");
        Assert.assertEquals("{}", json.toJson());
    }

    @org.junit.Test
    public void testEmpty4() throws Exception {
        Json json = Json.parse("}");
        Assert.assertEquals("{}", json.toJson());
    }

    @org.junit.Test
    public void testEmpty5() throws Exception {
        Json json = Json.parse("[");
        Assert.assertEquals("[]", json.toJson());
    }

    @org.junit.Test
    public void testEmpty6() throws Exception {
        Json json = Json.parse("]");
        Assert.assertEquals("{}", json.toJson());
    }


    @org.junit.Test
    public void testBase1() throws Exception {
        Json json = Json.parse("{}");
        Assert.assertEquals("{}", json.toJson());
    }

    @org.junit.Test
    public void testBase2() throws Exception {
        Json json = Json.parse(" { } ");
        Assert.assertEquals("{}", json.toJson());
    }

    @org.junit.Test
    public void testBase3() throws Exception {
        Json json = Json.parse("[]");
        Assert.assertEquals("[]", json.toJson());
    }

    @org.junit.Test
    public void testBase4() throws Exception {
        Json json = Json.parse(" [ ] ");
        Assert.assertEquals("[]", json.toJson());
    }

    @org.junit.Test
    public void testBase5() throws Exception {
        Json json = Json.parse("A");
        Assert.assertEquals("{\"A\"}", json.toJson());
    }

    @org.junit.Test
    public void testSimle1() throws Exception {
        Json json = Json.parse("{A}");
        Assert.assertEquals("{\"A\"}", json.toJson());
    }

    @org.junit.Test
    public void testSimle2() throws Exception {
        Json json = Json.parse("{\"A\"}");
        Assert.assertEquals("{\"A\"}", json.toJson());
    }

    @org.junit.Test
    public void testSimle3() throws Exception {
        Json json = Json.parse("{'A'}");
        Assert.assertEquals("{\"'A'\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair1() throws Exception {
        Json json = Json.parse("{\"A\":\"B\"}");
        Assert.assertEquals("{\"A\":\"B\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair2() throws Exception {
        Json json = Json.parse(" { \"A\" : \"B\" } ");
        Assert.assertEquals("{\"A\":\"B\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair3() throws Exception {
        Json json = Json.parse(" { \" A \" : \" B \" } ");
        Assert.assertEquals("{\" A \":\" B \"}", json.toJson());
    }

    @org.junit.Test
    public void testPair4() throws Exception {
        Json json = Json.parse("{\"A\\\"A\":\"B\\\\B\"}");
        Assert.assertEquals("{\"A\\\"A\":\"B\\\\B\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair5() throws Exception {
        Json json = Json.parse("{\"A\\/A\":\"B\\bB\"}");
        Assert.assertEquals("{\"A\\/A\":\"B\\bB\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair6() throws Exception {
        Json json = Json.parse("{\"A\\nA\":\"B\\rB\"}");
        Assert.assertEquals("{\"A\\nA\":\"B\\rB\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair7() throws Exception {
        Json json = Json.parse("{\"A\\tA\":\"B\\u0A1bB\"}");
        Assert.assertEquals("{\"A\\tA\":\"B\\u0A1bB\"}", json.toJson());
    }

    @org.junit.Test
    public void testPair8() throws Exception {
        Json json = Json.parse("{\"A\":\"B\\fB\"}");
        Assert.assertEquals("{\"A\":\"B\\fB\"}", json.toJson());
    }


    @org.junit.Test
    public void testDirect1() throws Exception {
        Json json = Json.parse("{\"A\": true }");
        Assert.assertEquals("{\"A\":true}", json.toJson());
    }

    @org.junit.Test
    public void testDirect2() throws Exception {
        Json json = Json.parse("{\"A\": \"true\" }");
        Assert.assertEquals("{\"A\":\"true\"}", json.toJson());
    }

    @org.junit.Test
    public void testDirect3() throws Exception {
        Json json = Json.parse("{\"A\": false }");
        Assert.assertEquals("{\"A\":false}", json.toJson());
    }

    @org.junit.Test
    public void testDirect4() throws Exception {
        Json json = Json.parse("{\"A\": null }");
        Assert.assertEquals("{\"A\":null}", json.toJson());
    }

    @org.junit.Test
    public void testNumber1() throws Exception {
        Json json = Json.parse("{\"A\": 123 }");
        Assert.assertEquals("{\"A\":123}", json.toJson());
    }

    @org.junit.Test
    public void testNumber2() throws Exception {
        Json json = Json.parse("{\"A\": 1.3 }");
        Assert.assertEquals("{\"A\":1.3}", json.toJson());
    }

    @org.junit.Test
    public void testNumber3() throws Exception {
        Json json = Json.parse("{\"A\": 0.3 }");
        Assert.assertEquals("{\"A\":0.3}", json.toJson());
    }

    @org.junit.Test
    public void testNumber4() throws Exception {
        Json json = Json.parse("{\"A\": -0.321 }");
        Assert.assertEquals("{\"A\":-0.321}", json.toJson());
    }

    @org.junit.Test
    public void testNumber5() throws Exception {
        Json json = Json.parse("{\"A\": 0.01E1 }");
        Assert.assertEquals("{\"A\":0.01E1}", json.toJson());
    }

    @org.junit.Test
    public void testNumber6() throws Exception {
        Json json = Json.parse("{\"A\": -0.01e-1 }");
        Assert.assertEquals("{\"A\":-0.01e-1}", json.toJson());
    }

    @org.junit.Test
    public void testNumber7() throws Exception {
        Json json = Json.parse("{\"A\": -0.01e+1 }");
        Assert.assertEquals("{\"A\":-0.01e+1}", json.toJson());
    }

    @org.junit.Test
    public void testArray1() throws Exception {
        Json json = Json.parse("[true]");
        Assert.assertEquals("[true]", json.toJson());
    }

    @org.junit.Test
    public void testArray2() throws Exception {
        Json json = Json.parse("[ false, true ]");
        Assert.assertEquals("[false,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray3() throws Exception {
        Json json = Json.parse("[ false, null, true ]");
        Assert.assertEquals("[false,null,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray4() throws Exception {
        Json json = Json.parse("[ false, 0, null, true ]");
        Assert.assertEquals("[false,0,null,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray5() throws Exception {
        Json json = Json.parse("[ false, 0, null, -5.92,0.001E1, true ]");
        Assert.assertEquals("[false,0,null,-5.92,0.001E1,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray6() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", null, -5.92,0.001E1, true ]");
        Assert.assertEquals("[false,0,\"bo\",null,-5.92,0.001E1,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray7() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", {}, null, -5.92,0.001E1, true ]");
        Assert.assertEquals("[false,0,\"bo\",{},null,-5.92,0.001E1,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray8() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", { \"A\" : \"B\" }, null, -5.92,0.001E1, true ]");
        Assert.assertEquals("[false,0,\"bo\",{\"A\":\"B\"},null,-5.92,0.001E1,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray9() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", { \"A\" : \"B\" }, [], null, -5.92,0.001E1, true ]");
        Assert.assertEquals("[false,0,\"bo\",{\"A\":\"B\"},[],null,-5.92,0.001E1,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray10() throws Exception {
        Json json = Json.parse("[ false, 0, \"bo\", { \"A\" : \"B\" }, [ \"C\", false ], null, -5.92,0.001E1, true ]");
        Assert.assertEquals("[false,0,\"bo\",{\"A\":\"B\"},[\"C\",false],null,-5.92,0.001E1,true]", json.toJson());
    }

    @org.junit.Test
    public void testArray11() throws Exception {
        Json json = Json.parse("{ \"test\" : [ false, 0, \"bo\", { \"A\" : \"B\" }, [ \"C\", false ], null, -5.92,0.001E1, true ]}");
        Assert.assertEquals("{\"test\":[false,0,\"bo\",{\"A\":\"B\"},[\"C\",false],null,-5.92,0.001E1,true]}", json.toJson());
    }

    @org.junit.Test
    public void testLoop1() throws Exception {
        Json json = Json.parse("{ \"A\" : { \"B\" : { \"C\" : { \"D\" : { \"E\" : \"F\"}}}}}");
        Assert.assertEquals("{\"A\":{\"B\":{\"C\":{\"D\":{\"E\":\"F\"}}}}}", json.toJson());
    }

    @org.junit.Test
    public void testLoop2() throws Exception {
        Json json = Json.parse("[ \"A\" , [ \"B\" , [ \"C\" , [ \"D\" , [ \"E\" , \"F\"]]]]]");
        Assert.assertEquals("[\"A\",[\"B\",[\"C\",[\"D\",[\"E\",\"F\"]]]]]", json.toJson());
    }

    @org.junit.Test
    public void testLoop3() throws Exception {
        Json json = Json.parse("{ \"A\" : [\"B\", { \"C\" : [\"D\", { \"E\" : [\"F\", \"G\"]}]}]}");
        Assert.assertEquals("{\"A\":[\"B\",{\"C\":[\"D\",{\"E\":[\"F\",\"G\"]}]}]}", json.toJson());
    }

    @org.junit.Test
    public void testBuild1() throws Exception {
        Json json = new Json();
        Assert.assertEquals("{}", json.toJson());
    }

    @org.junit.Test
    public void testBuild2() throws Exception {
        Json json = new Json();
        json.add("A","B");
        Assert.assertEquals("{\"A\":\"B\"}", json.toJson());
    }

    @org.junit.Test
    public void testBuild3() throws Exception {
        Json json = new Json();
        json.addJson("C").add("A","B");
        Assert.assertEquals("{\"C\":{\"A\":\"B\"}}", json.toJson());
    }

    @org.junit.Test
    public void testBuild4() throws Exception {
        Json json = new Json();
        ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        json.addList("C", list);
        Assert.assertEquals("{\"C\":[\"A\",\"B\"]}", json.toJson());
    }

    @org.junit.Test
    public void testBuild5() throws Exception {
        Json json = new Json();
        json.addList("C").addListEntry("A").addListEntry("B");
        Assert.assertEquals("{\"C\":[\"A\",\"B\"]}", json.toJson());
    }

    @org.junit.Test
    public void testBuild6() throws Exception {
        Json json = new Json();
        Json jsonList = json.addList("D").addListEntry("E");
        jsonList.addListAddJson().addList("C").addListEntry("A").addListEntry("B");
        jsonList.add("F", "G");
        json.add("H","I");
        Assert.assertEquals("{\"H\":\"I\",\"D\":{\"F\":\"G\",[\"E\",{\"C\":[\"A\",\"B\"]}]}}", json.toJson());
    }

    @org.junit.Test
    public void testBuild7() throws Exception {
        Json json = new Json();
        json.addListEntry("A").addListEntry("B");
        Assert.assertEquals("[\"A\",\"B\"]", json.toJson());
        Assert.assertEquals(0, json.getJsonList().size());
    }

    @org.junit.Test
    public void testBuild8() throws Exception {
        Json json = new Json();
        json.addListEntry("A").addListAddJson();
        Assert.assertEquals("[\"A\",{}]", json.toJson());
        Assert.assertEquals(1, json.getJsonList().size());
    }

    @org.junit.Test
    public void testBuild9() throws Exception {
        Json json = new Json();
        json.addListAddJson();
        Assert.assertEquals("[{}]", json.toJson());
    }



    @org.junit.Test
    public void testEncode1() throws Exception {
        Json json = new Json();
        json.addEncoded("A", "{[ABC]}");
        Assert.assertEquals("{\"A\":\"e1tBQkNdfQ==\"}", json.toJson());
        Assert.assertEquals("e1tBQkNdfQ==", json.get("A"));
        Assert.assertEquals("{[ABC]}", json.getDecoded("A"));
        Assert.assertEquals("", json.getDecoded("B"));
    }

    @org.junit.Test
    public void testEncode2() throws Exception {
        Json json = new Json();
        json.add("A", "{[ABC]}");
        Assert.assertEquals("{\"A\":\"{[ABC]}\"}", json.toJson());

        Assert.assertEquals("{\\\"A\\\":\\\"{[ABC]}\\\"}", json.toJsonEscaped());
    }

    @org.junit.Test
    public void testMethods1() throws Exception {
        Json json = new Json();
        Assert.assertFalse("Should not have any", json.hasElements());
        Assert.assertFalse("Should not have any", json.containsKey("A"));
        json.add("A","B");
        Assert.assertTrue("Should have any", json.hasElements());
        Assert.assertTrue("Should have any", json.containsKey("A"));
        Assert.assertEquals("", json.get("C"));
    }

    @org.junit.Test
    public void testMethods2() throws Exception {
        Json json = new Json();
        Assert.assertFalse("Should not have any", json.containsListKey(""));
        json = Json.parse("[ false, null, true ]");
        Assert.assertTrue("Should have any", json.containsListKey(""));
        ArrayList<String> list = json.getList("");
        Assert.assertEquals(3, list.size());
    }

    @org.junit.Test
    public void testMethods3() throws Exception {
        Json json = Json.parse("{ \"A\" : 3 }");
        Assert.assertEquals(3, json.getInt("A"));
    }

    @org.junit.Test
    public void testMethods4() throws Exception {
        Json json = Json.parse("{ \"A\" : \"3%\" }");
        Assert.assertEquals(3, json.getInt("A"));
        Assert.assertEquals(0, json.getInt("B"));
    }

    @org.junit.Test
    public void testError1() throws Exception {
        Json json = Json.parse("{ a \"b\" : \"c\" }");
        Assert.assertEquals("{\"b\":\"c\",\"a\"}", json.toJson());
    }

    @org.junit.Test
    public void testError2() throws Exception {
        Json json = Json.parse("{ \"b\" a : \"c\" }");
        Assert.assertEquals("{\"c\"}", json.toJson());
    }

    @org.junit.Test
    public void testError3() throws Exception {
        Json json = Json.parse("a { \"b\" : \"c\" }");
        Assert.assertEquals("{\"b\":\"c\",\"a\"}", json.toJson());
    }

    @org.junit.Test
    public void testError4() throws Exception {
        Json json = Json.parse("a [ \"b\" , \"c\" ]");
        Assert.assertEquals("{[\"b\",\"c\"]}", json.toJson());
    }

    @org.junit.Test
    public void testError5() throws Exception {
        Json json = Json.parse("{\"A\"");
        Assert.assertEquals("{\"A\"}", json.toJson());
    }

    @org.junit.Test
    public void testError6() throws Exception {
        Json json = Json.parse("{\"A\":{\"B\":\"C\"}}");
        Assert.assertEquals("{\"A\":{\"B\":\"C\"}}", json.toJson());
    }

    @org.junit.Test
    public void testError7() throws Exception {
        JsonElement jsonElement = new JsonElement(JsonElementType.JsonValue, null);
        Assert.assertFalse(jsonElement.isNumber());
        Assert.assertEquals("{}", jsonElement.toJson());
        Assert.assertEquals("", jsonElement.toElementJson());
        jsonElement.jsonType = JsonElementType.JsonSplit;
        Assert.assertEquals("", jsonElement.toElementJson());
    }
}
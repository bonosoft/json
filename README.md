# json
Fast and simple JSON handling

Handle json with a 9 KB library, that have NO dependencies - so it is only 9 KB.

The Library is fully tested and the jUnit test have 100% code coverage.

## Getting started

- Download the bsjson-x.x.jar file and place it in your class path.
- import bonosoft.json.Json; in the source file where you want to handle json

## Parsing Json

 Json json = Json.parse( stringWithJsonData );

## Reading json information

 on this json: { "A" : "B" }

 json.get("A") will be "B"
 
 

## Generating Json

 json.toJson();

## Building Json

 Json json = new Json();
 json.addPair("A","B");

 will be (json.toJson()): {"A":"B"}


 ArrayList<String> list = new ArrayList<>();
 list.add("A");
 list.add("B");
 Json json = new Json();
 json.addList("C", list);

 Will be (json.toJson()): {"C":["A","B"]}


 Json json = new Json();
 Json list = json.addList("C");
 list.addListEntry("A");
 list.addListEntry("B");

 Will be (json.toJson()): {"C":["A","B"]}




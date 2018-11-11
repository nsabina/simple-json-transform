# simple-json-transform

Simple JSON utility to filter and flatten nested Json file into flat object with proeprties map

Json file to process:

```javascript
{
  "name" : "test",
  "color" : " green",
  "properties" : {
    "property1" : "One",
    "property2" : "Two",
    "property3" : "Three"
  },
  "otherProperties" : {
    "otherProperty1" : "otherOne",
    "otherProperty2" : "otherTwo",
    "otherProperty3" : "otherThree"
  }
}
```
Mapping:

`
name=newName
properties.property2=newProperty2
otherProperties.otherProperty3=newOtherProperty3
`

Filtered Json:

```javascript
{
  "name" : "test",
  "properties" : {
    "property2" : "Two"
  },
  "otherProperties" : {
    "otherProperty3" : "otherThree"
  }
}
```
Output map:
`
{newName=test, newOtherProperty3=otherThree, newProperty2=Two}
`

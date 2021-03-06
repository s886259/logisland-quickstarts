curl "http://localhost:8983/solr/admin/collections?action=CREATE&name=historian&numShards=2&replicationFactor=1"

curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field-type" : {
     "name":"ngramtext",
     "class":"solr.TextField",
     "positionIncrementGap":"100",
     "indexAnalyzer" : {
        "tokenizer":{
           "class":"solr.NGramTokenizerFactory",
           "minGramSize":"2",
           "maxGramSize":"10"  },
        "filters":[{
           "class":"solr.LowerCaseFilterFactory" }]
      },
      "queryAnalyzer" : {
       "type": "query",
        "tokenizer":{
           "class":"solr.StandardTokenizerFactory" },
        "filters":[{
           "class":"solr.LowerCaseFilterFactory" }]
      }
    }
}' http://localhost:8983/solr/historian/schema

curl -X POST -H 'Content-type:application/json' --data-binary '{
  "add-field":{ "name":"chunk_start", "type":"plong" },
  "add-field":{ "name":"chunk_end",   "type":"plong"},
  "add-field":{ "name":"chunk_value",  "type":"string", "multiValued":false, "indexed":false },
  "add-field":{ "name":"chunk_avg",  "type":"pdouble"  },
  "add-field":{ "name":"chunk_size_bytes",  "type":"pint" },
  "add-field":{ "name":"chunk_size",  "type":"pint" },
  "add-field":{ "name":"chunk_count",  "type":"pint" },
  "add-field":{ "name":"chunk_min",  "type":"pdouble" },
  "add-field":{ "name":"chunk_max",  "type":"pdouble" },
  "add-field":{ "name":"chunk_sax",  "type":"ngramtext" },
  "add-field":{ "name":"chunk_trend",  "type":"boolean"},
  "add-field":{ "name":"chunk_window_ms",  "type":"plong" },
  "add-field":{ "name":"timestamp",  "type":"plong" },
  "add-field":{ "name":"value", "type":"pdouble" },
  "add-field":{ "name":"chunk_first", "type":"pdouble" },
  "add-field":{ "name":"tagname",  "type":"string", "multiValued":false },
  "add-field":{ "name":"name",  "type":"string", "multiValued":false },
  "add-field":{ "name":"record_value",  "type":"pdouble" },
  "add-field":{ "name":"code_install",  "type":"string", "multiValued":false },
  "add-field":{ "name":"sensor",  "type":"string", "multiValued":false},
  "add-field":{ "name":"quality",  "type":"pfloat" }
}' http://localhost:8983/solr/historian/schema

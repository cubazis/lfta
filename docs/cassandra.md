To connect to cqlsh `docker run -it --net lightningfasttweetanalysis_default --link lightningfasttweetanalysis_cassandra_1:cassandra --rm cassandra cqlsh cassandra`

CQL
```sql
CREATE OR REPLACE FUNCTION test.state_group_and_count( state map<text, int>, type text )
CALLED ON NULL INPUT
RETURNS map<text, int>
LANGUAGE java AS '
Integer count = (Integer) state.get(type);  if (count == null) count = 1; else count++; state.put(type, count); return state; ' ;

CREATE OR REPLACE AGGREGATE group_and_count(text) 
SFUNC state_group_and_count 
STYPE map<text, int> 
INITCOND {};
```
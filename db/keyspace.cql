CREATE KEYSPACE IF NOT EXISTS test WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };
CREATE TABLE IF NOT EXISTS test.lfta (
  tid text,
  text text,
  reason text,
  sentiment int,
  PRIMARY KEY (tid, reason)
);
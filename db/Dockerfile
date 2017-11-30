FROM library/cassandra:latest

RUN sed -i -r 's/enable_user_defined_functions=false/enable_user_defined_functions=true/' /etc/cassandra/cassandra.yaml

ADD keyspace.cql /docker-entrypoint-initdb.d/

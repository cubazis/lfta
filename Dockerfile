FROM hseeberger/scala-sbt

EXPOSE 8000

WORKDIR /server

ENTRYPOINT ["sbt", "clean", "~re-start"]
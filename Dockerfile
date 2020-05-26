FROM hseeberger/scala-sbt

COPY ./target/universal/managementstudentcourseapp-1.0-SNAPSHOT .

COPY ./prod.conf.

RUN unzip managementstudentcourseapp-1.0-SNAPSHOT.zip && \
    ls managementstudentcourseapp-1.0-SNAPSHOT/bin && \

WORKDIR managementstudentcourseapp-1.0-SNAPSHOT

CMD["bin/managementstudentcourseapp","-Dconfig.file=../prod.conf"]

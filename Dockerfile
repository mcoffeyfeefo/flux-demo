FROM bellsoft/liberica-openjre-alpine:17 
RUN apk add dumb-init
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
WORKDIR /user/demo
COPY --chown=demo ./build/libs/demo-0.0.1-SNAPSHOT.jar /user/demo/demo.jar
EXPOSE 8080
STOPSIGNAL SIGTERM
CMD "dumb-init" "java" "-jar" "/user/demo/demo.jar"

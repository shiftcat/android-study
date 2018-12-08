FROM java:8

COPY build/libs/AndroidSpring-0.0.1-SNAPSHOT.jar /app/AndroidSpring-0.0.1-SNAPSHOT.jar

RUN mkdir /upload \
    && mkdir /upload/original_files \
    && mkdir /upload/thumbnail_images

VOLUME /upload

EXPOSE 8080

CMD ["java", "-jar", "/app/AndroidSpring-0.0.1-SNAPSHOT.jar"]
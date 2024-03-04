FROM alpine:latest

RUN apk update && \
apk upgrade && \
apk add --no-cache bash maven openjdk11

RUN mkdir /app

WORKDIR /app

COPY /target/card-cost-api-1.0.0.jar /app

EXPOSE 8087

CMD ["sh", "-c", "sleep 5s && java -jar card-cost-api-1.0.0.jar"]
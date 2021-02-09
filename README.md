# sokujichan
8dx 6v6 score calculation bot with docker.

`docker-compose.yml`
```yml
version: '3.8'

services:
  sokujichan:
    build: .
    container_name: sokujichan
    image: iamtakagi/sokujichan:latest
    restart: always
    ports:
      - 8080:8080
    environment:
      # Bot Token (Required)
      BOT_TOKEN: XXX
      # Base Uri
      BASE_URI: /
      # Server Host
      HOST: 0.0.0.0
      # Server Port
      PORT: 8080
      # Logger
      LOG: INFO
      # Embed color
      EMBED_COLOR: 83,221,172
```

```
# イメージ更新
docker pull slashnephy/saya:latest

# 起動
docker-compose up -d
```

# sokujichan

8dx 6v6 score streaming overlay with discord bot.

![](https://i.gyazo.com/3a394b3260d101fd58c29cc528dc93a3.jpg)

![](https://i.gyazo.com/e2b6f639ddd5adcde9e856d6148f04da.png)

`_sokujichan for Help`   

![](https://i.gyazo.com/4578c6b17349bbfffcff9086506fa15b.png)

## インストール / Installation

### Dockerでの導入を推奨します

`docker-compose.yml`
```yml
version: '3.8'

services:
  sokujichan:
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
# イメージ更新 / Update
docker pull iamtakagi/sokujichan:latest

# 起動 / Start
docker-compose up -d
```

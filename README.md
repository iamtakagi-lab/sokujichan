# 💬 sokujichan: 8dx 6v6 the score overlay for broadcast with discord bot.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.21-blue)](https://kotlinlang.org)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/releases)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/iam-takagi/sokujichan/Docker)](https://hub.docker.com/r/iamtakagi/sokujichan)
[![Docker Image Size (latest by date)](https://img.shields.io/docker/image-size/iamtakagi/sokujichan)](https://hub.docker.com/r/iamtakagi/sokujichan)
[![Docker Pulls](https://img.shields.io/docker/pulls/iamtakagi/sokujichan)](https://hub.docker.com/r/iamtakagi/sokujichan)
[![license](https://img.shields.io/github/license/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/blob/master/LICENSE)
[![issues](https://img.shields.io/github/issues/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/issues)
[![pull requests](https://img.shields.io/github/issues-pr/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/pulls)

## What is this? / これはなに
マリオカート8DX 6v6 のスコアオーバーレイを配信ソフト上で表示するDiscord Botです。/
MarioKart 8DX 6v6 the score overlay for broadcast with discord bot.

Botの一般提供は行っていません / No provided a public bot.

![](https://i.gyazo.com/3a394b3260d101fd58c29cc528dc93a3.jpg)

![](https://i.gyazo.com/e2b6f639ddd5adcde9e856d6148f04da.png)

`_sokujichan for help`   

![](https://i.gyazo.com/4578c6b17349bbfffcff9086506fa15b.png)

## インストール / Installation

`git clone https://github.com/iam-takagi/sokujichan.git`

### Dockerでの導入を推奨します

このようなタグがあります\
`:latest` master ブランチへのプッシュの際にビルドされます。安定しています。\
`:dev` dev ブランチへのプッシュの際にビルドされます。開発版のため, 不安定である可能性があります。\
`:v<tag>` GitHub 上のリリースに対応します。

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
      # Bot Token (必須)
      BOT_TOKEN: xxx
      # Base Uri
      BASE_URI: /
      # Server Host
      HOST: 0.0.0.0
      # Server Port  (必要次第で書き換えてください)
      PORT: 8080
      # HOSTNAME (外部公開しない場合: null で可)
      HOSTNAME:
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

# 停止 / Shutdown
docker-compose down
```

### 配信ソフト (OBSでの設定例)
`ソース -> ブラウザ -> URLを貼り付け`\
※ 途中でオーバーレイが消えてしまう場合は `オーバーレイを右クリック -> 対話` を表示したままにしてください

![](https://i.gyazo.com/d01c8e6b26ff5e7f37bdd3fc4f85daa7.png)

インストールはこれで終了です。

## 外部公開する場合
ポート開放は各自で行ってください。\
セキュリティに関する問題は、一切の責任を負いかねます。

`docker-compose.yml`
```yml
# Server Port (必要次第で書き換えてください)
PORT: 8080
# HOSTNAME (外部公開しない場合: null で可)
HOSTNAME: ドメイン名 (xxx.jp 等)
```

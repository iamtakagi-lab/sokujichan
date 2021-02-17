# 💬 sokujichan: 8dx 6v6 the score overlay for broadcast with discord bot.

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.21-blue)](https://kotlinlang.org)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/releases)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/iam-takagi/sokujichan/Docker)](https://hub.docker.com/r/iamtakagi/sokujichan)
[![Docker Image Size (latest by date)](https://img.shields.io/docker/image-size/iamtakagi/sokujichan)](https://hub.docker.com/r/iamtakagi/sokujichan)
[![Docker Pulls](https://img.shields.io/docker/pulls/iamtakagi/sokujichan)](https://hub.docker.com/r/iamtakagi/sokujichan)
[![license](https://img.shields.io/github/license/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/blob/master/LICENSE)
[![issues](https://img.shields.io/github/issues/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/issues)
[![pull requests](https://img.shields.io/github/issues-pr/iam-takagi/sokujichan)](https://github.com/iam-takagi/sokujichan/pulls)

# これはなに / What is this?
マリオカート8DX 6v6 のスコアオーバーレイを配信ソフト上で表示するDiscord Botです。\
MarioKart 8DX 6v6 the score overlay for broadcast with discord bot.

Botの一般提供は行っていません。各自でインストールを行ってください。\
No provided a public bot. Please install yourself.

![](https://i.gyazo.com/3a394b3260d101fd58c29cc528dc93a3.jpg)

![](https://i.gyazo.com/72579869774e02a21108e7326d2acb98.png)

`_sokujichan for help`   

![](https://i.gyazo.com/4578c6b17349bbfffcff9086506fa15b.png)

# 動作環境
Linux/macOS/Windows

# インストールに必要なもの
- [Git](https://git-scm.com/downloads)
- [Docker](https://www.docker.com/get-started)

# インストール / Installation

- [Discord Developer Portal](https://discord.com/developers/applications) にてBotを作成します。
  - Privileged Gateway Intents を ON の状態にします。

- Cloneします
```console
git clone https://github.com/iam-takagi/sokujichan.git
cd sokujichan
```

## Dockerでの導入 (推奨): こちらのほうが環境構築が容易です

このようなタグがあります\
`iamtakagi/sokujichan:latest` master ブランチへのプッシュの際にビルドされます。安定しています。\
`iamtakagi/sokujichan:dev` dev ブランチへのプッシュの際にビルドされます。開発版のため, 不安定である可能性があります。\
`iamtakagi/sokujichan:<tag>` GitHub 上のリリースに対応します。

`BOT_TOKEN` にBotのTokenを入力します。

`docker-compose.yml`
```yml
version: '3.8'

services:

  # Bot
  sokujichan:
    container_name: sokujichan
    image: iamtakagi/sokujichan:latest
    restart: always
    ports:
      - 8080:8080
    depends_on:
      - mongo
    environment:
      # Bot Token (ここだけ書き換えれば動く: 入力必須)
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
      LOG: DEBUG
      # Embed color
      EMBED_COLOR: 83,221,172
      # Mongo DB (基本的には書き換えない)
      MONGO_HOST: mongo
      MONGO_PORT: 27017
      MONGO_USER: user
      MONGO_PASS: pass
  # DB
  mongo:
    image: mongo
    container_name: mongo
    restart: always
    volumes:
      - ./mongodb:/data/db
    environment:
      MONGO_INITDB_ROOT_USERNAME: user
      MONGO_INITDB_ROOT_PASSWORD: pass
    ports:
      - 27017:27017
```

```console
# イメージ更新 / Update
docker pull iamtakagi/sokujichan:latest

# 起動 / Start
docker-compose up -d

# 停止 / Shutdown
docker-compose down

# ログ確認 / Logs
docker-compose logs -f
```

## 直接実行 (非推奨)

リリースから Jar を取ってきて実行するか `./gradlew run` で実行できます。

設定値の変更は 環境変数経由でしか行なえません。ご了承ください。

```console
LOG=DEBUG java -jar /path/to/sokujichan.jar
```

***

インストールはこれで終了です。

# 配信ソフト (OBSでの設定例)
`ソース -> ブラウザ -> URLを貼り付け`\
※ 途中でオーバーレイが消えてしまう場合は `オーバーレイを右クリック -> 対話` を表示したままにしてください

![](https://i.gyazo.com/d01c8e6b26ff5e7f37bdd3fc4f85daa7.png)

# 外部公開する場合
ポート開放は各自で行ってください。\
セキュリティに関する問題は、一切の責任を負いかねます。

`docker-compose.yml`
```yml
# Server Port (必要次第で書き換えてください)
PORT: 8080
# HOSTNAME (外部公開しない場合: null で可)
HOSTNAME: ホスト名, ドメイン, IPアドレス等
```

# 貢献 / Contribution

## Issues
バグの報告・改善点・提案等を行ってください。

## Pull Requests
開発には Intellij IDEA を推奨しています。

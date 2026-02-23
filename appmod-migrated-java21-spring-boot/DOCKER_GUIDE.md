# Docker実行ガイド - Spring Boot版

## 概要

このガイドでは、DockerとDocker Composeを使用してSpring Boot 3.5.0アプリケーションを実行する方法を説明します。

## 前提条件

- Docker Desktop (最新版)
- Docker Compose V2以降

## アーキテクチャ

- **アプリケーション**: Spring Boot 3.5.0 + Java 25 (Eclipse Temurin)
- **データベース**: PostgreSQL 15
- **ビルドツール**: Maven 3.9.9
- **コンテナ**: Alpine Linux (軽量化)

## クイックスタート

### 1. アプリケーションのビルドと起動

```bash
# すべてのサービスをビルド・起動
docker-compose up --build

# バックグラウンドで起動
docker-compose up -d --build
```

### 2. アプリケーションへのアクセス

- アプリケーション: http://localhost:8080
- ヘルスチェック: http://localhost:8080/actuator/health
- PostgreSQLデータベース: localhost:5432

### 3. ログの確認

```bash
# すべてのログを表示
docker-compose logs -f

# アプリケーションのログのみ
docker-compose logs -f app

# データベースのログのみ
docker-compose logs -f db
```

### 4. 停止と削除

```bash
# 停止
docker-compose stop

# 停止して削除
docker-compose down

# ボリュームも含めて削除
docker-compose down -v
```

## 環境変数

docker-compose.ymlで以下の環境変数を設定できます：

### アプリケーション設定

- `SPRING_PROFILES_ACTIVE`: Springプロファイル (デフォルト: prod)
- `SPRING_DATASOURCE_URL`: データベースURL
- `SPRING_DATASOURCE_USERNAME`: データベースユーザー名
- `SPRING_DATASOURCE_PASSWORD`: データベースパスワード
- `LOGGING_LEVEL_ROOT`: ルートログレベル (デフォルト: INFO)
- `LOGGING_LEVEL_COM_SKISHOP`: アプリケーションログレベル (デフォルト: DEBUG)

### HikariCP設定

- `SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE`: 最大プールサイズ (デフォルト: 20)
- `SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE`: 最小アイドル接続数 (デフォルト: 5)
- `SPRING_DATASOURCE_HIKARI_CONNECTION_TIMEOUT`: 接続タイムアウト (デフォルト: 30000ms)

## トラブルシューティング

### データベース接続エラー

```bash
# データベースの状態を確認
docker-compose ps db

# データベースに直接接続
docker-compose exec db psql -U skishop -d skishop
```

### アプリケーションが起動しない

```bash
# コンテナの状態を確認
docker-compose ps

# アプリケーションログを確認
docker-compose logs app

# コンテナ内でシェルを実行
docker-compose exec app sh
```

### ポートが既に使用されている

```bash
# 8080ポートを使用しているプロセスを確認
lsof -i :8080

# または別のポートを使用
# docker-compose.ymlのportsセクションを変更
# - "8081:8080"
```

### イメージを再ビルド

```bash
# キャッシュを使わずに再ビルド
docker-compose build --no-cache

# 古いイメージを削除
docker image prune -a
```

## 開発モードでの実行

開発モードでH2データベースを使用する場合：

```bash
# 環境変数を設定してアプリケーションのみ起動
docker run -it --rm \
  -e SPRING_PROFILES_ACTIVE=dev \
  -p 8080:8080 \
  skishop-springboot-app
```

または、docker-compose.ymlを編集して`SPRING_PROFILES_ACTIVE: dev`に変更してください。

## データの永続化

データベースのデータは`db-data`という名前のDockerボリュームに永続化されます。

```bash
# ボリュームの一覧を表示
docker volume ls

# ボリュームの詳細を表示
docker volume inspect struts_db-data

# データを完全に削除
docker-compose down -v
```

## 本番環境への展開

本番環境では、以下の点に注意してください：

1. **環境変数の管理**: 機密情報は環境変数で管理し、docker-compose.ymlにハードコードしない
2. **ログ管理**: ログドライバーを設定してログを適切に管理
3. **リソース制限**: `deploy.resources`でメモリとCPUを制限
4. **セキュリティ**: 不要なポートを公開しない
5. **バックアップ**: データベースの定期的なバックアップ

## ヘルスチェック

アプリケーションとデータベースのヘルスチェックが設定されています：

```bash
# アプリケーションのヘルスチェック
curl http://localhost:8080/actuator/health

# 期待されるレスポンス
{"status":"UP"}
```

## パフォーマンスチューニング

### JVMオプションの調整

Dockerfileを編集してJVMオプションを追加：

```dockerfile
ENTRYPOINT ["java", \
  "-Xms512m", \
  "-Xmx2g", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
```

### データベースチューニング

docker-compose.ymlにPostgreSQLの設定を追加：

```yaml
db:
  command:
    - "postgres"
    - "-c"
    - "max_connections=200"
    - "-c"
    - "shared_buffers=256MB"
```

## サポート

問題が発生した場合は、MIGRATION_REPORT.mdを参照してください。

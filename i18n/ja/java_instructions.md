${app}: 始めに
-------------------------------------
これは、Java と Cloudant NoSQL DB サービスのスターター・アプリケーションです。

このサンプルは、お気に入り編成アプリケーションで、ユーザーはお気に入りを編成および管理することができ、また各カテゴリーでさまざまなタイプがサポートされます。このサンプルでは、Ektorp Java API を使用して、アプリケーションにバインドされる Cloudant NoSQL DB サービスにアクセスする方法を示します。

1. [cf コマンド・ライン・ツールをインストールします](${doc-url}/#starters/BuildingWeb.html#install_cf)。
2. [スターター・アプリケーション・パッケージをダウンロードします](${ace-url}/rest/apps/${app-guid}/starter-download)。
3. パッケージを解凍し、そのパッケージに 'cd' で移動します。
4. Bluemix への接続:

		cf api ${api-url}

5. Bluemix へのログイン:

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. JAVA コードをコンパイルし、Ant を使用して war パッケージを生成します。
7. アプリのデプロイ:

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. アプリへのアクセス: [${route}](http://${route})

開始使用 ${app}
-------------------------------------
這是適用於 Java 的入門範本應用程式，搭配 Cloudant NoSQL DB 服務。

範例為 Favorites Organizer 應用程式，其可讓使用者組織及管理我的最愛，以及支援每一個種類中的不同類型。此範例示範如何使用 Ektorp Java API，存取連結至應用程式的 Cloudant NoSQL DB 服務。

1. [安裝 cf 指令行工具](${doc-url}/#starters/BuildingWeb.html#install_cf)。
2. [下載入門範本應用程式套件](${ace-url}/rest/apps/${app-guid}/starter-download)。
3. 解開套件並 'cd' 至其中。
4. 連接至 Bluemix：

		cf api ${api-url}

5. 登入 Bluemix：

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. 編譯 JAVA 程式碼並使用 ant 產生 war 套件。
7. 部署您的應用程式：

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. 存取您的應用程式：[${route}](http://${route})

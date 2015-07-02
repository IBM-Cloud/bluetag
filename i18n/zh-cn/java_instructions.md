${app} 入门
-------------------------------------
这是用于 Cloudant NoSQL DB 的 Java 的启动器应用程序。

样本为 Favorites Organizer 应用程序，此应用程序允许用户组织和管理器收藏夹，支持每种类别中存在不同类型。此样本说明如何访问使用 Ektorp Java API 绑定到应用程序的 Cloudant NoSQL DB 服务。

1. [安装 cf 命令行工具](${doc-url}/#starters/BuildingWeb.html#install_cf)。
2. [下载起动器应用程序包](${ace-url}/rest/apps/${app-guid}/starter-download)。
3. 解压缩程序包，并对其执行“cd”命令以切换到相应目录。
4. 连接到 Bluemix：

		cf api ${api-url}

5. 登录到 Bluemix：

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. 编译 JAVA 代码并使用 ant 生成 war 程序包。
7. 部署您的应用程序：

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. 访问您的应用程序：[${route}](http://${route})

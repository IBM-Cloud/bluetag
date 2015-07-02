Einführung in ${app}
-------------------------------------
Dies ist eine Starteranwendung für Java mit Cloudant NoSQL DB-Service.

Bei dem Beispiel handelt es sich um eine Favorites Organizer-Anwendung, mit deren Hilfe Benutzer ihre Favoriten organisieren und verwalten können und die unterschiedliche Typen in den einzelnen Kategorien unterstützt. Dieses Beispiel veranschaulicht den Zugriff auf den Cloudant NoSQL DB-Service, der an die Anwendung gebunden wird, und zwar mithilfe von Ektorp-Java-APIs.

1. [Das Befehlszeilentool 'cf' installieren](${doc-url}/#starters/BuildingWeb.html#install_cf).
2. [Das Starteranwendungspaket herunterladen](${ace-url}/rest/apps/${app-guid}/starter-download).
3. Das Paket extrahieren und in das entsprechende Verzeichnis ('cd') wechseln.
4. Verbindung zu Bluemix herstellen:

		cf api ${api-url}

5. Bei Bluemix anmelden:

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. Java-Code kompilieren und WAR-Paket mit 'ant' generieren.
7. Ihre Anwendung (App) bereitstellen:

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. Auf Ihre Anwendung (App) zugreifen: [${route}](http://${route})

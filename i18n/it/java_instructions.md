Introduzione a ${app}
-------------------------------------
Applicazione starter per Java con il servizio Cloudant NoSQL DB.

L'esempio è un'applicazione Favorites Organizer, che consente agli utenti di organizzare e gestire i propri preferiti e supporta tipi differenti di ogni categoria. Questo esempio mostra come accedere al servizio Cloudant NoSQL DB che viene collegato all'applicazione, utilizzando le API Java Ektorp.

1. [Installare lo strumento della riga di comando cf](${doc-url}/#starters/BuildingWeb.html#install_cf).
2. [Scaricare il package applicazione starter](${ace-url}/rest/apps/${app-guid}/starter-download).
3. Estrarre il package e il 'cd' in esso.
4. Connetti a Bluemix:

		cf api ${api-url}

5. Accedi a Bluemix:

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. Compilare il codice JAVA e generare il package war utilizzando ant.
7. Distribuisci la applicazione:

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. Accedi alla applicazione: [${route}](http://${route})

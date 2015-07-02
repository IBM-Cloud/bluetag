Initiation à ${app}
-------------------------------------
Il s'agit d'une application de module de démarrage pour Java avec le service Cloudant NoSQL DB.

L'exemple est une application d'organisateur de favoris qui permet aux utilisateurs d'organiser et de gérer leurs favoris et qui prend en charge
différents types dans chaque catégorie. Il explique comment accéder au service Cloudant NoSQL DB lié à l'application à l'aide d'API Java Ektorp.


1. [Installez l'outil de ligne de commande cf](${doc-url}/#starters/BuildingWeb.html#install_cf).
2. [Téléchargez le package d'applications du module de démarrage](${ace-url}/rest/apps/${app-guid}/starter-download).
3. Procédez à l'extraction du package et placez-vous dans le répertoire du package à l'aide de la commande 'cd'. 
4. Accédez à Bluemix :

		cf api ${url-api}

5. Connectez-vous à Bluemix :

		cf login -u ${nom-utilisateur}
		cf target -o ${org} -s ${espace}
		
6. Compilez le code JAVA et générez le package war avec ant. 
7. Déployez votre application :

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. Accédez à votre application : [${route}](http://${route})

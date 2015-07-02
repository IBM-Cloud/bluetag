Introdução ao ${app}
-------------------------------------
Esse é um aplicativo iniciador para Java com serviço de banco de dados Cloudant NoSQL.

A amostra é um aplicativo Organizador de favoritos que permite aos usuários organizar e gerenciar seus
diferentes tipos de favoritos e suportes em cada categoria. Essa amostra demonstra como acessar o serviço de banco de
dados Cloudant NoSQL que se liga ao aplicativo, usando APIs Java Ektorp.

1. [Instale a ferramenta de linha de comandos cf](${doc-url}/#starters/BuildingWeb.html#install_cf).
2. [Faça o download do pacote de aplicativo iniciador](${ace-url}/rest/apps/${app-guid}/starter-download).
3. Extraia o pacote e 'cd' para ele.
4. Conecte-se ao Bluemix:

		cf api ${api-url}

5. Efetue login no Bluemix:

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. Compile o código JAVA e gere o pacote war usando ant.
7. Implemente seu app:

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. Acesse seu app: [${route}](http://${route})

Empiece a trabajar con ${app}
-------------------------------------
Esta es una aplicación de inicio para Java con el servicio Cloudant NoSQL DB.

El ejemplo es una aplicación de organización de favoritos que permite a los usuarios organizar y gestionar sus archivos favoritos y da soporte a distintos tipos en cada categoría. En el ejemplo es muestra cómo acceder al servicio Cloudant NoSQL DB que enlaza con la aplicación mediante las API Java de Ektorp.

1. [Instale la herramienta de línea de mandatos cf](${doc-url}/#starters/BuildingWeb.html#install_cf).
2. [Descargue el paquete de aplicación de inicio](${ace-url}/rest/apps/${app-guid}/starter-download).
3. Extraiga el paquete y ejecute 'cd' para ir al mismo. 
4. Conéctese a Bluemix:

		cf api ${api-url}

5. Inicie la sesión en Bluemix:

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. Compile el código JAVA y genere el paquete war mediante ant. 
7. Despliegue su app:

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. Acceda a la app: [${ruta}](http://${route})

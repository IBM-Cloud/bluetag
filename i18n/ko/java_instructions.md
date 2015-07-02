${app} 시작하기
-------------------------------------
Cloudant NoSQL DB 서비스가 포함되어 있는 Java용 스타터 애플리케이션입니다.

샘플은 Favorites Organizer 애플리케이션으로 사용자들이 즐겨찾기를 구성하고 관리하며 각 카테고리의 다른 유형을 지원할 수 있습니다. 이 샘플은 Ektorp Java API를 사용하여 애플리케이션에 바인딩하는 Cloudant NoSQL DB 서비스에 액세스하는 방법을 보여줍니다. 

1. [cf 명령행 도구를 설치](${doc-url}/#starters/BuildingWeb.html#install_cf)하십시오.
2. [스타터 애플리케이션 패키지를 다운로드](${ace-url}/rest/apps/${app-guid}/starter-download)하십시오.
3. 패키지의 압축을 풀고 해당 위치로 'cd'하십시오.
4. Bluemix에 연결하십시오.

		cf api ${api-url}

5. Bluemix에 로그인하십시오.

		cf login -u ${username}
		cf target -o ${org} -s ${space}
		
6. JAVA 코드를 컴파일하고 Ant를 사용하여 war 패키지를 생성하십시오. 
7. 앱을 배치하십시오.

		cf push ${app} -p JavaCloudantDB.war -m 512M

8. [${route}](http://${route})로 앱에 액세스하십시오.

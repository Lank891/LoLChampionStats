# LoLChampionStats

Pierwsza niewielka apka w Kotlinie - łączy się z Riot Api i wyrzuca stronę html ze statystykami maestrii wszystkich championów danego przywoływacza (z EUNE)

Pod koniec sierpnia 2018 riot zmienia api, nie wiem, czy app zostanie zaktualizowana ._.

-----

Aby użyć należy:

(\*) Otworzyć plik `api_key` dowolnym edytorem tekstu i wpisać tam swój klucz api

(\*) Otworzyć konsolę i przejść do folderu ze skompilowanym plikiem .jar

(\*) Wpisać `java -jre MasteryStatistics.jar nazwaPrzywoływaczaEUNE`, na przykład `java -jar MasteryStatistics.jar Wojownik987`

(\*) Jeżeli wszystko pójdzie zgodnie z planem w folderze pojawi się plik o nazwie `nazwaPrzywoływaczaStatistics.html`, np. `Wojownik987Statistics.html` - należy otworzyć go w dowolnej przeglądarce

-----

Pamiętać o aktualizowaniu champsData i być może nowym parsowaniu pobieranych jsonów, ew. zamianie api, kiedy pojawi się nowe

-----

Co znajduje się w plikach:

(\*) Folder `Program`:

(\*) `api_key` - klucz api (należy mieć swój :3)

(\*) `champsData` - informacje o wszystych championach w formacie json pobrane z LOL-STATIC-DATA-V3

(\*) `upperPart` i `lowerPart` - górna i dolna część generowanej strony, po połączeniu są prawie puste (i nie mają podomykanych tagów)

(\*) `main.kt` - kod programu (Kotlin)

(\*) `MasteryStatistics.jar` - skompilowany program do executable jar, po podaniu klucza api można go używać

(\*) Folder `Sample Outputs` - kilka przykładowych stron ze statystykami, zrobione dnia 10.08.2018 00:00

-----

Głowne przyczyny błędów:

(\*) Niepoprawny klucz api

(\*) Niepoprawna nazwa przywoływacza

(\*) Brak internetu

(\*) Zmieniło się api, a program nie, przez co nie może się połązyć lub źle odczytuje wyniki (przy braku połączenia pojawi się prosty błąd, a przy złej interpretacji jsona - skomplikowany)

-----

Ostatnia zmiana: 10.08.2018 00:00

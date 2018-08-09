import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class Champion(id: Int, name: String, version: String) {
    var id = id
    var name = name
    var level = -1
    var points = -1
    var chest = false
    var imageURL = "http://ddragon.leagueoflegends.com/cdn/${version}/img/champion/${getUrlName(name)}.png"

    private fun getUrlName(name: String): String {
        if(name == "Kha'Zix" || name == "Kai'Sa" || name == "LeBlanc" || name == "Vel'Koz" || name == "Cho'Gath") return name.toLowerCase().filter { c -> c.isLetter() }.capitalize()
        if(name == "Wukong") return "MonkeyKing"
        return name.filter { c -> c.isLetter() }
    }
}

fun showChampion(champ: Champion): String {

    var htmlCode = "<div><img src='${champ.imageURL}' style=\"float: left; padding-right: 2em\"><Strong>Nazwa: </Strong>${champ.name}<br>"

    if(champ.level < 1) {
        htmlCode += "Champion nadal czeka, aby nim zagrać!<br><br><br><br></div>\n"
        return htmlCode
    }

    htmlCode += "<strong>Poziom: </strong> ${champ.level}<br><strong>Punkty: </strong>${champ.points}<br>"
    htmlCode += "${if(champ.chest) "<strong>Skrzynia</strong> została zdobyta" else "<strong>Skrzynia</strong> czeka na zdobycie"}.</div><br>\n"

    return htmlCode
}

fun getApiKey(): String = File("api_key").readText()

fun getUrlResponse(url: URL): Int {
    with(url.openConnection() as HttpURLConnection)
    {
        return responseCode
    }
}

fun didApiReturnedError(code: Int): Boolean {
    when(code) {
        400 -> { println("Kod błędu: 400; Treść błędu: \"Bad request\""); return true}
        401 -> { println("Kod błędu: 401; Treść błędu: \"Unauthorized\""); return true}
        403 -> { println("Kod błędu: 403; Treść błędu: \"Forbidden\""); return true}
        404 -> { println("Kod błędu: 404; Treść błędu: \"Data not found\""); return true}
        405 -> { println("Kod błędu: 405; Treść błędu: \"Method not allowed\""); return true}
        415 -> { println("Kod błędu: 415; Treść błędu: \"Unsupported media type\""); return true}
        422 -> { println("Kod błędu: 422; Treść błędu: \"Player exists, but hasn't played since match history collection began\""); return true}
        429 -> { println("Kod błędu: 429; Treść błędu: \"Rate limit exceeded\""); return true}
        500 -> { println("Kod błędu: 500; Treść błędu: \"Internal server error\""); return true}
        502 -> { println("Kod błędu: 502; Treść błędu: \"Bad gateway\""); return true}
        503 -> { println("Kod błędu: 503; Treść błędu: \"Service unavailable\""); return true}
        504 -> { println("Kod błędu: 504; Treść błędu: \"Gateway timeout\""); return true}
        else -> return false
    }
}

fun getSummonerID(summonerContent: String): String {
    return summonerContent.substring(6, summonerContent.indexOfFirst{ c -> c == ',' })
    //Api zwraca:
    //{"id":4567886513, "accountId": (...)
    //Potrzebujemy tylko ID, które zaczyna się jako 7dmy znak (indeks 6), a kończy pierwszym odnalezionym przecinkiem
}

fun createChampions(arr: ArrayList<Champion>, text: String, version: String) {
    var textCopy = text

    var id: Int
    var name: String

    var indexOfValue: Int? = 0

    while(indexOfValue != null) {
        indexOfValue = textCopy.findAnyOf( listOf("\"id\":") )?.first
        if (indexOfValue == null) break
        textCopy = textCopy.removeRange(0, indexOfValue + 5) // `"id":` ma 5 znaków
        id = textCopy.substring(0, textCopy.indexOfFirst{ c -> c == ',' }).toInt()

        indexOfValue = textCopy.findAnyOf( listOf("\"name\":\"") )?.first
        if (indexOfValue == null) break
        textCopy = textCopy.removeRange(0, indexOfValue + 8) //`"name":"` ma 8 znaków
        name = textCopy.substring(0, textCopy.indexOfFirst { c -> c == '"' })

        arr.add(Champion(id, name, version))
    }
}

fun findChampionPositionById(arr: ArrayList<Champion>, id: Int): Int? {
    for(i in 0..arr.size){
        if(arr[i].id == id) return i
    }
    return null
}

fun fillChampions(arr: ArrayList<Champion>, text: String) {
    var textCopy = text

    var id: Int
    var lvl: Int
    var pts: Int
    var chest: Boolean

    var indeksInArray: Int?

    var indexOfValue: Int? = 0

    while(indexOfValue != null) {


        indexOfValue = textCopy.findAnyOf( listOf("\"championId\":") )?.first
        if (indexOfValue == null) break
        textCopy = textCopy.removeRange(0, indexOfValue + 13) // `"championId":` ma 13 znaków
        id = textCopy.substring(0, textCopy.indexOfFirst{ c -> c == ',' }).toInt()


        indexOfValue = textCopy.findAnyOf( listOf("\"championLevel\":") )?.first
        if (indexOfValue == null) break
        textCopy = textCopy.removeRange(0, indexOfValue + 16) // `"championLevel":` ma 16 znaków
        lvl = textCopy.substring(0, textCopy.indexOfFirst{ c -> c == ',' }).toInt()


        indexOfValue = textCopy.findAnyOf( listOf("\"championPoints\":") )?.first
        if (indexOfValue == null) break
        textCopy = textCopy.removeRange(0, indexOfValue + 17) // `"championPoints":` ma 17 znaków
        pts = textCopy.substring(0, textCopy.indexOfFirst{ c -> c == ',' }).toInt()


        indexOfValue = textCopy.findAnyOf( listOf("\"chestGranted\":") )?.first
        if (indexOfValue == null) break
        textCopy = textCopy.removeRange(0, indexOfValue + 15) // `"chestGranted":` ma 15 znaków
        chest = textCopy.substring(0, textCopy.indexOfFirst{ c -> c == ',' }).toBoolean()

        indeksInArray = findChampionPositionById(arr, id)
        if(indeksInArray == null) {
            println("Nieznany champion o id ${id}!")
            continue
        }

        arr[indeksInArray].chest = chest
        arr[indeksInArray].points = pts
        arr[indeksInArray].level = lvl

    }
}

fun main(args: Array<String>) {

    if(args.size < 1) {println("Podaj nazwę przywoływacza (EUNE) po nazwie programu, jako argument."); return}
    val name = args[0]

    val summonerDataURL = URL("https://eun1.api.riotgames.com/lol/summoner/v3/summoners/by-name/${name}?api_key=${getApiKey()}")
    if( didApiReturnedError( getUrlResponse(summonerDataURL) ) ) return

    val summonerID = getSummonerID( summonerDataURL.readText() )

    val maesteryDataURL = URL("https://eun1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries/by-summoner/${summonerID}?api_key=${getApiKey()}")
    if( didApiReturnedError( getUrlResponse(maesteryDataURL) ) ) return

    val versionsURL = URL("https://ddragon.leagueoflegends.com/api/versions.json")
    if( didApiReturnedError( getUrlResponse(versionsURL) ) ) return

    val versionsJSON = versionsURL.readText()

    val endIndexOfVersion = versionsJSON.findAnyOf(listOf("\""), 3, false)?.first
    if( endIndexOfVersion == null) {println("Błąd odczytywania wersji!"); return}
    val version = versionsJSON.substring(2, endIndexOfVersion)

    val champions = arrayListOf<Champion>()

    createChampions(champions, File("champsData").readText(), version)

    fillChampions(champions, maesteryDataURL.readText())

    champions.sortByDescending { c -> c.points }

    val upperPagePart = File("upperPart").readText()

    var addedPart = " ${name}</strong></h2>"
    for(champ in champions) {
        addedPart += showChampion(champ)
    }

    var lowerPagePart = File("lowerPart").readText()

    val outFile = File("${name}Statistics.html")
    if(outFile.exists() && outFile.isFile()) outFile.delete()
    outFile.createNewFile()
    if(!outFile.canWrite()){println("Nie można zapisać pliku wyjściowego!"); return}
    outFile.writeText(upperPagePart)
    outFile.appendText(addedPart)
    outFile.appendText(lowerPagePart)
}

val bookList = listOf(
    Book(1111, true, "BFF", 50, "FF2"),
    Book(1112, true, "BEE", 500, "FF3"),
    Book(1113, false, "BRR", 5, "FF4")
)
val newspaperList = listOf(
    Newspaper(2111, true, "NFF", 50),
    Newspaper(2112, true, "NEE", 51),
    Newspaper(2113, false, "NRR", 52)
)
val diskList = listOf(
    Disk(3111, true, "DFF", "DVD"),
    Disk(3112, true, "DEE", "CD"),
    Disk(3113, false, "DRR", "DVD")
)

abstract class LibraryItem(open val id: Int, open var isEnable: Boolean, open val name: String){
    open fun shortInfo(): String{
        return "$name доступна: ${ if (isEnable) {"Да"} else  "Нет"}"
    }
    abstract fun fullInfo(): String
}

class Book(id: Int, isEnable: Boolean, name: String, val numberOfPages: Int, val author: String) : LibraryItem(id, isEnable, name){
    override fun fullInfo(): String {
        return "книга: $name($numberOfPages стр.) автора: $author с id: $id доступна: ${ if (isEnable) {"Да"} else  "Нет"}"
    }
}

class Newspaper(id: Int, isEnable: Boolean, name: String, val releaseNum: Int) : LibraryItem(id, isEnable, name){
    override fun fullInfo(): String {
        return "выпуск: $releaseNum газеты:$name с id: $id доступен: ${ if (isEnable) {"Да"} else  "Нет"}"
    }
}

class Disk(id: Int, isEnable: Boolean, name: String, val diskType: String) : LibraryItem(id, isEnable, name){
    override fun fullInfo(): String {
        return "$diskType $name доступен: ${ if (isEnable) {"Да"} else  "Нет"}"
    }
}


class TaskMenu(){
    lateinit var listType: List<LibraryItem>

    fun showShortInfo(listItems : List<LibraryItem>){
        var count = 1
        for (i in listItems)
            println("${count++}. ${i.shortInfo()}")
    }
    fun itemTypeLoop(){
        runCatching {

        println("1.Показать книги\n" +
                "2.Показать газеты\n" +
                "3.Показать диски")

        loop1@ while (true){
            when (readlnOrNull()?.toIntOrNull()) {
                1 -> { listType = bookList; break@loop1}
                2 -> { listType = newspaperList; break@loop1}
                3 -> { listType = diskList; break@loop1}
                else -> println("неверный выбор, попробуйте еще раз")
            }
        }
        showShortInfo(listType)
        chooseObjectLoop()
    }.onFailure {
            println("Что-то пошло не так. Возвращение к выбору типа")
            itemTypeLoop()
    }
    }
    fun chooseObjectLoop(){
        println("Введите 0, чтобы вернуться к выбору типа.\n" +
                "Или введите номер интересующего вас объекта.")
        loop2@ while (true){
            val inp: Int? = readlnOrNull()?.toIntOrNull()
            try  {require(inp!=null) }
            catch (e: IllegalArgumentException) {
                println("неверный выбор, попробуйте еще раз")
                continue@loop2
            }
            if (inp == 0) {
                itemTypeLoop()
                break@loop2
            }
            if (inp in 1..listType.size){
                actionLoop(inp-1) //так как в списке нумерация с 0, а не с 1
                break@loop2
            } else
                println("неверный выбор, попробуйте еще раз")
        }
    }
    fun actionLoop(number: Int){
        println("0.Вернуться к выбору типа\n" +
                "1.Взять домой\n" +
                "2.Читать в читальном зале\n" +
                "3.Показать подробную информацию\n" +
                "4.Вернуть")
        loop3@ while (true){
            when (readlnOrNull()?.toIntOrNull()) {
                0 -> {itemTypeLoop(); break@loop3}
                1 -> {
                    if (listType == newspaperList){
                        println("Газету нельзя брать домой")
                        continue@loop3
                    }
                    if (!listType[number].isEnable){
                        println("В данный момент этот объект недоступен")
                        continue@loop3
                    }
                    listType[number].isEnable = false
                    println("${if (listType == bookList) {"Книгу"} else "Диск"} ${listType[number].id} взяли домой.")
                }
                2 -> {
                    if (listType == diskList){
                        println("Диск нельзя читать в читальном зале")
                        continue@loop3
                    }
                    if (!listType[number].isEnable){
                        println("В данный момент этот объект недоступен")
                        continue@loop3
                    }
                    listType[number].isEnable = false
                    println("${if (listType == bookList) {"Книгу"} else "Газету"} ${listType[number].id} взяли в читальный зал.")
                }
                3 -> println(listType[number].fullInfo())
                4 -> {
                    if (listType[number].isEnable){
                        println("Этот объект нельзя вернуть")
                        continue@loop3
                    }
                    listType[number].isEnable = true
                    println("${if (listType == bookList) {"Книгу"} 
                               else if (listType == diskList) {"Диск"}
                               else "Газету"} ${listType[number].id} вернули.")
                }
                else -> println("неверный выбор, попробуйте еще раз")
            }
        }
    }
}

fun main(){
    println("Hello, user")
    val taskMenu = TaskMenu()
    taskMenu.itemTypeLoop()
}

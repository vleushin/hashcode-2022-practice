import java.io.File

fun readClients(file: File): List<Client> {
    val lines = file.readLines()
    val clients = mutableListOf<Client>()
    for (i in 1 until (lines.size-1) step 2) {
        val likes = lines[i].split(' ').drop(1).toSet()
        val dislikes = lines[i+1].split(' ').drop(1).toSet()
        clients.add(Client(likes, dislikes))
    }
    return clients.toList()
}

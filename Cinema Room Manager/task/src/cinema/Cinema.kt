package cinema
import java.lang.*

fun printCinema(room: MutableList<MutableList<Char>>) {
    println("Cinema:")
    println("  " + (1..room.first().size).joinToString(" "))
    var count = 1
    for (row in room) {
        println("$count ${row.joinToString(" ")}")
        count++
    }
}

fun buildCinema(rows: Int, seatsPerRow: Int):  MutableList<MutableList<Char>>{
    val room = MutableList<MutableList<Char>>(rows) {
        MutableList<Char>(seatsPerRow) {'S'}
    }
    return room
}

fun checkPrice(room: MutableList<MutableList<Char>>, seat: MutableList<Int>): Int {
    val nRows = room.size
    val nSeatsEachRow = room.first().size
    val nSeats = nRows * nSeatsEachRow
    val price = if (nSeats <= 60) 10 else {
        if (seat.first() <= nRows / 2) 10 else 8
    }
    return price
}

fun updateCinema(room: MutableList<MutableList<Char>>, seat: MutableList<Int>): MutableList<MutableList<Char>> {
    room[seat.first() - 1][seat.last() - 1] = 'B'
    return room
}

fun printMenu() {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
}

fun buyTicket(room: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
    println("Enter a row number:")
    val row = readln().toInt()
    println("Enter a seat number in that row:")
    val col = readln().toInt()
    val seat = mutableListOf<Int>(row, col)

    try {
        if (room[row - 1][col - 1] == 'B') throw IllegalArgumentException()
        val price = checkPrice(room, seat)
        println("Ticket price: \$$price")
        return updateCinema(room, seat)
    } catch (e: IllegalArgumentException) {
        println("That ticket has already been purchased!")
        return buyTicket(room)
    } catch (e: IndexOutOfBoundsException) {
        println("Wrong input!")
        return room
    }

}

fun printStats(room: MutableList<MutableList<Char>>) {
    val nRows = room.size
    val nSeatsEachRow = room.first().size
    val nSeats = nRows * nSeatsEachRow

    var seatsFree = 0
    var seatsBooked = 0
    var income = 0
    var totalIncome = 0

    if (nSeats <= 60) {

        for (row in room) { for (seat in row) { if (seat == 'S') seatsFree++
        else if (seat == 'B') seatsBooked++ }}
        income = seatsBooked * 10
        totalIncome = nSeats * 10
    } else {
        var seatsBookedHigh = 0
        var seatsBookedLow = 0
        for (row in room.indices) { 
            for (seat in room[row]) {
                if (seat == 'S') seatsFree++
                else if (seat == 'B') {
                    if (row+1 <= nRows / 2) {
                        seatsBookedHigh++
                    } else seatsBookedLow++
                }
            }
        }
        seatsBooked = seatsBookedHigh + seatsBookedLow
        income = seatsBookedHigh * 10 + seatsBookedLow * 8
        totalIncome = ((nRows / 2) * 10 + (nRows - (nRows / 2)) * 8) * nSeatsEachRow
    }

    val percentage = seatsBooked.toDouble() / nSeats * 100
    val formatPercentage = "%.2f".format(percentage)

    println("Number of purchased tickets: $seatsBooked")
    println("Percentage: $formatPercentage%")
    println("Current income: \$$income")
    println("Total income: \$$totalIncome")


}


fun main() {
    println("Enter the number of rows:")
    val nRows = readln().toInt()
    println("Enter the number of seats in each row:")
    val nSeatsEachRow = readln().toInt()
    var room = buildCinema(nRows, nSeatsEachRow)

    while (true) {
        printMenu()
        when(readln().toInt()) {
            1 -> {
                printCinema(room)
            }
            2 -> {
                room = buyTicket(room)
            }
            3 -> {
                printStats(room)
            }
            0 -> {
                break
            }
        }
    }

}
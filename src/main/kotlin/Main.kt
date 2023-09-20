fun main() {
    val wordLength = 4
    val maxAttemptsCount = 3
    val secretExample = "ACEB"
    val alphabet = "ABCDEFGH"

    println(getGameRules(wordLength, maxAttemptsCount, secretExample, alphabet))

    playGame(
        generateSecret(
            wordLength,
            alphabet
        ),
        wordLength,
        maxAttemptsCount,
        alphabet,
    )
}

fun getGameRules(
    wordLength: Int,
    maxAttemptsCount: Int,
    secretExample: String,
    alphabet: String,
) = """
        Welcome to the game! 

        Two people play this game: one chooses a word (a sequence of letters), the other guesses it.
        In this version, the computer chooses the word: a sequence of $wordLength letters (for example, $secretExample).
        The user has several attempts to guess it (the max number is $maxAttemptsCount).
        For each attempt, the number of complete matches (letter and position) and partial matches (letter only) is reported. 
        The possible symbols in the word: $alphabet.
        
        For example, with $secretExample as the hidden word, the BCDF guess will give 1 full match (C) and 1 partial match (B).
    """.trimIndent()

fun generateSecret(
    wordLength: Int,
    alphabet: String,
) = List(wordLength) { alphabet.random() }.joinToString("")

fun countPartialMatches(
    secret: String,
    guess: String,
): Int {
    val secretMatch = secret.filterIndexed { index, value ->
        value != guess[index] && guess.indexOf(value) != -1
    }

    val guestMatch = guess.filterIndexed { index, value ->
        value != secret[index] && secret.indexOf(value) != -1
    }

    return minOf(secretMatch.length, guestMatch.length)
}

fun countExactMatches(
    secret: String,
    guess: String,
) = secret.filterIndexed { index, value ->
    guess[index] == value
}.length

fun playGame(
    secret: String,
    wordLength: Int,
    maxAttemptsCount: Int,
    alphabet: String,
) {
    var attempts = 0
    var complete = false

    do {
        println(
            """
            Please input your guess. It should be of length $wordLength,
            and each symbol should be from the alphabet: $alphabet.
            """.trimIndent()
        )

        val guess = readln()

        if (isCorrectInput(guess, wordLength, alphabet)) {
            complete = isComplete(secret, guess)
            attempts++
        }

        printRoundResults(secret, guess)
    } while (!complete && attempts <= maxAttemptsCount)

    val result = when {
        isWin(complete, attempts, maxAttemptsCount) ->
            "Congratulations! You guessed it!"

        isLoss(complete, attempts, maxAttemptsCount) ->
            "Sorry, you lost! :( My word is $secret"

        else -> TODO()
    }

    println(result)
}

fun isComplete(
    secret: String,
    guess: String,
) = secret == guess

fun isWin(
    complete: Boolean,
    attempts: Int,
    maxAttemptsCount: Int,
) = complete && attempts <= maxAttemptsCount

fun isLoss(
    complete: Boolean,
    attempts: Int,
    maxAttemptsCount: Int,
) = !complete && attempts > maxAttemptsCount

fun printRoundResults(
    secret: String,
    guess: String,
) {
    val exactMatches = countExactMatches(secret, guess)
    val partialMatches = countPartialMatches(secret, guess)

    println("Your guess has $exactMatches full matches and $partialMatches partial matches.")
}

fun isCorrectInput(
    userInput: String,
    wordLength: Int,
    alphabet: String,
) = when {
    userInput.length != wordLength -> {
        println("The length of your guess should be $wordLength characters! Try again!")
        false
    }

    userInput.filterNot { it in alphabet }.isNotEmpty() -> {
        println("All symbols in your guess should be the $alphabet alphabet characters! Try again!")
        false
    }

    else -> true
}
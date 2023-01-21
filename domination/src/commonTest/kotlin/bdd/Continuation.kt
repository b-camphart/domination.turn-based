package bdd

sealed interface Continuation<Scope, T> {

    @BddKeyword
    suspend fun <R> And(def: Scope.(T) -> R): Continuation<Scope, R>

    @BddKeyword
    suspend fun <R> But(def: Scope.(T) -> R): Continuation<Scope, R>

}
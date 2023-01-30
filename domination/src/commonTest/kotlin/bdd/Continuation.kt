package bdd

sealed interface Continuation<Scope, T> {

    @BddKeyword
    suspend fun <R> And(def: suspend Scope.(T) -> R): Continuation<Scope, R>

    @BddKeyword
    suspend fun <R> But(def: suspend Scope.(T) -> R): Continuation<Scope, R>

}
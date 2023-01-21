package bdd

@BddKeyword
suspend fun <T> Then(def: suspend ThenScope.() -> T): ThenCont<T> {
    return ThenCont(ThenScope, ThenScope.def())
}

object ThenScope

class ThenCont<T>(private val scope: ThenScope, private val value: T) : Continuation<ThenScope, T> {

    @BddKeyword
    override suspend fun <R> And(def: ThenScope.(T) -> R): ThenCont<R> {
        return ThenCont(scope, scope.def(value))
    }

    @BddKeyword
    override suspend fun <R> But(def: ThenScope.(T) -> R): ThenCont<R> {
        return ThenCont(scope, scope.def(value))
    }

}
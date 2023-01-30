package bdd

@BddKeyword
suspend fun <T> Given(def: suspend GivenScope.() -> T): GivenCont<T> {
    return GivenCont(GivenScope, GivenScope.def())
}

object GivenScope

class GivenCont<T>(private val scope: GivenScope, private val value: T) : Continuation<GivenScope, T> {

    @BddKeyword
    override suspend fun <R> And(def: suspend GivenScope.(T) -> R): GivenCont<R> {
        return GivenCont(scope, scope.def(value))
    }

    @BddKeyword
    override suspend fun <R> But(def: suspend GivenScope.(T) -> R): GivenCont<R> {
        return GivenCont(scope, scope.def(value))
    }

}
package ru.mobileup.samples.features.optimistic_update.data

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.single.PhysicalReplica
import me.aartikov.replica.single.behaviour.ReplicaBehaviour

// TODO: это временная реализация механизма ReplicaAction для реплики. Позже он появится в самой библиотеке.

interface ReplicaAction

// TODO: сделано через глобальный объект, потому что без изменения библиотеки нельзя добавить поле в ReplicaClient
private object ReplicaClientFields {

    private val _actions = MutableSharedFlow<ReplicaAction>(
        extraBufferCapacity = 1000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val actions: Flow<ReplicaAction> get() = _actions.asSharedFlow()

    fun sendAction(action: ReplicaAction) {
        _actions.tryEmit(action)
    }
}

fun ReplicaClient.sendAction(action: ReplicaAction) {
    ReplicaClientFields.sendAction(action)
}

class DoOnAction<T : Any, A : ReplicaAction>(
    private val mapNotNull: (ReplicaAction) -> A?,
    private val handler: suspend PhysicalReplica<T>.(action: A) -> Unit
) : ReplicaBehaviour<T> {

    override fun setup(replica: PhysicalReplica<T>) {
        ReplicaClientFields.actions
            .mapNotNull(mapNotNull)
            .onEach { action ->
                replica.handler(action)
            }
            .launchIn(replica.coroutineScope)
    }
}

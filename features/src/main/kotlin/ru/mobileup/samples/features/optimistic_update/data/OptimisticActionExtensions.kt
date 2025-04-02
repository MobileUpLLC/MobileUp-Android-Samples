package ru.mobileup.samples.features.optimistic_update.data

import me.aartikov.replica.client.ReplicaClient
import me.aartikov.replica.common.OptimisticUpdate
import me.aartikov.replica.common.performOptimisticUpdate
import me.aartikov.replica.single.PhysicalReplica
import java.util.UUID

enum class OptimisticUpdateSignal {
    Begin,
    Commit,
    Rollback,
}

data class OptimisticReplicaAction<A : ReplicaAction>(
    val sourceAction: A,
    val signal: OptimisticUpdateSignal,
    val key: Any?
) : ReplicaAction {

    private class OptimisticUpdateHolder<T : Any>(
        val key: Any?,
        val impl: OptimisticUpdate<T>
    ) : OptimisticUpdate<T> by impl {

        override fun hashCode() = key.hashCode()

        override fun equals(other: Any?): Boolean {
            return other is OptimisticUpdateHolder<*>
                    && other.key == key
        }
    }

    suspend fun <T : Any> applyOptimisticUpdate(
        replica: PhysicalReplica<T>,
        update: OptimisticUpdate<T>
    ) {
        val holder = OptimisticUpdateHolder(key, update)

        when (signal) {
            OptimisticUpdateSignal.Begin -> replica.beginOptimisticUpdate(holder)
            OptimisticUpdateSignal.Commit -> replica.commitOptimisticUpdate(holder)
            OptimisticUpdateSignal.Rollback -> replica.rollbackOptimisticUpdate(holder)
        }
    }
}

inline fun <reified R : ReplicaAction> ReplicaAction.checkIsOptimistic(): OptimisticReplicaAction<R>? {
    if (this !is OptimisticReplicaAction<*>) return null

    val source = sourceAction as? R ?: return null

    return OptimisticReplicaAction(source, signal, key)
}

suspend inline fun ReplicaClient.sendOptimisticAction(
    action: ReplicaAction,
    noinline onSuccess: (suspend () -> Unit)? = null,
    noinline onError: (suspend (Exception) -> Unit)? = null,
    noinline onCanceled: (suspend () -> Unit)? = null,
    noinline onFinished: (suspend () -> Unit)? = null,
    block: () -> Unit
) {
    val key = UUID.randomUUID()
    val sendOptimistic: (OptimisticUpdateSignal) -> Unit = {
        sendAction(
            OptimisticReplicaAction(
                sourceAction = action,
                signal = it,
                key = key
            )
        )
    }

    performOptimisticUpdate(
        begin = { sendOptimistic(OptimisticUpdateSignal.Begin) },
        rollback = { sendOptimistic(OptimisticUpdateSignal.Rollback) },
        commit = { sendOptimistic(OptimisticUpdateSignal.Commit) },
        onSuccess = onSuccess,
        onError = onError,
        onCanceled = onCanceled,
        onFinished = onFinished,
        block = block
    )
}

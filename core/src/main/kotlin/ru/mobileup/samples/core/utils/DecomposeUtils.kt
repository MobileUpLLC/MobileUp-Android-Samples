package ru.mobileup.samples.core.utils

import androidx.compose.runtime.State
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigator
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import me.aartikov.replica.decompose.coroutineScope

/**
 * Creates a [ChildStack] with a single active component. Should be used to create a stack for Jetpack Compose preview.
 */
fun <T : Any> createFakeChildStack(instance: T): ChildStack<*, T> {
    return ChildStack(
        configuration = "<fake>",
        instance = instance
    )
}

fun <T : Any> createFakeChildStackStateFlow(instance: T): StateFlow<ChildStack<*, T>> {
    return MutableStateFlow(createFakeChildStack(instance))
}

/**
 * Creates a [ChildSlot] with given [configuration] and [instance]. Should be used to create a slot for Jetpack Compose preview.
 */
fun <C : Any, T : Any> createFakeChildSlot(
    configuration: C,
    instance: T,
): StateFlow<ChildSlot<C, T>> {
    return MutableStateFlow(
        ChildSlot(
            Child.Created(
                configuration = configuration,
                instance = instance
            )
        )
    )
}

/**
 * Converts [Value] from Decompose to [State] from Jetpack Compose.
 */
fun <T : Any> Value<T>.toStateFlow(lifecycle: Lifecycle): StateFlow<T> {
    val state: MutableStateFlow<T> = MutableStateFlow(this.value)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        val observer = { value: T -> state.value = value }
        val cancellation = subscribe(observer)
        lifecycle.doOnDestroy {
            cancellation.cancel()
        }
    }

    return state
}

/**
 * Creates a coroutine scope tied to Decompose lifecycle. A scope is canceled when a component is destroyed.
 */
val ComponentContext.componentScope: CoroutineScope
    get() {
        val scope = (instanceKeeper.get(ComponentScopeKey) as? CoroutineScopeWrapper)?.scope
        if (scope != null) return scope

        val newScope = lifecycle.coroutineScope()
        instanceKeeper.put(ComponentScopeKey, CoroutineScopeWrapper(newScope))
        return newScope
    }

private object ComponentScopeKey

private class CoroutineScopeWrapper(val scope: CoroutineScope) : InstanceKeeper.Instance {
    override fun onDestroy() {
        // nothing
    }
}

/**
 * A helper function to save and restore component state.
 */
inline fun <T : Any> StateKeeperOwner.persistent(
    key: String = "PersistentState",
    serializer: KSerializer<T>,
    noinline save: () -> T,
    restore: (T) -> Unit,
) {
    stateKeeper.consume(key, serializer)?.run(restore)
    stateKeeper.register(key, serializer, save)
}

/**
 * Pushes a new configuration onto the navigation stack safely.
 *
 * This method ensures that the given configuration is removed from the stack first (if it exists),
 * and then re-added to ensure it appears at the top of the stack. This avoids duplicate entries
 * for the same configuration and ensures it is effectively "re-pushed" to the stack.
 *
 * @param configuration the configuration to push onto the stack.
 * @param onComplete called when the navigation is finished (either synchronously or asynchronously).
 */
fun <C : Any> StackNavigator<C>.safePush(configuration: C, onComplete: () -> Unit = {}) {
    navigate(
        transformer = { it - configuration + configuration },
        onComplete = { _, _ -> onComplete() }
    )
}

/**
 * Retrieves the first child of type [C] from the child stack.
 * It will return `null` if no matching child is found.
 */
inline fun <reified C : Any> ChildStack<*, *>.getChild(): C? =
    items.map { it.instance }.filterIsInstance<C>().lastOrNull()

/**
 * A component that supports predictive back gesture handling.
 */
interface PredictiveBackComponent : BackHandlerOwner {

    fun onBackClick()
}

/**
 * A convenient wrapper around [com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation]
 * for components implementing [PredictiveBackComponent].
 */
@OptIn(ExperimentalDecomposeApi::class)
fun PredictiveBackComponent.predictiveBackAnimation(
    fallBackAnimation: StackAnimation<Any, Any> = stackAnimation(fade() + slide()),
    selector: (
        initialBackEvent: BackEvent,
        exitChild: Child.Created<Any, Any>,
        enterChild: Child.Created<Any, Any>,
    ) -> PredictiveBackAnimatable = { backEvent, _, _ ->
        androidPredictiveBackAnimatable(backEvent)
    },
) = predictiveBackAnimation(
    backHandler = backHandler,
    fallbackAnimation = fallBackAnimation,
    selector = selector,
    onBack = ::onBackClick
)

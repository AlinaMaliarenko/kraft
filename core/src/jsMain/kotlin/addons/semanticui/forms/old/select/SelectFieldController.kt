package de.peekandpoke.kraft.addons.semanticui.forms.old.select

import de.peekandpoke.ultra.semanticui.SemanticTag

internal class SelectFieldController<T>(component: SelectFieldComponent<T>) {

    enum class State {
        Closed,
        Opening,
        Open,
        Closing,
    }

    var state by component.value(State.Closed)

    ////  STATE MODIFICATIONS  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun toggleState() {
        state = when (state) {
            State.Closed -> State.Opening
            State.Closing -> State.Opening

            State.Open -> State.Closing
            State.Opening -> State.Closing
        }
    }

    fun advanceState() {
        state = when (state) {
            State.Closed, State.Open -> state
            State.Closing -> State.Closed
            State.Opening -> State.Open
        }
    }

    fun close() {
        state = when (state) {
            State.Closed, State.Closing -> state
            else -> State.Closing
        }
    }

    ////  RENDERING  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun applyStateOnField(tag: SemanticTag): SemanticTag = when (state) {
        State.Open -> tag.active.visible
        State.Opening -> tag.active.visible
        State.Closing -> tag.visible
        State.Closed -> tag
    }

    fun applyStateOnOptions(tag: SemanticTag): SemanticTag = when (state) {
        State.Open -> tag.transition.visible
        State.Opening -> tag.transition.visible.with("slide down in")
        State.Closing -> tag.transition.visible.with("slide down out")
        State.Closed -> tag.transition.hidden
    }
}

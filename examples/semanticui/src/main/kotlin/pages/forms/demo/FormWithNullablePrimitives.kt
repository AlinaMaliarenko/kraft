package de.peekandpoke.kraft.examples.semanticui.pages.forms.demo

import de.peekandpoke.kraft.addons.forms.formController
import de.peekandpoke.kraft.addons.forms.validation.numbers.greaterThan
import de.peekandpoke.kraft.addons.forms.validation.numbers.lessThan
import de.peekandpoke.kraft.addons.semanticui.forms.UiInputField
import de.peekandpoke.kraft.components.NoProps
import de.peekandpoke.kraft.components.PureComponent
import de.peekandpoke.kraft.components.comp
import de.peekandpoke.kraft.components.onClick
import de.peekandpoke.kraft.examples.semanticui.helpers.invoke
import de.peekandpoke.kraft.examples.semanticui.helpers.renderStateAndDraftTable
import de.peekandpoke.kraft.vdom.VDom
import de.peekandpoke.ultra.semanticui.ui
import kotlinx.html.FlowContent
import kotlinx.html.Tag

@Suppress("FunctionName")
fun Tag.FormWithNullablePrimitives() = comp {
    FormWithNullablePrimitives(it)
}

class FormWithNullablePrimitives(ctx: NoProps) : PureComponent(ctx) {

    //  STATE  //////////////////////////////////////////////////////////////////////////////////////////////////

    data class State(
        val textInput: String? = null,
        val intInput: Int? = null,
        val floatInput: Float? = null,
        val doubleInput: Double? = null,
    )

    private var state by value(State())
    private var draft by value(state)

    private val formCtrl = formController()

    //  IMPL  ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun VDom.render() {

        ui.two.column.grid {
            ui.column {
                ui.form {
                    ui.two.fields {
                        UiInputField.nullable(draft.textInput, { draft = draft.copy(textInput = it) }) {
                            label { +"Text Input" }
                            placeholder("Enter some text")
                        }

                        UiInputField.nullable(draft.intInput, { draft = draft.copy(intInput = it) }) {
                            label { +"Int Input" }
                            placeholder("Enter a number")
                            step(3)

                            accepts(
                                greaterThan(6.0),
                                lessThan(15.0)
                            )
                        }
                    }

                    ui.two.fields {
                        UiInputField.nullable(state.floatInput, { draft = draft.copy(floatInput = it) }) {
                            label { +"Float Input" }
                            placeholder("Enter a number")

                            accepts(
                                greaterThan(5.0),
                                lessThan(20.0)
                            )
                        }

                        UiInputField.nullable(state.doubleInput, { draft = draft.copy(doubleInput = it) }) {
                            label { +"Double Input" }
                            placeholder("Enter a number")
                            step(0.5)

                            accepts(
                                greaterThan(3.0),
                                lessThan(10.0)
                            )
                        }
                    }
                }

                ui.divider {}

                val canSubmit = formCtrl.isValid && draft != state

                ui.button.given(!canSubmit) { disabled }.then {
                    +"Submit"

                    onClick {
                        if (formCtrl.validate() && canSubmit) {
                            state = draft
                        }
                    }
                }
            }

            ui.column {
                renderDataTable()
            }
        }
    }

    private fun FlowContent.renderDataTable() {

        renderStateAndDraftTable(
            state,
            draft,
            listOf(
                State::textInput { it.toString() },
                State::intInput { it.toString() },
                State::floatInput { it.toString() },
                State::doubleInput { it.toString() },
            )
        )
    }
}

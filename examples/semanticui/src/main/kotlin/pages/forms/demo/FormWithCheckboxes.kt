package de.peekandpoke.kraft.examples.semanticui.pages.forms.demo

import de.peekandpoke.kraft.addons.forms.formController
import de.peekandpoke.kraft.addons.semanticui.forms.UiCheckboxField
import de.peekandpoke.kraft.components.NoProps
import de.peekandpoke.kraft.components.PureComponent
import de.peekandpoke.kraft.components.comp
import de.peekandpoke.kraft.components.onClick
import de.peekandpoke.kraft.examples.semanticui.helpers.invoke
import de.peekandpoke.kraft.examples.semanticui.helpers.renderStateAndDraftTable
import de.peekandpoke.kraft.vdom.VDom
import de.peekandpoke.ultra.semanticui.ui
import kotlinx.html.Tag

@Suppress("FunctionName")
fun Tag.FormWithCheckboxes() = comp {
    FormWithCheckboxes(it)
}

class FormWithCheckboxes(ctx: NoProps) : PureComponent(ctx) {

    //  STATE  //////////////////////////////////////////////////////////////////////////////////////////////////

    data class Obj(val x: String)

    data class State(
        val boolean: Boolean = false,
        val string: String = "yes",
        val obj: Obj = Obj("yes"),
    )

    private var state by value(State())
    private var draft by value(state)

    private val formCtrl = formController()

    //  IMPL  ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun VDom.render() {

        ui.two.column.grid {
            ui.column {
                ui.form {
                    ui.three.fields {
                        UiCheckboxField(draft.boolean, { draft = draft.copy(boolean = it) }) {
                            label { +State::boolean.name }
                        }

                        UiCheckboxField(
                            value = draft.string,
                            off = "no",
                            on = "yes",
                            onChange = { draft = draft.copy(string = it) },
                        ) {
                            label { +State::string.name }
                            toggle()
                        }

                        UiCheckboxField(
                            value = draft.obj,
                            off = Obj("no"),
                            on = Obj("yes"),
                            onChange = { draft = draft.copy(obj = it) },
                        ) {
                            label { +State::obj.name }
                            slider()
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
                renderStateAndDraftTable(
                    state,
                    draft,
                    listOf(
                        State::boolean { it.toString() },
                        State::string { it },
                        State::obj { it.toString() },
                    )
                )
            }
        }
    }
}

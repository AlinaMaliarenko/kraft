package de.peekandpoke.kraft.examples.semanticui.pages.forms.demo

import de.peekandpoke.kraft.addons.forms.formController
import de.peekandpoke.kraft.addons.semanticui.forms.UiDateTimeField
import de.peekandpoke.kraft.components.NoProps
import de.peekandpoke.kraft.components.PureComponent
import de.peekandpoke.kraft.components.comp
import de.peekandpoke.kraft.components.onClick
import de.peekandpoke.kraft.examples.semanticui.helpers.invoke
import de.peekandpoke.kraft.examples.semanticui.helpers.renderStateAndDraftTable
import de.peekandpoke.kraft.vdom.VDom
import de.peekandpoke.ultra.common.datetime.Kronos
import de.peekandpoke.ultra.common.datetime.MpLocalDateTime
import de.peekandpoke.ultra.common.datetime.MpTimezone
import de.peekandpoke.ultra.common.datetime.MpZonedDateTime
import de.peekandpoke.ultra.semanticui.ui
import kotlinx.html.Tag

@Suppress("FunctionName")
fun Tag.FormWithDateTimes() = comp {
    FormWithDateTimes(it)
}

class FormWithDateTimes(ctx: NoProps) : PureComponent(ctx) {

    //  STATE  //////////////////////////////////////////////////////////////////////////////////////////////////

    data class State(
        val localDateTime: MpLocalDateTime =
            Kronos.systemUtc.localDateTimeNow(),
        val zonedDateTime: MpZonedDateTime =
            Kronos.systemUtc.zonedDateTimeNow(MpTimezone.of("Europe/Berlin")).atStartOfHour(),
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
                        UiDateTimeField(draft.localDateTime, { draft = draft.copy(localDateTime = it) }) {
                            label { +State::localDateTime.name }
                        }

                        UiDateTimeField(draft.zonedDateTime, { draft = draft.copy(zonedDateTime = it) }) {
                            label { +State::zonedDateTime.name }
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
                        State::localDateTime { it.toIsoString() },
                        State::zonedDateTime { it.toIsoString() },
                    )
                )
            }
        }
    }
}

package de.peekandpoke.kraft.addons.semanticui.forms

import de.peekandpoke.kraft.addons.forms.FieldOptions
import de.peekandpoke.kraft.addons.forms.GenericFormField
import de.peekandpoke.kraft.addons.forms.KraftFormsDsl
import de.peekandpoke.kraft.addons.semanticui.forms.UiCheckBoxComponent.Options
import de.peekandpoke.kraft.components.Ctx
import de.peekandpoke.kraft.components.comp
import de.peekandpoke.kraft.components.onChange
import de.peekandpoke.kraft.vdom.VDom
import de.peekandpoke.ultra.semanticui.ui
import kotlinx.html.InputType
import kotlinx.html.Tag
import kotlinx.html.div
import kotlinx.html.input
import org.w3c.dom.HTMLInputElement
import kotlin.reflect.KMutableProperty0

@KraftFormsDsl
val Tag.UiCheckboxField get() = UiCheckboxFieldRenderer(this)

@Suppress("FunctionName")
fun <T> Tag.UiCheckboxField(
    value: T,
    onChange: (T) -> Unit,
    off: T,
    on: T,
    builder: Options<T>.() -> Unit = {},
) = comp(
    UiCheckBoxComponent.Props(
        value = value,
        onChange = onChange,
        off = off,
        on = on,
        options = Options<T>().apply(builder),
    )
) {
    UiCheckBoxComponent(it)
}

class UiCheckBoxComponent<T, P : UiCheckBoxComponent.Props<T>>(ctx: Ctx<P>) :
    GenericFormField<T, Options<T>, P>(ctx) {

    class Options<T> : FieldOptions.Base<T>(), SemanticOptions<T>, SemanticOptions.Checkbox<T>

    data class Props<X>(
        override val value: X,
        override val onChange: (X) -> Unit,
        override val options: Options<X>,
        val on: X,
        val off: X,
    ) : GenericFormField.Props<X, Options<X>>

    override fun VDom.render() {

        ui.with(options.appear.getOrDefault { this }).given(hasErrors) { error }.field {
            div {
                ui.with(options.style.getOrDefault { this }).checkbox {
                    input {
                        onChange {
                            when ((it.target as HTMLInputElement).checked) {
                                true -> setValue(props.on)
                                false -> setValue(props.off)
                            }
                        }
                        type = InputType.checkBox
                        checked = currentValue == props.on
                    }

                    renderLabel {
                        setValue(
                            when (currentValue) {
                                props.on -> props.off
                                else -> props.on
                            }
                        )
                    }
                }
            }

            renderErrors(this)
        }
    }
}

class UiCheckboxFieldRenderer(private val tag: Tag) {
    /**
     * Renders the field for a Boolean
     */
    @KraftFormsDsl
    operator fun invoke(
        prop: KMutableProperty0<Boolean>,
        builder: Options<Boolean>.() -> Unit = {},
    ) = invoke(prop(), prop::set, builder)

    /**
     * Renders the field for a Boolean
     */
    @KraftFormsDsl
    operator fun invoke(
        value: Boolean,
        onChange: (Boolean) -> Unit,
        builder: Options<Boolean>.() -> Unit = {},
    ) = invoke(value = value, onChange = onChange, off = false, on = true, builder = builder)

    /**
     * Renders the field for an the type [T]
     */
    @KraftFormsDsl
    operator fun <T> invoke(
        value: T,
        onChange: (T) -> Unit,
        on: T,
        off: T,
        builder: Options<T>.() -> Unit = {},
    ) = tag.UiCheckboxField(value = value, onChange = onChange, off = off, on = on, builder = builder)
}

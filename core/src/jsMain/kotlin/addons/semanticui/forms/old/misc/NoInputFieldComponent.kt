package de.peekandpoke.kraft.addons.semanticui.forms.old.misc

import de.peekandpoke.kraft.addons.forms.FormFieldComponent
import de.peekandpoke.kraft.addons.forms.validation.Rule
import de.peekandpoke.kraft.addons.semanticui.forms.renderErrors
import de.peekandpoke.kraft.components.Ctx
import de.peekandpoke.kraft.vdom.VDom
import de.peekandpoke.ultra.semanticui.ui

open class NoInputFieldComponent<T>(ctx: Ctx<Props<T>>) : FormFieldComponent<T, NoInputFieldComponent.Props<T>>(ctx) {

    data class Props<P>(
        val config: Config<P>,
    ) : FormFieldComponent.Props<P> {
        override val initialValue: P = config.value
        override val fromStr: (String) -> P get() = { initialValue }
        override val onChange: (P) -> Unit get() = {}
        override val rules: List<Rule<P>> get() = config.rules
        val render: VDom.(field: NoInputFieldComponent<P>) -> Unit = config.render
    }

    @Suppress("FunctionName")
    class Config<T>(
        var value: T,
    ) {
        val asProps get() = Props(config = this)

        /** The validation rules */
        val rules: MutableList<Rule<T>> = mutableListOf()

        /** The rendering function */
        var render: VDom.(field: NoInputFieldComponent<T>) -> Unit = { field ->
            ui.given(field.hasErrors) { error }.field {
                renderErrors(field, this)
            }
        }
            private set

        /** Adds a validation rule */
        fun accepts(rule: Rule<T>, vararg rules: Rule<T>) = apply {
            this.rules.add(rule)
            this.rules.addAll(rules)
        }

        /** Set the rendering function */
        fun render(block: VDom.(field: NoInputFieldComponent<T>) -> Unit) {
            render = block
        }
    }

    override fun onNextProps(newProps: Props<T>, previousProps: Props<T>) {

        super.onNextProps(newProps, previousProps)

        if (newProps.initialValue != previousProps.initialValue) {
            validate()
        }
    }

    override fun VDom.render() {
        props.render(
            this@render,
            this@NoInputFieldComponent,
        )
    }
}

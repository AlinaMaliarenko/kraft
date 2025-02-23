package de.peekandpoke.kraft.examples.helloworld

import de.peekandpoke.kraft.components.Component
import de.peekandpoke.kraft.components.Ctx
import de.peekandpoke.kraft.components.comp
import de.peekandpoke.kraft.components.onClick
import de.peekandpoke.kraft.vdom.VDom
import kotlinx.html.Tag
import kotlinx.html.button
import kotlinx.html.div

@Suppress("FunctionName")
fun Tag.CounterComponent(
    start: Int,
) = comp(
    CounterComponent.Props(start = start)
) {
    CounterComponent(it)
}

class CounterComponent(ctx: Ctx<Props>) : Component<CounterComponent.Props>(ctx) {

    //  PROPS  //////////////////////////////////////////////////////////////////////////////////////////////////

    data class Props(
        val start: Int,
    )

    //  STATE  //////////////////////////////////////////////////////////////////////////////////////////////////

    private var counter by value(props.start)

    //  IMPL  ///////////////////////////////////////////////////////////////////////////////////////////////////

    override fun VDom.render() {
        div {
            div { +"Value: $counter" }

            div {
                button {
                    onClick { counter-- }
                    +"Minus"
                }
                button {
                    onClick { counter++ }
                    +"Plus"
                }
            }
        }
    }
}

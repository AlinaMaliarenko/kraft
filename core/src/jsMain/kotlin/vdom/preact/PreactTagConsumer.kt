package de.peekandpoke.kraft.vdom.preact

import de.peekandpoke.kraft.components.Component
import de.peekandpoke.kraft.components.ComponentRef
import de.peekandpoke.kraft.components.Ctx
import de.peekandpoke.kraft.vdom.VDomElement
import de.peekandpoke.kraft.vdom.VDomTagConsumer
import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.Unsafe
import org.w3c.dom.events.Event
import kotlin.reflect.KClass

/**
 * Implementation of [VDomTagConsumer] for the mithril engine
 *
 * see: https://mithril.js.org
 */
internal class PreactTagConsumer(
    private val engine: PreactVDomEngine,
    private val host: Component<*>?,
) : VDomTagConsumer {

    /** The root element */
    private val root = PreactElements.RootElement()

    /** The stack of elements we are visiting */
    private val stack = mutableListOf<VDomElement>(
        root
    )

    override fun <P, C : Component<P>> onComponent(
        props: P,
        creatorFn: (Ctx<P>) -> C,
        cls: KClass<C>,
    ): ComponentRef<C> {
//        console.log("onComponent", params, component)

        val element = PreactElements.ComponentElement(
            Ctx(engine, host, props),
            creatorFn,
            cls,
        )

        stack.last().appendChild(element)

        return element.ref
    }

    override fun finalize(): VDomElement {
//        console.log("finalize")

        return stack.first()
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
//        console.log("onTagAttributeChange", tag, attribute, value)
    }

    override fun onTagComment(content: CharSequence) {
        console.log("onTagComment", content)
    }

    override fun onTagContent(content: CharSequence) {
//        console.log("onTagContent", content)

        stack.last().appendChild(
            PreactElements.ContentElement(content)
        )
    }

    override fun onTagContentEntity(entity: Entities) {
//        console.log("onTagContentEntity", entity)
    }

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        // see https://github.com/preactjs/preact/issues/844
        stack.last().appendChild(
            PreactElements.UnsafeContentElement().apply(block)
        )
    }

    override fun onTagEnd(tag: Tag) {
//        console.log("onTagEnd", tag)

        stack.removeLast()
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
//        console.log("onTagEvent", tag, event, value)

        stack.last().addEvent(event, value)
    }

    override fun onTagStart(tag: Tag) {
//        console.log("onTagStart", tag)

        val element = PreactElements.TagElement(
            tag = tag
        )

        stack.last().appendChild(element)

        stack.add(element)
    }
}

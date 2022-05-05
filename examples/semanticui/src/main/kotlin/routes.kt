package de.peekandpoke.kraft.examples.semanticui

import de.peekandpoke.kraft.addons.routing.RouterBuilder
import de.peekandpoke.kraft.addons.routing.Static
import de.peekandpoke.kraft.examples.semanticui.pages.elements.button.ButtonPage
import de.peekandpoke.kraft.examples.semanticui.pages.elements.divider.DividerPage
import de.peekandpoke.kraft.examples.semanticui.pages.elements.header.HeaderPage
import de.peekandpoke.kraft.examples.semanticui.pages.elements.icon.IconPage
import de.peekandpoke.kraft.examples.semanticui.pages.forms.demo.FormDemosPage
import de.peekandpoke.kraft.examples.semanticui.pages.home.HomePage

class Routes {
    val home = Static("/")

    val elementsButton = Static("/elements/button")
    val elementsDivider = Static("/elements/divider")
    val elementsHeader = Static("/elements/header")
    val elementsIcon = Static("/elements/icon")

    val formDemos = Static("/form/demos")
}

fun RouterBuilder.mount(routes: Routes) {
    mount(routes.home) { HomePage() }

    mount(routes.elementsButton) { ButtonPage() }
    mount(routes.elementsDivider) { DividerPage() }
    mount(routes.elementsHeader) { HeaderPage() }
    mount(routes.elementsIcon) { IconPage() }

    mount(routes.formDemos) { FormDemosPage() }
}

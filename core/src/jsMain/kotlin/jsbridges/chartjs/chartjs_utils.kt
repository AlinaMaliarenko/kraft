package de.peekandpoke.kraft.jsbridges.chartjs

object ChartJsUtils {

    private var alreadyRegistered = false

    fun registerAllModules() {
        if (!alreadyRegistered) {
            alreadyRegistered = true
            console.log("Chart.register", Chart.Companion::register)
            console.log("registerables", registerables)
            Chart.register(*registerables)
        }
    }

    fun numberLabels(count: Int): Array<String> = numberLabels(1..count)

    fun numberLabels(range: IntRange) = range.map { it.toString() }.toTypedArray()
}

package de.peekandpoke.kraft.addons.routing

import de.peekandpoke.kraft.utils.decodeURIComponent
import de.peekandpoke.kraft.utils.encodeURIComponent

/**
 * Common Route representation
 */
interface Route {

    /**
     * Represents a route match
     */
    data class Match(
        val route: Route,
        /** Route params extract from url placeholder */
        val routeParams: Map<String, String>,
        /** Query params after like ?p=1&t=2 */
        val queryParams: Map<String, String>,
    ) {
        companion object {
            val default = Match(route = Route.default, routeParams = emptyMap(), queryParams = emptyMap())
        }

        /** Shortcut to the pattern of the [route] */
        val pattern = route.pattern

        /** Combination of route and query params */
        val allParams = routeParams.plus(queryParams)

        /** Gets the route param with the given [key] / name */
        operator fun get(key: String) = param(key)

        /** Gets the route param with the given [name] or defaults to [default] */
        fun param(name: String, default: String = ""): String = routeParams[name] ?: default

        /** Returns a new instance with the query params set */
        fun withQueryParams(params: Map<String, String?>): Match {
            @Suppress("UNCHECKED_CAST")
            return copy(
                queryParams = params.filterValues { v -> v != null } as Map<String, String>
            )
        }

        /** Returns a new instance with the query params set */
        fun withQueryParams(vararg params: Pair<String, String?>): Match = withQueryParams(params.toMap())
    }

    companion object {
        /** Basic default route */
        val default = Static("")
    }

    /** The pattern of the route */
    val pattern: String

    /**
     * Matches the given [uri] against the [pattern] of the route.
     *
     * Returns an instance of [Match] or null if the pattern did not match.
     */
    fun match(uri: String): Match?

    /**
     * Internal helper for building uris
     */
    fun String.replacePlaceholder(placeholder: String, value: String) =
        replace("{$placeholder}", encodeURIComponent(value))

    /**
     * Builds a uri with the given [routeParams]
     */
    fun buildUri(vararg routeParams: String): String

    /**
     * Builds a uri with the given [routeParams] and [queryParams]
     */
    fun buildUri(routeParams: Map<String, String>, queryParams: Map<String, String>): String
}

/**
 * A parameterized route with one route parameter
 */
abstract class RouteBase(final override val pattern: String, numParams: Int) : Route {

    companion object {
        @Suppress("RegExpRedundantEscape")
        val placeholderRegex = "\\{([^}]*)\\}".toRegex()
        const val extractRegexPattern = "([^/]*)"
    }

    /**
     * We extract all placeholders from the pattern
     */
    private val placeholders = placeholderRegex
        .findAll(pattern)
        .map { it.groupValues[1] }
        .toList()

    /**
     * We construct a regex for matching the whole pattern with param placeholders
     */
    private val matchingRegex =
        placeholders.fold(pattern) { acc, placeholder ->
            acc.replace("{$placeholder}", extractRegexPattern)
        }.replace("/", "\\/").toRegex()


    init {
        // Sanity check
        if (numParams != placeholders.size) {
            error("The route '$pattern' has [${placeholders.size}] route-params but should have [$numParams]")
        }
    }

    /**
     * Tries to match the given [uri] against the [pattern] of the route.
     */
    override fun match(uri: String): Route.Match? {

        val (route, query) = when (val queryIdx = uri.indexOf("?")) {
            -1 -> arrayOf(uri, "")
            else -> arrayOf(uri.substring(0, queryIdx), uri.substring(queryIdx + 1))
        }

        val match = matchingRegex.matchEntire(route) ?: return null

//        console.log(placeholders)
//        console.log(match)
//        console.log(match.groupValues)

        val routeParams = placeholders
            .zip(match.groupValues.drop(1).map(::decodeURIComponent))
            .toMap()

        val queryParams = when (query.isEmpty()) {
            true -> emptyMap()
            else -> query.split("&").associate {
                when (val equalsIdx = it.indexOf("=")) {
                    -1 -> Pair(it, "")
                    else -> Pair(
                        it.substring(0, equalsIdx),
                        decodeURIComponent(it.substring(equalsIdx + 1)),
                    )
                }
            }
        }

        return Route.Match(route = this, routeParams = routeParams, queryParams = queryParams)
    }

    /**
     * Builds a uri with the given [routeParams]
     */
    override fun buildUri(vararg routeParams: String): String =
        routeParams.foldIndexed("#$pattern") { idx, pattern, param ->
            pattern.replacePlaceholder(placeholders[idx], param)
        }

    /**
     * Builds a uri with the given [routeParams] and [queryParams]
     */
    override fun buildUri(routeParams: Map<String, String>, queryParams: Map<String, String>): String {

        val withoutQuery = routeParams.entries.fold("#$pattern") { pattern, entry ->
            pattern.replacePlaceholder(entry.key, entry.value)
        }

        return when (queryParams.isEmpty()) {
            true -> withoutQuery
            else -> "$withoutQuery?" + queryParams
                .map { "${it.key}=${encodeURIComponent(it.value)}" }.joinToString("&")
        }
    }
}


/**
 * A static route is a route that does not have any route parameters
 */
open class Static(pattern: String) : RouteBase(pattern, 0) {
    operator fun invoke() = buildUri()
}

/**
 * A parameterized route with one route parameter
 */
open class Route1(pattern: String) : RouteBase(pattern, 1) {
    fun build(p1: String) = buildUri(p1)
}

/**
 * A parameterized route with two route parameter
 */
open class Route2(pattern: String) : RouteBase(pattern, 2) {
    fun build(p1: String, p2: String) = buildUri(p1, p2)
}

/**
 * A parameterized route with two route parameter
 */
open class Route3(pattern: String) : RouteBase(pattern, 3) {
    fun build(p1: String, p2: String, p3: String) = buildUri(p1, p2, p3)
}

/**
 * A parameterized route with two route parameter
 */
open class Route4(pattern: String) : RouteBase(pattern, 4) {
    fun build(p1: String, p2: String, p3: String, p4: String) = buildUri(p1, p2, p3, p4)
}

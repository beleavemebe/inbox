package io.github.beleavemebe.inbox.core.di

import androidx.fragment.app.Fragment
import io.github.beleavemebe.inbox.core.utils.allParents

inline fun <reified D : Dependencies> Fragment.findDependencies(): D =
    findDependenciesByClass(D::class.java)

@Suppress("unchecked_cast")
fun <D : Dependencies> Fragment.findDependenciesByClass(
    depClass: Class<D>
): D {
    val parents = allParents.mapNotNull { it as? HasDependencies }
    return parents
        .mapNotNull { it.depsMap[depClass] }
        .firstOrNull() as? D
        ?: error("No $depClass in $parents")
}

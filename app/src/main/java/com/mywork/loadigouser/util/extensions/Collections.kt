package com.mywork.loadigouser.util.extensions

fun List<Any>.equalsList( list2: List<Any>): Boolean {

    if (this.size != list2.size)
        return false

    val pairList = this.zip(list2)

    return pairList.all { (elt1, elt2) ->
        elt1 == elt2
    }
}
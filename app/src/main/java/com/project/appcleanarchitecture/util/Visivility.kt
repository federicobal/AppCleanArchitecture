package com.project.appcleanarchitecture.util

import android.view.View

/**
 * Created by fbal on 19/4/2022.
 */
fun View.hide() {
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

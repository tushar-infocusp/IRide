package com.example.iride.data

import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
actual fun showToast(message: String) {

    dispatch_async(dispatch_get_main_queue()) {

        val alert = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "OK",
                style = UIAlertActionStyleDefault,
                handler = null
            )
        )

        val rootVC = UIApplication.sharedApplication
            .keyWindow
            ?.rootViewController

        rootVC?.presentViewController(
            alert,
            animated = true,
            completion = null
        )
    }
}
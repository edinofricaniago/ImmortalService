# ImmortalService

This is a repository to experiment with a service that lives forever.
This service will try to restart in all case when it stopped except when the user click "force close" in the applications menu.


## Scenarios where service needs restart

1. Uncaught exception occures
2. Your service stopped / killed by the system
3. The device has been rebooted

### Uncaught exception occures

If your application crashes it is usually better to let it fail. However if you would like to restart your 
service when an uncaught exception happens than you can register your own [UncaughtExceptionHandler](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.UncaughtExceptionHandler.html) using the [Thread.setUncaughtExceptionHandler](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#setUncaughtExceptionHandler) method.

### Your service stopped / killed by the system

Android respects your service as much as you ask and as it can. However there are situations 
where your service stopped / killed and you would like to restart it.

You can monitor your service as long as your app lives by registering a BroadcastReceiver for [ACTION_TIME_TICK](https://developer.android.com/reference/android/content/Intent.html#ACTION_TIME_TICK). However this will be registered as a [Context-registered receiver](https://developer.android.com/guide/components/broadcasts#context-registered-receivers) so according to the documentation: "Context-registered receivers receive broadcasts as long as their registering context is valid.". 

A better approach is to use the [AlarmManager](https://developer.android.com/reference/android/app/AlarmManager) to schedule a PendingIntent that starts your service and reschedule the intent in your Service if this was already started.

### The device has been rebooted

You can register a [Manifest-declared receiver](https://developer.android.com/guide/components/broadcasts#manifest-declared-receivers) that will receive [ACTION_BOOT_COMPLETED](https://developer.android.com/reference/android/content/Intent.html#ACTION_BOOT_COMPLETED) when the device has been rebooted. 


## Other useful resources and discussions

### Canceling pending intent

https://stackoverflow.com/questions/14029400/flag-cancel-current-or-flag-update-current

### "Exit" the application and activity backstack

You should take care of managing the Activity backstack (and your application state) when you 
restart the application on uncaught exceptions. To check what happens when you restart your application declare SplashActivity as your main entry point in the manifest file
and check out how the Activity backstack behaves.

More resource on activity backstack:
https://inthecheesefactory.com/blog/understand-android-activity-launchmode/en

This application will be killed when an uncaught exception happens. This allows to restart your application
 instead of allowing android to show the "Your application has stopped" pop-up.
But there is no exact way to kill an application. System.exit() and android.os.Process.killProcess() results 
the following: the application will be killed but it immediately restarts with the
previous backstack but not having the last activty on the stack.
I suspect that the android runtime registers a shutdown hook method that restarts the application.

Some resources about killing an android application:

System.exit() documentation: https://developer.android.com/reference/java/lang/System.html#exit(int)

"Terminates the currently running Java Virtual Machine. The argument serves as a status code; by convention, a nonzero status code indicates abnormal termination.
This method calls the exit method in class Runtime. This method never returns normally.
The call System.exit(n) is effectively equivalent to the call:
Runtime.getRuntime().exit(n)"


http://sterl.org/2016/02/android-global-exception-handler/
https://stackoverflow.com/questions/18292016/difference-between-finish-and-system-exit0
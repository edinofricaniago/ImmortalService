# ImmortalService

This is a repository to experiment with a service that lives forever.
This service will try to restart in all case when it stopped except when the user click "force close" in the applications menu.

## Activity backstack

These "service restarting" methods are not intended to manage the Activity backstack.
Declare SplashActivity as your main entry point in the manifest file
and check out how the Activity backstack behaves.

More resource on activity backstack:
https://inthecheesefactory.com/blog/understand-android-activity-launchmode/en


## "Exit" the application

I try to kill the application when an uncaught exception happens instead
of allowing android to show the "Your application has stopped" pop-ip.
However there is no exact way to kill an application. System.exit() results
the application being killed but it immediately restarts with the Activity
backstack before except having the last activty on the stack.
I suspect that the android runtime registers a shutdown hook method that restarts the application.

Some resources about killing an android application:

System.exit() documentation: https://developer.android.com/reference/java/lang/System.html#exit(int)

"Terminates the currently running Java Virtual Machine. The argument serves as a status code; by convention, a nonzero status code indicates abnormal termination.
This method calls the exit method in class Runtime. This method never returns normally.
The call System.exit(n) is effectively equivalent to the call:
Runtime.getRuntime().exit(n)"


http://sterl.org/2016/02/android-global-exception-handler/
https://stackoverflow.com/questions/18292016/difference-between-finish-and-system-exit0
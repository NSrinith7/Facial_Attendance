package com.faceattendance;

// Logs message using log_textview present in activity_main.xml
class Logger {

    companion object {

        fun log( message : String ) {
            MainActivity.setMessage(  MainActivity.logTextView.text.toString() + "\n" + ">> $message" )
            while ( MainActivity.logTextView.canScrollVertically(1) ) {
                MainActivity.logTextView.scrollBy(0, 10)
            }
        }
        fun logName( message : String ) {
            MainActivity.setMessage(  MainActivity.logTextView.text.toString() + "\n" + ">> $message" )
            while ( MainActivity.logTextView.canScrollVertically(1) ) {
                MainActivity.logTextView.scrollBy(0, 10)
            }
        }

        fun addName(name : String){
            MainActivity.names.add(name)
        }

    }

}
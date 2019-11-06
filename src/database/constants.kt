package database

const val USER = "root"
const val PASSWORD = "1234"
const val HOST = "localhost"
const val PORT = "3306"

const val DRIVER = "jdbc:mysql"
const val DATABASE = "Deliveries?serverTimezone=UTC"

const val URL = "$DRIVER://$HOST:$PORT/$DATABASE"

const val EC_POSITION: Short = 1
const val DISP_POSITION: Short = 2
const val CHIEF_POSITION: Short = 3


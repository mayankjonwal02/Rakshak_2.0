import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private val MEDICAL_ID_COL = "medicalid"
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + DATA_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PATIENT_ID_COL + " TEXT, "
                + MEDICAL_ID_COL + " TEXT, "  // Update this line
                + ECG_COL + " REAL, "
                + HEART_RATE_COL + " REAL, "
                + SPO2_COL + " REAL, "
                + TEMPERATURE_COL + " REAL, "
                + TIMESTAMP_COL + " TEXT)")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun addPatientData(patientId: String?, ecg: Float?, heartRate: Float?, spo2: Float?, temperature: Float?, medicalId: String?) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(PATIENT_ID_COL, patientId)
        values.put(ECG_COL, ecg)
        values.put(HEART_RATE_COL, heartRate)
        values.put(SPO2_COL, spo2)
        values.put(TEMPERATURE_COL, temperature)
        values.put(TIMESTAMP_COL, System.currentTimeMillis().toString())
        values.put(MEDICAL_ID_COL, medicalId)  // Update this line

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleteAllPatientData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    fun getAllPatientData(): List<PatientData> {
        val patientDataList = mutableListOf<PatientData>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val dataIdColumnIndex = cursor.getColumnIndex(DATA_ID_COL)
            val patientIdColumnIndex = cursor.getColumnIndex(PATIENT_ID_COL)
            val ecgColumnIndex = cursor.getColumnIndex(ECG_COL)
            val heartRateColumnIndex = cursor.getColumnIndex(HEART_RATE_COL)
            val spo2ColumnIndex = cursor.getColumnIndex(SPO2_COL)
            val temperatureColumnIndex = cursor.getColumnIndex(TEMPERATURE_COL)
            val timestampColumnIndex = cursor.getColumnIndex(TIMESTAMP_COL)
            val medicalIdColumnIndex = cursor.getColumnIndex(medical_ID_COL)

            val dataId = if (dataIdColumnIndex != -1) cursor.getInt(dataIdColumnIndex) else 0
            val patientId = if (patientIdColumnIndex != -1) cursor.getString(patientIdColumnIndex) else ""
            val ecg = if (ecgColumnIndex != -1) cursor.getFloat(ecgColumnIndex) else 0.0f
            val heartRate = if (heartRateColumnIndex != -1) cursor.getFloat(heartRateColumnIndex) else 0.0f
            val spo2 = if (spo2ColumnIndex != -1) cursor.getFloat(spo2ColumnIndex) else 0.0f
            val temperature = if (temperatureColumnIndex != -1) cursor.getFloat(temperatureColumnIndex) else 0.0f
            val timestamp = if (timestampColumnIndex != -1) cursor.getString(timestampColumnIndex) else ""
            val medicalID = if (timestampColumnIndex != -1) cursor.getString(medicalIdColumnIndex) else ""

            val patientData = PatientData(dataId, patientId, ecg, heartRate, spo2, temperature, timestamp,medicalID)
            patientDataList.add(patientData)
        }

        cursor.close()
        db.close()
        return patientDataList
    }


    companion object {
        private const val DATA_ID_COL = "dataid"
        private const val PATIENT_ID_COL = "patientid"
        private const val ECG_COL = "ecg"
        private const val HEART_RATE_COL = "heartrate"
        private const val SPO2_COL = "spo2"
        private const val TEMPERATURE_COL = "temperature"
        private const val TIMESTAMP_COL = "timestamp"
        private const val medical_ID_COL = "medicalid"

        private const val DB_NAME = "patientdb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "patientdata"

    }
}

data class PatientData(
    val dataId: Int,
    val patientId: String,
    val ecg: Float,
    val heartRate: Float,
    val spo2: Float,
    val temperature: Float,
    val timestamp: String,
    var Medical_id : String
)

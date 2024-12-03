package uz.marupoov.reminderapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class TaskData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val task: String,
    val date: Long,
    val time: Long,
    val uuid: String,
    val completeState: Int
) : Parcelable